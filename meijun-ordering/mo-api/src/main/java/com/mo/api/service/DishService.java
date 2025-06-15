package com.mo.api.service;

import com.mo.entity.Dish;

import java.util.List;

public interface DishService {

    List<Dish> getCategories();

    List<Dish> getPage(int offset, int size);

    List<Dish> getRecommendations();

    List<Dish> getDishByCategory(Long categoryId);

    List<Dish> getSearchResult(String name);

    void saveDish(Dish dish);

    int getDishCount();

    Dish getDishById(Long id);
}
