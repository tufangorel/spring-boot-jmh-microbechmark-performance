package com.company.customerinfo.controller;

import com.company.customerinfo.model.Customer;
import com.company.customerinfo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create a new customer record")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "201", description = "customer created", content = { @Content(mediaType = "application/json")} ),
            @ApiResponse(responseCode = "404", description = "Bad request") })
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Customer customer) {
        Customer response = customerService.save(customer);
        return new ResponseEntity<Customer>( response, HttpStatus.OK );
    }

    @Operation(summary = "View a list of customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping(value = "/list", produces = "application/json")
    public Iterable<Customer> list(){
        Iterable<Customer> customers = customerService.findAll();
        return customers;
    }

    @Operation(summary = "Delete a customer")
    @DeleteMapping(value="/delete/{id}", produces = "application/json")
    public ResponseEntity delete(@PathVariable Integer id){
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update a customer")
    @PutMapping(value = "/update/{id}", produces = "application/json")
    public ResponseEntity updateCustomer(@PathVariable Integer id, @RequestBody Customer customer){
        Optional<Customer> storedCustomer = customerService.findCustomerByID(id);
        storedCustomer.get().setAge(customer.getAge());
        customerService.save(storedCustomer.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
