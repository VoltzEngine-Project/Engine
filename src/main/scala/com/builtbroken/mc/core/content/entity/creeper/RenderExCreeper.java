package com.builtbroken.mc.core.content.entity.creeper;

import com.builtbroken.mc.api.explosive.IExCreeperHandler;
import com.builtbroken.mc.api.explosive.IExplosive;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Copy of the creeper renderer without its flashing render code
 * Created by robert on 2/3/2015.
 */
@SideOnly(Side.CLIENT)
public class RenderExCreeper extends RenderLiving
{
    public static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RenderExCreeper()
    {
        super(new ModelCreeper(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
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
        return creeperTextures;
    }
}
