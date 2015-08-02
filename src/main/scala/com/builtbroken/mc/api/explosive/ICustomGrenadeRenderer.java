package com.builtbroken.mc.api.explosive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/** WIP interface so may change in the future
 * Applied to an explosive handler to allow overriding rendering of the grenade
 * Created by Dark on 8/2/2015.
 */
public interface ICustomGrenadeRenderer
{
    @SideOnly(Side.CLIENT)
    void renderGrenade(Entity entity, double xx, double yy, double zz);

    @SideOnly(Side.CLIENT)
    boolean shouldHandleGrenadeRenderer(Entity entity);

    /**
     * Grenades use an icon for inventory storage rather than a renderer to save on framerate.
     * @param itemStack - itemstack that is the grenade
     * @return a valid icon
     */
    @SideOnly(Side.CLIENT)
    IIcon getGrenadeIcon(ItemStack itemStack);

    @SideOnly(Side.CLIENT)
    void registerGrenadeIcons(IIconRegister reg);
}
