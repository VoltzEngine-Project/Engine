package com.builtbroken.mc.framework.block.meta;

import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.BlockPropertyData;
import com.builtbroken.mc.lib.json.IJsonGenMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class BlockMeta extends BlockBase
{
    public MetaData[] meta = new MetaData[16];

    public BlockMeta(BlockPropertyData data)
    {
        super(data);
    }

    @Override
    public void register(IJsonGenMod mod, ModManager manager)
    {
        if (!registered)
        {
            registered = true;
            manager.newBlock(data.ID, this, ItemBlockMeta.class);
            if(data.tileEntityProvider != null)
            {
                data.register(mod, manager);
            }
        }
    }

    @Override
    public void onRegistered()
    {
        //Register main ore name
        if (data.oreName != null)
        {
            if (!data.oreName.contains("$"))
            {
                OreDictionary.registerOre(data.oreName, new ItemStack(this));
            }
            else
            {
                //Option for automatic ore name selection using a format key with replacement entries
                for (int i = 0; i < meta.length; i++)
                {
                    if (meta[i] != null)
                    {
                        String oreName = data.oreName.replace("${metaLocalization}", meta[i].localization);
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
                for (String s : meta[i].oreNames)
                {
                    if (s != null && !s.isEmpty())
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
        return "BlockMeta[" + data.name + "]";
    }
}
