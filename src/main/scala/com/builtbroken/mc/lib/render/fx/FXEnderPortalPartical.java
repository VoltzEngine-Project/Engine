package com.builtbroken.mc.lib.render.fx;

import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FXEnderPortalPartical extends ParticlePortal
{
	public FXEnderPortalPartical(World par1World, Pos position, float red, float green, float blue, float scale, double distance)
	{
		super(par1World, position.x(), position.y(), position.z(), 0, 0, 0);
		this.particleScale = scale;
		try
		{
			ReflectionHelper.setPrivateValue(ParticlePortal.class, this, this.particleScale, 0);
		}
		catch (Exception e)
		{
			FMLLog.warning("[Voltz Engine] Failed to correctly spawn portal effects.");
		}
		//this.renderDistanceWeight = distance;
	}
}
