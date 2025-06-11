package com.mo.api.service;

import com.mo.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAll();

    List<Customer> searchForCustomer(String name);
}
