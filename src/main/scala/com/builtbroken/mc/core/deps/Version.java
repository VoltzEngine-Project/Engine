package com.builtbroken.mc.core.deps;

/**
 * Used to cache a version number
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/12/2015.
 */
public class Version
{
    public final int major;
    public final int minor;
    public final int revis;
    public final int build;

    public Version(int major, int minor, int revis, int build)
    {
        this.major = major;
        this.minor = minor;
        this.revis = revis;
        this.build = build;
    }

    public Version(String version)
    {
        int firstDot = version.indexOf(".");
        int secondDot = version.indexOf(".", firstDot + 1);
        int thirdDot = version.indexOf("b", secondDot + 1);

        major = Integer.parseInt(version.substring(0, firstDot));
        minor = Integer.parseInt(version.substring(firstDot + 1, secondDot));
        revis = Integer.parseInt(version.substring(secondDot + 1, thirdDot));
        build = Integer.parseInt(version.substring(thirdDot + 1, version.length()));
    }

    /**
     * Checks if the version passed in is newer than this one
     *
     * @param v - version
     * @return true if it is newer
     */
    public boolean isNewer(Version v)
    {
        if (v.major < this.major)
            return false;
        if (v.minor < this.minor)
            return false;
        if (v.revis < this.revis)
            return false;
        if (v.build <= this.build)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return major + "." + minor + "." + revis + "b" + build;
    }
}
