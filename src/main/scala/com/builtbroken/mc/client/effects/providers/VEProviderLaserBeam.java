package com.builtbroken.mc.client.effects.providers;

import com.builtbroken.mc.client.SharedAssets;
import com.builtbroken.mc.client.effects.VisualEffectProvider;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.render.fx.FxBeam;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.awt.*;

/**
 * Spawns a shock wave effect at the location
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2017.
 */
public class VEProviderLaserBeam extends VisualEffectProvider
{
    public VEProviderLaserBeam()
    {
        super("laser");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayEffect(World world, double x, double y, double z, double mx, double my, double mz, boolean movementIsEndpoint, NBTTagCompound otherData)
    {
        Color color = Color.red;
        int lifeTicks = 2;
        if (otherData != null)
        {
            if (otherData.hasKey("red"))
            {
                color = new Color(otherData.getInteger("red"), otherData.getInteger("green"), otherData.getInteger("blue"));
            }
            else if (otherData.hasKey("color"))
            {
                color = new Color(otherData.getInteger("color"));
            }
        }
        FxBeam laser = new FxBeam(SharedAssets.GREY_TEXTURE, world, new Pos(x, y, z), new Pos(mx, my, mz), 1, 0, 0, lifeTicks);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(laser);
    }
}
