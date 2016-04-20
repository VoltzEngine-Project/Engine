package com.builtbroken.mc.prefab.tile.interfaces.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import java.util.ArrayList;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface ITileItem extends ITile
{
    ItemStack getPickBlock(MovingObjectPosition target);

    ArrayList<ItemStack> getDrops(int metadata, int fortune);
}
