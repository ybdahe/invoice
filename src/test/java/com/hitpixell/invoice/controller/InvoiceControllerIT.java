package com.hitpixell.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitpixell.invoice.model.Client;
import com.hitpixell.invoice.model.Invoice;
import com.hitpixell.invoice.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    InvoiceService invoiceService;


    @Test
    void getInvoiceWithNoContent() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("clientName", "xyz");
        Invoice invoice = new Invoice();
        given(invoiceService
                .generateInvoice("xyz"))
                .willReturn(invoice);

        mockMvc.perform(get("/api/invoice").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceId").isEmpty());
    }

    @Test
    void getInvoice()
            throws Exception {

        Client client = new Client();
        client.setClientName("Pizza House 3");

        Invoice invoice = new Invoice();
        invoice.setInvoiceId("INV0001");
        invoice.setClientName(client.getClientName());

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("clientName", client.getClientName());

        given(invoiceService
                .generateInvoice(client.getClientName()))
                .willReturn(invoice);

        mockMvc.perform(get("/api/invoice").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceId").value(invoice.getInvoiceId()));
    }
}