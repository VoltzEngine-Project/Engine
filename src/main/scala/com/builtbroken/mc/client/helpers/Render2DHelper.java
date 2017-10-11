package com.builtbroken.mc.client.helpers;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2017.
 */
public class Render2DHelper
{
    public static float zLevel = 0;

    /**
     * Renders a 2D image rect with the provides data
     *
     * @param x            - position to render
     * @param y            - position to render
     * @param u            - position in texture
     * @param v            - position in texture
     * @param height       - height of texture
     * @param capHeight    - height of end caps
     * @param renderHeight - total height to render, including caps and mid section
     */
    public static void renderWithRepeatVertical(int x, int y, int u, int v, int width, int height, int capHeight, int renderHeight)
    {
        renderWithRepeatVertical(x, y, u, v, width, height, capHeight, capHeight, renderHeight - capHeight * 2);
    }

    /**
     * Renders a 2D image rect with the provides data
     *
     * @param x            - position to render
     * @param y            - position to render
     * @param u            - position in texture
     * @param v            - position in texture
     * @param height       - height of texture
     * @param capTopHeight - height of cap
     * @param capBotHeight - height of cap
     * @param midHeight    - height of mid section
     */
    public static void renderWithRepeatVertical(int x, int y, int u, int v, int width, int height, int capTopHeight, int capBotHeight, int midHeight)
    {
        //Render top
        drawTexturedModalRect(x, y, u, v, width, capTopHeight);

        //Render middle
        int sizeRender = 0;
        int sectionSize =(int)Math.floor((height - capBotHeight - capTopHeight) / 2f);
        int sections = (int) Math.ceil(midHeight / (float) sectionSize);
        for (int s = 0; s < sections; s++)
        {
            if (s == sections - 1)
            {
                int remain = midHeight - sizeRender;
                drawTexturedModalRect(x, y + capTopHeight + sizeRender, u, v + capTopHeight, width, remain);
            }
            else
            {
                drawTexturedModalRect(x, y + capTopHeight + sizeRender, u, v + capTopHeight, width, sectionSize);
                sizeRender += sectionSize;
            }
        }
        //Render bottom
        drawTexturedModalRect(x, y + capTopHeight + midHeight, u, v + height - capBotHeight, width, capBotHeight);
    }

    /**
     * Renders a 2D image rect with the provides data
     *
     * @param x            - position to render
     * @param y            - position to render
     * @param u            - position in texture
     * @param v            - position in texture
     * @param height       - height of texture
     * @param capTopWidth - height of cap
     * @param capBotWidth - height of cap
     * @param midWidth    - height of mid section
     */
    public static void renderWithRepeatHorizontal(int x, int y, int u, int v, int width, int height, int capTopWidth, int capBotWidth, int midWidth)
    {
        //Render top
        drawTexturedModalRect(x, y, u, v, capTopWidth, height);

        //Render middle
        int sizeRender = 0;
        int sectionSize =(int)Math.floor((width - capBotWidth - capTopWidth) / 2f);
        int sections = (int) Math.ceil(midWidth / (float) sectionSize);
        for (int s = 0; s < sections; s++)
        {
            if (s == sections - 1)
            {
                int remain = midWidth - sizeRender;
                drawTexturedModalRect(x + capTopWidth + sizeRender, y, u + capTopWidth, v, remain, height);
            }
            else
            {
                drawTexturedModalRect(x + capTopWidth + sizeRender, y, u + capTopWidth, v, sectionSize, height);
                sizeRender += sectionSize;
            }
        }
        //Render bottom
        drawTexturedModalRect(x + capTopWidth + midWidth, y, u + width - capBotWidth, v, capBotWidth, height);
    }

    public static void drawHorizontalLine(int p_73730_1_, int p_73730_2_, int p_73730_3_, int p_73730_4_)
    {
        if (p_73730_2_ < p_73730_1_)
        {
            int i1 = p_73730_1_;
            p_73730_1_ = p_73730_2_;
            p_73730_2_ = i1;
        }

        drawRect(p_73730_1_, p_73730_3_, p_73730_2_ + 1, p_73730_3_ + 1, p_73730_4_);
    }

