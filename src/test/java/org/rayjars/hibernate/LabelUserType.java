package org.rayjars.hibernate;


import org.rayjars.hibernate.model.Label;

public class LabelUserType extends JacksonUserType {

    @Override
    public Class returnedClass() {
        return Label.class;
    }
}
