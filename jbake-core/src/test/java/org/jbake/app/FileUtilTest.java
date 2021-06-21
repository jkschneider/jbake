package org.jbake.app;

import org.jbake.TestUtils;
import org.jbake.app.configuration.ConfigUtil;
import org.jbake.app.configuration.DefaultJBakeConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by frank on 28.03.16.
 */
public class FileUtilTest {

    @Test
    public void testGetRunningLocation() throws Exception {

        File path = FileUtil.getRunningLocation();
        assertEquals(new File("build/classes").getAbsolutePath(), path.getPath());
    }

    @Test
    public void testIsFileInDirectory() throws Exception {
        File fixtureDir = new File(this.getClass().getResource("/fixture").getFile());
        File jbakeFile = new File(fixtureDir.getCanonicalPath() + File.separatorChar + "jbake.properties");
        assertTrue(FileUtil.isFileInDirectory(jbakeFile, fixtureDir), "jbake.properties expected to be in /fixture directory");

        File contentFile = new File(fixtureDir.getCanonicalPath() + File.separatorChar + "content" + File.separatorChar + "projects.html");
        assertTrue(FileUtil.isFileInDirectory(contentFile, fixtureDir), "projects.html expected to be nested in the /fixture directory");

        File contentDir = contentFile.getParentFile();
        assertFalse(FileUtil.isFileInDirectory(jbakeFile, contentDir), "jbake.properties file should not be in the /fixture/content directory");
    }

    @Test
    public void testGetContentRoothPath() throws Exception {

        File source = TestUtils.getTestResourcesAsSourceFolder();
        ConfigUtil util = new ConfigUtil();
        DefaultJBakeConfiguration config = (DefaultJBakeConfiguration) util.loadConfig(source);

        String path = FileUtil.getUriPathToContentRoot(config, new File(config.getContentFolder(), "index.html"));
        assertThat(path).isEqualTo("");

        path = FileUtil.getUriPathToContentRoot(config, new File(config.getContentFolder(), "/blog/index.html"));
        assertThat(path).isEqualTo("../");

        path = FileUtil.getUriPathToContentRoot(config, new File(config.getContentFolder(), "/blog/level2/index.html"));
        assertThat(path).isEqualTo("../../");
    }
}
