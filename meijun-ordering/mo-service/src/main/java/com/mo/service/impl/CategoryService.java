package com.mo.service.impl;

import com.mo.entity.Category;
import com.mo.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements com.mo.api.service.CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        return categoryMapper.getCategories();
    }
}
