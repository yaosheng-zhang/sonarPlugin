package com.zhangys.carplugin.Service;
import com.zhangys.carplugin.Entity.FixRecords;
import com.zhangys.carplugin.Entity.Issue;
import com.zhangys.carplugin.Utils.CppMethodExtractor;
import com.zhangys.carplugin.Utils.DiffHandleUtils;
import com.zhangys.carplugin.Utils.Generator;
import com.zhangys.carplugin.repository.IssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.zhangys.carplugin.Entity.Constant.FIX_BASIS_ADR;
import static com.zhangys.carplugin.Entity.Constant.JUDGE_BASIS_ADR;


@Slf4j
@Service
public class IssueService {

    @Resource
    private IssueRepository issueRepository;

    public String fixByGpt(Issue issue) throws Exception {




        //真实路径
        String address = dealPath(issue.getPath());
        String content = getCode(address, issue.getLine());
        //获取该行代码 和 对应行数
        CppMethodExtractor cppMethodExtractor = new CppMethodExtractor();
        String acode = cppMethodExtractor.getAcode(address, issue.getLine());
        Integer realLine = cppMethodExtractor.realLine(content, acode);



        boolean isExistIssue = issueRepository.existsById(issue.getId());
        if (isExistIssue)
        {

            FixRecords fixRecords = issueRepository.findById(issue.getId()).orElse(null);
            String originCode = fixRecords.getOriginCode();
            String str1 = originCode.trim().replaceAll("\\s+", "");
            String str2 = content.trim().replaceAll("\\s+", "");
            if (str1.equals(str2))
            {
                return fixRecords.getDiffCode();
            }
        }
        System.out.println(content);

        //形成prompt
        String prompt = getPrompt(content, issue.getMsg(), realLine, acode);

        String res = settleSmell(prompt);
        String diffCode= getStringBuilder(content, res);

        //修复后的结果持久化到数据库中
        LocalDate now = LocalDate.now();

        String projectName = issue.getPath().substring(0, issue.getPath().indexOf(':'));
        String msg = issue.getMsg();
        issueRepository.save(new FixRecords(issue.getId(),content,res,diffCode,projectName,msg,now));


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
        String adr = FIX_BASIS_ADR+projectName+'/'+fileName;
//        String adr = BASIC_PATH+fileName;
        System.out.println(adr);
        return adr;
    }

    /**
     * 异味重构
     *
     */
    public static String settleSmell(String prompt) {


        //2.通过模型得到重构的回答
        Generator generator = new Generator();

        System.out.println(prompt);
        String codeFromModle = generator.getCodeFromModle(prompt);
        System.out.println(codeFromModle);
        return codeFromModle;
    }

    /**
     * prompt生成
     *
     * @param content
     * @param msg
     * @param line
     * @param acode
     * @return
     */

        public static String getPrompt(String content, String msg, Integer line, String acode){



            String prompt = "你的任务是根据MISRA的要求和规范对下面代码中指定代码行进行重构，若满足规范则直接返回\n代码如下：\n"+content+"\n"
                    +"出现问题的行数为："+line+"\t代码为:"+acode+"\n违反的Misra规则为:"+msg+
                    "\n只需要重构给定代码的第"+line+"行代码并且返回重构后的整体代码，非代码部分必须以JAVA注释 // 的方式出现"
                    ;

            return prompt;
        }
        /*
        根据行号获取到代码上下文
         */
    public String getCode(String path, Integer line) throws FileNotFoundException {
        CppMethodExtractor cppMethodExtractor = new CppMethodExtractor();
        String code = cppMethodExtractor.extracted(path, line);
        return code;
    }

    public boolean getJudgeInfo(String id) throws InterruptedException, IOException {
        FixRecords byId = issueRepository.findById(id).orElse(null);
        String message = byId.getMessage();
        String postCode = byId.getPostCode();
        String adr = JUDGE_BASIS_ADR + "tem.c";
        File file = new File(adr);
        PrintStream printStream = new PrintStream(file);

        printStream.print(postCode);
        printStream.close();


        String command = "sudo sh /home/ngtl/jar/tem/cppcheck_start.sh";
        Process process = Runtime.getRuntime().exec(command);
        int i = process.waitFor();
//        String command = "cppcheck --rule=misra_c --addon=\"/usr/share/cppcheck/addons/misra.json\" -j 8 --xml --xml-version=2 \""+adr+"\" 2> "+JUDGE_BASIS_ADR+"cppcheck-result.xml";
        System.out.println("Command"+command + i);


        String cpp_check_xml=JUDGE_BASIS_ADR+"cppcheck-result.xml";
        Path path = Paths.get(cpp_check_xml);

        int maxTries = 10;
        int tries = 0;
        final int second = 1000;
        while (!Files.exists(path) && tries < maxTries) {
            Thread.sleep(second); // 等待1秒:
            tries++;
        }

        boolean flag=false;
        try {
           flag = exporter(cpp_check_xml,message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // 返回命令执行结果
        return flag;
    }

    public boolean exporter(String path,String message) throws Exception {
        File file = new File(path);
        Document parse = parse(file);
        Element rootElement = parse.getRootElement();
        List<Element> errors = rootElement.element("errors").elements();
        boolean flag=true;
        for (int i = 0; i < errors.size(); i++) {
            Element element = errors.get(i);
            String msg = element.attribute("msg").getValue();

            if (message.equals(msg))
            {
                flag=false;
                break;
            }
        }


        return flag;
    }

    public Document parse(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }





}

