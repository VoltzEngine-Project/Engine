package resonant.lib.prefab.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/** Basic prefab for laser based damage sourced that are created by an entity.
 * 
 * @author Darkguardsman */
public class EntityDamageSourceLaser extends EntityDamageSource
{
    public EntityDamageSourceLaser(Entity source)
    {
        super("Laser", source);
    }

    @ForgeSubscribe
    public void LivingDeathEvent(LivingDeathEvent event)
    {
        if (event.entity instanceof EntityCreeper)
        {
            if (!event.entity.worldObj.isRemote && event.source instanceof EntityDamageSourceLaser)
            {
                boolean flag = event.entity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");

                if (((EntityCreeper) event.entity).getPowered())
                {
                    event.entity.worldObj.createExplosion(event.entity, event.entity.posX, event.entity.posY, event.entity.posZ, 3 * 2, flag);
                }
                else
                {
                    event.entity.worldObj.createExplosion(event.entity, event.entity.posX, event.entity.posY, event.entity.posZ, 3, flag);
                }
            }
        }
    }
}
