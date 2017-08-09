package com.builtbroken.mc.client.listeners.blocks;

import com.builtbroken.mc.api.tile.listeners.ITileEventListener;
import com.builtbroken.mc.api.tile.listeners.ITileEventListenerBuilder;
import com.builtbroken.mc.api.tile.listeners.client.IIconListener;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.listeners.TileListener;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

/**
 * Handles rotating textures on a block
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2017.
 */
public class RotatableIconListener extends TileListener implements IIconListener
{
    public final BlockBase block;

    public RotatableIconListener(BlockBase block)
    {
        this.block = block;
    }

    @Override
    public IIcon getTileIcon(int side, int meta)
    {
        //South
        if (meta == 3)
        {
            if (side == 2)
            {
                return block.getIconFromJson(3, meta);
            }
            else if (side == 3)
            {
                return block.getIconFromJson(2, meta);
            }
            else if (side == 4)
            {
                return block.getIconFromJson(5, meta);
            }
            else if (side == 5)
            {
                return block.getIconFromJson(4, meta);
            }
        }
        //West
        else if (meta == 4)
        {
            if (side == 2)
            {
                return block.getIconFromJson(4, meta);
            }
            else if (side == 3)
            {
                return block.getIconFromJson(5, meta);
            }
            else if (side == 4)
            {
                return block.getIconFromJson(2, meta);
            }
            else if (side == 5)
            {
                return block.getIconFromJson(3, meta);
            }
        }
        //East
        else if (meta == 5)
        {
            if (side == 2)
            {
                return block.getIconFromJson(5, meta);
            }
            else if (side == 3)
            {
                return block.getIconFromJson(4, meta);
            }
            else if (side == 4)
            {
                return block.getIconFromJson(3, meta);
            }
            else if (side == 5)
            {
                return block.getIconFromJson(2, meta);
            }
        }
        return block.getIconFromJson(side, meta);
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
