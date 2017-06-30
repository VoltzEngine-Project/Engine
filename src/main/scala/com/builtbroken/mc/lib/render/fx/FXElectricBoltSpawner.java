package com.builtbroken.mc.lib.render.fx;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A spawner used to spawn in multiple electrical bolts for a specific duration.
 */
public class FXElectricBoltSpawner extends EntityFX
{
	private IPos3D start;
	private IPos3D end;

	public FXElectricBoltSpawner(World world, IPos3D startVec, IPos3D targetVec, long seed, int duration)
	{
		super(world, startVec.x(), startVec.y(), startVec.z(), 0.0D, 0.0D, 0.0D);

		if (seed == 0)
		{
			this.rand = new Random();
		}
		else
		{
			this.rand = new Random(seed);
		}

		this.start = startVec;
		this.end = targetVec;
		this.particleMaxAge = duration;
	}

	@Override
	public void onUpdate()
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new FXElectricBolt(this.worldObj, this.start, this.end, 0));
		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setDead();
		}
	}

}
