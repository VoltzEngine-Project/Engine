package com.builtbroken.mc.framework.block;

import com.builtbroken.mc.framework.block.tile.ITileProvider;
import com.builtbroken.mc.lib.helper.MaterialDict;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.lib.json.processors.JsonGenData;
import net.minecraft.block.material.Material;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public class BlockPropertyData extends JsonGenData
{
    /** Unique id to register the block with */
    public final String registryKey;
    /** Mod that owners the block */
    public final String MOD;
    /** Name of the block, used for localizations */
    public final String name;


    private Material material;
    private float hardness = 5;
    private float resistance = 5;

    /** Localization of the block */
    public String localization = "tile.${name}";
    /** Global ore dict name of the block */
    public String oreName;

    /** Handles supplying the tile entity for the block */
    public ITileProvider tileEntityProvider;

    public BlockPropertyData(IJsonProcessor processor, String registryKey, String MOD, String name)
    {
        super(processor);
        this.registryKey = registryKey;
        this.MOD = MOD;
        this.name = name;
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(String matName)
    {
        this.material = MaterialDict.get(matName);
    }

    public float getHardness()
    {
        return hardness;
    }

    public void setHardness(float hardness)
    {
        this.hardness = hardness;
    }

    public float getResistance()
    {
        return resistance;
    }

    public void setResistance(float resistance)
    {
        this.resistance = resistance;
    }
}
