package com.builtbroken.mc.framework.block;

import com.builtbroken.mc.framework.block.tile.ITileProvider;
import com.builtbroken.mc.lib.helper.MaterialDict;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.lib.json.loading.JsonProcessorData;
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

    /** Localization of the block */
    public String localization = "tile.${name}";
    /** Global ore dict name of the block */
    public String oreName;

    /** Handles supplying the tile entity for the block */
    public ITileProvider tileEntityProvider;


    //Block data
    private Material material = Material.clay;
    private boolean isOpaqueCube;
    private boolean supportsRedstone = false;
    private boolean isAlpha = false;
    private float hardness = 5;
    private float resistance = 5;
    private int renderType = 0;
    private int color = -1;
    private int lightValue;


    public BlockPropertyData(IJsonProcessor processor, String registryKey, String MOD, String name)
    {
        super(processor);
        this.registryKey = registryKey;
        this.MOD = MOD;
        this.name = name;
    }

    @Override
    public String getMod()
    {
        return MOD;
    }

    public Material getMaterial()
    {
        return material;
    }

    @JsonProcessorData("material")
    public void setMaterial(String matName)
    {
        this.material = MaterialDict.get(matName);
    }

    public float getHardness()
    {
        return hardness;
    }

    @JsonProcessorData(value = "hardness", type = "float")
    public void setHardness(float hardness)
    {
        this.hardness = hardness;
    }

    public float getResistance()
    {
        return resistance;
    }

    @JsonProcessorData(value = "resistance", type = "float")
    public void setResistance(float resistance)
    {
        this.resistance = resistance;
    }

    public int getRenderType()
    {
        return renderType;
    }

    @JsonProcessorData(value = "renderType", type = "int")
    public void setRenderType(int renderType)
    {
        this.renderType = renderType;
    }

    public int getColor()
    {
        return color;
    }

    @JsonProcessorData(value = "renderColor", type = "int")
    public void setColor(int color)
    {
        this.color = color;
    }

    public boolean isOpaqueCube()
    {
        return isOpaqueCube;
    }

    @JsonProcessorData("isOpaqueCube")
    public void setOpaqueCube(boolean opaqueCube)
    {
        this.isOpaqueCube = opaqueCube;
    }

    public boolean isSupportsRedstone()
    {
        return supportsRedstone;
    }

    @JsonProcessorData("supportsRedstone")
    public void setSupportsRedstone(boolean supportsRedstone)
    {
        this.supportsRedstone = supportsRedstone;
    }

    public boolean isAlpha()
    {
        return isAlpha;
    }

    @JsonProcessorData("hasAlphaTextures")
    public void setAlpha(boolean alpha)
    {
        isAlpha = alpha;
    }

    public int getLightValue()
    {
        return lightValue;
    }

    @JsonProcessorData(value = "lightOutput", type = "int")
    public void setLightValue(int lightValue)
    {
        this.lightValue = lightValue;
    }
}
