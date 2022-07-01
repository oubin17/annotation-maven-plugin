package org.example.annotation;

import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * MavenClassLoader
 *
 * @author oubin.ob
 * @version : MavenClassLoader.java v 0.1 2022/7/1 10:07 oubin.ob Exp $$
 */
public class MavenClassLoader {

    private MavenProject project;

    /**
     *
     * @return
     */
    protected ClassLoader getClassLoader() {
        try {
            // ���е���·��������Ҳ����ֱ���� compilePath
            List classpathElements = project.getCompileClasspathElements();

            classpathElements.add(project.getBuild().getOutputDirectory());
            classpathElements.add(project.getBuild().getTestOutputDirectory());
            // תΪ URL ����
            URL urls[] = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File((String) classpathElements.get(i)).toURL();
            }
            // �Զ����������
            return new URLClassLoader(urls, this.getClass().getClassLoader());
        } catch (Exception e) {
            return this.getClass().getClassLoader();
        }
    }

    public MavenClassLoader(MavenProject project) {
        this.project = project;
    }
}
