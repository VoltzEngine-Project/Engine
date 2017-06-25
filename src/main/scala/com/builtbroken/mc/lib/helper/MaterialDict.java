package com.builtbroken.mc.lib.helper;

import net.minecraft.block.material.Material;

import java.util.HashMap;

/**
 * Stores references to materials by a string name. Uses in loading configs, json, etc
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class MaterialDict
{
    private static HashMap<String, Material> map = new HashMap();

    static
    {
        add("air", Material.air);
        add("grass", Material.grass);
        add("ground", Material.ground);
        add("wood", Material.wood);
        add("rock", Material.rock);
        add("stone", Material.rock);
        add("iron", Material.iron);
        add("metal", Material.iron);
        add("anvil", Material.anvil);
        add("water", Material.water);
        //TODO add rest
    }

    public static void add(String name, Material mat)
    {
        map.put(name.toLowerCase(), mat);
    }

    public static Material get(String name)
    {
        return map.containsKey(name.toLowerCase()) ? map.get(name.toLowerCase()) : Material.rock;
    }
}