    public static void drawVerticalLine(int p_73728_1_, int p_73728_2_, int p_73728_3_, int p_73728_4_)
    {
        if (p_73728_3_ < p_73728_2_)
        {
            int i1 = p_73728_2_;
            p_73728_2_ = p_73728_3_;
            p_73728_3_ = i1;
        }

        drawRect(p_73728_1_, p_73728_2_ + 1, p_73728_1_ + 1, p_73728_3_, p_73728_4_);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
     */
    public static void drawRect(int p_73734_0_, int p_73734_1_, int p_73734_2_, int p_73734_3_, int p_73734_4_)
    {
        int j1;

        if (p_73734_0_ < p_73734_2_)
        {
            j1 = p_73734_0_;
            p_73734_0_ = p_73734_2_;
            p_73734_2_ = j1;
        }

        if (p_73734_1_ < p_73734_3_)
        {
            j1 = p_73734_1_;
            p_73734_1_ = p_73734_3_;
            p_73734_3_ = j1;
        }

        float f3 = (float) (p_73734_4_ >> 24 & 255) / 255.0F;
        float f = (float) (p_73734_4_ >> 16 & 255) / 255.0F;
        float f1 = (float) (p_73734_4_ >> 8 & 255) / 255.0F;
        float f2 = (float) (p_73734_4_ & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f, f1, f2, f3);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double) p_73734_0_, (double) p_73734_3_, 0.0D);
        tessellator.addVertex((double) p_73734_2_, (double) p_73734_3_, 0.0D);
        tessellator.addVertex((double) p_73734_2_, (double) p_73734_1_, 0.0D);
        tessellator.addVertex((double) p_73734_0_, (double) p_73734_1_, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    public static void drawGradientRect(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_)
    {
        float f = (float) (p_73733_5_ >> 24 & 255) / 255.0F;
        float f1 = (float) (p_73733_5_ >> 16 & 255) / 255.0F;
        float f2 = (float) (p_73733_5_ >> 8 & 255) / 255.0F;
        float f3 = (float) (p_73733_5_ & 255) / 255.0F;
        float f4 = (float) (p_73733_6_ >> 24 & 255) / 255.0F;
        float f5 = (float) (p_73733_6_ >> 16 & 255) / 255.0F;
        float f6 = (float) (p_73733_6_ >> 8 & 255) / 255.0F;
        float f7 = (float) (p_73733_6_ & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex((double) p_73733_3_, (double) p_73733_2_, (double) zLevel);
        tessellator.addVertex((double) p_73733_1_, (double) p_73733_2_, (double) zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double) p_73733_1_, (double) p_73733_4_, (double) zLevel);
        tessellator.addVertex((double) p_73733_3_, (double) p_73733_4_, (double) zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Renders the specified text to the screen, center-aligned.
     */
    public void drawCenteredString(FontRenderer p_73732_1_, String p_73732_2_, int p_73732_3_, int p_73732_4_, int p_73732_5_)
    {
        p_73732_1_.drawStringWithShadow(p_73732_2_, p_73732_3_ - p_73732_1_.getStringWidth(p_73732_2_) / 2, p_73732_4_, p_73732_5_);
    }

    /**
     * Renders the specified text to the screen.
     */
    public void drawString(FontRenderer p_73731_1_, String p_73731_2_, int p_73731_3_, int p_73731_4_, int p_73731_5_)
    {
        p_73731_1_.drawStringWithShadow(p_73731_2_, p_73731_3_, p_73731_4_, p_73731_5_);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(int p_73729_1_, int p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (p_73729_1_ + 0), (double) (p_73729_2_ + p_73729_6_), (double) zLevel, (double) ((float) (p_73729_3_ + 0) * f), (double) ((float) (p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + p_73729_5_), (double) (p_73729_2_ + p_73729_6_), (double) zLevel, (double) ((float) (p_73729_3_ + p_73729_5_) * f), (double) ((float) (p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + p_73729_5_), (double) (p_73729_2_ + 0), (double) zLevel, (double) ((float) (p_73729_3_ + p_73729_5_) * f), (double) ((float) (p_73729_4_ + 0) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + 0), (double) (p_73729_2_ + 0), (double) zLevel, (double) ((float) (p_73729_3_ + 0) * f), (double) ((float) (p_73729_4_ + 0) * f1));
        tessellator.draw();
    }
}
