package com.mo.api.service;

import com.mo.entity.AfterSale;

import java.util.List;

public interface AfterSaleService {

    void saveAfterSale(AfterSale afterSale);

    int updateAfterSale(AfterSale afterSale);

    int deleteAfterSale(Long id);

    AfterSale getAfterSaleById(Long id);

    List<AfterSale> getAfterSales();

    List<AfterSale> getAfterSalePage(int offset, int size);

    int getAfterSaleCount();
}
