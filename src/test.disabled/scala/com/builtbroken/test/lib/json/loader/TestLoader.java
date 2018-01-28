package com.builtbroken.test.lib.json.loader;

import com.builtbroken.mc.framework.json.JsonContentLoader;
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
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test.jar");

        assertTrue(file.exists());

        Path path = Paths.get(file.getPath());
        URI uri = new URI("jar", path.toUri().toString(),  null);

        JsonContentLoader loader = new JsonContentLoader();

        loader.loadResourcesFromPackage(uri, "content");
        assertEquals(1, loader.jsonEntries.size());
    }

    @Test
    public void testJarLoadWithFilePathSpace() throws Exception
    {
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test test/test.jar");

        assertTrue(file.exists());

        Path path = Paths.get(file.getPath());
        URI uri = new URI("jar", path.toUri().toString(),  null);

        JsonContentLoader loader = new JsonContentLoader();

        loader.loadResourcesFromPackage(uri, "content");
        assertEquals(1, loader.jsonEntries.size());
    }
}
