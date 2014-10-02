package com.github.mrcritical.ironcache.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object that represents an increment request.
 * 
 * @author pjarrell
 * 
 */
@Data
public class Increment {

	@JsonProperty("value")
	private int amount;

	@JsonProperty("msg")
	private String msg;

}