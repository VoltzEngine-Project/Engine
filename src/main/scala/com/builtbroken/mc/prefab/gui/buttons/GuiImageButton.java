package com.builtbroken.mc.prefab.gui.buttons;

import com.builtbroken.mc.core.References;
import com.builtbroken.mc.prefab.gui.GuiButton2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

/**
 * Simple button that uses images instead of text
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/25/2016.
 */
public class GuiImageButton extends GuiButton2
{
    int u, v;

    protected GuiImageButton(int id, int x, int y, int width, int height, int u, int v)
    {
        super(id, x, y, width, height, "");
        this.u = u;
        this.v = v;
    }

    public static GuiImageButton newSaveButton(int id, int x, int y)
    {
        return new GuiImageButton(id, x, y, 18, 18, 18, 162);
    }

    public static GuiImageButton newRefreshButton(int id, int x, int y)
    {
        return new GuiImageButton(id, x, y, 18, 18, 18, 198);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(References.GUI_COMPONENTS);

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
