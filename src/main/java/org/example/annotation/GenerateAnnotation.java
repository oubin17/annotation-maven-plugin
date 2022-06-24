package org.example.annotation;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GenerateAnnotation
 *
 * @author oubin.ob
 * @version : GenerateAnnotation.java v 0.1 2022/6/24 16:36 oubin.ob Exp $$
 */
@Mojo(name = "field")
public class GenerateAnnotation extends AbstractMojo {

    @Parameter(name = "filePath", defaultValue = "null")
    private String filePath;


    public void execute() {

        try {
            generateAnnotation(getFileContext(filePath));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void generateAnnotation(String context) throws ClassNotFoundException, IOException {
        Class<?> clazz = Class.forName("com.example.demo.inter.QueryMemberRequest");

        List<String> fieldList = new ArrayList<String>();

        Map<String, String> fieldAnnotationMap = new HashMap<String, String>();

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldStr = "private-" + field.getType().getSimpleName() + "-" + field.getName();
            fieldList.add(fieldStr);
            if (field.getAnnotations().length != 0) {
                fieldAnnotationMap.put(fieldStr, generateExplain(field));
            }

        }

        String[] splits = context.split("\n");

        boolean flag1 = false;
        for (int i = 0; i < splits.length; i++) {
            splits[i] = splits[i] + "\r\n";
            if (splits[i].contains("* ** start validator")) {
                splits[i - 1] = splits[i - 1] + "* ** validator";
                splits[i - 2] = splits[i - 2] + "* ** validator";
                flag1 = true;

            }
            if (flag1) {
                splits[i] = splits[i] + "* ** validator";
            }
            if (splits[i].contains("* ** end validator")) {
                splits[i + 1] = splits[i + 1] + "* ** validator";
                i++;
                flag1 = false;
            }

        }

        List<String> collect = Arrays.stream(splits).filter(str -> !str.contains("* ** validator")).collect(Collectors.toList());

        LinkedList<String> linkedList = new LinkedList<>(collect);


        LinkedList<String> realLinkedList = new LinkedList<>(collect);

        for (String fields : fieldList) {
            String[] split = fields.split("-");
            for (int j = 0; j < linkedList.size(); j++) {
                if (linkedList.get(j).contains(split[0]) && linkedList.get(j).contains(split[1]) && linkedList.get(j).contains(split[2])) {
                    for (int k = j; k < realLinkedList.size(); k++) {
                        if (realLinkedList.get(k).contains(split[0]) && realLinkedList.get(k).contains(split[1]) && realLinkedList.get(k).contains(split[2])) {
                            for (int x = k - 1; x > 0; x--) {
                                if (!realLinkedList.get(x).trim().startsWith("@")) {
                                    if (fieldAnnotationMap.get(fields) != null) {
                                        realLinkedList.add(x + 1, fieldAnnotationMap.get(fields));
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }

                }
            }
        }

        writeFile(realLinkedList);
    }

    public static String generateExplain(Field field) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("    /**\r\n" +
                "     *\r\n" +
                "     * ** start validator\r\n");

        NotNull notNull = field.getAnnotation(NotNull.class);
        if (notNull != null) {
            stringBuilder.append("     * NotNull : true\r\n");
        }

        NotBlank notBlank = field.getAnnotation(NotBlank.class);
        if (notBlank != null) {
            stringBuilder.append("     * NotBlank : true\r\n");
        }

        Min min = field.getAnnotation(Min.class);
        if (min != null) {
            stringBuilder.append("     * Min : value = ").append(min.value()).append(", message = ").append(min.message()).append("\r\n");
        }
        stringBuilder.append("     * ** end validator\r\n");
        stringBuilder.append("     */\r\n");

        return stringBuilder.toString();

    }

    private void writeFile(LinkedList<String> fileContext) throws IOException {

        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file.getName());
        for (String context : fileContext) {
            fileWriter.write(context);
        }
        fileWriter.close();

    }


    /**
     * get file context
     *
     * @param filePath
     * @return
     */
    private String getFileContext(String filePath) {
        File file = new File(filePath);
        Long fileLength = file.length();

        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String fileStr = new String(fileContent);
        System.out.println(fileStr);
        return fileStr;
    }

}
