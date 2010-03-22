package com.elolozone.trafficwave.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * Simple POJO domain object with an id property.
 * Used as a base class for objects needing this property.
 * 
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 3794699774087502239L;

	private String id;

	protected BaseEntity() {
	}

	@Id
	@GeneratedValue(generator = "strategy-uuid")
	@GenericGenerator(name = "strategy-uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
