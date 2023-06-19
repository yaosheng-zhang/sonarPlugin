package com.zhangys.carplugin.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Confidence {
    private Double confidenceValue;
    private String reason;
}
