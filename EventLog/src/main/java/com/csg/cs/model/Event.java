package com.csg.cs.model;

import lombok.Data;

@Data
public class Event {

	private String id;

	private String type;

	private String host;

	private String state;

	private long timestamp;
}
