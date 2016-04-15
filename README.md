hibernate-jackson
=================


[![Build Status](https://travis-ci.org/velo/hibernate-native-json.svg?branch=master)](https://travis-ci.org/velo/hibernate-native-json?branch=master) 
[![Coverage Status](https://coveralls.io/repos/github/velo/hibernate-native-json/badge.svg?branch=master)](https://coveralls.io/github/velo/hibernate-native-json?branch=master) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.marvinformatics/hibernate-native-json/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.marvinformatics/hibernate-native-json/) 
[![Issues](https://img.shields.io/github/issues/velo/hibernate-native-json.svg)](https://github.com/velo/hibernate-native-json/issues) 
[![Forks](https://img.shields.io/github/forks/velo/hibernate-native-json.svg)](https://github.com/velo/hibernate-native-json/network) 
[![Stars](https://img.shields.io/github/stars/velo/hibernate-native-json.svg)](https://github.com/velo/hibernate-native-json/stargazers)


Read/Write an object to JSON / JSON to object into a database table field (declared as a string column).

Implementing a hibernate UserType by using the jackson object mappper to do a fast serialize/deserialize of a json string representation.

Check the src/test folder to see a full example.

More information  [how to implements a user type](http://blog.xebia.com/2009/11/09/understanding-and-writing-hibernate-user-types/)

### Exemple with a Simple Object

Create your own implementation by extends JacksonUserType

```
//Need to be serializable to manage the different case scenario of the user type
public class Label implements Serializable{
    private String value;
    private String lang;

    public Label() {
    }

    ...getter/setter
}

public class LabelUserType extends JacksonUserType {
     @Override
    public Class returnedClass() {
        return Label.class;
    }
}

@Entity
@Table(name="ITEMS")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Type(type = "org.rayjars.hibernate.LabelUserType")
    @Column(name = "label")
    private Label label;

    ...getter/setter
}
```

And now you can persist your object...with your hibernate session / jpa repository



### Exemple with a object collection

```
public class LabelsUserType extends JacksonListUserType {
    @Override
    public Class returnedClass() {
        return Label.class;
    }
}

@Entity
@Table(name="ORDERS")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Type(type = "org.rayjars.hibernate.LabelsUserType")
    @Column(name = "labels")
	private List<Label> labels = new ArrayList<Label>();

	 ...getter/setter
}
```