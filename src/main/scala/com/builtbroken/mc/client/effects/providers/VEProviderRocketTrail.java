package com.builtbroken.mc.client.effects.providers;

import com.builtbroken.mc.client.effects.VisualEffectProvider;
import com.builtbroken.mc.lib.render.fx.FxRocketFire;
import com.builtbroken.mc.lib.render.fx.FxRocketSmokeTrail;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.awt.*;

/**
 * Spawns a laser from point A to point B
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2017.
 */
public class VEProviderRocketTrail extends VisualEffectProvider
{
    public VEProviderRocketTrail()
    {
        super("rocketTrail");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayEffect(World world, double x, double y, double z, double mx, double my, double mz, boolean movementIsEndpoint, NBTTagCompound otherData)
    {
        Color fireColor = null;
        Color smokeColor = Color.GRAY;

        int lifeTicks = 200;
        if (otherData != null)
        {
            //load life timer for particle
            if (otherData.hasKey("lifeTime"))
            {
                lifeTicks = otherData.getInteger("lifeTime");
            }

            //Load color
            if (otherData.hasKey("fireColor"))
            {
                fireColor = getColor(otherData.getCompoundTag("fireColor"));
            }

            if (otherData.hasKey("smokeColor"))
            {
                smokeColor = getColor(otherData.getCompoundTag("smokeColor"));
            }
        }

        Minecraft.getMinecraft().effectRenderer.addEffect(new FxRocketFire(world, fireColor, x, y, z, mx, my, mz));
        Minecraft.getMinecraft().effectRenderer.addEffect(new FxRocketSmokeTrail(world, smokeColor, x, y, z, mx, my, mz, lifeTicks));


    }

    @Override
    protected boolean shouldDisplay(World world, double x, double y, double z)
    {
        //TODO add graphics override options
        return Minecraft.getMinecraft().gameSettings.fancyGraphics && Minecraft.getMinecraft().gameSettings.particleSetting != 1;
    }
}
