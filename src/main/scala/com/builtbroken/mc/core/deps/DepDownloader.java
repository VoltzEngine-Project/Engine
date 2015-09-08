package com.builtbroken.mc.core.deps;

import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dependency download for VoltzEngine
 * Created by Dark on 9/7/2015.
 */
public class DepDownloader
{
    private File modsDir;
    private File v_modsDir;

    private LaunchClassLoader loader = (LaunchClassLoader) DepDownloader.class.getClassLoader();

    protected List<Dep> depsToLoad = new ArrayList();

    public DepDownloader()
    {
        String mc_version = (String) FMLInjectionData.data()[4];
        File mc_directory = (File) FMLInjectionData.data()[6];

        modsDir = new File(mc_directory, "mods");
        v_modsDir = new File(mc_directory, "mods/" + mc_version);
        if (!v_modsDir.exists())
            v_modsDir.mkdirs();
    }

    public void start()
    {
        for (Dep dep : depsToLoad)
        {
            //TODO add md5 check to ensure it is the right file and a user didn't rename it for lolz
            File file = new File(dep.getOutputFolderPath(), dep.getFileName());
            if (!file.exists())
            {
                //TODO add JOption pane to ask user if they want to download missing files
                boolean found = false;
                for (File f : v_modsDir.listFiles())
                {
                    if (f.getName().contains(dep.getGenericFileName()))
                    {
                        if (dep.isNewerVersion(f.getName()))
                        {
                            found = true;
                        }
                        else
                        {
                            try
                            {
                                f.delete();
                            } catch (Exception e)
                            {
                                throw new RuntimeException("Failed to delete old versions of " + dep.getGenericFileName() + ". Crashing game to prevent more issues, try deleting the file manually and restarting the game", e);
                            }
                        }
                    }
                }
                if (!found)
                {
                    FileDownloader.downloadDep(dep);
                }
            }
        }
    }

    private void addClasspath(File file)
    {
        try
        {
            loader.addURL(file.toURI().toURL());
        } catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    //TODO have scan all VE mods and download deps for them
    public static void load()
    {
        DepDownloader downloader = new DepDownloader();
        downloader.depsToLoad.add(new MavenDep("http://api.dmodoomsirius.me/", "com/builtbroken/codinglib", "CodingLib", 0, 0, 1, 14));
        downloader.start();
    }
}
