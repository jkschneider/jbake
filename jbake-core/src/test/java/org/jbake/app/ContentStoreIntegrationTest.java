package org.jbake.app;

import org.apache.commons.vfs2.util.Os;
import org.jbake.TestUtils;
import org.jbake.app.configuration.ConfigUtil;
import org.jbake.app.configuration.DefaultJBakeConfiguration;
import org.junit.jupiter.api.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;

public abstract class ContentStoreIntegrationTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();
    protected static ContentStore db;
    protected static DefaultJBakeConfiguration config;
    protected static StorageType storageType = StorageType.MEMORY;
    protected static File sourceFolder;

    @BeforeAll
    public static void setUpClass() throws Exception {

        sourceFolder = TestUtils.getTestResourcesAsSourceFolder();
        Assertions.assertTrue(sourceFolder.exists(), "Cannot find sample data structure!");

        config = (DefaultJBakeConfiguration) new ConfigUtil().loadConfig(sourceFolder);
        config.setSourceFolder(sourceFolder);

        Assertions.assertEquals(".html", config.getOutputExtension());
        config.setDatabaseStore(storageType.toString());
        String dbPath = folder.newFolder("documents" + System.currentTimeMillis()).getAbsolutePath();

        // setting the database path with a colon creates an invalid url for OrientDB.
        // only one colon is expected. there is no documentation about proper url path for windows available :(
        if (Os.isFamily(Os.OS_FAMILY_WINDOWS)) {
            dbPath = dbPath.replace(":","");
        }
        config.setDatabasePath(dbPath);
        db = DBUtil.createDataStore(config);
    }

    @AfterAll
    public static void cleanUpClass() {
        db.close();
        db.shutdown();
    }

    @BeforeEach
    public void setUp() {
        db.startup();
    }

    @AfterEach
    public void tearDown() {
        db.drop();
    }

    protected enum StorageType {
        MEMORY, PLOCAL;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

}
