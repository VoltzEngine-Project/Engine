package com.builtbroken.lib.prefab.poison;

import net.minecraft.entity.EntityLivingBase;
import com.builtbroken.lib.prefab.damage.CustomDamageSource;
import com.builtbroken.lib.prefab.potion.CustomPotionEffect;
import com.builtbroken.lib.transform.vector.Vector3;

public class PoisonRadiation extends Poison
{
	public static final Poison INSTANCE = new PoisonRadiation("radiation");
	public static final CustomDamageSource damageSource = new CustomDamageSource("radiation").setDamageBypassesArmor();
	public static boolean disabled = false;

	public PoisonRadiation(String name)
	{
		super(name);
	}

	@Override
	public boolean isEntityProtected(Vector3 emitPosition, EntityLivingBase entity, int amplifier)
	{
		return (emitPosition != null ? this.getAntiPoisonBlockCount(entity.worldObj, emitPosition, new Vector3(entity)) <= amplifier : false) && super.isEntityProtected(emitPosition, entity, amplifier);
	}

	@Override
	protected void doPoisonEntity(Vector3 emitPosition, EntityLivingBase entity, int amplifier)
	{
		if (!PoisonRadiation.disabled)
		{
			entity.addPotionEffect(new CustomPotionEffect(PotionRadiation.INSTANCE.getId(), 20 * 15 * (amplifier + 1), amplifier, null));
		}
	}

}
