package com.builtbroken.mc.framework.multiblock;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Dark on 8/18/2015.
 */
public class RendererMultiBlock extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tile, double xx, double yy, double zz, float delta)
    {
        if (tile instanceof TileMulti)
        {
            //TODO implement a system to allow the multiblock to be set with a renderer object.
            //This way we can render only the peaces of the multiblock structure that are on the player's screen
        }
    }
}
