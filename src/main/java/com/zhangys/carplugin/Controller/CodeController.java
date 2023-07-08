package com.zhangys.carplugin.Controller;

import com.zhangys.carplugin.Service.CodeService;
import com.zhangys.carplugin.Utils.CppMethodExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

/**
 * @author Xzzz
 * @data 2023/07/07
 */
@CrossOrigin
@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    private CodeService codeService;
    //1.根据文件名获取文件中的信息
    @GetMapping
    public String getOriginCode(@RequestParam String path) throws FileNotFoundException {
        return codeService.getOriginCode(path);
    }
    //2.git上传功能
    @GetMapping("/git")
    public Boolean upLoadCode(@RequestParam String path)
    {
        return codeService.upLoadCode(path);
    }
    //3.保存功能
    @PutMapping
    public Boolean saveModifiedCode(@RequestParam String path,@RequestParam String modifiedCode){
        return codeService.saveModifiedCode(path,modifiedCode);
    }
    //4.查看元我呢提

}
