package com.builtbroken.mc.prefab.gui.buttons;

import com.builtbroken.mc.client.SharedAssets;
import com.builtbroken.mc.prefab.gui.GuiButton2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Simple button that uses images instead of text
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/25/2016.
 */
public class GuiImageButton extends GuiButton2
{
    private int u, v;
    private ResourceLocation textureOverride;

    protected GuiImageButton(int id, int x, int y, int width, int height, int u, int v)
    {
        super(id, x, y, width, height, "");
        this.u = u;
        this.v = v;
    }

    /**
     * Creates a new save button
     *
     * @param id
     * @param x
     * @param y
     * @return
     */
    public static GuiImageButton newSaveButton(int id, int x, int y)
    {
        return new GuiImageButton(id, x, y, 18, 18, 18, 162);
    }

    /**
     * Creates a new refresh button
     *
     * @param id
     * @param x
     * @param y
     * @return
     */
    public static GuiImageButton newRefreshButton(int id, int x, int y)
    {
        return new GuiImageButton(id, x, y, 18, 18, 18, 198);
    }

    /**
     * Creates a new button with a width and height of 18 pixels
     *
     * @param id  - button id
     * @param x   - pos x
     * @param y   - pos y
     * @param row - row in the texture sheet
     * @param col - colume in the texture sheet
     * @return button
     */
    public static GuiImageButton newButton18(int id, int x, int y, int row, int col)
    {
        return new GuiImageButton(id, x, y, 18, 18, col * 18, row * 18);
    }

    /**
     * Overrides the default texture for buttons
     *
     * @param location
     */
    public GuiImageButton setTexture(ResourceLocation location)
    {
        this.textureOverride = location;
        return this;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(textureOverride == null ? SharedAssets.GUI_COMPONENTS : textureOverride);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, u + (field_146123_n ? width : 0), v, this.width, this.height);
            //this.drawString(mc.fontRenderer, "" + id, this.xPosition, this.yPosition, Color.red.getRGB()); TODO add hot key to enable button id debug
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
