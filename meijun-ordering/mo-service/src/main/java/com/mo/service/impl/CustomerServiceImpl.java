package com.mo.service.impl;

import com.mo.api.service.CustomerService;
import com.mo.entity.Customer;
import com.mo.service.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @Override
    public List<Customer> getAll(){
        return customerMapper.getAllCustomer();
    }

    @Override
    public List<Customer> searchForCustomer(String name){
        return customerMapper.searchForCustomer(name);
    }
}
