package com.builtbroken.lib.prefab.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

/**
 * Extend this class to create more custom damage sources.
 *
 * @author Calclavia
 */
public class CustomDamageSource extends DamageSource
{
	/**
	 * Use this damage source for all types of electrical attacks.
	 */
	public static ElectricalDamage electrocution = new ElectricalDamage(null);

	protected Object damageSource;

	public CustomDamageSource(String damageType)
	{
		super(damageType);
	}

	public CustomDamageSource(String damageType, Object source)
	{
		this(damageType);
		this.damageSource = source;
	}

	@Override
	public CustomDamageSource setDamageBypassesArmor()
	{
		super.setDamageBypassesArmor();
		return this;
	}

	@Override
	public CustomDamageSource setDamageAllowedInCreativeMode()
	{
		super.setDamageAllowedInCreativeMode();
		return this;
	}

	@Override
	public CustomDamageSource setFireDamage()
	{
		super.setFireDamage();
		return this;
	}

	@Override
	public CustomDamageSource setProjectile()
	{
		super.setProjectile();
		return this;
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
		EntityLivingBase attacker = victum.func_94060_bK();
		String deathTranslation = "death.attack." + this.damageType;
		String playerKillTranslation = deathTranslation + ".player";
		String machineKillTranslation = deathTranslation + ".machine";
		if (damageSource instanceof TileEntity)
		{
			if (StatCollector.canTranslate(machineKillTranslation))
			{
				return new ChatComponentTranslation(machineKillTranslation, new Object[] { victum.func_145748_c_() });
			}
		}
		else if (attacker != null)
		{
			if (StatCollector.canTranslate(playerKillTranslation))
			{
				return new ChatComponentTranslation(playerKillTranslation, new Object[] { victum.func_145748_c_(), attacker.func_145748_c_() });
			}
		}
		else if (StatCollector.canTranslate(deathTranslation))
		{
			return new ChatComponentTranslation(deathTranslation, new Object[] { victum.func_145748_c_() });
		}
		return null;
	}

}
