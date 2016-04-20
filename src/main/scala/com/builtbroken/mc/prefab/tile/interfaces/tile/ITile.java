package com.builtbroken.mc.prefab.tile.interfaces.tile;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface ITile extends IPos3D, IWorldPosition
{
    void onAdded();

    void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack);

    void onPostPlaced(int metadata);

    void onRemove(Block block, int par6);

    boolean removeByPlayer(EntityPlayer player, boolean willHarvest);

    boolean isSolid(int side);

    int getLightValue();

}
