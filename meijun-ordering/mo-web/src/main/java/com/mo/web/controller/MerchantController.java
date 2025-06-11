package com.mo.web.controller;

import com.mo.api.dto.*;
import com.mo.api.service.*;
import com.mo.api.vo.CouponVO;
import com.mo.api.vo.PromotionVO;
import com.mo.common.constant.MessageConstant;
import com.mo.common.enumeration.AfterSaleStatus;
import com.mo.common.enumeration.OrderStatus;
import com.mo.common.exception.OrderBusinessException;
import com.mo.common.result.PageResult;
import com.mo.common.result.Result;
import com.mo.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/merchants")
@Tag(name = "商户管理")
public class MerchantController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AfterSaleService afterSaleService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private StatisticService statisticService;

    @Operation(summary = "获取商户订单列表")
    @Parameters({
            @Parameter(name = "orderId", description = "订单ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Order.class)))
    @GetMapping("/{orderId}")
    public Result<Order> getOrderById( @PathVariable Long orderId){
        log.info("查询订单{}", orderId);
        Order order = (Order) redisService.getEntity("order:" + orderId, Order.class);
        if(order == null) order = orderService.getOrderById(orderId);
        if(order == null) return Result.error();
        redisService.setEntity("order:" + orderId, order);

        return Result.success(order);
    }

    @Operation(summary = "修改订单状态", description = "修改指定订单的状态")
    @Parameters({
            @Parameter(name = "orderId", description = "订单ID", required = true),
            @Parameter(name = "status", description = "订单状态", required = true)
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Order.class)))
    @PutMapping("/orders/{orderId}/status")
    Result<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam Integer status){
        Order order = (Order) redisService.getEntity("order:" + orderId, Order.class);
        if(order == null) order = orderService.getOrderById(orderId);
        if(order == null) throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        OrderStatus orderStatus = OrderStatus.fromValue(status);
        order.setStatus(orderStatus);

        orderService.updateOrder(order);
        redisService.del("order:" + orderId);

        return Result.success(order);
    }

    @Operation(summary = "获取所有员工")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Employee.class)))
    @GetMapping("/staff")
    public Result<List<Employee>> getEmployee(){
        List<Employee> list = employeeService.getAllEmployee();

        return Result.success(list);
    }

    @Operation(summary = "获取员工分页")
    @Parameters({
            @Parameter(name = "employeePageQueryDTO", schema = @Schema(implementation = EmployeePageQueryDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Employee.class)))
    @GetMapping("/staff/page")
    public PageResult getEmployeePage(@RequestBody EmployeePageQueryDTO employeePageQueryDTO){
        int pageNum = employeePageQueryDTO.getPageNum();
        int pageSize = employeePageQueryDTO.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        int size = pageSize;

        List<Employee> list = employeeService.getEmployeePage(offset, size);

        return PageResult.success(list.size(), list, pageNum, pageSize);
    }

    @Operation(summary = "保存员工信息")
    @Parameters({
            @Parameter(name = "employee", schema = @Schema(implementation = EmployeeDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Employee.class)))
    @PostMapping("/staff")
    public Result<Employee> saveEmployee(@RequestBody EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeService.saveEmployee(employee);

        return Result.success(employee);
    }

    @Operation(summary = "修改员工信息")
    @Parameters({
            @Parameter(name = "id", description = "员工ID", required = true),
            @Parameter(name = "employeeDTO", description = "员工参数", required = true, schema = @Schema(implementation = EmployeeDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功")
    @PutMapping("/staff/{id}")
    public Result<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setId(id);

        employeeService.updateEmployee(employee);

        return Result.success();
    }

    @Operation(summary = "删除员工")
    @Parameters({
            @Parameter(name = "id", description = "员工ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "成功")
    @DeleteMapping("/staff/{id}")
    public Result<String> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);

        return Result.success();
    }

    @Operation(summary = "获取门店列表")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Store.class)))
    @GetMapping("/stores")
    public Result<List<Store>> getStore(){
        List<Store> list = storeService.getStore();

        return Result.success(list);
    }

    @Operation(summary = "修改门店信息")
    @Parameters({
            @Parameter(name = "id", description = "门店ID", required = true),
            @Parameter(name = "storeDTO", description = "门店参数", required = true, schema = @Schema(implementation = StoreDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功")
    @PutMapping("/stores/{id}")
    public Result<String> updateStore(@PathVariable Long id, StoreDTO storeDTO){
        Store store = new Store();
        BeanUtils.copyProperties(storeDTO, store);
        store.setId(id);
        storeService.updateStore(store);

        return Result.success();
    }

    @Operation(summary = "获取所有用户")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Customer.class)))
    @GetMapping("/users")
    public Result<List<Customer>> getAllCustomer() {
        List<Customer> list = customerService.getAll();

        return  Result.success(list);
    }

    @Operation(summary = "搜索用户")
    @Parameters ({
            @Parameter(name = "name", description = "用户名", required = true),
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Customer.class)))
    @GetMapping("/users/search")
    public Result<List<Customer>> searchForCustomer(@RequestParam String name){
        List<Customer> list = customerService.searchForCustomer(name);

        return Result.success(list);
    }

    @Operation(summary = "获取售后列表")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = AfterSale.class)))
    @GetMapping("/after-sale")
    public Result<List<AfterSale>> getAfterSales() {
        return Result.success(afterSaleService.getAfterSales());
    }

    @Operation(summary = "审批售后")
    @Parameters({
            @Parameter(name = "approveDTO", description = "审批参数", required = true, schema = @Schema(implementation = ApproveDTO.class))
    })
    @PostMapping("/after-sale/approve")
    public Result<String> approveAfterSale(@RequestBody ApproveDTO approveDTO){
        AfterSale afterSale = new AfterSale();
        afterSale.setStatus(AfterSaleStatus.approved);
        afterSale.setId(approveDTO.getRequestId());

        afterSaleService.updateAfterSale(afterSale);

        return Result.success();
    }

    @Operation(summary = "拒绝售后")
    @Parameters({
            @Parameter(name = "approveDTO", description = "审批参数", required = true, schema = @Schema(implementation = ApproveDTO.class))
    })
    @PostMapping("/after-sale/reject")
    public Result<String> RejectAfterSale(@RequestBody ApproveDTO approveDTO){
        AfterSale afterSale = new AfterSale();
        afterSale.setStatus(AfterSaleStatus.rejected);
        afterSale.setId(approveDTO.getRequestId());

        afterSaleService.updateAfterSale(afterSale);

        return Result.success();
    }

    @Operation(summary = "获取优惠券列表")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Coupon.class)))
    @GetMapping("/coupons")
    public Result<List<Coupon>> getCoupons() {
        List<Coupon> list = couponService.getAllCoupons();

        return Result.success(list);
    }

    @Operation(summary = "添加优惠券")
    @Parameters({
            @Parameter(name = "couponDTO", description = "优惠券参数", required = true, schema = @Schema(implementation = CouponDTO.class))
    })
     @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CouponVO.class)))
    @PostMapping("/coupons")
    public Result<CouponVO> addCoupon(@RequestBody CouponDTO dto) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(dto, coupon);
        coupon.setUserId(null);

        Long couponId = couponService.saveCoupon(coupon);

        CouponVO couponVO = CouponVO.builder()
                .couponId(couponId)
                .status("success")
                .build();

        return Result.success(couponVO);
    }

    @Operation(summary = "删除优惠券")
    @Parameter( name = "couponId", description = "优惠券ID", required = true)
    @DeleteMapping("/coupons/{couponId}")
    public Result<String> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCouponById(couponId);

        return Result.success();
    }

    @Operation(summary = "修改优惠券")
    @Parameters({
            @Parameter(name = "couponId", description = "优惠券ID", required = true),
            @Parameter(name = "couponDTO", description = "优惠券参数", required = true, schema = @Schema(implementation = CouponDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = CouponVO.class)))
    @PutMapping("/coupons/{couponId}")
    public Result<CouponVO> updateCoupon(@PathVariable Long couponId, @RequestBody CouponDTO dto) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(dto, coupon);
        coupon.setUserId(null);
        coupon.setId(couponId);

        couponService.updateCoupon(coupon);

        CouponVO couponVO = CouponVO.builder()
                .couponId(couponId)
                .status("success")
                .build();

        return Result.success(couponVO);
    }

    @Operation(summary = "发放优惠券")
    @Parameters({
            @Parameter (name = "distributeCouponDTO", description = "发放优惠券参数", required = true, schema = @Schema(implementation = DistributeCouponDTO.class))
    })
    @PostMapping("/coupons/distribute")
    public Result<String> distributeCoupon(@RequestBody DistributeCouponDTO dto) {
        Coupon coupon = couponService.getCouponById(dto.getCouponId());
        List<Long> ids = dto.getCustomerIds();

        for(Long customerId : ids) {
            coupon.setUserId(customerId);
            couponService.saveCoupon(coupon);
        }

        return Result.success();
    }

    @Operation(summary = "获取优惠活动列表")
    @ApiResponse (responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Promotion.class)))
    @GetMapping("/promotions")
    public Result<List<Promotion>> getPromotions() {
        List<Promotion> list = promotionService.getPromotion();

        return Result.success(list);
    }

    @Operation(summary = "添加优惠活动")
    @Parameters({
            @Parameter(name = "promotionDTO", description = "优惠活动参数", required = true, schema = @Schema(implementation = PromotionDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = PromotionVO.class)))
    @PostMapping("/promotions")
    public Result<PromotionVO> savePromotion(@RequestBody PromotionDTO dto) {
        Promotion promotion = new Promotion();
        BeanUtils.copyProperties(dto, promotion);
        promotionService.savePromotion(promotion);

         PromotionVO promotionVO = PromotionVO.builder()
                .promotionId(promotion.getId())
                .status("success")
                .build();

        return Result.success(promotionVO);
    }

    @Operation(summary = "添加优惠活动")
    @Parameter(name = "promotionId", description =  "优惠活动ID", required = true)
    @DeleteMapping("/promotions/{promotionId}")
    public Result<String> deletePromotion(@PathVariable Long promotionId) {
        promotionService.deletePromotion(promotionId);

        return Result.success();
    }

    @Operation(summary = "添加优惠活动")
    @Parameters({
            @Parameter(name = "promotionId", description =  "优惠活动ID", required = true),
            @Parameter(name = "promotionDTO", description = "优惠活动参数", required = true, schema = @Schema(implementation = PromotionDTO.class))
    })
    @PutMapping("/promotions/{promotionId}")
    public Result<String> updatePromotion(@PathVariable Long promotionId, @RequestBody PromotionDTO dto) {
        Promotion promotion = new Promotion();
        BeanUtils.copyProperties(dto, promotion);
        promotion.setId(promotionId);

        promotionService.updatePromotion(promotion);

        return Result.success();
    }

    @Operation (summary = "获取销售数据")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Product.class)))
    @GetMapping("/sales")
    public Result<List<Product>> getSales(){

        List<Product> list = statisticService.getSales();

        return Result.success(list);
    }

    @Operation (summary = "获取流量数据")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Integer.class)))
    @GetMapping("/traffic")
    public Result<Integer> getTraffic(){
        Integer traffic = statisticService.getTraffic();

        return Result.success(traffic);
    }

    @Operation (summary = "获取销售总额")
    @ApiResponse (responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = BigDecimal.class)))
    @GetMapping("/sales/total")
    public Result<BigDecimal> getSalesTotal(){
        double total = statisticService.getSalesTotal();
        BigDecimal res = BigDecimal.valueOf(total);

        return Result.success(res);
    }
}
