package com.builtbroken.mc.lib.json.processors.block;

import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.lib.helper.MaterialDict;
import com.builtbroken.mc.lib.json.IJsonGenMod;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Block generated through a json based file format... Used to reduce dependency on code
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class BlockJson extends Block implements IRegistryInit, IJsonGenObject
{
    /** Unique id to register the block with */
    public final String ID;
    /** Mod that owners the block */
    public final String MOD;
    /** Name of the block, used for localizations */
    public final String name;

    /** Localization of the block */
    public String localization = "tile.${name}";
    /** Global ore dict name of the block */
    public String oreName;

    protected boolean registered = false;

    public BlockJson(String name, String mat, String id, String mod)
    {
        super(MaterialDict.get(mat));
        this.ID = id;
        this.MOD = mod;
        this.name = name;
        this.setBlockName(localization.replace("${name}", name));
    }

    @Override
    public String getLoader()
    {
        return "block";
    }

    @Override
    public String getMod()
    {
        return MOD;
    }

    public void setCreativeTab(String name)
    {
        //TODO implement
    }

    @Override
    public void register(IJsonGenMod mod, ModManager manager)
    {
        if (!registered)
        {
            registered = true;
            manager.newBlock(ID, this);
        }
    }

    @Override
    public void onRegistered()
    {
        if (oreName != null)
        {
            OreDictionary.registerOre(oreName, new ItemStack(this));
        }
    }

    @Override
    public void onClientRegistered()
    {

    }

    @Override
    public String toString()
    {
        return "BlockJson[" + name + "]";
    }
}
