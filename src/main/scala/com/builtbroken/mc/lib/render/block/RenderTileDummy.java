package com.builtbroken.mc.lib.render.block;

import com.builtbroken.mc.lib.transform.vector.Vector3;
import com.builtbroken.mc.prefab.tile.Tile;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

/**
 * Created by robert on 1/11/2015.
 */
public class RenderTileDummy extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        if(tile instanceof Tile)
        {
            GL11.glPushMatrix();
            Render.renderOffsetAABB(((Tile) tile).getCollisionBounds().toAABB(), x, y, z);
            GL11.glPopMatrix();

            ((Tile) tile).renderDynamic(new Vector3(x, y, z), f, 0);
        }
    }
}
