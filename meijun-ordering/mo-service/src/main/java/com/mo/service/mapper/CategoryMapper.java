package com.mo.service.mapper;

import com.mo.entity.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper {

    List<Category> getCategories();
}
