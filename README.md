hibernate-jackson
=================

Persist an Object to JSON into database table field (declared as a string column).

Implementing a hibernate Jackson UserType by using the jackson object mappper to do a fast serialize/deserialize of a json string representation.

Check the src/test folder to see a full example.


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



### Exemple with an object collection

```
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

