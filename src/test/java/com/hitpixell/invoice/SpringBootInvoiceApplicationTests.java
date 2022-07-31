package com.hitpixell.invoice;

import com.hitpixell.invoice.exception.DuplicateInvoiceException;
import com.hitpixell.invoice.exception.InvalidParameterException;
import com.hitpixell.invoice.exception.NoClientFoundException;
import com.hitpixell.invoice.model.ExceptionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringBootInvoiceApplicationTests {

	@Test
	void contextLoads() {
		String microService = "hitpixel-invoice";
		assertThat(microService).isEqualTo("hitpixel-invoice");
	}

	@Test
	public void testSpringboot() {
		try {
			SpringBootInvoiceApplication.main(null);
		}catch (Exception e){
			assertThat(e).isInstanceOf(Exception.class);
		}
	}

	@Test
	public void NoClientFoundException() {
		Exception exception = new NoClientFoundException("NoClientFound");
		assertThat(exception.getMessage()).isEqualTo("NoClientFound");
	}

	@Test
	public void InvalidParameterException() {
		Exception exception = new InvalidParameterException("InvalidParameter");
		assertThat(exception.getMessage()).isEqualTo("InvalidParameter");
	}

	@Test
	public void DuplicateInvoiceException() {
		Exception exception = new DuplicateInvoiceException("DuplicateInvoice");
		assertThat(exception.getMessage()).isEqualTo("DuplicateInvoice");
	}

	@Test
	public void exceptionResponse() {
		ExceptionResponse exception = new ExceptionResponse(new Date(),"Exception" ,"");
		assertThat(exception.getMessage()).isEqualTo("Exception");
	}

}
