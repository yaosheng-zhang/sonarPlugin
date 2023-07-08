package com.zhangys.carplugin.Utils;

import com.zhangys.carplugin.Entity.Issue;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhangys.carplugin.Entity.Constant.FIX_BASIS_ADR;

@Data
public class CppMethodExtractor {

    private  Integer methodStart =-1;
    private  Integer methodEnd =-1;


    public  String extracted(String filePath, Integer lineNumber) throws FileNotFoundException {

        StringBuilder context=new StringBuilder();
        List<String> lines = new ArrayList<>();

        try {
            lines = readCppFile(filePath);
            List<String> methodLines = extractMethod(lines, lineNumber);

            if (!methodLines.isEmpty()) {
                for (String methodLine : methodLines) {
                    context.append(methodLine+"\n");
                }

            }
            else {
//                List<String> surroundingLines = getSurroundingLines(filePath, lineNumber);
//                for (String surroundingLine : surroundingLines) {
//                    context.append(surroundingLine).append("\n");
//                }
                context.append(lines.get(lineNumber-1));

            }



        } catch (IOException e) {
            throw new FileNotFoundException("文件不存在");
        }
        return context.toString();
    }

    public static List<String> readCppFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private  List<String> extractMethod(List<String> lines, Integer lineNumber) {


        // Initialize the number of braces to 0
        int braceCount = 0;
        // Create a pattern to find the start of a method
        Pattern methodStartPattern = Pattern.compile("\\w+\\s+\\w+\\s*\\(.*\\)\\s*(const)?\\s*\\{|\\w+\\s+\\w+\\s*\\(.*\\)");
        Pattern structStartPattern = Pattern.compile("typedef\\s+(struct|union)\\s+\\w+\\s*\\{|typedef\\s+(struct|union)\\s+\\w+\\s*|typedef\\s+(struct|union)\\s+\\{|typedef\\s+(struct|union)\\s+|(struct|union)\\s+\\w+\\s*\\{|(struct|union)\\s+\\w+\\s*|(struct|union)\\s+\\{|(struct|union)\\s+");
        //判断是否是函数头
        String s = lines.get(lineNumber - 1);
        Matcher matcher1 = methodStartPattern.matcher(s);
        Matcher matcher2 = structStartPattern.matcher(s);
        if (matcher1.find()||matcher2.find())
        {
            methodStart=lineNumber-1;
            methodEnd=findMethodEnd(lines,methodStart,0);
        }

        else {
            // Iterate through the lines
            for (int i = 0; i < lines.size(); i++) {
                // Get the current line
                String line = lines.get(i);
                // If the line is not the line number minus 1
                if (i < lineNumber - 1) {
                    // Find the start of the method
                    Matcher matcher = methodStartPattern.matcher(line);
                    Matcher matcher3=structStartPattern.matcher(line);
                    // If the method is found
                    if (matcher.find()||matcher3.find()) {
                        // Set the method start to the current line
                        methodStart = i;
                        // Set the number of braces to 1
                        braceCount +=countBraces(line);
                    } else if (methodStart >= 0) {
                        // Increment the number of braces
                        braceCount += countBraces(line);
                        // If the number of braces is 0, set the method start to -1
                        if (braceCount == 0) {
                            methodStart = -1;
                        }
                    }
                } else if (i == lineNumber - 1) {
                    // If the method is found
                    if (methodStart >= 0) {
                        // Find the end of the method
                        methodEnd = findMethodEnd(lines, i, braceCount);
                        // Break out of the loop
                        break;
                    }
                }
            }

        }

        // If the method start and end are found
        if (methodStart >= 0 && methodEnd >= 0) {
            return lines.subList(methodStart, methodEnd + 1);
        } else {
            // Otherwise, return an empty list

            return new ArrayList<>();
        }
    }

    private static int countBraces(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == '{') {
                count++;
            } else if (c == '}') {
                count--;
            }
        }
        return count;
    }

    private  int findMethodEnd(List<String> lines, int startLine, int braceCount) {

        for (int i = startLine; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!(line.contains("{")||line.contains("}")))
            {
                continue;
            }
            braceCount += countBraces(line);
            if (braceCount == 0) {
                return i;
            }
        }
        return -1;
    }

    public   String getAcode(String path , Integer line) throws IOException {
        List<String> list = readCppFile(path);
        String s = list.get(line - 1);


        return s;
    }


    public Integer realLine(String content , String code ) throws Exception {
        String[] split = content.split("\n");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(code))
            {
                return i+1;
            }
        }
        throw new Exception("Code not found in list");
    }
    public static String dealPath(String path)
    {
        String projectName = path.substring(0, path.indexOf(':'));
        String  fileName= path.substring(path.indexOf(':')+1);
        String adr = FIX_BASIS_ADR+projectName+'/'+fileName;
//        String adr = FIX_BASIS_DEV_ADR+fileName;
        System.out.println(adr);
        return adr;
    }
//    public  List<String> getSurroundingLines(String filePath, int lineNumber) {
//        List<String> lines = new ArrayList<>();
//        List<String> resultLines = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                lines.add(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        int totalLines = lines.size();
//        int firstLine = Math.max(0, lineNumber - 6);
//        int lastLine = Math.min(totalLines, lineNumber + 10);
//
//        if (lastLine - firstLine < 16) {
//            if (firstLine == 0) {
//                lastLine = Math.min(totalLines, lastLine + (16 - (lastLine - firstLine)));
//            } else {
//                firstLine = Math.max(0, firstLine - (16 - (lastLine - firstLine)));
//            }
//        }
//
//        for (int i = firstLine; i < lastLine; i++) {
//            resultLines.add(lines.get(i));
//        }
//
//        return resultLines;
//    }
}
