package calclavia.lib.prefab;

import net.minecraft.util.DamageSource;

/**
 * Extend this class to create more custom damage sources.
 * 
 * @author Calclavia
 * 
 */
public class CustomDamageSource extends DamageSource
{
	/**
	 * Use this damage source for all types of electrical attacks.
	 */
	public static final CustomDamageSource electrocution = ((CustomDamageSource) new CustomDamageSource("electrocution").setDamageBypassesArmor());

	public CustomDamageSource(String damageType)
	{
		super(damageType);
	}

	@Override
	public DamageSource setDamageBypassesArmor()
	{
		return super.setDamageBypassesArmor();
	}

	@Override
	public DamageSource setDamageAllowedInCreativeMode()
	{
		return super.setDamageAllowedInCreativeMode();
	}

	@Override
	public DamageSource setFireDamage()
	{
		return super.setFireDamage();
	}
}
