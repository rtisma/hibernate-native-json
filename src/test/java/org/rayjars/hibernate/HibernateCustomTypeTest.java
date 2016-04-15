/**
 * Copyright (C) ${year} Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rayjars.hibernate;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.rayjars.hibernate.model.Item;
import org.rayjars.hibernate.model.Label;
import org.rayjars.hibernate.model.Order;
import org.rayjars.hibernate.util.HibernateUtility;

import com.fasterxml.jackson.core.JsonProcessingException;

public class HibernateCustomTypeTest {

    private SessionFactory sessionFactory;
    private Session session;
    private Serializable item1;
    private Serializable item2;
    private Serializable order1;
    private Serializable order2;
    private Serializable order3;

    @Before
    public void createSession() throws Exception {
        sessionFactory = HibernateUtility.getSessionFactory();

        Session s = sessionFactory.openSession();
        Transaction t = s.beginTransaction();
        item1 = s.save(new Item("test1", new Label("french label", "fr")));
        item2 = s.save(new Item("test2", new Label("label without lang")));

        order1 = s.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a66", "", new Label("french label", "fr"), new Label("english label", "en")));
        order2 = s.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a69", "", new Label("label without lang")));
        order3 = s.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a67", ""));
        t.commit();
        s.close();
    }

    /* @After
    public void closeSession() {
        try {
            sessionFactory.close();
        } catch (HibernateException e) {
    
        }
    }*/

    @Test
    public void shouldCreateLabel() {
        Item item = new Item().label(new Label("Message French", "fr"));

        Item id = (Item) save(item);

        assertThat(id.getId(), greaterThan(0l));

    }

    @Test
    public void shouldLoadLabel() {
        Item item = (Item) load(Item.class, item1);

        assertThat(item, hasProperty("label",
                allOf(
                        hasProperty("value", is("french label")),
                        hasProperty("lang", is("fr")))));
    }

    @Test
    public void shouldUpdateLabel() {
        Item loaded = (Item) load(Item.class, item2);
        loaded.getLabel().setLang("en").setValue("new text");

        save(loaded);

        loaded = (Item) load(Item.class, item2);

        assertThat(loaded, hasProperty("label",
                allOf(
                        hasProperty("value", is("new text")),
                        hasProperty("lang", is("en")))));

    }

    @Test
    public void shouldDeleteLabel() {
        Item item = (Item) load(Item.class, item2);
        item.label(null);

        update(item);

        Item loadedItem = (Item) load(Item.class, item2);

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
                        hasProperty("lang", is("fr"))),
                allOf(
                        hasProperty("value", is("english value")),
                        hasProperty("lang", is("en")))));

    }

    @Test
    public void shouldUpdateLabels() {
        Order loadedOrder = (Order) load(Order.class, order1);
        loadedOrder.getLabels().get(0).setValue("new value").setLang("zh");

        update(loadedOrder);

        Order refreshOrder = (Order) load(Order.class, order1);

        assertThat(refreshOrder.getLabels(), containsInAnyOrder(
                allOf(
                        hasProperty("value", is("new value")),
                        hasProperty("lang", is("zh"))),
                allOf(
                        hasProperty("value", is("english label")),
                        hasProperty("lang", is("en")))));

    }

    @Test
    public void shouldDeleteLabels() {
        Order loadedOrder = (Order) load(Order.class, order1);
        loadedOrder.labels(new ArrayList<Label>());

        update(loadedOrder);

        Order refreshOrder = (Order) load(Order.class, order1);

        assertThat(refreshOrder.getLabels(), empty());
    }

    private Object load(Class clazz, Serializable id) {
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
