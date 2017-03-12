package com.builtbroken.mc.lib.json.processors.block;

import com.builtbroken.mc.core.registry.implement.IRegistryInit;
import com.builtbroken.mc.lib.helper.MaterialDict;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import cpw.mods.fml.common.registry.GameRegistry;
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
    public String name;
    public String localization = "tile.${name}";
    public String oreName;

    public BlockJson(String name, String mat)
    {
        super(MaterialDict.get(mat));
        this.name = name;
        this.setBlockName(localization.replace("${name}", name));
    }

    @Override
    public void register()
    {
        GameRegistry.registerBlock(this, name);
    }

    @Override
    public String getLoader()
    {
        return "block";
    }

    public void setCreativeTab(String name)
    {
        //TODO implement
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
        return "BlockJson[" + name +"]";
    }
}
