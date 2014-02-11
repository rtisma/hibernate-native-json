package org.rayjars.hibernate.model;


import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="ORDERS")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "SSN")
	private String SSN;

	@Column(name = "DESCRIPTION")
	private String description;

    @Type(type = "org.rayjars.hibernate.LabelsUserType")
    @Column(name = "labels")
	private List<Label> labels = new ArrayList<Label>();

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="ORDER_ID")
    private List<Item> items = new ArrayList<Item>();

	public Order() {
	
	}
	
	public Order addLabel(Label label){
		if(label == null){
			throw new NullPointerException();
		}
		
		labels.add(label);
        return this;
	}

    public Order addItem(Item item){
        if(item == null){
            throw new NullPointerException();
        }

        items.add(item);
        return this;
    }

	public Long getId() {
		return id;
	}

	public Order setId(Long id) {
		this.id = id;
        return this;
	}

	public String getSSN() {
		return SSN;
	}

	public Order ssn(String sSN) {
		SSN = sSN;
        return this;
	}

	public String getDescription() {
		return description;
	}

	public Order description(String description) {
		this.description = description;
        return this;
	}

    public Order labels(List<Label> labels) {
        this.labels = labels;
        return this;
    }

    public List<Label> getLabels() {
        return labels;
    }


    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
