package com.builtbroken.mc.core.content.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Copy of the creeper renderer without its flashing render code
 * Created by robert on 2/3/2015.
 */
@SideOnly(Side.CLIENT)
public class RenderExCreeper extends RenderLiving
{
    public static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RenderExCreeper(RenderManager rendermanagerIn)
    {
        super(rendermanagerIn, new ModelCreeper(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        /* TODO update
        if(entity instanceof IExplosive)
        {
            IExplosiveHandler ex = ((IExplosive) entity).getExplosive();
            if(ex instanceof IExCreeperHandler)
            {
                ResourceLocation texture = ((IExCreeperHandler) ex).getCreeperTexture(entity);
                if(texture != null)
                {
                    return texture;
                }
            }
        }
        */
        return creeperTextures;
    }
}
