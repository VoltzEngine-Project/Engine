package com.builtbroken.mc.core.deps;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dark on 7/29/2015.
 */
public class MavenDep extends Dep
{
    final String repoURL;
    final String groupID;
    final String artifactID;
    final String classifier;
    final String ext;
    String build_seperator = "b";

    final Version version;

    public MavenDep(String mavenRepo, String groupId, String artifactId, String major, String minor, String revis, String build)
    {
        //TODO replace string parsing with something like new Version(major, minor, revis, build) that will auto parse and convert to int
        this(mavenRepo, groupId, artifactId, Integer.parseInt(major), Integer.parseInt(minor), Integer.parseInt(revis), Integer.parseInt(build));
    }

    public MavenDep(String mavenRepo, String groupId, String artifactId, int major, int minor, int revis, int build)
    {
        this(mavenRepo, groupId, artifactId, major, minor, revis, build, "", ".jar");
    }

    public MavenDep(String mavenRepo, String groupId, String artifactId, int major, int minor, int revis, int build, String classifier)
    {
        this(mavenRepo, groupId, artifactId, major, minor, revis, build, classifier, ".jar");
    }

    public MavenDep(String mavenRepo, String groupId, String artifactId, int major, int minor, int revis, int build, String classifier, String ext)
    {
        this.repoURL = mavenRepo;

        this.groupID = groupId;
        this.artifactID = artifactId;
        version = new Version(major, minor, revis, build);
        this.classifier = classifier;
        this.ext = ext;
    }

    public String getMavenFolderPath()
    {
        return this.groupID.replaceAll("\\.", "/") + "/" + this.artifactID + "/" + this.version();
    }

    public String version()
    {
        return version.toString();
    }

    @Override
    public String getFileName()
    {
        return this.artifactID + "-" + this.version() + (this.classifier.isEmpty() ? "" : "-" + this.classifier) + this.ext;
    }

    @Override
    public String getGenericFileName()
    {
        return artifactID;
    }

    @Override
    public boolean isNewerVersion(String fileName)
    {
        return this.version.isNewer(getVersion(fileName));
    }

    /**
     * Converts the file name into a version object
     *
     * @param fileName - name of the file, including extension
     * @return version
     */
    public Version getVersion(String fileName)
    {
        int firstIndex = fileName.indexOf("-");
        int secondIndex = fileName.indexOf("-", firstIndex);
        if (secondIndex < 0)
        {
            secondIndex = fileName.lastIndexOf(".");
        }
        return new Version(fileName.substring(firstIndex, secondIndex));
    }

    @Override
    public URL getURL()
    {
        try
        {
            return new URL(this.repoURL + getMavenFolderPath() + "/" + getFileName());
        } catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
