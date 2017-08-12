package com.builtbroken.mc.seven.framework.block.listeners;

import com.builtbroken.mc.framework.block.imp.IBlockListener;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.block.imp.IPlacementListener;
import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.framework.block.imp.ITileEventListenerBuilder;
import com.builtbroken.mc.lib.helper.BlockUtility;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2017.
 */
public class RotatableListener extends TileListener implements IPlacementListener, IBlockListener
{
    @Override
    public void onPlacedBy(EntityLivingBase entityLivingBase, ItemStack stack)
    {
        int rotation = BlockUtility.determineRotation(entityLivingBase.rotationYaw);
        if (!setMeta(rotation, 3))
        {
            Engine.logger().error("Failed to set rotation for block at " + x() + "x," + y() + "y," + z() + "z,");
        }
    }

    @Override
    public List<String> getListenerKeys()
    {
        List<String> list = new ArrayList();
        list.add("placement");
        return list;
    }

    public static class Builder implements ITileEventListenerBuilder
    {
        @Override
        public ITileEventListener createListener(Block block)
        {
            return new RotatableListener();
        }

        @Override
        public String getListenerKey()
        {
            return "rotation";
        }
    }
}
