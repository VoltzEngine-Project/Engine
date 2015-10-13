package com.builtbroken.test.core.dep;

import com.builtbroken.mc.core.deps.MavenDep;
import com.builtbroken.mc.core.deps.Version;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/12/2015.
 */
public class MavenDepTest extends TestCase
{
    @Test
    public void testInit()
    {
        //Loops threw all methods and tests them
        MavenDep dep = null;
        for (int i = 0; i < 3; i++)
        {
            switch (i)
            {
                case 0:
                    dep = new MavenDep("maven", "groupID", "artifactId", "0", "1", "5", "12");
                    break;
                case 1:
                    dep = new MavenDep("maven", "groupID", "artifactId", 0, 1, 5, 12);
                    break;
                case 2:
                    dep = new MavenDep("maven", "groupID", "artifactId", 0, 1, 5, 12, "classifier");
                    break;
                case 3:
                    dep = new MavenDep("maven", "groupID", "artifactId", 0, 1, 5, 12, "classifier", ".zip");
                    break;
            }

            //Check to ensure values are assigned
            assertEquals(dep.repoURL, "maven");
            assertEquals(dep.groupID, "groupID");
            assertEquals(dep.artifactID, "artifactId");
            assertEquals(dep.classifier, i > 1 ? "classifier" : "");
            assertEquals(dep.ext, i != 3 ? ".jar" : ".zip");
            assertEquals(dep.version, new Version(0, 1, 5, 12));
        }
    }

    @Test
    public void testGetVersion()
    {
        //CodingLib-0.0.2b26-universal.jar  CodingLib-0.0.1b12.jar
        MavenDep dep = new MavenDep("maven", "groupID", "artifactId", 0, 1, 5, 12);

        Version version = dep.getVersion("CodingLib-0.0.2b26-universal.jar");
        assertEquals(version, new Version(0, 0, 2, 26));

        version = dep.getVersion("CodingLib-0.0.1b12.jar");
        assertEquals(version, new Version(0, 0, 1, 12));
    }
}
