package calclavia.lib.render.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import universalelectricity.api.vector.IVector3;
import universalelectricity.api.vector.Vector3;
import calclavia.components.CalclaviaLoader;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FxLaser extends FxBeam
{
	public FxLaser(World world, IVector3 position, IVector3 target, float red, float green, float blue, int age)
	{
		super(new ResourceLocation(CalclaviaLoader.DOMAIN, CalclaviaLoader.TEXTURE_PATH + "laser.png"), world, position, target, red, green, blue, age);
	}
}