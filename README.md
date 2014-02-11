hibernate-jackson
=================

Persist an Object to JSON in database (using hibernate custom field)


Create your own implementation by extends JacksonUserType

```
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

And now you can persist your object...with your hibernate session

Its also supports a collection object, create a custom type implementation

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

