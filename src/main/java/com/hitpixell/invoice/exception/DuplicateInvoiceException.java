package com.hitpixell.invoice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateInvoiceException extends RuntimeException {

	public DuplicateInvoiceException(String exception) {
		super(exception);
	}

}
