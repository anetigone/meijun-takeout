package com.mo.service.mapper;

import com.mo.entity.AfterSale;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AfterSaleMapper {

    void saveAfterSale(AfterSale afterSale);

    AfterSale getAfterSaleById(Long id);

    int updateAfterSale(AfterSale afterSale);

    int deleteAfterSale(Long id);

    List<AfterSale> getAfterSales();

    List<AfterSale> getAfterSalePage(int offset, int size);

    int getAfterSaleCount();
}
