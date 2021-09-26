package com.company.customerinfo.benchmark.impl;


import com.company.customerinfo.CustomerInfoApplication;
import com.company.customerinfo.model.Customer;
import com.company.customerinfo.model.ShippingAddress;
import com.company.customerinfo.service.CustomerService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)  // lower number is good
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class CustomerOrderServiceBenchMark {

    private ConfigurableApplicationContext context;
    private CustomerService customerService;

    @Setup(Level.Trial)
    public void init(){
        context = SpringApplication.run(CustomerInfoApplication.class);
        customerService = context.getBean(CustomerService.class);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        context.close();
    }

    @Benchmark
    public void saveCustomerWithOrdersBenchMark(final Blackhole bh) {

        Customer customer = new Customer();
        customer.setName("name1");
        customer.setAge(1);

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setCountry("TR");
        shippingAddress.setCity("Ankara");
        shippingAddress.setStreetName("KaleSokak");
        customer.setShippingAddress(shippingAddress);

        bh.consume(executeSaveCustomer(customer));
    }

    private boolean executeSaveCustomer(final Customer customer) {
        customerService.save(customer);
        return true;
    }

}