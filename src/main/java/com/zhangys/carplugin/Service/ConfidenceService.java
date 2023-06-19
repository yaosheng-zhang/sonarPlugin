package com.zhangys.carplugin.Service;

import com.google.gson.Gson;
import com.zhangys.carplugin.Entity.Confidence;
import com.zhangys.carplugin.Entity.Issue;
import com.zhangys.carplugin.Utils.Generator;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ConfidenceService {

    private final static String BASIC_PATH = "/data/jenkins_home/workspace/";

    public Confidence getConfidenceService(Issue issue) throws FileNotFoundException {
        String adr = dealPath(issue.getPath());
        String line = extracted(adr, issue);
//        String prompt = "{\"code\":"+line+",\n\"msg\":"+issue.getMsg()+"\n}";
        HashMap<String, String> map = new HashMap<>();
        map.put("code", line);
        map.put("msg", issue.getMsg());
        String s1 = map.toString();
        Generator generator = new Generator();
        String confidenceFromModle = generator.getConfidenceFromModle(s1);
        Gson gson = new Gson();
        Confidence jsonObject = gson.fromJson(confidenceFromModle, Confidence.class);
        String jsonResult = gson.toJson(jsonObject);
        return jsonObject;

    }


    /**
     * prompt生成
     *
     * @param content
     * @param msg
     * @param line
     * @return
     */

    public static String getPrompt(String content, String msg, Integer line) {


        String prompt = "你的任务是根据MISRA的要求和规范对下面代码中指定代码行进行重构\n代码如下：" + content + "\n"
                + "出现问题的行数为：" + line + "违反的Misra规则为:" + msg +
                "\n返回的结果中只需要出现重构后的结果，非代码部分必须以JAVA注释 // 的方式出现";

        return prompt;
    }

    /**
     * 寻址
     *
     * @param path
     * @return
     */
    public String dealPath(String path) {
        String projectName = path.substring(0, path.indexOf(':'));
        String fileName = path.substring(path.indexOf(':') + 1);
        String adr = BASIC_PATH + projectName + '/' + fileName;
        System.out.println(adr);
        return adr;
    }

    public static String extracted(String filePath, Issue issue) throws FileNotFoundException {

        Integer lineNumber = issue.getLine();
        StringBuilder context = new StringBuilder();
        List<String> lines = new ArrayList<>();

        try {
            lines = readCppFile(filePath);
            return lines.get(lineNumber - 1);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<String> readCppFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
