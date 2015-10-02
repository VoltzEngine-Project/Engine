package com.builtbroken.mc.core.proxy;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.mod.loadable.AbstractLoadable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Deprecated - moved and modified @see {@link com.builtbroken.mc.lib.mod.compat.nei.NEIProxy}
 * Created by robert on 2/9/2015.
 */
@Deprecated
public class NEIProxy extends AbstractLoadable
{
    private static boolean lock = false;

    private static List hideItems = new ArrayList();

    @Deprecated
    public static void hideItem(Item item)
    {
        hideItems.add(item);
    }

    @Deprecated
    public static void hideItem(Block item)
    {
        hideItems.add(item);
    }

    @Deprecated
    public static void hideItem(ItemStack item)
    {
        hideItems.add(item);
    }

    @Override
    public void postInit()
    {
        if (!lock)
        {
            try
            {
                Class nei_api_class = Class.forName("codechicken.nei.api.API");
                Method method = nei_api_class.getMethod("hideItem", ItemStack.class);
                for (Object obj : hideItems)
                {
                    if (obj instanceof Block || obj instanceof Item)
                    {
                        List list = new ArrayList();
                        if (obj instanceof Block)
                        {
                            ((Block) obj).getSubBlocks(Item.getItemFromBlock((Block) obj), ((Block) obj).getCreativeTabToDisplayOn(), list);
                        }
                        else
                        {
                            ((Item) obj).getSubItems((Item) obj, ((Item) obj).getCreativeTab(), list);
                        }
                        for (Object o : list)
                        {
                            if (o instanceof ItemStack)
                            {
                                method.invoke(null, (ItemStack) o);
                            }
                        }
                    }
                    else if (obj instanceof ItemStack)
                    {
                        method.invoke(null, (ItemStack) obj);
                    }
                }
            } catch (ClassNotFoundException e)
            {
                Engine.instance.logger().error("Failed to locate NEI API class", e);
                lock = true;
            } catch (NoSuchMethodException e)
            {
                Engine.instance.logger().error("Failed to locate NEI hideItem method", e);
                lock = true;
            } catch (InvocationTargetException e)
            {
                Engine.instance.logger().error("Failed to invoke NEI hideItem method", e);
            } catch (IllegalAccessException e)
            {
                Engine.instance.logger().error("Failed to access NEI hideItem method", e);
            }
        }
    }

    @Override
    public boolean shouldLoad()
    {
        return FMLCommonHandler.instance().getSide() == Side.CLIENT && Loader.isModLoaded("NotEnoughItems");
    }
}
