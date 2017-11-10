package com.builtbroken.test.json.loader;

import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.loading.JsonLoader;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/1/2017.
 */
public class TestLoader extends TestCase
{
    @Test
    public void testJarFilePathLoading() throws Exception
    {
        for (String folderPath : new String[]{"", "/[test]test", "/{test}test", "/test test", "/test.test", "/test_test", "/test-test", "/.local"})
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test" + folderPath + "/test.jar");

            assertTrue("No file with path: " + file, file.exists());

            JsonContentLoader loader = new JsonContentLoader();

            loader.loadResourcesFromPackage(new URL("jar:file:/" + file.getAbsolutePath() + "!/content"));
            assertEquals("Failed for: " + folderPath, 1, loader.jsonEntries.size());
        }
    }

    @Test
    public void testJarPathDetector() throws Exception
    {
        for (String folderPath : new String[]{"[test]test", "{test}test", "test test", "test.test", "test_test", "test-test"})
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test/" + folderPath + "/test.jar");

            assertTrue(file.exists());
            URL url = new URL("jar:file:/" + file.getAbsolutePath() + "!/content");

            List<String> files = JsonLoader.getResourceListing(url);

            assertTrue("Failed for: " + folderPath, !files.isEmpty());
            assertEquals("Failed for: " + folderPath, "content/test.json", files.get(0));
        }
    }
}
