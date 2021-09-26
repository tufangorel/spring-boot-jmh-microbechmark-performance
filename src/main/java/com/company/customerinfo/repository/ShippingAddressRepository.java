package com.company.customerinfo.repository;

import com.company.customerinfo.model.Customer;
import com.company.customerinfo.model.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {

    @Query("SELECT s.customer FROM ShippingAddress s WHERE s.id = :shippingAddressID")
    Customer findCustomerByShippingAddressID(Integer shippingAddressID);

}