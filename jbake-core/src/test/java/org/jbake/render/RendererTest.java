package org.jbake.render;

import org.jbake.TestUtils;
import org.jbake.app.ContentStore;
import org.jbake.app.Renderer;
import org.jbake.app.configuration.ConfigUtil;
import org.jbake.app.configuration.DefaultJBakeConfiguration;
import org.jbake.model.DocumentModel;
import org.jbake.template.DelegatingTemplateEngine;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RendererTest {

    @TempDir
    public File folder;
    private DefaultJBakeConfiguration config;
    private File outputPath;

    @Mock
    private ContentStore db;

    @Mock
    private DelegatingTemplateEngine renderingEngine;

    @BeforeEach
    public void setup() throws Exception {

        File sourcePath = TestUtils.getTestResourcesAsSourceFolder();
        if (!sourcePath.exists()) {
            throw new Exception("Cannot find base path for test!");
        }
        outputPath = newFolder(folder, "output");
        config = (DefaultJBakeConfiguration) new ConfigUtil().loadConfig(sourcePath);
        config.setDestinationFolder(outputPath);
    }

    /**
     * See issue #300
     *
     * @throws Exception
     */
    @Test
    public void testRenderFileWorksWhenPathHasDotInButFileDoesNot() throws Exception {

        Assumptions.assumeFalse(TestUtils.isWindows(), "Ignore running on Windows");
        String FOLDER = "real.path";

        final String FILENAME = "about";
        config.setOutputExtension("");
        config.setTemplateFolder(folder.newFolder("templates"));
        Renderer renderer = new Renderer(db, config, renderingEngine);

        DocumentModel content = new DocumentModel();
        content.setType("page");
        content.setUri("/" + FOLDER + "/" + FILENAME);
        content.setStatus("published");

        renderer.render(content);

        File outputFile = new File(outputPath.getAbsolutePath() + File.separatorChar + FOLDER + File.separatorChar + FILENAME);
        assertThat(outputFile).isFile();
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
