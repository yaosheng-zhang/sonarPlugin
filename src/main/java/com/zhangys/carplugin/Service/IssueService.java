package com.zhangys.carplugin.Service;

import com.zhangys.carplugin.Entity.FixRecords;
import com.zhangys.carplugin.Entity.Issue;
import com.zhangys.carplugin.Utils.CppMethodExtractor;
import com.zhangys.carplugin.Utils.DiffHandleUtils;
import com.zhangys.carplugin.Utils.Generator;
import com.zhangys.carplugin.repository.IssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;



@Slf4j
@Component
public class IssueService {

    @Resource
    private IssueRepository issueRepository;
    private final static String BASIC_PATH="/data/jenkins_home/workspace/";

    public String fixByGpt(Issue issue) throws FileNotFoundException {
        String id = issue.getId();
        boolean isExistIssue = issueRepository.existsById(id);
        String content = "";
        if (isExistIssue)
        {
            FixRecords fixRecords = issueRepository.findById(id).orElse(null);
            content = fixRecords.getDiffCode();
            return content;
        }

        String adr = dealPath(issue.getPath());
        content = CppMethodExtractor.extracted(adr,issue);
        System.out.println(content);
        String res = settleSmell(content, issue);
        String diffCode= getStringBuilder(content, res);


        //修复后的结果持久化到数据库中
        LocalDate now = LocalDate.now();
        issueRepository.save(new FixRecords(id,content,res,diffCode,now));


        return diffCode;

    }

    private static String getStringBuilder(String content, String res) {
        List<String> contentList = toList(content);
        List<String> resList = toList(res);
        List<String> diffString = DiffHandleUtils.diffString(contentList,resList);
        StringBuilder builder = new StringBuilder();
        for (String line : diffString) {
            builder.append(line);
            builder.append("\n");
        }
        return builder.toString();
    }

    private static List<String> toList(String res) {
        String[] split = res.split("\n");
        List<String> resList = Arrays.asList(split);
        return resList;
    }
//    public Error getIssue(Issue issue) {
//        String adr = dealPath(issue.getPath());
//
//    }

    /**
     * 寻址
     * @param path
     * @return
     */
    public String dealPath(String path)
    {
        String projectName = path.substring(0, path.indexOf(':'));
        String  fileName= path.substring(path.indexOf(':')+1);
        String adr = BASIC_PATH+projectName+'/'+fileName;
        System.out.println(adr);
        return adr;
    }

    /**
     * 异味重构
     * @param content
     * @param issue
     * @return
     */
    public static String settleSmell(String content,Issue issue) {


        //2.通过模型得到重构的回答
        Generator generator = new Generator();
        Integer line = issue.getLine();
        String msg = issue.getMsg();

        String prompt = getPrompt(content, msg,line);
        System.out.println(prompt);
        String codeFromModle = generator.getCodeFromModle(prompt);
        System.out.println(codeFromModle);
        return codeFromModle;
    }

    /**
     * prompt生成
     * @param content
     * @param msg
     * @param line
     * @return
     */

        public static String getPrompt(String content,String msg,Integer line){


            String prompt = "你的任务是根据MISRA的要求和规范对下面代码中指定代码行进行重构\n代码如下："+content+"\n"
                    +"出现问题的行数为："+line+"违反的Misra规则为:"+msg+
                    "\n返回的结果中只需要出现重构后的结果，非代码部分必须以JAVA注释 // 的方式出现"
                    ;

            return prompt;
        }

//    /**
//     * 结果处理函数
//     *
//     * @param content
//     * @param res
//     * @return
//     */
//    public static List<CodeLine> splitString(String content, String res) {
//        List<CodeLine> sources = new ArrayList<>();
//        List<String> contentList = Arrays.asList(content.split("\n"));
//        List<String> collect = contentList.stream().map(x -> x.replaceAll(" ", "")).collect(Collectors.toList());
//
//
//
//        List<String> resList = Arrays.asList(res.split("\\n"));
//
//
//
//        // 遍历每行数据，将其存储到 HashMap 中
//        for (int i = 0; i < resList.size(); i++) {
//            String line = resList.get(i);
//            String s = line.replaceAll(" ", "");
//            Boolean isRefactor=false;
//            if (!collect.contains(s))
//            {
//                isRefactor=true;
//            }
//            CodeLine codeLine = new CodeLine(i,resList.get(i),isRefactor);
//            sources.add(codeLine);
//        }
//        return sources;
//
//    }



}

