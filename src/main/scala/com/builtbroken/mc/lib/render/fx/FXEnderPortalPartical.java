package com.builtbroken.mc.lib.render.fx;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;
import com.builtbroken.mc.imp.transform.vector.Pos;

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
			FMLLog.warning("[Voltz Engine] Failed to correctly spawn portal effects.");
		}
		this.renderDistanceWeight = distance;
	}

}
