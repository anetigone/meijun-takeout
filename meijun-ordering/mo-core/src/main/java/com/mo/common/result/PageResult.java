package com.mo.common.result;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "分页查询结果")
public class PageResult implements Serializable {

    private long total; //总记录数
    private List<?> records; //当前页数据集合
    private int current;//当前页码
    private int size;//每页记录数

    private Integer code;
    private String msg;

    public static PageResult success(long total, List<?> records, int current, int size) {
        return PageResult.builder()
                .code(ResultCode.getSuccessCode())
                .msg(ResultCode.getSuccessMsg())
                .total(total)
                .records(records)
                .current(current)
                .size(size)
                .build();
    }

    public static PageResult success() {
        return PageResult.builder()
                .code(ResultCode.getSuccessCode())
                .msg(ResultCode.getSuccessMsg())
                .build();
    }

    public static PageResult error() {
        return PageResult.builder()
                .code(ResultCode.getErrorCode())
                .msg(ResultCode.getErrorMsg())
                .build();
    }

}
