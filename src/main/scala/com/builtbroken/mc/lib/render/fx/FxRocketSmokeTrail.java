package com.builtbroken.mc.lib.render.fx;

import com.builtbroken.mc.core.References;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Zmaster
 *         edited by DarkGuardsman
 */
public class FxRocketSmokeTrail extends EntityFX
{
    public static final ResourceLocation icon = new ResourceLocation(References.DOMAIN, "textures/particle/soft.png");

    public FxRocketSmokeTrail(World world, double x, double y, double z, double motx, double moty, double motz, int age)
    {
        this(world, .4F, .4F, .4F, x, y, z, motx, moty, motz, age);
    }

    public FxRocketSmokeTrail(World world, Color color, double x, double y, double z, double motx, double moty, double motz, int age)
    {
        this(world, (color.getRed() / 255f), (color.getGreen() / 255f), (color.getBlue() / 255f), x, y, z, motx, moty, motz, age);
    }

    public FxRocketSmokeTrail(World world, float r, float g, float b, double x, double y, double z, double motx, double moty, double motz, int age)
    {
        super(world, x, y, z, motx, moty, motz);

        this.prevPosX = this.posX = this.lastTickPosX = x;
        this.prevPosY = this.posY = this.lastTickPosY = y;
        this.prevPosZ = this.posZ = this.lastTickPosZ = z;

        float chroma = this.rand.nextFloat() * 0.2f;
        this.particleRed = r + chroma;
        this.particleGreen = g + chroma;
        this.particleBlue = b + chroma;
        this.setSize(0.12F, 0.12F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 4F;
        this.motionX = motx;
        this.motionY = moty;
        this.motionZ = motz;
        this.particleMaxAge = age;
    }

    @Override
    public void renderParticle(Tessellator tess, float x1, float y1, float z1, float x2, float y2, float z2)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(icon);

        GL11.glPushMatrix();

        float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) x1 - interpPosX);
        float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) x1 - interpPosY);
        float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) x1 - interpPosZ);
        float f10 = 0.1F * this.particleScale;


        tess.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);

        tess.addVertexWithUV((double) (f11 - y1 * f10 - y2 * f10), (double) (f12 - z1 * f10), (double) (f13 - x2 * f10 - z2 * f10), 1, 1);
        tess.addVertexWithUV((double) (f11 - y1 * f10 + y2 * f10), (double) (f12 + z1 * f10), (double) (f13 - x2 * f10 + z2 * f10), 1, 0);
        tess.addVertexWithUV((double) (f11 + y1 * f10 + y2 * f10), (double) (f12 + z1 * f10), (double) (f13 + x2 * f10 + z2 * f10), 0, 0);
        tess.addVertexWithUV((double) (f11 + y1 * f10 - y2 * f10), (double) (f12 - z1 * f10), (double) (f13 + x2 * f10 - z2 * f10), 0, 1);

        GL11.glPopMatrix();
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    @Override
    public void onUpdate()
    {
        //TODO add gravity
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        //Change color and alpha over lifespan
        this.particleAlpha = 1 - this.particleAge / (float) this.particleMaxAge;
        this.particleScale *= 1.002f;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
            return;
        }

        if (particleAge % 20 == 0)
        {
            Block block = worldObj.getBlock((int) posX, (int) posY, (int) posZ);
            if (block != Blocks.air && !block.isAir(worldObj, (int) posX, (int) posY, (int) posZ))
            {
                setDead();
                return;
            }
        }

        this.setPosition(posX + this.motionX, posY + this.motionY, posZ + this.motionX);
    }
}