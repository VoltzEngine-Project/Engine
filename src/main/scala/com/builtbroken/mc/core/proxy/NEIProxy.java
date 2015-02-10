package com.builtbroken.mc.core.proxy;

import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import com.builtbroken.mc.lib.mod.loadable.LoadableHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import codechicken.nei.api.API;

/**
 * Created by robert on 2/9/2015.
 */
public class NEIProxy extends AbstractLoadable
{
    private static List hideItems = new ArrayList();

    public static void hideItem(Item item)
    {
        hideItems.add(item);
    }

    public static void hideItem(Block item)
    {
        hideItems.add(item);
    }

    public static void hideItem(ItemStack item)
    {
        hideItems.add(item);
    }

    @Override
    public void postInit()
    {
        for(Object obj: hideItems)
        {
            if(obj instanceof Block || obj instanceof Item)
            {
                List list = new ArrayList();
                if(obj instanceof Block)
                {
                    ((Block) obj).getSubBlocks(Item.getItemFromBlock((Block) obj), ((Block) obj).getCreativeTabToDisplayOn(), list);
                }
                else
                {
                    ((Item)obj).getSubItems((Item)obj, ((Item)obj).getCreativeTab(), list);
                }
                for(Object o : list)
                {
                    if(o instanceof ItemStack)
                    {
                        API.hideItem((ItemStack)o);
                    }
                }
            }
            else if(obj instanceof ItemStack)
            {
                API.hideItem((ItemStack)obj);
            }
        }
    }

    @Override
    public boolean shouldLoad()
    {
        return FMLCommonHandler.instance().getSide() == Side.CLIENT && Loader.isModLoaded("NotEnoughItems");
    }
}
