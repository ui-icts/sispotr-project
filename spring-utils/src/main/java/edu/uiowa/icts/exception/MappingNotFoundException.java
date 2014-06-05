package edu.uiowa.icts.exception;

public class MappingNotFoundException extends Exception {

	private static final long serialVersionUID = 2890579965738044986L;
	
	private String mapping;

	// ----------------------------------------------
	// Default constructor - initializes instance variable to unknown

	public MappingNotFoundException() {
		super(); // call superclass constructor
		mapping = "unknown";
	}

	// -----------------------------------------------
	// Constructor receives some kind of message that is saved in an instance
	// variable.

	public MappingNotFoundException(String mapping) {
		super(mapping); // call super class constructor
		this.mapping = mapping; // save message
	}

	// ------------------------------------------------
	// public method, callable by exception catcher. It returns the error
	// message.
	public String getError() {
		return mapping;
	}
}