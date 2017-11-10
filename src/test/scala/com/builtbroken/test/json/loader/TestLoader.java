package com.builtbroken.test.json.loader;

import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.loading.JsonLoader;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/1/2017.
 */
public class TestLoader extends TestCase
{
    @Test
    public void testJarLoad() throws Exception
    {
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/test.jar");

        assertTrue(file.exists());

        Path path = Paths.get(file.getPath());
        URI uri = new URI("jar", path.toUri().toString(), null);

        JsonContentLoader loader = new JsonContentLoader();

        loader.loadResourcesFromPackage(uri, "content");
        assertEquals(1, loader.jsonEntries.size());
    }

    @Test
    public void testJarLoadWithDot() throws Exception
    {
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/.local/test.jar");

        assertTrue(file.exists());

        Path path = Paths.get(file.getPath());
        URI uri = new URI("jar", path.toUri().toString(), null);

        JsonContentLoader loader = new JsonContentLoader();

        loader.loadResourcesFromPackage(uri, "content");
        assertEquals(1, loader.jsonEntries.size());
    }

    @Test
    public void testJarLoadWithUniqueChars() throws Exception
    {
        for (String folderPath : new String[]{"[test]test", "{test}test", "test test", "test.test", "test_test", "test-test"})
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test/" + folderPath + "/test.jar");

            assertTrue("No file with path: " + file, file.exists());

            Path path = Paths.get(file.getPath());
            URI uri = new URI("jar", path.toUri().toString(), null);

            JsonContentLoader loader = new JsonContentLoader();

            loader.loadResourcesFromPackage(uri, "content");
            assertEquals("Failed for: " + folderPath, 1, loader.jsonEntries.size());
        }
    }

    @Test
    public void testJarPathDetector() throws Exception
    {
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/test.jar");

        assertTrue(file.exists());

        Path path = Paths.get(file.getPath());
        URI uri = new URI("jar", path.toUri().toString() + "!/content/", null);

        String[] files = JsonLoader.getResourceListing(uri.toURL());

        assertTrue(files != null);
        assertEquals("content/test.json", files[0]);
    }
}
