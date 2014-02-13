package org.rayjars.hibernate;


import org.rayjars.hibernate.model.Label;

public class LabelsUserType extends JacksonListUserType {

    @Override
    public Class returnedClass() {
        return Label.class;
    }
}
