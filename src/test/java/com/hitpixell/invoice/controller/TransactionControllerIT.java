package com.hitpixell.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitpixell.invoice.model.Transaction;
import com.hitpixell.invoice.model.TransactionDTO;
import com.hitpixell.invoice.repository.TransactionRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionRepository transactionRepository;


    @Test
    void getAllTransactionsWithNoContent() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("clientName", "xyz");
        mockMvc.perform(get("/api/transactions").params(paramsMap))
                .andExpect(status().isNoContent());
    }

    @Test
    void createTransaction() throws Exception {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setClientName("Pizza House 3");
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    public void getAllTransactions()
            throws Exception {

        Transaction transaction = new Transaction();
        transaction.setOrderName("Veg Pizza");

        List<Transaction> allTransactions = Collections.singletonList(transaction);

        given(transactionRepository
                .findAll())
                .willReturn(allTransactions);

        mockMvc.perform(get("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test()
    @Disabled
    void updateTransaction() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setOrderName("Veg Pizza");
        given(transactionRepository
                .save(transaction))
                .willReturn(transaction);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/transactions/{id}" + transaction.getId())
                        .content(mapper.writeValueAsString(transaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTransactions() throws Exception {
        doNothing().when(transactionRepository).deleteAll();
        mockMvc.perform(delete("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}