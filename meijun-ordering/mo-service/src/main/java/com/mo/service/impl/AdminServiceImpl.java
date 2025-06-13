package com.mo.service.impl;

import com.mo.api.service.AdminService;
import com.mo.entity.Admin;
import com.mo.entity.Order;
import com.mo.entity.OrderDetail;
import com.mo.service.annotation.AutoFillTime;
import com.mo.service.annotation.AutoFillUuid;
import com.mo.service.mapper.AdminMapper;
import com.mo.service.mapper.OrderDetailMapper;
import com.mo.service.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Admin> getAllAdmins() {
        return adminMapper.getAllAdmin();
    }

    @Override
    public List<Admin> getPage(int offset, int size){
        return adminMapper.getPage(offset, size);
    }

    @Override
    @AutoFillTime
    public void updateAdmin(Admin admin) {
        adminMapper.updateAdmin(admin);
    }

    @Override
    public void delete(Long id) {
        adminMapper.deleteAdmin(id);
    }

    @Override
    @AutoFillTime
    @AutoFillUuid
    public void saveAdmin(Admin admin){
        adminMapper.saveAdmin(admin);
    }

    @Override
    public List<OrderDetail> getAllOrderDetail() {
        return orderDetailMapper.getAllOrderDetail();
    }

    @Override
    public int getTraffic() {
        return orderMapper.getAll().size();
    }

    @Override
    public BigDecimal getSalesTotal() {
        return orderMapper.getAll().stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public int getAdminCount() {
        return adminMapper.getAdminCount();
    }
}
