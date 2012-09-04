/* ************************************************************************** *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * This file is licensed to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ************************************************************************** */
package org.ubimix.commons.json;

/**
 * @author kotelnikov
 */
public class JsonParser extends AbstractJsonParser {

    private int fLength;

    private int fPos;

    private CharSequence fStream;

    @Override
    protected char getChar() {
        return fPos < fLength ? fStream.charAt(fPos) : 0;
    }

    @Override
    protected boolean incPos() {
        if (fPos >= fLength) {
            return false;
        }
        fPos++;
        return true;
    }

    public void parse(CharSequence stream, IJsonListener listener) {
        fStream = stream;
        fLength = fStream.length();
        fPos = 0;
        doParse(listener);
    }

    public void parse(String str, IJsonListener listener) {
        parse((CharSequence) str, listener);
    }

}