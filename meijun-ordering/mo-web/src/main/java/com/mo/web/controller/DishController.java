package com.mo.web.controller;

import com.mo.api.dto.DishDTO;
import com.mo.api.dto.DishPageQueryDTO;
import com.mo.api.service.CategoryService;
import com.mo.api.service.DishService;
import com.mo.common.result.PageResult;
import com.mo.common.result.Result;
import com.mo.entity.Category;
import com.mo.entity.Dish;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/dishes")
@Tag(name = "菜品管理")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "获取菜品分类")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Dish.class)))
    @GetMapping("/categories")
    public Result<List<Category>> getCategories(){
        List<Category> dishes = categoryService.getCategories();

        return Result.success(dishes);
    }

    @Operation(summary = "获取菜品分页")
    @Parameters({
            @Parameter(name = "dishPageQueryDTO", schema = @Schema(implementation = DishPageQueryDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Dish.class)))
    @GetMapping("/page")
    public PageResult getPage(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size) {

        int offset = (page - 1) * size;
        List<Dish> list = dishService.getPage(offset,size);
        int total = dishService.getDishCount();

        return PageResult.success(total, list, page, size);
    }

    @Operation(summary = "获取推荐菜品")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Dish.class)))
    @GetMapping("/recommendations")
    public Result<List<Dish>> getRecommendations(){
        List<Dish> dishes = dishService.getRecommendations();

        return Result.success(dishes);
    }

    @Operation(summary = "添加菜品")
    @Parameters({
            @Parameter(name = "dishDTO", schema = @Schema(implementation = DishDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/save")
    public Result<String> saveDish(@RequestBody DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishService.saveDish(dish);

        return Result.success();
    }

    @Operation(summary = "获取搜索菜品")
    @Parameters({
            @Parameter(name = "name", schema = @Schema(implementation = String.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Dish.class)))
    @GetMapping("/search")
    public Result<List<Dish>> getSearchResult(@RequestParam String name){
        List<Dish> dishes = dishService.getSearchResult(name);

        return Result.success(dishes);
    }

    @GetMapping("/{id}")
    public Result<Dish> getDishById(@PathVariable Long id){
        Dish dish = dishService.getDishById(id);

        return Result.success(dish);
    }

    @PostMapping("/update")
    public Result<String> updateDish(@RequestBody DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishService.updateDish(dish);

        return Result.success();
    }
}
