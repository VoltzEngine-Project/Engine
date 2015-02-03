package com.builtbroken.mc.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by robert on 2/3/2015.
 */
public interface IExCreeperRender
{
    /**
     * Gets the texture to use instead of the default creeper texture
     *
     * @param entity - entity that will use the texture for its renderer
     * @return Valid resource location, or null to use default texture
     */
    public ResourceLocation getTexture(Entity entity);
}
