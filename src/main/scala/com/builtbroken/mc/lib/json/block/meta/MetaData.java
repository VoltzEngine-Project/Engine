package com.builtbroken.mc.lib.json.block.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores data about a meta value
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/26/2016.
 */
public class MetaData
{
    /** Name of the texture */
    public String textureName;
    /** Suffix to append to the block's localization */
    public String localization;
    /** List of ore names to register after block is registered */
    public List<String> oreNames;
    /** Index in the meta array, between 0-15 */
    public int index;

    /**
     * Adds an ore name to be registered
     *
     * @param name
     */
    public void addOreName(String name)
    {
        //TODO throw error if added after game has loaded
        if (oreNames == null)
        {
            oreNames = new ArrayList();
        }
        //TODO validate? order of words, camel case

        if (!oreNames.contains(name))
        {
            oreNames.add(name);
        }
    }
}
