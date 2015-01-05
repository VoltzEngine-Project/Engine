package com.builtbroken.mc.core.resources;

import com.builtbroken.mc.core.References;

/** Registry for handling the metadata for ore items.
 * Ore item is separated into 128 sets of 100. This
 * allows for 100 ores or materials to be registered
 * plus for 128 unique item sets to exist for these ores
 * Created by robert on 11/28/2014.
 */
public class OreItemRegistry
{
    public static GeneratedOreItem[] generators = new GeneratedOreItem[128];
    private static int nextID = 0;

    public static void register(GeneratedOreItem item)
    {
        register(nextID++, item);
    }

    /** Registers the generator to the set ID
     * @param id - id
     * @param item - generator
     */
    public static void register(int id, GeneratedOreItem item)
    {
        if(id >= 0 && id < generators.length)
        {
            if(generators[id] == null)
            {
                generators[id] = item;
            }
            else
            {
                throw new RuntimeException(References.NAME + ": Failed to register generator as the slot is already taken by "+ generators[id] +" when registering " + item );
            }
        }
    }

    /** Gets the set for the meta value */
    public static int getSetFromMeta(int meta)
    {
        return meta % 100;
    }

    /** Gets the generator for the meta */
    public static GeneratedOreItem getGeneratorForMeta(int meta)
    {
        int set = getSetFromMeta(meta);
        if(set >= 0 && set < generators.length)
        {
            return generators[set];
        }
        return null;
    }
}
