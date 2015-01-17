package com.builtbroken.mc.lib.render.fx;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;
import com.builtbroken.mc.lib.transform.vector.Pos;

@SideOnly(Side.CLIENT)
public class FXEnderPortalPartical extends EntityPortalFX
{
	public FXEnderPortalPartical(World par1World, Pos position, float red, float green, float blue, float scale, double distance)
	{
		super(par1World, position.x(), position.y(), position.z(), 0, 0, 0);
		this.particleScale = scale;
		try
		{
			ReflectionHelper.setPrivateValue(EntityPortalFX.class, this, this.particleScale, 0);
		}
		catch (Exception e)
		{
			FMLLog.warning("[Resonant Engine] Failed to correctly spawn portal effects.");
		}
		this.renderDistanceWeight = distance;
	}

}
