package com.builtbroken.mc.core.content.entity.bat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderExBat extends RenderLiving
{
    private static final ResourceLocation batTextures = new ResourceLocation("textures/entity/bat.png");

    public RenderExBat()
    {
        super(new ModelBat(), 0.25F);
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
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return batTextures;
    }
}