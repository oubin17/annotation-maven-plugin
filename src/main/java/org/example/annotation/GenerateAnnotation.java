package org.example.annotation;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GenerateAnnotation
 *
 * @author oubin.ob
 * @version : GenerateAnnotation.java v 0.1 2022/6/24 16:36 oubin.ob Exp $$
 */

/**
 * @requiresDependencyResolution compile
 */
@Mojo(name = "field")
public class GenerateAnnotation extends AbstractMojo {

    @Parameter(name = "filePath", defaultValue = "null")
    private String filePath;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true, required = true)
    private List<String> compilePath;


    public void execute() {

        try {
            generateAnnotation(getFileContext(filePath));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            Class<?> clazz = getClassLoader(this.project).loadClass("com.example.demo.inter.QueryMemberRequest");
//
//            List<String> fieldList = new ArrayList<>();
//
//            Map<String, String> fieldAnnotationMap = new HashMap<>();
//
//            Field[] declaredFields = clazz.getDeclaredFields();
//            for (Field field : declaredFields) {
//                String fieldStr = "private-" + field.getType().getSimpleName() + "-" + field.getName();
//                fieldList.add(fieldStr);
//                if (field.getAnnotations().length != 0) {
//                    fieldAnnotationMap.put(fieldStr, generateExplain(field));
//                }
//
//            }
//            fieldAnnotationMap.forEach((str1, str2) -> getLog().info(str1 + " ///// " + str2));
//
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    private ClassLoader getClassLoader(MavenProject project) {
        try {
            // 所有的类路径环境，也可以直接用 compilePath
            List classpathElements = project.getCompileClasspathElements();

            classpathElements.add(project.getBuild().getOutputDirectory());
            classpathElements.add(project.getBuild().getTestOutputDirectory());
            // 转为 URL 数组
            URL urls[] = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File((String) classpathElements.get(i)).toURL();
            }
            // 自定义类加载器
            return new URLClassLoader(urls, this.getClass().getClassLoader());
        } catch (Exception e) {
            getLog().debug("Couldn't get the classloader.");
            return this.getClass().getClassLoader();
        }
    }

    private void generateAnnotation(List<String> context) throws ClassNotFoundException, IOException {
        Class<?> clazz = getClassLoader(this.project).loadClass("com.example.demo.inter.QueryMemberRequest");

        List<String> fieldList = new ArrayList<>();

        Map<String, String> fieldAnnotationMap = new HashMap<>();

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldStr = "private-" + field.getType().getSimpleName() + "-" + field.getName();
            fieldList.add(fieldStr);
            if (field.getAnnotations().length != 0) {
                fieldAnnotationMap.put(fieldStr, generateExplain(field));
            }

        }

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

        List<String> collect = context.stream().filter(str -> !str.contains("* ** validator")).collect(Collectors.toList());

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


        writeFile(realLinkedList);
    }

    public static String generateExplain(Field field) {

        StringBuilder stringBuilder = new StringBuilder(256);

        stringBuilder.append("    /**\r\n" +
                "     *\r\n" +
                "     * <div style=\"display:none\">start validator</div>\r\n");

        NotNull notNull = field.getAnnotation(NotNull.class);
        if (notNull != null) {
            stringBuilder.append("     * <p>NotNull : true</p>\r\n");
        }

        NotBlank notBlank = field.getAnnotation(NotBlank.class);
        if (notBlank != null) {
            stringBuilder.append("     * <p>NotBlank : true</p>\r\n");
        }

        Min min = field.getAnnotation(Min.class);
        if (min != null) {
            stringBuilder.append("     * <p>Min : value = ").append(min.value()).append(", message = ").append(min.message()).append("</p>\r\n");
        }
        stringBuilder.append("     * <div style=\"display:none\">end validator</div>\r\n");
        stringBuilder.append("     */\r\n");

        return stringBuilder.toString();
    }

    /**
     * write context to file
     *
     * @param fileContext
     * @throws IOException
     */
    private void writeFile(LinkedList<String> fileContext) throws IOException {

        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
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
    private List<String> getFileContext(String filePath) {
        File file = new File(filePath);

        List<String> fileContents = new ArrayList<>();

        try {
            FileInputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                fileContents.add(line);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContents;
    }

}
