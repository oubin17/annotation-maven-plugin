package org.example.annotation;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * FileHandler
 *
 * @author oubin.ob
 * @version : FileHandler.java v 0.1 2022/7/1 10:06 oubin.ob Exp $$
 */
public class FileHandler {

    /**
     * 根据文件路径获取文件
     *
     * @param filePath
     * @return
     */
    protected static Map<String, Object> getFileContext(String filePath) {
        Map<String, Object> contentMap = new HashMap<>();

        File file = new File(filePath);

        List<String> fileContents = new ArrayList<>();

        String packagePath = null;
        String className = null;

        try {
            FileInputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                fileContents.add(line);
                if (line.contains("package") ) {
                    String[] split = line.split("\\s+");
                    if (split.length == 2) {
                        packagePath = split[1].replace(";", "");
                    }
                }

                if (line.contains("interface") || line.contains("class")) {
                    String[] split = line.split("\\s+");
                    if (split.length >= 3) {
                        className = split[2];
                    }
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        contentMap.put("packagePath", packagePath);
        contentMap.put("className", className);
        contentMap.put("content", fileContents);

        return contentMap;
    }

    /**
     * 写文件
     *
     * @param fileContext
     * @throws IOException
     */
    protected static void writeFile(LinkedList<String> fileContext, String filePath) throws IOException {

        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        for (String context : fileContext) {
            fileWriter.write(context);
        }
        fileWriter.close();

    }


    /**
     * 清除原本字段上已有的用插件生成的注释
     *
     * @param context
     * @return
     */
    protected static List<String> cleanExistedAnnotation(List<String> context) {
        boolean flag1 = false;
        for (int i = 0; i < context.size(); i++) {
            context.set(i, context.get(i) + "\r\n");
            if (context.get(i).contains("* <div style=\"display:none\">start validator</div>")) {
                context.set(i - 1, context.get(i - 1) + "* ** validator");
                context.set(i - 2, context.get(i - 2) + "* ** validator");
                flag1 = true;

            }
            if (flag1) {
                context.set(i, context.get(i) + "* ** validator");
            }
            if (context.get(i).contains("* <div style=\"display:none\">end validator</div>")) {
                context.set(i + 1, context.get(i + 1) + "* ** validator");
                i++;
                flag1 = false;
            }

        }

        return context.stream().filter(str -> !str.contains("* ** validator")).collect(Collectors.toList());
    }


}
