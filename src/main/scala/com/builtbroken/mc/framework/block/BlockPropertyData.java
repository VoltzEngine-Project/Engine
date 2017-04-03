package com.builtbroken.mc.framework.block;

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
    public final String ID;
    /** Mod that owners the block */
    public final String MOD;
    /** Name of the block, used for localizations */
    public final String name;

    private String material;

    /** Localization of the block */
    public String localization = "tile.${name}";
    /** Global ore dict name of the block */
    public String oreName;

    public BlockPropertyData(IJsonProcessor processor, String ID, String MOD, String name)
    {
        super(processor);
        this.ID = ID;
        this.MOD = MOD;
        this.name = name;
    }

    public Material getMaterial()
    {
        return MaterialDict.get(material);
    }

    public void setMaterial(String matName)
    {
        this.material = matName;
    }
}
