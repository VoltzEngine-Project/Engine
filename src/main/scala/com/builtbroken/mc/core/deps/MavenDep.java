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

    final int major;
    final int minor;
    final int revis;
    final int build;

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
        this.major = major;
        this.minor = minor;
        this.revis = revis;
        this.build = build;

        this.classifier = classifier;
        this.ext = ext;
    }

    public String getMavenFolderPath()
    {
        return this.groupID.replaceAll("\\.", "/") + "/" + this.artifactID + "/" + this.version();
    }

    public String version()
    {
        return major + "." + minor + "." + revis + build_seperator + build;
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
        String version = fileName.replace(artifactID + "-", "");
        int major = Integer.parseInt(version.substring(0, 1));
        int minor = Integer.parseInt(version.substring(2, 3));
        int revis = Integer.parseInt(version.substring(4, 5));
        int build = Integer.parseInt(version.substring(6, version.length() - 1));
        if (major < this.major)
            return false;
        if (minor < this.minor)
            return false;
        if (revis < this.revis)
            return false;
        if (build <= this.build)
            return false;
        return true;
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
