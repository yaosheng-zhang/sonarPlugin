package com.zhangys.carplugin.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeLine implements Serializable {
    private int line;
    private String code;
    private Boolean isRefator;
}
