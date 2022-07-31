package com.hitpixell.invoice.controller;

import com.hitpixell.invoice.model.Client;
import com.hitpixell.invoice.model.ClientDTO;
import com.hitpixell.invoice.repository.ClientRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "Manage Clients", tags = {"clients"})
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/clients")
    @ApiOperation(value = "API to get clients ", response = Client.class)
    public ResponseEntity<List<Client>> getAllClients() {
        try {

            List<Client> clients = new ArrayList<>(clientRepository.findAll());

            if (clients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clients/{clientName}")
    @ApiOperation(value = "API to get client by name ", response = Client.class)
    public ResponseEntity<Client> getClientByClientName(@PathVariable("clientName") String clientName) {
        Optional<Client> client = Optional.ofNullable(clientRepository.findByClientName(clientName));
        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/clients/{id}")
    @ApiOperation(value = "API to get client by id ", response = Client.class)
    public ResponseEntity<Client> getClientById(@PathVariable("id") long id) {
        Optional<Client> client = clientRepository.findById(id);

        return client.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/clients")
    @ApiOperation(value = "API to add client ", response = Client.class)
    public ResponseEntity<Client> createClient(@RequestBody ClientDTO client) {
        try {
            Client clientData = clientRepository
                    .save(new Client(client.getClientName(), client.getStatus(), client.getBillingInterval(), client.getEmail(),
                            client.getFeesType(), client.getFees(), LocalDateTime.now()));
            return new ResponseEntity<>(clientData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/clients/{id}")
    @ApiOperation(value = "API to update client by id ", response = Client.class)
    public ResponseEntity<Client> updateClient(@PathVariable("id") long id, @RequestBody ClientDTO client) {
        Optional<Client> clientData = clientRepository.findById(id);

        if (clientData.isPresent()) {
            Client clientObj = clientData.get();
            clientObj.setClientName(client.getClientName());
            clientObj.setStatus(client.getStatus());
            clientObj.setBillingInterval(client.getBillingInterval());
            clientObj.setClientName(client.getEmail());
            clientObj.setFeesType(client.getFeesType());
            clientObj.setFees(client.getFees());
            clientObj.setModifiedAt(LocalDateTime.now());
            return new ResponseEntity<>(clientRepository.save(clientObj), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/clients/{id}")
    @ApiOperation(value = "API to delete client by id ", response = HttpStatus.class)
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") long id) {
        try {
            clientRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/clients")
    @ApiOperation(value = "API to delete all clients ", response = HttpStatus.class)
    public ResponseEntity<HttpStatus> deleteAllClients() {
        try {
            clientRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
