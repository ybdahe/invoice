package com.hitpixell.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitpixell.invoice.model.Client;
import com.hitpixell.invoice.model.ClientDTO;
import com.hitpixell.invoice.repository.ClientRepository;
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

@WebMvcTest(ClientController.class)
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientRepository clientRepository;


    @Test
    void getAllClientsWithNoContent() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("clientName", "xyz");
        mockMvc.perform(get("/api/clients").params(paramsMap))
                .andExpect(status().isNoContent());
    }

    @Test
    void createClient() throws Exception {

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientName("Pizza House 3");
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    public void getAllClients()
            throws Exception {

        Client client = new Client();
        client.setClientName("Pizza House 3");

        List<Client> allClients = Collections.singletonList(client);

        given(clientRepository
                .findAll())
                .willReturn(allClients);

        mockMvc.perform(get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test()
    @Disabled
    void updateClients() throws Exception {

        Client client = new Client();
        client.setId(1);
        client.setClientName("Pizza House 3");

        given(clientRepository
                .save(client))
                .willReturn(client);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/clients/{id}" + client.getId())
                        .content(mapper.writeValueAsString(client))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteClients() throws Exception {
        doNothing().when(clientRepository).deleteAll();
        mockMvc.perform(delete("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}