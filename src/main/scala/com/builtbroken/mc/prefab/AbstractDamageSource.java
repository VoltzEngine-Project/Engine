package com.builtbroken.mc.prefab;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

/**
 * Extend this class to create more custom damage sources.
 */
public abstract class AbstractDamageSource extends DamageSource
{
    /** Source of the damage (can be an entity, tile, or the world itself) */
	protected Object damageSource;

	public AbstractDamageSource(String damageType)
	{
		super(damageType);
	}

	public AbstractDamageSource(String damageType, Object source)
	{
		this(damageType);
		this.damageSource = source;
	}

	@Override
	public Entity getEntity()
	{
		return damageSource instanceof Entity ? ((Entity) damageSource) : null;
	}

	public TileEntity getTileEntity()
	{
		return damageSource instanceof TileEntity ? ((TileEntity) damageSource) : null;
	}

	@Override
	public IChatComponent func_151519_b(EntityLivingBase victum)
	{
        //TODO JUnit test to see if this method outputs the correct data
		EntityLivingBase attacker = victum.func_94060_bK();
		String deathTranslation = "death.attack." + this.damageType;
		String playerKillTranslation = deathTranslation + ".player";
		String machineKillTranslation = deathTranslation + ".machine";
		if (damageSource instanceof TileEntity)
		{
			if (StatCollector.canTranslate(machineKillTranslation))
			{
				return new ChatComponentTranslation(machineKillTranslation, victum.func_145748_c_());
			}
		}
		else if (attacker != null)
		{
			if (StatCollector.canTranslate(playerKillTranslation))
			{
				return new ChatComponentTranslation(playerKillTranslation, victum.func_145748_c_(), attacker.func_145748_c_());
			}
		}
		else if (StatCollector.canTranslate(deathTranslation))
		{
			return new ChatComponentTranslation(deathTranslation, victum.func_145748_c_());
		}
		return null;
	}

}
