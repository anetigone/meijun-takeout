package com.mo.web.controller;

import com.mo.api.dto.*;
import com.mo.api.service.AdminService;
import com.mo.api.service.EmployeeService;
import com.mo.api.service.StatisticService;
import com.mo.api.service.WorkspaceService;
import com.mo.api.vo.OrderOverviewVO;
import com.mo.api.vo.WorkspaceDataVO;
import com.mo.common.enumeration.UserIdentity;
import com.mo.common.result.PageResult;
import com.mo.common.result.Result;
import com.mo.entity.Admin;
import com.mo.entity.Employee;
import com.mo.entity.Product;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "管理员接口")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WorkspaceService workspaceService;

    @Operation(summary = "获取所有管理员")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Admin.class)))
    @GetMapping("/all")
    public Result<List<Admin>> getAll(){
        List<Admin> list = adminService.getAllAdmins();

        return Result.success(list);
    }

    @Operation(summary = "分页获取管理员")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Admin.class)))
    @GetMapping("/page")
    public PageResult getPage(AdminPageQueryDTO dto){
        int num = dto.getPageNum();
        int size = dto.getPageSize();
        int offset = (num - 1) * size;
        List<Admin> list = adminService.getPage(offset, size);

        return PageResult.success(list.size(), list, num, size);
    }

    @Operation(summary = "更新管理员")
    @Parameters({
            @Parameter(name = "adminUpdateDTO", description = "管理员更新参数", required = true, schema = @Schema(implementation = AdminUpdateDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = String.class)))
    @PutMapping("/update")
    public Result<String> update(AdminUpdateDTO dto){
        Admin admin = new Admin();
        BeanUtils.copyProperties(dto, admin);
        adminService.updateAdmin(admin);

        return Result.success();
    }

    @Operation(summary = "删除管理员")
    @Parameters({
            @Parameter(name = "id", description = "管理员id", required = true, schema = @Schema(implementation = Long.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = String.class)))
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id){
        adminService.delete(id);

        return Result.success();
    }

    @Operation(summary = "添加管理员")
    @Parameters({
            @Parameter(name = "admin", description = "管理员参数", required = true, schema = @Schema(implementation = Admin.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/add")
    public Result<String> add(@RequestBody AdminSaveDTO dto){
        Admin admin = new Admin();
        BeanUtils.copyProperties(dto, admin);
        admin.setIdentity(UserIdentity.ADMIN);

        adminService.saveAdmin(admin);

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

    @Operation (summary = "获取所有员工")
    @ApiResponse (responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Employee.class)))
    @GetMapping("/employee/all")
    public Result<List<Employee>>  getAllEmployee(){
        List<Employee> list = employeeService.getAllEmployee();

        return Result.success(list);
    }

    @Operation (summary = "分页获取员工")
     @Parameters ({
            @Parameter(name = "employeePageQueryDTO", description = "员工分页参数", required = true, schema = @Schema(implementation = EmployeePageQueryDTO.class))
    })
    @ApiResponse (responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Employee.class)))
    @GetMapping("/employee/page")
    public PageResult getEmployeePage(EmployeePageQueryDTO dto){
        int num = dto.getPageNum();
        int size = dto.getPageSize();
        int offset = (num - 1) * size;
        List<Employee> list = employeeService.getEmployeePage(offset, size);

        return PageResult.success(list.size(), list, num, size);
    }

    @Operation (summary = "添加员工")
    @Parameters ({
             @Parameter(name = "employeeDTO", description = "员工参数", required = true, schema = @Schema(implementation = EmployeeDTO.class))
    })
    @ApiResponse (responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/employee/add")
    public Result<String> addEmployee(@RequestBody EmployeeDTO dto){
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);

        employeeService.saveEmployee(employee);

        return Result.success();
    }

    @Operation (summary = "更新员工")
     @Parameters ({
             @Parameter(name = "id", description = "员工id", required = true, schema = @Schema(implementation = Long.class)),
             @Parameter(name = "employeeDTO", description = "员工参数", required = true, schema = @Schema(implementation = EmployeeDTO.class))
    })
    @PutMapping ("/employee/{id}/update")
    public Result<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO dto){
         Employee employee = new Employee();
         BeanUtils.copyProperties(dto, employee);
         employee.setId(id);

          employeeService.updateEmployee(employee);

          return Result.success();
    }

    @Operation (summary = "删除员工")
    @Parameters ({
            @Parameter(name = "id", description = "员工id", required = true, schema = @Schema(implementation = Long.class))
    })
    @DeleteMapping ("/employee/{id}/delete")
     public Result<String> deleteEmployee(@PathVariable Long id){
         employeeService.deleteEmployee(id);

         return Result.success();
    }

    @GetMapping("/workspace/data")
    public Result<WorkspaceDataVO> getWorkspaceData(){
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        WorkspaceDataVO workspaceDataVO = workspaceService.getWorkspaceData(begin, end);

        return Result.success(workspaceDataVO);
    }

    @GetMapping("/workspace/order/overview")
    public Result<OrderOverviewVO> getOrderOverview(){
        OrderOverviewVO orderOverviewVO = workspaceService.getOrderOverview();

        return Result.success(orderOverviewVO);
    }
}
