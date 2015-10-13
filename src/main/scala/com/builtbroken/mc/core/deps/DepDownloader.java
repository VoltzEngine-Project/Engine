package com.builtbroken.mc.core.deps;

import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.launchwrapper.LaunchClassLoader;

import javax.swing.*;
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

            //TODO add JOption pane to ask user if they want to download missing files

            boolean found = false;
            File newestFile = null;
            for (File nextFile : v_modsDir.listFiles())
            {
                try
                {
                    //Checks to see if the file name is close enough to what we are looking to check
                    if (nextFile.getName().contains(dep.getGenericFileName()))
                    {
                        //If file name is the same or is a newer version then we found the file
                        if (nextFile.getName().equals(file.getName()) || dep.isNewerVersion(nextFile.getName()))
                        {
                            if (newestFile != null)
                            {
                                newestFile.delete();
                            }
                            newestFile = file;
                            found = true;
                        }
                        //Keep iterating to remove all old versions of the file
                        else
                        {
                            nextFile.delete();
                        }
                    }
                } catch (Exception e)
                {
                    throw new RuntimeException("Failed to parse file " + nextFile.getName() + ". Crashing game to prevent more issues, try deleting the file manually and restarting the game", e);
                }
            }
            if (!found)
            {
                Thread thr = Thread.currentThread();
                if (!thr.getName().equals("Server thread"))
                {
                    int reply = JOptionPane.showConfirmDialog(null, "Missing required version of " + dep.getGenericFileName() + ". Do you want to download?\nIf you click no the game will close as it will crash without this file.", "Missing dependency", JOptionPane.YES_OPTION);
                    if (reply != JOptionPane.YES_OPTION)
                    {
                        return;
                    }
                }
                FileDownloader.downloadDep(dep);
                addClasspath(file);
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
        downloader.depsToLoad.add(new MavenDep("@bbm_url@", "@CL@", "@CL-name@", "@CL_maj@", "@CL_min@", "@CL_rev@", "@CL_bu@", "universal"));
        //downloader.depsToLoad.add(new MavenDep("http://api.dmodoomsirius.me/", "com/builtbroken/codinglib", "CodingLib", "0", "0", "2", "26", "universal"));
        downloader.start();
    }
}
