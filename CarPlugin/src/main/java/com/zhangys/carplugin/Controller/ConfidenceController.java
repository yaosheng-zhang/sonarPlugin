package com.zhangys.carplugin.Controller;


import com.zhangys.carplugin.Entity.Confidence;
import com.zhangys.carplugin.Entity.Issue;
import com.zhangys.carplugin.Service.ConfidenceService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileNotFoundException;

@RestController
@Api(tags = "优化警告插件")
@RequestMapping("/alert")
public class ConfidenceController {

    @Resource
    private ConfidenceService confidenceService;

    @PostMapping(value = "/action")
    public Confidence getConfidence(@RequestBody Issue issue) throws FileNotFoundException {
        return confidenceService.getConfidenceService(issue);
    }


}
