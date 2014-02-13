package org.rayjars.hibernate;

import org.junit.Before;
import org.junit.Test;
import org.rayjars.hibernate.model.Label;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class JacksonListUserTypeTest {

    private JacksonListUserType type = null;


    @Before
    public void createType(){
        type = new JacksonListUserType(){
            @Override
            public Class returnedClass() {
                return Label.class;
            }
        };
    }


    @Test
    public void testConvertJsonToObject() throws Exception {
        String json = "[{\"value\": \"french label\", \"lang\":\"fr\"}, {\"value\": \"english label\", \"lang\":\"en\"}]";

        List<Label> labels = (List<Label>) type.convertJsonToObject(json);

        assertThat(labels, hasSize(2));
        assertThat(labels, containsInAnyOrder(
                allOf(hasProperty("value", is("french label")),  hasProperty("lang", is("fr"))),
                allOf(hasProperty("value", is("english label")), hasProperty("lang", is("en")))
        ));
    }

    @Test
    public void testConvertObjectToJson() throws Exception {
        List<Label> labels = new ArrayList<Label>();
        labels.add(new Label("french label", "fr"));
        labels.add(new Label("english label", "en"));

        String json = type.convertObjectToJson(labels);

        assertThat(json, containsString("{\"value\":\"french label\",\"lang\":\"fr\"}"));
        assertThat(json, containsString("{\"value\":\"english label\",\"lang\":\"en\"}"));
    }

    @Test
    public void testDeepCopy() throws Exception {
        List<Label> labels = new ArrayList<Label>();
        labels.add(new Label("french label", "fr"));
        labels.add(new Label("english label", "en"));

        List<Label> copy = (List<Label>) type.deepCopy(labels);

        assertThat(labels, hasSize(2));
        assertThat(copy, equalTo(labels));
        assertThat(copy, not(sameInstance(labels)));

        assertThat(labels, containsInAnyOrder(
                allOf(hasProperty("value", is("french label")),  hasProperty("lang", is("fr"))),
                allOf(hasProperty("value", is("english label")), hasProperty("lang", is("en")))
        ));


    }

}
