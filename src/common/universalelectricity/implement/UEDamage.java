package universalelectricity.implement;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;

public class UEDamage extends DamageSource
{
	/**
     * Use this damage source for all types of electrical attacks.
     */
    public static final UEDamage electrocution = (UEDamage)new UEDamage("electrocution", "%PLAYER% got electrocuted.").setDamageBypassesArmor();
    
    public String deathMessage;
    
    public UEDamage(String damageType)
	{
		super(damageType);
	}
    
    public UEDamage(String damageType, String deathMessage)
	{
		this(damageType);
		this.setDeathMessage(deathMessage);
	}
	
	public UEDamage setDeathMessage(String deathMessage)
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
