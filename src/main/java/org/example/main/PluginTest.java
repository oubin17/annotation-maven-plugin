package org.example.main;

import org.example.annotation.GenerateAnnotation;

import java.io.IOException;

/**
 * PluginTest
 *
 * @author oubin.ob
 * @version : PluginTest.java v 0.1 2022/7/1 11:07 oubin.ob Exp $$
 */
public class PluginTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        String filePath = "/Users/oubin/demo_project/annotation-maven-plugin/src/main/java/org/example/request/QueryMemberRequest.java";

        GenerateAnnotation generateAnnotation = new GenerateAnnotation();
        generateAnnotation.setFilePath(filePath);

        generateAnnotation.testExecute();


    }
}
