package com.builtbroken.mc.core.content.entity.bat.ex;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.builtbroken.mc.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderExBat extends RenderLiving
{
    private static final ResourceLocation batTextures = new ResourceLocation(References.DOMAIN, References.ENTITY_TEXTURE_DIRECTORY + "bat.tnt.png");

    public RenderExBat()
    {
        super(new ModelExBat(), 0.25F);
    }

    @Override
    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        if (p_77043_1_ instanceof EntityExBat)
        {
            if (!((EntityExBat) p_77043_1_).isBatHanging())
            {
                GL11.glTranslatef(0.0F, MathHelper.cos(p_77043_2_ * 0.3F) * 0.1F, 0.0F);
            }
            else
            {
                GL11.glTranslatef(0.0F, -0.1F, 0.0F);
            }
        }

        super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        GL11.glScalef(0.35F, 0.35F, 0.35F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        if (entity instanceof EntityExBat)
        {
            EntityExBat bat = (EntityExBat) entity;
            IExplosiveHandler ex = bat.getExplosive();
            if (ex != null)
            {
                //Get render data for explosive
                RenderData renderData = ClientDataHandler.INSTANCE.getRenderData(ex.getID());
                if (renderData != null)
                {
                    //Try several keys
                    for (String key : new String[]{"exBat.entity." + bat.ai_type, "exBat.entity"})
                    {
                        IRenderState renderState = renderData.getState(key);
                        if (renderState != null)
                        {
                            TextureData textureData = renderState.getTextureData(0);
                            if (textureData != null)
                            {
                                return textureData.getLocation();
                            }
                        }
                    }
                }
            }
        }
        return batTextures;
    }
}