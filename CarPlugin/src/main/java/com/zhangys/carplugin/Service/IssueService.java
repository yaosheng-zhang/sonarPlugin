package com.zhangys.carplugin.Service;

import com.zhangys.carplugin.Entity.CodeLine;
import com.zhangys.carplugin.Entity.Issue;
import com.zhangys.carplugin.Utils.CppMethodExtractor;
import com.zhangys.carplugin.Utils.Generator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class IssueService {
    private final static String BASIC_PATH="/data/jenkins_home/workspace/";
    public List<CodeLine> fixByGpt(Issue issue) throws FileNotFoundException {
        String adr = dealPath(issue.getPath());
        String content = CppMethodExtractor.extracted(adr,issue);
        System.out.println(content);
        String res = settleSmell(content, issue);
        List<CodeLine> codeLines = splitString(content,res);
        return codeLines;

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

    /**
     * 结果处理函数
     *
     * @param content
     * @param res
     * @return
     */
    public static List<CodeLine> splitString(String content, String res) {
        List<CodeLine> sources = new ArrayList<>();
        List<String> contentList = Arrays.asList(content.split("\n"));
        List<String> collect = contentList.stream().map(x -> x.replaceAll(" ", "")).collect(Collectors.toList());



        List<String> resList = Arrays.asList(res.split("\\n"));



        // 遍历每行数据，将其存储到 HashMap 中
        for (int i = 0; i < resList.size(); i++) {
            String line = resList.get(i);
            String s = line.replaceAll(" ", "");
            Boolean isRefactor=false;
            if (!collect.contains(s))
            {
                isRefactor=true;
            }
            CodeLine codeLine = new CodeLine(i,resList.get(i),isRefactor);
            sources.add(codeLine);
        }
        return sources;

    }



}

