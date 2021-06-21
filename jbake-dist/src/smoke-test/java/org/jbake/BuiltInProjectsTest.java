package org.jbake;

import org.apache.commons.vfs2.util.Os;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class BuiltInProjectsTest {
    public String projectName;
    public String extension;
    @TempDir
    public File folder;
    private File projectFolder;
    private File templateFolder;
    private File outputFolder;
    private String jbakeExecutable;
    private BinaryRunner runner;


    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"thymeleaf", "thyme"},
                {"freemarker", "ftl"},
                {"jade", "jade"},
                {"groovy", "gsp"},
                {"groovy-mte", "tpl"}
        });
    }

    @BeforeEach
    public void setup() throws IOException {
        if (Os.isFamily(Os.OS_FAMILY_WINDOWS)) {
            jbakeExecutable = new File("build\\install\\jbake\\bin\\jbake.bat").getAbsolutePath();
        } else {
            jbakeExecutable = new File("build/install/jbake/bin/jbake").getAbsolutePath();
        }
        projectFolder = newFolder(folder, "project");
        templateFolder = new File(projectFolder, "templates");
        outputFolder = new File(projectFolder, "output");
        runner = new BinaryRunner(projectFolder);
    }

    @MethodSource("data")
    @ParameterizedTest(name = " {0} ")
    public void shouldBakeWithProject(String projectName, String extension) throws Exception {
        initBuiltInProjectsTest(projectName, extension);
        shouldInitProject(projectName, extension);
        shouldBakeProject();
    }

    private void shouldInitProject(String projectName, String extension) throws IOException, InterruptedException {
        Process process = runner.runWithArguments(jbakeExecutable, "-i", "-t", projectName);
        assertThat(process.exitValue()).isEqualTo(0);
        assertThat(new File(projectFolder, "jbake.properties")).exists();
        assertThat(new File(templateFolder, String.format("index.%s", extension))).exists();
        process.destroy();
    }

    private void shouldBakeProject() throws IOException, InterruptedException {
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

    public void initBuiltInProjectsTest(String projectName, String extension) {
        this.projectName = projectName;
        this.extension = extension;
    }

}
