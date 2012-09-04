/* ************************************************************************** *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. This file is licensed to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * **************************************************************************
 */
package org.ubimix.commons.json;

/**
 * @author kotelnikov
 */
public abstract class AbstractJsonParser {

    protected static abstract class CharChecker {

        public boolean accept(char c) {
            return get(c) != '\0';
        }

        public abstract char get(char c);
    }

    private IJsonListener fListener;

    protected CharChecker fQuotChecker = new CharChecker() {
        @Override
        public char get(char c) {
            if (c == '\'' || c == '"') {
                return c;
            }
            return '\0';
        }
    };

    protected CharChecker fSpaceChecker = new CharChecker() {
        @Override
        public char get(char c) {
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                return c;
            }
            return '\0';
        }
    };

    protected CharChecker fTokenEndChecker = new CharChecker() {
        @Override
        public char get(char c) {
            if (fSpaceChecker.get(c) == c) {
                return c;
            }
            if (c == '{'
                || c == '}'
                || c == '['
                || c == ']'
                || c == ':'
                || c == ',') {
                return c;
            }
            return '\0';
        }
    };;

    protected CharChecker fValueEndChecker = new CharChecker() {
        @Override
        public char get(char c) {
            if (c == '}' || c == ']' || c == ',') {
                return c;
            }
            return '\0';
        }
    };

    public AbstractJsonParser() {
    }

    protected void doParse(IJsonListener listener) {
        fListener = listener;
        int ch = getChar();
        while (ch != '{' && ch != '[') {
            if (!incPos()) {
                break;
            }
            ch = getChar();
        }
        if (skipArray() || skipObject()) {
            // OK
        }
        fListener = null;
    }

    /**
     * Returns the current character or '\0' symbol if the stream does not
     * contain new characters anymore.
     * 
     * @return the current character or the '\0' symbol for the end of the
     *         stream.
     */
    protected abstract char getChar();

    /**
     * Goes to the next character in the stream and returns <code>true</code> if
     * the position was successfully incremented.
     * 
     * @return <code>true</code> if the position was successfully incremented
     */
    protected abstract boolean incPos();

    private boolean skipArray() {
        skipSpaces();
        char ch = getChar();
        if (ch <= 0) {
            return false;
        }
        if (ch != '[') {
            return false;
        }
        incPos();
        fListener.beginArray();
        while ((ch = getChar()) > 0) {
            if (getChar() == ']') {
                incPos();
                break;
            }
            fListener.beginArrayElement();
            skipValue();
            fListener.endArrayElement();
            skipSpaces();
            if (getChar() == ',') {
                incPos();
                skipSpaces();
            }
        }
        fListener.endArray();
        return true;
    }

    private boolean skipObject() {
        skipSpaces();
        char ch = getChar();
        if (ch != '{') {
            return false;
        }
        incPos();
        fListener.beginObject();
        while (true) {
            skipSpaces();
            ch = getChar();
            if (ch == '}') {
                incPos();
                break;
            }
            if (ch <= 0) {
                break;
            }

            skipSpaces();
            String property = skipQuot();
            if (property == null) {
                property = skipToken(fTokenEndChecker);
            }
            if (property == null || property.length() == 0) {
                break;
            }

            skipSpaces();
            fListener.beginObjectProperty(property);
            ch = getChar();
            if (ch == ':') {
                incPos();
                skipValue();
            } else {
                fListener.onValue(null);
            }
            fListener.endObjectProperty(property);
            skipSpaces();

            ch = getChar();
            if (ch == ',') {
                incPos();
                skipSpaces();
            }
        }
        fListener.endObject();
        return true;
    }

    private String skipQuot() {
        char ch = getChar();
        if (ch <= 0) {
            return null;
        }
        final char quot = fQuotChecker.get(ch);
        if (quot == 0) {
            return null;
        }
        incPos();
        String result = skipToken(new CharChecker() {
            @Override
            public char get(char c) {
                return quot == c ? c : '\0';
            }
        });
        if (getChar() == quot) {
            incPos();
        }
        return result;
    }

    private boolean skipSpaces() {
        int i;
        for (i = 0; fSpaceChecker.accept(getChar()); incPos(), i++) {
        }
        return i > 0;
    }

    private String skipToken(CharChecker endChecker) {
        StringBuffer buf = new StringBuffer();
        boolean escaped = false;
        char ch;
        for (; (ch = getChar()) > 0; incPos()) {
            if (escaped) {
                switch (ch) {
                    case 'n':
                        buf.append('\n');
                        break;
                    case 'r':
                        buf.append('\r');
                        break;
                    case 't':
                        buf.append('\t');
                        break;
                    case 'f':
                        buf.append('\f');
                        break;
                    case 'u':
                    case 'U':
                        int code = 0;
                        for (int i = 0; i < 4; i++) {
                            if (!incPos()) {
                                break;
                            }
                            ch = getChar();
                            int v;
                            if (ch >= '0' && ch <= '9') {
                                v = ch - '0';
                            } else if (ch >= 'a' && ch <= 'f') {
                                v = ch - 'a' + 10;
                            } else if (ch >= 'A' && ch <= 'F') {
                                v = ch - 'A' + 10;
                            } else {
                                break;
                            }
                            code |= v << ((3 - i) * 4);
                        }
                        ch = (char) code;
                        buf.append(ch);
                        break;
                    default:
                        buf.append(ch);
                        break;
                }
                escaped = false;
                continue;
            }
            escaped = ch == '\\';
            if (escaped) {
                continue;
            }
            if (endChecker.accept(ch)) {
                break;
            }
            buf.append(ch);
        }
        return buf.toString();
    }

    private boolean skipValue() {
        boolean result = false;
        skipSpaces();
        if (!(result = skipObject())) {
            if (!(result = skipArray())) {
                skipSpaces();
                String value = skipQuot();
                if (value != null) {
                    fListener.onValue(value);
                } else {
                    value = skipToken(fValueEndChecker);
                    if (value == null) {
                        fListener.onValue(null);
                    } else {
                        value = value.trim();
                        String v = value.toLowerCase();
                        if ("null".equals(v)) {
                            fListener.onValue(null);
                        } else if ("true".equals(v)) {
                            fListener.onValue(true);
                            result = true;
                        } else if ("false".equals(v)) {
                            fListener.onValue(false);
                            result = true;
                        } else {
                            int i = 0;
                            try {
                                i = Integer.parseInt(v);
                                result = true;
                            } catch (NumberFormatException e) {
                            }
                            if (result) {
                                fListener.onValue(i);
                            } else {
                                long l = 0;
                                try {
                                    l = Long.parseLong(v);
                                    result = true;
                                } catch (NumberFormatException e) {
                                }
                                if (result) {
                                    fListener.onValue(l);
                                } else {
                                    double d = 0;
                                    try {
                                        d = Double.parseDouble(v);
                                        result = true;
                                    } catch (NumberFormatException e) {
                                    }
                                    if (result) {
                                        fListener.onValue(d);
                                    } else {
                                        result = true;
                                        fListener.onValue(value);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}