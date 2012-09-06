package universalelectricity.extend;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;

public class CustomDamage extends DamageSource
{
	/**
     * Use this damage source for all types of electrical attacks.
     */
    public static final CustomDamage electrocution = (CustomDamage)new CustomDamage("electrocution", "%PLAYER% got electrocuted.").setDamageBypassesArmor();
    
    public String deathMessage;
    
    public CustomDamage(String damageType)
	{
		super(damageType);
	}
    
    public CustomDamage(String damageType, String deathMessage)
	{
		this(damageType);
		this.setDeathMessage(deathMessage);
	}
	
	public CustomDamage setDeathMessage(String deathMessage)
	{
		this.deathMessage = deathMessage;
		return this;
	}
	
	@Override
	public String getDeathMessage(EntityPlayer par1EntityPlayer)
    {
		if(this.deathMessage == null || this.deathMessage == "")
		{
			return super.getDeathMessage(par1EntityPlayer);
		}
		else
		{
			return this.deathMessage.replaceAll("%PLAYER%", par1EntityPlayer.username);
		}
    }
}
