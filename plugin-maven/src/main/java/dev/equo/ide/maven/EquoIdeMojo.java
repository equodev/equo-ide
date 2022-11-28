package dev.equo.ide.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "equoIde")
public class EquoIdeMojo extends AbstractMojo {
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		System.out.println("LAUNCH THE IDE");
	}
}
