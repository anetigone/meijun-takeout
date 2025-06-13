package com.mo.api.service;

import com.mo.entity.Admin;
import com.mo.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();

    List<Admin> getPage(int offset, int size);

    void updateAdmin(Admin admin);

    void delete(Long id);

    void saveAdmin(Admin admin);

    List<OrderDetail> getAllOrderDetail();

    int getTraffic();

    BigDecimal getSalesTotal();

    int getAdminCount();
}
