package org.rayjars.hibernate.model;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ITEMS")
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Type(type = "org.rayjars.hibernate.LabelUserType")
    @Column(name = "label")
    private Label label;

    public Item() {
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Label getLabel() {
        return label;
    }

    public Item label(Label label) {
        this.label = label;
        return this;
    }
}
