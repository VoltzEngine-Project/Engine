package com.builtbroken.mc.seven.client.listeners.blocks;

import com.builtbroken.mc.framework.block.imp.ITileEventListener;
import com.builtbroken.mc.framework.block.imp.ITileEventListenerBuilder;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.seven.framework.block.listeners.client.IIconListener;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;


/**
 * Handles rotating textures on a block
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2017.
 */
public class RotatableIconListener extends JsonIconListener implements IIconListener
{
    public RotatableIconListener(BlockBase block)
    {
        super(block);
    }

    @Override
    public ResourceLocation getTileIcon(int side, IBlockState state)
    {
        int meta = state.getValue(BlockBase.META);
        //South
        if (meta == 3)
        {
            if (side == 2)
            {
                return super.getTileIcon(3, state);
            }
            else if (side == 3)
            {
                return super.getTileIcon(2, state);
            }
            else if (side == 4)
            {
                return super.getTileIcon(5, state);
            }
            else if (side == 5)
            {
                return super.getTileIcon(4, state);
            }
        }
        //West
        else if (meta == 4)
        {
            if (side == 2)
            {
                return super.getTileIcon(4, state);
            }
            else if (side == 3)
            {
                return super.getTileIcon(5, state);
            }
            else if (side == 4)
            {
                return super.getTileIcon(2, state);
            }
            else if (side == 5)
            {
                return super.getTileIcon(3, state);
            }
        }
        //East
        else if (meta == 5)
        {
            if (side == 2)
            {
                return super.getTileIcon(5, state);
            }
            else if (side == 3)
            {
                return super.getTileIcon(4, state);
            }
            else if (side == 4)
            {
                return super.getTileIcon(3, state);
            }
            else if (side == 5)
            {
                return super.getTileIcon(2, state);
            }
        }
        return super.getTileIcon(side, state);
    }

    public static class Builder implements ITileEventListenerBuilder
    {
        @Override
        public ITileEventListener createListener(Block block)
        {
            if (block instanceof BlockBase)
            {
                return new RotatableIconListener((BlockBase) block);
            }
            return null;
        }

        @Override
        public String getListenerKey()
        {
            return "rotationIcon";
        }
    }
}
