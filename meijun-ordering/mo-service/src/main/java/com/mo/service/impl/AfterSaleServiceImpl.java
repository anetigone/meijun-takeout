package com.mo.service.impl;

import com.mo.api.service.AfterSaleService;
import com.mo.entity.AfterSale;
import com.mo.service.mapper.AfterSaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AfterSaleServiceImpl implements AfterSaleService {
    @Autowired
    private AfterSaleMapper afterSaleMapper;

    @Override
    public void saveAfterSale(AfterSale afterSale){
        afterSaleMapper.saveAfterSale(afterSale);
    }

    @Override
    public int updateAfterSale(AfterSale afterSale) {
        return afterSaleMapper.updateAfterSale(afterSale);
    }

    @Override
    public int deleteAfterSale(Long id) {
        return afterSaleMapper.deleteAfterSale(id);
    }

    @Override
    public AfterSale getAfterSaleById(Long id){
        return afterSaleMapper.getAfterSaleById(id);
    }

    @Override
    public List<AfterSale> getAfterSales() {
        return afterSaleMapper.getAfterSales();
    }

    @Override
    public List<AfterSale> getAfterSalePage(int offset, int size){
        return afterSaleMapper.getAfterSalePage(offset, size);
    }

    @Override
    public int getAfterSaleCount(){
        return afterSaleMapper.getAfterSaleCount();
    }
}
