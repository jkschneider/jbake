package org.jbake;

import org.apache.commons.vfs2.util.Os;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectWebsiteTest {


    private static final String WEBSITE_REPO_URL = "https://github.com/jbake-org/jbake.org.git";

    @TempDir
    public File folder;
    private File projectFolder;
    private File outputFolder;
    private String jbakeExecutable;
    private BinaryRunner runner;

    @BeforeEach
    public void setup() throws IOException, GitAPIException {
        Assumptions.assumeTrue(!isJava7(), "JDK 7 is not supported for this test");
        if (Os.isFamily(Os.OS_FAMILY_WINDOWS)) {
            jbakeExecutable = new File("build\\install\\jbake\\bin\\jbake.bat").getAbsolutePath();
        } else {
            jbakeExecutable = new File("build/install/jbake/bin/jbake").getAbsolutePath();
        }
        projectFolder = newFolder(folder, "project");
        new File(projectFolder, "templates");
        outputFolder = new File(projectFolder, "output");

        runner = new BinaryRunner(projectFolder);
        cloneJbakeWebsite();

    }

    private boolean isJava7() {
        return System.getProperty("java.specification.version").equals("1.7");
    }

    private void cloneJbakeWebsite() throws GitAPIException {
        CloneCommand cmd = Git.cloneRepository();
        cmd.setBare(false);
        cmd.setBranch("master");
        cmd.setRemote("origin");
        cmd.setURI(WEBSITE_REPO_URL);
        cmd.setDirectory(projectFolder);

        cmd.call();

        assertThat(new File(projectFolder, "README.md").exists()).isTrue();
    }

    @Test
    public void shouldBakeWebsite() throws IOException, InterruptedException {
        Process process = runner.runWithArguments(jbakeExecutable, "-b");
        assertThat(process.exitValue()).isEqualTo(0);
        assertThat(new File(outputFolder, "index.html")).exists();
        process.destroy();
    }

    private static File newFolder(File root, String... subDirs) throws IOException {
        String subFolder = String.join("/", subDirs);
        File result = new File(root, subFolder);
        if (!result.mkdirs()) {
            throw new IOException("Couldn't create folders " + root);
        }
        return result;
    }

}
