package org.rayjars.hibernate;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.rayjars.hibernate.model.Item;
import org.rayjars.hibernate.model.Label;
import org.rayjars.hibernate.model.Order;
import org.rayjars.hibernate.util.HibernateUtility;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class HibernateCustomTypeTest {

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void createSession() throws Exception {
        sessionFactory = HibernateUtility.getSessionFactory();
        cleanlyInsertDataset(readDataSet());
    }

   /* @After
    public void closeSession() {
        try {
            sessionFactory.close();
        } catch (HibernateException e) {

        }
    }*/

    private IDataSet readDataSet() throws Exception {
        InputStream stream = this.getClass().getResourceAsStream("/dbunit-dataset.xml");
        return new FlatXmlDataSetBuilder().build(stream);
    }

    private void cleanlyInsertDataset(IDataSet dataSet) throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(
                "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:demo;DB_CLOSE_DELAY=-1", "sa", "");
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }


    @Test
    public void shouldCreateLabel() {
        Item item = new Item().label(new Label("Message French", "fr"));

        Item id = (Item) save(item);

        assertThat(id.getId(), greaterThan(0l));

    }

    @Test
    public void shouldLoadLabel() {
        Item item = (Item) load(Item.class, -1l);

        assertThat(item, hasProperty("label",
                allOf(
                        hasProperty("value", is("french label")),
                        hasProperty("lang", is("fr"))
                )
        )
        );
    }


    @Test
    public void shouldUpdateLabel() {
        Item loaded = (Item) load(Item.class, -2l);
        loaded.getLabel().setLang("en").setValue("new text");

        save(loaded);

        loaded = (Item) load(Item.class, -2l);

        assertThat(loaded, hasProperty("label",
                allOf(
                        hasProperty("value", is("new text")),
                        hasProperty("lang", is("en"))
                )
        )
        );

    }


    @Test
    public void shouldDeleteLabel() {
        Item item = (Item) load(Item.class, -2l);
        item.label(null);

        update(item);

        Item loadedItem = (Item) load(Item.class, -2l);

        assertThat(loadedItem.getLabel(), nullValue());

    }

    @Test
    public void shouldCreateLabels() throws JsonProcessingException {

        Order order = new Order()
                .description("customer order")
                .ssn(UUID.randomUUID().toString())
                .addLabel(new Label("french value", "fr"))
                .addLabel(new Label("english value", "en"));

        Order saved = (Order) save(order);

        Order loadedOrder = (Order) load(Order.class, saved.getId());

        assertThat(loadedOrder.getLabels(), hasSize(2));

        assertThat(loadedOrder.getLabels(), containsInAnyOrder(
                allOf(
                        hasProperty("value", is("french value")),
                        hasProperty("lang", is("fr"))
                ),
                allOf(
                        hasProperty("value", is("english value")),
                        hasProperty("lang", is("en"))
                )
        )
        );



    }

    @Test
    public void shouldUpdateLabels() {
        Order loadedOrder = (Order) load(Order.class, -1l);
        loadedOrder.getLabels().get(0).setValue("new value").setLang("zh");

        update(loadedOrder);

        Order refreshOrder = (Order) load(Order.class, -1l);

        assertThat(refreshOrder.getLabels(), containsInAnyOrder(
                allOf(
                        hasProperty("value", is("new value")),
                        hasProperty("lang", is("zh"))
                ),
                allOf(
                        hasProperty("value", is("english label")),
                        hasProperty("lang", is("en"))
                )
        )
        );

    }


    @Test
    public void shouldDeleteLabels() {
        Order loadedOrder = (Order) load(Order.class, -1l);
        loadedOrder.labels(new ArrayList<Label>());

        update(loadedOrder);

        Order refreshOrder = (Order) load(Order.class, -1l);

        assertThat(refreshOrder.getLabels(), empty());
    }


    private Object load(Class clazz, Long id) {
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Object entity = session.get(clazz, id);

        session.getTransaction().commit();

        return entity;

    }


    private Object save(Object entity) {
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Object entityAttached = session.merge(entity);

        session.getTransaction().commit();

        return entityAttached;
    }

    private void update(Object entity) {
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(entity);

        session.getTransaction().commit();
    }

}
