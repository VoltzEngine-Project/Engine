package com.builtbroken.mc.client.helpers;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

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
        int sectionSize = (int) Math.floor((height - capBotHeight - capTopHeight) / 2f);
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
    public static void drawRect(int left, int top, int right, int bottom, int color)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
        bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    protected static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) right, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    /**
     * Renders the specified text to the screen, center-aligned.
     */
    public static void drawCenteredString(FontRenderer p_73732_1_, String p_73732_2_, int p_73732_3_, int p_73732_4_, int p_73732_5_)
    {
        p_73732_1_.drawStringWithShadow(p_73732_2_, p_73732_3_ - p_73732_1_.getStringWidth(p_73732_2_) / 2, p_73732_4_, p_73732_5_);
    }

    /**
     * Renders the specified text to the screen.
     */
    public static void drawString(FontRenderer p_73731_1_, String p_73731_2_, int p_73731_3_, int p_73731_4_, int p_73731_5_)
    {
        p_73731_1_.drawStringWithShadow(p_73731_2_, p_73731_3_, p_73731_4_, p_73731_5_);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) zLevel).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) zLevel).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}
