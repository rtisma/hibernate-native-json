package org.rayjars.hibernate;

import org.junit.Before;
import org.junit.Test;
import org.rayjars.hibernate.model.Label;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class JacksonUserTypeTest {

    private JacksonUserType type = null;

    @Before
    public void createType(){
        type = new JacksonUserType(){
            @Override
            public Class returnedClass() {
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
                hasProperty("lang", is("fr"))
        ));
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
                hasProperty("lang", is("fr"))
        ));



    }
}
