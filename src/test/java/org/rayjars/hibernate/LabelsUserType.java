package org.rayjars.hibernate;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rayjars.hibernate.model.Label;

import java.util.List;

public class LabelsUserType extends JacksonUserType {

    @Override
    public Class returnedClass() {
        return Label.class;
    }

    @Override
    public JavaType createJavaType(ObjectMapper mapper) {
        return mapper.getTypeFactory().constructCollectionType(List.class, returnedClass());
    }
}
