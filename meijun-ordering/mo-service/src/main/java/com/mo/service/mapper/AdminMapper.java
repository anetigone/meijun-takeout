package com.mo.service.mapper;

import com.mo.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {

    Admin getAdminByUsername(@Param("username") String username);
    Admin getAdminByUuid(@Param("uuid") String uuid);
    
    List<Admin> getAllAdmin();
    List<Admin> getPage(int offset, int size);

    void updateAdmin(@Param("admin") Admin admin);

    void deleteAdmin(Long id);

    void saveAdmin(@Param("admin") Admin admin);

    int getAdminCount();
    @Select("select * from admins where id = #{id}")
    Admin getAdminById(Long id);
}
