package com.builtbroken.mc.lib.helper;

import net.minecraft.block.material.Material;

import java.util.HashMap;

/**
 * Stores references to materials by a string name. Uses in loading configs, json, etc
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
@Deprecated
public class MaterialDict
{
    private static HashMap<String, Material> map = new HashMap();
    private static HashMap<Material, String> mapRev = new HashMap();

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
        add("lava", Material.lava);
        add("leaves", Material.leaves);
        add("plants", Material.plants);
        add("vine", Material.vine);
        add("sponge", Material.sponge);
        add("cloth", Material.cloth);
        add("fire", Material.fire);
        add("sand", Material.sand);
        add("circuits", Material.circuits);
        add("circuits", Material.circuits);
        add("carpet", Material.carpet);
        add("glass", Material.glass);
        add("redstoneLight", Material.redstoneLight);
        add("tnt", Material.tnt);
        add("coral", Material.coral);
        add("ice", Material.ice);
        add("packedIce", Material.packedIce);
        add("snow", Material.snow);
        add("craftedSnow", Material.craftedSnow);
        add("cactus", Material.cactus);
        add("clay", Material.clay);
        add("gourd", Material.gourd);
        add("dragonEgg", Material.dragonEgg);
        add("portal", Material.portal);
        add("cake", Material.cake);
        add("web", Material.web);
        add("piston", Material.piston);
        //TODO add rest
    }

    public static void add(String name, Material mat)
    {
        map.put(name.toLowerCase(), mat);
        mapRev.put(mat, name.toLowerCase());
    }

    public static Material get(String name)
    {
        return map.containsKey(name.toLowerCase()) ? map.get(name.toLowerCase()) : Material.air;
    }

    public static String getName(Material material)
    {
        return mapRev.get(material);
    }
}
