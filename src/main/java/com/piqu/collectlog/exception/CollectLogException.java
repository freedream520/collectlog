package com.piqu.collectlog.exception;

public class CollectLogException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 327240507372985699L;

	public CollectLogException(String message) {
        super(message);
    }
	
	public CollectLogException(String message, Throwable cause) {
        super(message, cause);
    }

}
