package com.company.customerinfo.service;


import com.company.customerinfo.model.CustomerOrder;
import com.company.customerinfo.model.OrderItem;
import com.company.customerinfo.repository.CustomerOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    @Transactional
    public CustomerOrder save(CustomerOrder customerOrder){

        for( OrderItem orderItem: customerOrder.getOrderItems() ) {
            orderItem.setCustomerOrder(customerOrder);
        }
        CustomerOrder result = customerOrderRepository.save(customerOrder);
        return result;
    }

    public List<CustomerOrder> findAll(){
        return customerOrderRepository.findAll();
    }

    @Transactional
    public void deleteCustomerOrder(Integer id) {
        customerOrderRepository.deleteById(id);
    }

    public Optional<CustomerOrder> findByID(Integer id) {
        return customerOrderRepository.findById(id);
    }
}