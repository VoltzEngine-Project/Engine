package resonant.lib.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import org.lwjgl.opengl.GL11;

import resonant.lib.render.block.RenderBlockEntity;

public class FluidRenderUtility
{
    private static Map<Fluid, int[]> flowingRenderCache = new HashMap<Fluid, int[]>();
    private static Map<Fluid, int[]> stillRenderCache = new HashMap<Fluid, int[]>();

    public static final int DISPLAY_STAGES = 100;

    private static final BlockRenderInfo liquidBlock = new BlockRenderInfo();

    public static ResourceLocation getFluidSheet(FluidStack fluidStack)
    {
        return getFluidSheet(fluidStack.getFluid());
    }

    public static ResourceLocation getFluidSheet(Fluid fluid)
    {
        return RenderUtility.getSpriteTexture(fluid.getSpriteNumber());
    }

    public static Icon getFluidTexture(Fluid fluid, boolean flowing)
    {
        if (fluid == null)
        {
            return null;
        }
        Icon icon = flowing ? fluid.getFlowingIcon() : fluid.getStillIcon();
        if (icon == null)
        {
            icon = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
        }
        return icon;
    }

    public static void setColorForFluidStack(FluidStack fluidstack)
    {
        if (fluidstack == null)
        {
            return;
        }

        int color = fluidstack.getFluid().getColor(fluidstack);
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, 1);
    }

    public static int[] getFluidDisplayLists(FluidStack fluidStack, World world, boolean flowing)
    {
        if (fluidStack == null)
        {
            return null;
        }
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null)
        {
            return null;
        }
        Map<Fluid, int[]> cache = flowing ? flowingRenderCache : stillRenderCache;
        int[] diplayLists = cache.get(fluid);
        if (diplayLists != null)
        {
            return diplayLists;
        }

        diplayLists = new int[DISPLAY_STAGES];

        if (fluid.getBlockID() > 0)
        {
            liquidBlock.baseBlock = Block.blocksList[fluid.getBlockID()];
            liquidBlock.texture = getFluidTexture(fluidStack.getFluid(), flowing);
        }
        else
        {
            liquidBlock.baseBlock = Block.waterStill;
            liquidBlock.texture = getFluidTexture(fluidStack.getFluid(), flowing);
        }

        cache.put(fluid, diplayLists);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        for (int s = 0; s < DISPLAY_STAGES; ++s)
        {
            diplayLists[s] = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(diplayLists[s], 4864 /* GL_COMPILE */);

            liquidBlock.min.x = 0.01f;
            liquidBlock.min.y = 0;
            liquidBlock.min.z = 0.01f;

            liquidBlock.max.x = 0.99f;
            liquidBlock.max.y = (float) s / (float) DISPLAY_STAGES;
            liquidBlock.max.z = 0.99f;

            RenderBlockEntity.INSTANCE.renderBlock(liquidBlock, world, 0, 0, 0, false, true);

            GL11.glEndList();
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);

        return diplayLists;
    }

    /** Based on Open Blocks tank renderer
     * 
     * @param tank */
    public static void renderFluidTesselation(IFluidTank tank, double ySouthEast, double yNorthEast, double ySouthWest, double yNorthWest)
    {
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack != null && fluidStack.amount > 0)
        {
            GL11.glPushMatrix();
            GL11.glDisable(2896);

            Fluid fluid = fluidStack.getFluid();

            Icon texture = fluid.getStillIcon();
            RenderUtility.bind(getFluidSheet(fluid));
            int color = fluid.getColor(fluidStack);

            Tessellator t = Tessellator.instance;

            double uMin = texture.getInterpolatedU(0.0);
            double uMax = texture.getInterpolatedU(16.0);
            double vMin = texture.getInterpolatedV(0.0);
            double vMax = texture.getInterpolatedV(16.0);

            double vHeight = vMax - vMin;

            float r = (color >> 16 & 0xFF) / 255.0F;
            float g = (color >> 8 & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;

            // north side
            t.startDrawingQuads();
            t.setColorOpaque_F(r, g, b);
            t.addVertexWithUV(0.5, -0.5, -0.5, uMax, vMin); // bottom
            t.addVertexWithUV(-0.5, -0.5, -0.5, uMin, vMin); // bottom
            // top
            t.addVertexWithUV(-0.5, -0.5 + yNorthWest, -0.5, uMin, vMin + (vHeight * yNorthWest));
            // north/west
            t.addVertexWithUV(0.5, -0.5 + yNorthEast, -0.5, uMax, vMin + (vHeight * yNorthEast));
            // top
            // north/east
            t.draw();

            // south side
            t.startDrawingQuads();
            t.setColorOpaque_F(r, g, b);
            // top
            t.addVertexWithUV(0.5, -0.5, 0.5, uMin, vMin);
            // south
            t.addVertexWithUV(0.5, -0.5 + ySouthEast, 0.5, uMin, vMin + (vHeight * ySouthEast));
            // east
            t.addVertexWithUV(-0.5, -0.5 + ySouthWest, 0.5, uMax, vMin + (vHeight * ySouthWest));
            // top
            // south
            // west
            t.addVertexWithUV(-0.5, -0.5, 0.5, uMax, vMin);
            t.draw();

            // east side
            t.startDrawingQuads();
            t.setColorOpaque_F(r, g, b);
            t.addVertexWithUV(0.5, -0.5, -0.5, uMin, vMin);// top
            // north/east
            t.addVertexWithUV(0.5, -0.5 + yNorthEast, -0.5, uMin, vMin + (vHeight * yNorthEast));
            // top
            // south/east
            t.addVertexWithUV(0.5, -0.5 + ySouthEast, 0.5, uMax, vMin + (vHeight * ySouthEast));
            t.addVertexWithUV(0.5, -0.5, 0.5, uMax, vMin);
            t.draw();

            // west side
            t.startDrawingQuads();
            t.setColorOpaque_F(r, g, b);
            t.addVertexWithUV(-0.5, -0.5, 0.5, uMin, vMin);
            // top
            // south/west
            t.addVertexWithUV(-0.5, -0.5 + ySouthWest, 0.5, uMin, vMin + (vHeight * ySouthWest));
            // top
            // north/west
            t.addVertexWithUV(-0.5, -0.5 + yNorthWest, -0.5, uMax, vMin + (vHeight * yNorthWest));
            t.addVertexWithUV(-0.5, -0.5, -0.5, uMax, vMin);
            t.draw();

            // top
            t.startDrawingQuads();
            t.setColorOpaque_F(r, g, b);
            // south
            // east
            t.addVertexWithUV(0.5, -0.5 + ySouthEast, 0.5, uMax, vMin);
            // north
            // east
            t.addVertexWithUV(0.5, -0.5 + yNorthEast, -0.5, uMin, vMin);
            // north
            // west
            t.addVertexWithUV(-0.5, -0.5 + yNorthWest, -0.5, uMin, vMax);
            // south
            // west
            t.addVertexWithUV(-0.5, -0.5 + ySouthWest, 0.5, uMax, vMax);
            t.draw();

            // bottom
            t.startDrawingQuads();
            t.setColorOpaque_F(r, g, b);
            t.addVertexWithUV(0.5, -0.5, -0.5, uMax, vMin);
            t.addVertexWithUV(0.5, -0.5, 0.5, uMin, vMin);
            t.addVertexWithUV(-0.5, -0.5, 0.5, uMin, vMax);
            t.addVertexWithUV(-0.5, -0.5, -0.5, uMax, vMax);
            t.draw();

            GL11.glEnable(2896);
            GL11.glPopMatrix();
        }
    }

}
