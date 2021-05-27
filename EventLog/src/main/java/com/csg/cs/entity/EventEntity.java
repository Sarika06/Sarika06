package com.csg.cs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "EVENT")
@Data
public class EventEntity {

	@Id
	@Column(name = "EVENT_ID")
	private String id;
	@Column(name = "EVENT_TYPE")
	private String type;
	@Column(name = "EVENT_HOST")
	private String host;
	@Column(name = "EVENT_DURATION")
	private int duration;
	@Column(name = "EVENT_ALERT")
	private boolean alert;

}
