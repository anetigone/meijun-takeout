package com.mo.service.impl;

import com.mo.api.service.DishService;
import com.mo.entity.Dish;
import com.mo.service.annotation.AutoFillTime;
import com.mo.service.annotation.AutoFillUser;
import com.mo.service.mapper.DishMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Override
    public List<Dish> getCategories() {
        return dishMapper.getCategories();
    }

    @Override
    public List<Dish> getPage(int offset, int size) {
        return dishMapper.getPage(offset,size);
    }

    @Override
    public List<Dish> getRecommendations() {
        List<Dish> list = dishMapper.getCategories();
        list.sort(new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Long.compare(o1.getSale(), o2.getSale());
            }
        });
        return list;
    }

    @Override
    public List<Dish> getDishByCategory(Long categoryId) {
        return dishMapper.getDishByCategory(categoryId);
    }

    @Override
    public List<Dish> getSearchResult(String name) {
        return dishMapper.getSearchResult(name);
    }

    @Override
    @AutoFillTime
    @AutoFillUser
    public void saveDish(Dish dish) {
        dishMapper.saveDish(dish);
    }

    @Override
    public int getDishCount() {
        return dishMapper.getDishCount();
    }

    @Override
    public Dish getDishById(Long id) {
        return dishMapper.getDishById(id);
    }

    @Override
    @AutoFillTime
    public void updateDish(Dish dish) {
        dishMapper.updateDish(dish);
    }
}
