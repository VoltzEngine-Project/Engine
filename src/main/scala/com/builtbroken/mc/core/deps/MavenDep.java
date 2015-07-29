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
    final String version;
    final String classifier;
    final String ext;

    public MavenDep(String mavenRepo, String groupId, String artifactId, String version)
    {
        this(mavenRepo, groupId, artifactId, version, "", ".jar");
    }

    public MavenDep(String mavenRepo, String groupId, String artifactId, String version, String classifier)
    {
        this(mavenRepo, groupId, artifactId, version, classifier, ".jar");
    }

    public MavenDep(String mavenRepo, String groupId, String artifactId, String version, String classifier, String ext)
    {
        this.repoURL = mavenRepo.isEmpty() ? "http://ci.builtbroken.com/maven/" : mavenRepo;

        this.groupID = groupId;
        this.artifactID = artifactId;
        this.version = version;

        this.classifier = classifier;
        this.ext = ext;
    }

    public String getDir()
    {
        return this.groupID.replaceAll("\\.", "/") + "/" + this.artifactID + "/" + this.version;
    }

    @Override
    public String getPath()
    {
        return getDir() + "/" + this.artifactID + "-" + this.version + (this.classifier.isEmpty() ? "" : "-" + this.classifier) + this.ext;
    }

    @Override
    public URL getURL()
    {
        try
        {
            return new URL(this.repoURL + getPath());
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
