package com.zhangys.carplugin.Entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "通用PI接口返回", description = "Common Api Response")
public class Issue implements Serializable {
    @ApiModelProperty(value = "路径", required = true)
    private String path;
    @ApiModelProperty(value = "错误行数", required = true)
    private Integer line;
    @ApiModelProperty(value = "错误信息", required = true)
    private String msg;



}
