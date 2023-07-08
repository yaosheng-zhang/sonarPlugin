package com.zhangys.carplugin;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffAlgorithmListener;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.zhangys.carplugin.Entity.Issue;
import com.zhangys.carplugin.Service.IssueService;
import com.zhangys.carplugin.Utils.Generator;
import com.zhangys.carplugin.repository.ApiKeysRepository;
import org.assertj.core.util.diff.DiffAlgorithm;
import org.assertj.core.util.diff.myers.MyersDiff;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CarPluginApplicationTests {


//            @Test
//            void testGPT(){
//                Generator generator = new Generator();
//                String content ="static void misra_2_2(int x) {\n" +
//                        "    int a;\n" +
//                        "    a = x + 0; // 2.2\n" +
//                        "    a = 0 + x; // 2.2\n" +
//                        "    a = x * 0; // 2.2\n" +
//                        "    a = 0 * x; // 2.2\n" +
//                        "    a = x * 1; // 2.2\n" +
//                        "    a = 1 * x; // 2.2\n" +
//                        "    a = MISRA_2_2;\n" +
//                        "    (void)a;\n" +
//                        "}";
//
//                //认不到代码行数
//                Integer line = 5;
//                String msg = "Rule Required 2.2: There shall be no dead code.";
//                String prompt = "你的任务是根据MISRA的要求和规范对下面代码中指定代码行进行重构，若满足规范则直接返回\n代码如下：\n"+content+"\n"
//                        +"出现问题的行数为："+line+"\t   a = x * 0; "+"\n违反的Misra规则为:"+msg+
//                        "\n只需要重构给定代码的第"+line+"行代码并且返回重构后的整体代码，非代码部分必须以JAVA注释 // 的方式出现"
//                        ;
//                System.out.println(prompt);
//                String codeFromModle = generator.getCodeFromModle(prompt, apiLists);
//                System.out.println(codeFromModle);
//            }
            @Resource
    IssueService issueService;

            @Test
            void testPrompt(){
                String path = "D:\\project\\新建文件夹\\CarPlugin\\src\\test\\java\\file\\test.c";
                Integer line = 103;
                String msg = "Rule Advisory 2.7: There should be no unused parameters in functions.";
                Issue issue = new Issue("1", path, line, msg);



            }

            @Test
            void diff(){
                String text1 = "This is some text.";
                String text2 = "This is some    text.";

                // 去除文本中的空格
                text1 = text1.replaceAll("\\s+", "");
                text2 = text2.replaceAll("\\s+", "");

                // 将文本转换为行列表
                List<String> lines1 = Arrays.asList(text1.split("\n"));
                List<String> lines2 = Arrays.asList(text2.split("\n"));


                // 使用Myers算法创建diff算法
                DiffAlgorithm diffAlgorithm = new MyersDiff();

                // 获取差异
                Patch<String> patch = DiffUtils.diff(lines1, lines2, (DiffAlgorithmListener) diffAlgorithm);

                // 输出差异
                for (AbstractDelta<String> delta : patch.getDeltas()) {
                    System.out.println(delta);
                }
            }

    @Resource
    ApiKeysRepository apiKeysRepository;
         @Test
         void  getAPikeys()
         {

             List<String> allKeys = apiKeysRepository.findAllKeys();
             System.out.println(allKeys);

         }

     @Test
    void test_antlr_func(){
             
     }

}
