package com.mo.api.service;

import com.mo.entity.Product;

import java.util.List;

public interface StatisticService {

    List<Product> getSales();

    int getTraffic();

    double getSalesTotal();
}
