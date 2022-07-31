package com.hitpixell.invoice.controller;

import com.hitpixell.invoice.model.Invoice;
import com.hitpixell.invoice.service.InvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * InvoiceController with GET /invoice api
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "Manage Invoices", tags = {"invoice"})
@RequestMapping("/api")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/invoice")
    @ApiOperation(value = "API to get billing invoice", response = Invoice.class)
    public ResponseEntity<Invoice> getInvoice(@RequestParam() @NonNull String clientName) {
        return new ResponseEntity<>(invoiceService.generateInvoice(clientName), HttpStatus.OK);
    }

}
