package com.builtbroken.lib.prefab.damage;

/**
 * Electrical based damage source
 *
 * @author Darkguardsman
 */
public class ElectricalDamage extends CustomDamageSource
{
	public ElectricalDamage(Object source)
	{
		super("electrocution");
		this.setDamageBypassesArmor();
		this.setDifficultyScaled();
	}
}