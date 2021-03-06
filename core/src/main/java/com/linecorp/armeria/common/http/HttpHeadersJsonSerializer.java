/*
 *  Copyright 2017 LINE Corporation
 *
 *  LINE Corporation licenses this file to you under the Apache License,
 *  version 2.0 (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package com.linecorp.armeria.common.http;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import io.netty.util.AsciiString;

/**
 * Jackson {@link JsonSerializer} for {@link HttpHeaders}.
 */
public class HttpHeadersJsonSerializer extends StdSerializer<HttpHeaders> {

    private static final long serialVersionUID = 4459242879396343114L;

    /**
     * Creates a new instance.
     */
    public HttpHeadersJsonSerializer() {
        super(HttpHeaders.class);
    }

    @Override
    public void serialize(HttpHeaders headers, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

        gen.writeStartObject();

        for (AsciiString name : headers.names()) {
            gen.writeFieldName(name.toString());
            final List<String> values = headers.getAll(name);
            if (values.size() == 1) {
                gen.writeString(values.get(0));
            } else {
                gen.writeStartArray();
                for (String value : values) {
                    gen.writeString(value);
                }
                gen.writeEndArray();
            }
        }

        gen.writeEndObject();
    }
}
