package com.zhangys.carplugin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {

    private String filePath;
    private Integer line;
    private Integer beginLine;
    private Integer endLine;
    private String context;
    private String msg;
    private HashMap<Integer,String> errorMap;


}