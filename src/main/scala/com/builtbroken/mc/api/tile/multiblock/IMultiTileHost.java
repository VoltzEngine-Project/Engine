package com.builtbroken.mc.api.tile.multiblock;

import codechicken.lib.vec.Vector3;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;

/**
 * Created by Dark on 8/9/2015.
 */
public interface IMultiTileHost
{
    void onMultiTileAdded(IMultiTile tileMulti);

    void onMultiTileBroken(IMultiTile tileMulti);

    void onTileInvalidate(IMultiTile tileMulti);

    void onMultiTileBrokenByExplosion(IMultiTile tile, Explosion ex);

    void onMultiTileActivated(IMultiTile tile, EntityPlayer player, int side, Vector3 hit);

    void onMultiTileClicked(IMultiTile tile, EntityPlayer player);
}
