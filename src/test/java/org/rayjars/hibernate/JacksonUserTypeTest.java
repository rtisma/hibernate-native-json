/**
 * Copyright (C) ${year} Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rayjars.hibernate;

import org.junit.Before;
import org.junit.Test;
import org.rayjars.hibernate.model.Label;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JacksonUserTypeTest {

    private JacksonUserType type = null;

    @Before
    public void createType() {
        type = new JacksonUserType() {
            @Override
            public Class<?> returnedClass() {
                return Label.class;
            }
        };
    }

    @Test
    public void testConvertJsonToObject() throws Exception {
        String json = "{\"value\": \"french label\", \"lang\":\"fr\"}";

        Label label = (Label) type.convertJsonToObject(json);

        assertThat(label, notNullValue());
        assertThat(label, allOf(
                hasProperty("value", is("french label")),
                hasProperty("lang", is("fr"))));
    }

    @Test
    public void testConvertObjectToJson() throws Exception {
        Label label = new Label("french label", "fr");

        String json = type.convertObjectToJson(label);
        assertThat(json, is("{\"value\":\"french label\",\"lang\":\"fr\"}"));
    }

    @Test
    public void testDeepCopy() throws Exception {
        Label label = new Label("french label", "fr");
        Label copy = (Label) type.deepCopy(label);

        assertThat(copy, equalTo(label));
        assertThat(copy, not(sameInstance(label)));

        assertThat(label, allOf(
                hasProperty("value", is("french label")),
                hasProperty("lang", is("fr"))));

    }
}
