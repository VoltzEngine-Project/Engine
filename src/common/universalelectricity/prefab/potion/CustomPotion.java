package universalelectricity.prefab.potion;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Potion;

public class CustomPotion extends Potion
{
    public CustomPotion(int id, boolean effectiveness, int color, String name, int indexX, int indexY)
    {
        super(id, effectiveness, color);
        this.setPotionName(name);
        this.setIconIndex(indexX, indexY);
    }
    
    public void register()
	{
    	Potion.potionTypes[this.id] = this;
	}
}
