package com.zhangys.carplugin.Controller;



import com.zhangys.carplugin.Entity.Issue;
import com.zhangys.carplugin.Service.IssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


@CrossOrigin
@RestController
@Api(tags = "自动修复Api接口")
@RequestMapping("/issue")
public class IssueController {

    @Resource
    private IssueService issueService;

    @GetMapping
    @ApiOperation(value = "获取代码信息")
    public String  fixIssue(@RequestParam String path , @RequestParam Integer line ) throws FileNotFoundException {
        return issueService.getCode(path,line);
    }

    @PostMapping (value = "/fix",consumes = "application/json")
    @ApiOperation(value = "获取修复信息")
    public String  fixIssue(@RequestBody Issue issue) throws Exception {
        return issueService.fixByGpt(issue);
    }

    @GetMapping(value = "/judge")
    @ApiOperation(value = "评估重估结果")
    @CrossOrigin(origins = "*",allowedHeaders = "*")
    public boolean judgeInfo(@RequestParam String id) throws IOException, InterruptedException {
        System.out.println(id);
        return issueService.getJudgeInfo(id);
    }



//    @PostMapping(value = "/info",consumes = "application/json")
//    @ApiOperation(value = "获取问题类别")
//    public Error getIssue(@RequestBody Issue issue){
//        return issueService.getIssue(issue);
//    }

}
