package com.mo.service.mapper;

import com.mo.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishMapper {

    List<Dish> getCategories();
    List<Dish> getPage(@Param("offset") int offset,@Param("size") int size);
    List<Dish> getRecommendations();
    List<Dish> getDishByCategory(@Param("categoryId") Long categoryId);
    List<Dish> getSearchResult(@Param("name") String name);

    Dish getDishById(@Param("id") Long id);

    void saveDish(@Param("dish") Dish dish);

    void updateDish(@Param("dish") Dish dish);
}
