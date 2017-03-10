package com.builtbroken.mc.lib.json.processors.block.meta;

import com.builtbroken.mc.lib.json.processors.block.BlockJson;
import com.builtbroken.mc.lib.json.processors.block.ItemBlockJson;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class BlockJsonMeta extends BlockJson
{
    public MetaData[] meta = new MetaData[16];

    public BlockJsonMeta(String name, String mat)
    {
        super(name, mat);
    }

    @Override
    public void register()
    {
        GameRegistry.registerBlock(this, ItemBlockJson.class, name);
    }

    @Override
    public void onRegistered()
    {
        //Register main ore name
        if (oreName != null)
        {
            if (!oreName.contains("$"))
            {
                OreDictionary.registerOre(oreName, new ItemStack(this));
            }
            else
            {
                //Option for automatic ore name selection using a format key with replacement entries
                for (int i = 0; i < meta.length; i++)
                {
                    if (meta[i] != null)
                    {
                        String oreName = this.oreName.replace("${metaLocalization}", meta[i].localization);
                        OreDictionary.registerOre(oreName, new ItemStack(this, 1, i));
                    }
                }
            }
        }

        //Load meta exclusive ore names
        for (int i = 0; i < meta.length; i++)
        {
            if (meta[i] != null && meta[i].oreNames != null)
            {
                for(String s : meta[i].oreNames)
                {
                    if(s != null && !s.isEmpty())
                    {
                        //TODO impalement formatting replacement
                        OreDictionary.registerOre(s, new ItemStack(this, 1, i));
                    }
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "BlockJsonMeta[" + name + "]";
    }
}
