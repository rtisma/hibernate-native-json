package org.rayjars.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rayjars.hibernate.model.Item;
import org.rayjars.hibernate.model.Label;
import org.rayjars.hibernate.model.Order;
import org.rayjars.hibernate.util.HibernateUtility;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HibernateCustomTypeTest {

	private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		try {
			sessionFactory = HibernateUtility.getSessionFactory();
		} finally {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}

	@After
	public void tearDown() {
		try {
			sessionFactory.close();
		} catch (HibernateException e) {

		}
	}

	@Test
	public void testSerializer() {
		Session session = sessionFactory.getCurrentSession();

		Order order = new Order()
		    .description("customer order")
		    .ssn(UUID.randomUUID().toString())
		    .addLabel(new Label("french value", "fr"))
            .addLabel(new Label("english value", "en"))
            .addItem(new Item().label(new Label("item in french", "fr")));

        order = (Order) session.merge(order);
		session.getTransaction().commit();		

        //

		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
        Order loadedOrder = (Order) session.get(Order.class, order.getId());

        assertThat(loadedOrder.getLabels(), hasSize(2));
        assertThat(loadedOrder.getItems(), hasSize(1));

        assertThat(loadedOrder.getItems(),
                contains(
                        hasProperty("label", notNullValue())
                )
        );

	}

	protected void flushSession() {
		sessionFactory.getCurrentSession().flush();
	}

	protected void clearSession() {
		sessionFactory.getCurrentSession().clear();
	}

}
