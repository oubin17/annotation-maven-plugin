package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * DemoMojo
 *
 * @author oubin.ob
 * @version : DemoMojo.java v 0.1 2022/6/24 15:47 oubin.ob Exp $$
 */
@Mojo(name = "facade")
public class DemoMojo extends AbstractMojo {

    @Parameter(name = "name", defaultValue = "kiwi")
    private String name;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("execute field annotation" + name);

    }
}
