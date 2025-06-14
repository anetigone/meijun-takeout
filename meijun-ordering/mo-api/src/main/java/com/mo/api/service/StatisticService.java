package com.mo.api.service;

import com.mo.entity.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatisticService {

    List<Product> getSales();

    int getTraffic();

    double getSalesTotal();

    List<Product> getSalesByPeriod(LocalDate begin, LocalDate end);

    double getSalesTotalByPeriod(LocalDate begin, LocalDate end);

    int getTrafficByPeriod(LocalDate begin, LocalDate end);
}
