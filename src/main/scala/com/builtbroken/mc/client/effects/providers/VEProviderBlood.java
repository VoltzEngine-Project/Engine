package com.builtbroken.mc.client.effects.providers;

import com.builtbroken.mc.client.effects.VisualEffectProvider;
import com.builtbroken.mc.lib.render.fx.EntityDropParticleFX;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.awt.*;

/**
 * Spawns a small blood drop
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/1/2017.
 */
public class VEProviderBlood extends VisualEffectProvider
{
    public VEProviderBlood()
    {
        super("bleeding");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayEffect(World world, double x, double y, double z, double mx, double my, double mz, boolean movementIsEndpoint, NBTTagCompound otherData)
    {
        Color color = Color.red;
        if (otherData != null)
        {
            //Load color
            if (otherData.hasKey("red"))
            {
                color = new Color(otherData.getInteger("red"), otherData.getInteger("green"), otherData.getInteger("blue"));
            }
            else if (otherData.hasKey("color"))
            {
                color = new Color(otherData.getInteger("color"));
            }
        }

        EntityDropParticleFX drop = new EntityDropParticleFX(world, x, y, z);
        drop.setRGB(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(drop);
    }
}
