package com.builtbroken.mc.lib.render.fx;

import com.builtbroken.mc.client.SharedAssets;
import com.builtbroken.mc.lib.render.RenderUtility;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

@SideOnly(Side.CLIENT)
public class FXShockWave extends EntityFX
{
	public ResourceLocation texture;

	public FXShockWave(World par1World, double x, double y, double z, float par8, float par10, float par12, double distance)
	{
		this(par1World, x, y, z, par8, par10, par12, 1.0F, distance);
	}

	public FXShockWave(World par1World, double x, double y, double z, float r, float g, float b, float size, double distance)
	{
		super(par1World, x, y, z, 0.0D, 0.0D, 0.0D);
		this.particleRed = r;
		this.particleGreen = g;
		this.particleBlue = b;
		this.particleScale = size;
		this.particleMaxAge = (int) (10D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int) (this.particleMaxAge * size);
		this.renderDistanceWeight = distance;
		this.noClip = false;
	}

	@Override
	public void renderParticle(Tessellator tessellator, float partialTickRate, float par3, float par4, float par5, float par6, float par7)
	{
		GL11.glPushMatrix();
		float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTickRate - interpPosX);
		float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTickRate - interpPosY);
		float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTickRate - interpPosZ);
		GL11.glTranslated(f11, f12, f13);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture != null ? texture : SharedAssets.GREY_TEXTURE);
		RenderUtility.enableBlending();
		RenderUtility.disableLighting();

		GL11.glColor4f(this.particleRed / 255, this.particleGreen / 255, this.particleBlue / 255, 0.5f);

		Sphere sphere = new Sphere();
		sphere.draw(this.particleScale, 32, 32);

		// Enable Lighting/Glow Off
		RenderUtility.enableLighting();

		// Disable Blending
		RenderUtility.disableBlending();
		GL11.glPopMatrix();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.particleScale++;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setDead();
		}
	}
}
