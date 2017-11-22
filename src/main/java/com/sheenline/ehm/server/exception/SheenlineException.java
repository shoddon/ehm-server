package com.sheenline.ehm.server.exception;

/**
 * @author Shoddon
 *
 */
public class SheenlineException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3534743202509148509L;
	
	private IError error;

	public SheenlineException(IError error) {
		super();
		this.error = error;
	}

	public IError getError() {
		return error;
	}

}
