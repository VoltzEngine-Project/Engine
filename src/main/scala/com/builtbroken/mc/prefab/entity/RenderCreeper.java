package com.builtbroken.mc.prefab.entity;

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
public class RenderCreeper extends RenderLiving
{
    public static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RenderCreeper()
    {
        super(new ModelCreeper(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return creeperTextures;
    }
}
