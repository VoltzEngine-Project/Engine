package com.builtbroken.mc.prefab.entity.selector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;

/**
 * Created by robert on 2/23/2015.
 */
public class EntityXpSelector extends EntitySelector
{
    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        return super.isEntityApplicable(entity) && entity instanceof EntityXPOrb;
    }
}
