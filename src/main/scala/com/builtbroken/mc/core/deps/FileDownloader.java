package com.builtbroken.mc.core.deps;

import cpw.mods.fml.relauncher.FMLInjectionData;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by Dark on 7/29/2015.
 */
public class FileDownloader
{
    public static void downloadDeps(Dep... deps)
    {
        for (Dep dep : deps)
        {
            downloadDep(dep);
        }
    }

    public static void downloadDep(Dep dep)
    {
        URL url = dep.getURL();
        if (url != null)
        {
            downloadFromURL(url, FMLInjectionData.data()[6] + "/mods/" + dep.getPath());
        }
    }

    public static void downloadFromURL(URL in, String out)
    {
        try
        {
            Path outPath = Paths.get(out);
            if (!outPath.toFile().exists())
            {
                Files.copy(in.openStream(), outPath, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
