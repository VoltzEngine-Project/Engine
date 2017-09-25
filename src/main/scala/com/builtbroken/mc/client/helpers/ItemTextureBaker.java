package com.builtbroken.mc.client.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Renders 2D textures and saves them to file
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/22/2017.
 */
public class ItemTextureBaker
{
    /** Itemstack to render */
    protected ItemStack stackToDraw;

    /** Minecraft instance */
    protected Minecraft minecraft;
    /** Render system to use */
    protected RenderItem itemRender;

    /** Instance to send render calls to */
    public static ItemTextureBaker instance = new ItemTextureBaker();

    public ItemTextureBaker()
    {
        minecraft = Minecraft.getMinecraft();
        itemRender = new RenderItem();
    }

    public static void saveTextureToFolder(String folder, ItemStack item)
    {
        if (item != null && item.getItem() != null)
        {
            //Get save folder
            final File saveFolder = new File(Minecraft.getMinecraft().mcDataDir, folder);
            if (!saveFolder.exists() && !saveFolder.mkdirs())
            {
                throw new RuntimeException("The output directory could not be created: " + saveFolder);
            }

            //Get name
            final String name = item.getUnlocalizedName();

            //Get save file
            final File saveFile = new File(saveFolder, name.replace(":", ".") + ".png");

            if (isFilenameValid(saveFile))
            {
                try
                {
                    //Set data
                    instance.stackToDraw = item;

                    //Render into buffer image
                    BufferedImage image = instance.renderItem();

                    if (image != null && image.getHeight() > 0 && image.getWidth() > 0)
                    {
                        //Save image to file
                        try
                        {
                            if (!ImageIO.write(image, "png", saveFile))
                            {
                                System.out.println("ItemTextureBaker: Failed to write image to file, item: " + item);
                            }
                            else
                            {
                                System.out.println("ItemTextureBaker: Saved item: " + item + "  to " + saveFile);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("ItemTextureBaker: Can't not save using file due to illegal characters in path, file: " + saveFile);
            }
        }
    }

    public static boolean isFilenameValid(File file)
    {
        try
        {
            file.getCanonicalPath();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    /** Draws the screen and all the components in it. */
    protected void drawItemStack()
    {
        //Setup
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();

        //move item to corner
        GL11.glTranslatef(0, 0, 0);
        GL11.glScalef(8, 8, 8);

        //Reset color
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        //Setup lighting
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);

        //Reset color
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        //Render item
        try
        {
            final int x = 0;
            final int y = 0;
            itemRender.renderItemAndEffectIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, stackToDraw, x, y);
            itemRender.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, stackToDraw, x, y);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (Tessellator.instance.isDrawing)
            {
                Tessellator.instance.draw();
            }
            GL11.glPopMatrix();
        }

        //Tear down
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    /**
     * Called to render the item in a small view port
     *
     * @return BufferedImage containing the rendered item image
     */
    protected BufferedImage renderItem()
    {
        int pixelsX = 128;
        int pixelsY = 128;

        //Start
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushClientAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        //Set up view
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, pixelsX, pixelsY);
        GL11.glOrtho(0.0D, pixelsX, pixelsY, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glLineWidth(1.0F);

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        //Draw image
        try
        {
            drawItemStack();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //Capture pixels
        ByteBuffer frameBuffer = ByteBuffer.allocateDirect(pixelsX * pixelsY * 3);
        GL11.glReadPixels(0, 0, pixelsX, pixelsY, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, frameBuffer);

        //Done / Reset
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        GL11.glPopClientAttrib();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        //swap out buffer
        try
        {
            Display.swapBuffers();
        }
        catch (LWJGLException e1)
        {
            e1.printStackTrace();
        }

        //Clone buffer into an image
        BufferedImage image = new BufferedImage(pixelsX, pixelsY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < pixelsX; x++)
        {
            for (int y = 0; y < pixelsY; y++)
            {
                int i = (x + (pixelsX * y)) * 3;
                int r = frameBuffer.get(i) & 0xFF;
                int g = frameBuffer.get(i + 1) & 0xFF;
                int b = frameBuffer.get(i + 2) & 0xFF;
                image.setRGB(x, pixelsY - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        return image;
    }
}