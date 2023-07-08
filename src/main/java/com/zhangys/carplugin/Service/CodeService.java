package com.zhangys.carplugin.Service;

import com.zhangys.carplugin.Utils.CppMethodExtractor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.zhangys.carplugin.Entity.Constant.FIX_BASIS_ADR;

/**
 * @author Xzzz
 * @data 2023/07/07
 */

@Service
public class CodeService {
    public String getOriginCode(String path)  {

        String file_path = CppMethodExtractor.dealPath(path);
        StringBuilder originCode = new StringBuilder();
        List<String> list = new ArrayList<>();
        try {
            list = CppMethodExtractor.readCppFile(file_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String s : list) {
            originCode.append(s).append("\n");
        }
        System.out.println(originCode);
        return originCode.toString();

    }

    public Boolean saveModifiedCode(String path, String code) {
       String filePath = CppMethodExtractor.dealPath(path);
        try {
            PrintStream printStream = new PrintStream(filePath);
            printStream.print(code);
            printStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return  true;
    }

    public Boolean upLoadCode(String path) {
        String directoryName = path.substring(0,path.indexOf(":"));
        String directoryPath = FIX_BASIS_ADR+directoryName;
        String filePath = CppMethodExtractor.dealPath(path);
        String command = "sh upload_file.sh " +directoryPath+" "+filePath;
        try {
            Process exec = Runtime.getRuntime().exec(command);
            int execStatus = exec.waitFor();
            if (execStatus==0)
            {
                return true;
            }
            else
                return false;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
