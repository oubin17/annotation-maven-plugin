package org.example.annotation;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

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

        MavenClassLoader mavenClassLoader = new MavenClassLoader(project);

        try {
            generateAnnotation(FileHandler.getFileContext(filePath), mavenClassLoader);
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

    private void generateAnnotation(Map<String, Object> contentMap, MavenClassLoader mavenClassLoader) throws ClassNotFoundException, IOException {

        List<String> context = (List<String>) contentMap.get("content");
        String packagePath = (String) contentMap.get("packagePath");
        String className = (String) contentMap.get("className");

        Class<?> clazz = mavenClassLoader.getClassLoader(this.project).loadClass(packagePath + "." + className);

        List<String> fieldList = new ArrayList<>();

        Map<String, String> fieldAnnotationMap = new HashMap<>();

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldStr = "private-" + field.getType().getSimpleName() + "-" + field.getName();
            fieldList.add(fieldStr);
            if (field.getAnnotations().length != 0) {
                fieldAnnotationMap.put(fieldStr, AnnotationGenerateHandler.generateExplain(field));
            }

        }

        List<String> collect = FileHandler.cleanExistedAnnotation(context);

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
        FileHandler.writeFile(realLinkedList, filePath);
    }

}
