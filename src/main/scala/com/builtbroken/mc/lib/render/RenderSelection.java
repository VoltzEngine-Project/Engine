package com.builtbroken.mc.lib.render;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.lib.transform.region.Cuboid;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 1/25/2015.
 */
public class RenderSelection
{
    public static List<Cuboid> cube_render_list = new ArrayList();
    public static Cuboid selection = new Cuboid();


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void render(RenderWorldLastEvent event)
    {
        for (Cuboid cube : cube_render_list)
        {
            render(cube, false, false);
        }
        render(selection, true, true);
    }

    public static void render(Cuboid selection, boolean mark_points, boolean is_selection)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tess = Tessellator.instance;
        Tessellator.renderingWorldRenderer = false;

        // GL11.glLineWidth(20f);

        boolean render1 = false;

        if (mark_points)
        {
            // render p1
            if (selection.min() != null)
            {
                IPos3D vec1 = selection.min();
                GL11.glTranslated(vec1.x() - RenderManager.renderPosX, vec1.y() + 1 - RenderManager.renderPosY, vec1.z() - RenderManager.renderPosZ);
                GL11.glScalef(1.0F, -1.0F, -1.0F);
                if (is_selection)
                    GL11.glColor3f(255, 0, 0);
                else
                    GL11.glColor3f(Colors.RED.color().getRed(), Colors.RED.color().getBlue(), Colors.RED.color().getGreen());
                renderBlockBox(tess);
                render1 = true;
            }

            // render p2
            if (selection.max() != null)
            {
                IPos3D p1 = selection.min();
                IPos3D p2 = selection.max();

                if (render1)
                {
                    float x = (float) (p2.x() - p1.x());
                    float y = (float) (p1.y() - p2.y()) + 1;
                    float z = (float) (p1.z() - p2.z()) - 1;

                    GL11.glTranslated(x, y, z);
                }
                else
                {
                    GL11.glTranslated(p2.x() - RenderManager.renderPosX, p2.y() + 1 - RenderManager.renderPosY, p2.z() - RenderManager.renderPosZ);
                }

                GL11.glScalef(1.0F, -1.0F, -1.0F);
                if (is_selection)
                    GL11.glColor3f(0, 255, 0);
                else
                    GL11.glColor3f(Colors.CORRUPTION_PURPLE.color().getRed(), Colors.CORRUPTION_PURPLE.color().getBlue(), Colors.CORRUPTION_PURPLE.color().getGreen());
                renderBlockBox(tess);
            }
        }

        if (selection.min() != null && selection.max() != null)
        {
            float x = (float) (selection.min().x() - selection.max().x());
            float y = (float) (selection.min().y() - selection.max().y());
            float z = (float) (selection.min().z() - selection.max().z()) - 1;

            // translate to the low point..
            GL11.glTranslated(x, y, z);

            GL11.glScalef(1.0F, -1.0F, -1.0F);
            if (is_selection)
                GL11.glColor3f(0, 5, 100);
            else
                GL11.glColor3f(Colors.DARK_RED.color().getRed(), Colors.DARK_RED.color().getBlue(), Colors.DARK_RED.color().getGreen());


            renderBlockBoxTo(tess, new Pos(selection.xSize(), -selection.ySize(), -selection.zSize()));
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // tess.renderingWorldRenderer = true;
        GL11.glPopMatrix();
    }

    /**
     * must be translated to proper point before calling
     */
    private static void renderBlockBox(Tessellator tess)
    {
        tess.startDrawing(GL11.GL_LINES);

        // FRONT
        tess.addVertex(0, 0, 0);
        tess.addVertex(0, 1, 0);

        tess.addVertex(0, 1, 0);
        tess.addVertex(1, 1, 0);

        tess.addVertex(1, 1, 0);
        tess.addVertex(1, 0, 0);

        tess.addVertex(1, 0, 0);
        tess.addVertex(0, 0, 0);

        // BACK
        tess.addVertex(0, 0, -1);
        tess.addVertex(0, 1, -1);
        tess.addVertex(0, 0, -1);
        tess.addVertex(1, 0, -1);
        tess.addVertex(1, 0, -1);
        tess.addVertex(1, 1, -1);
        tess.addVertex(0, 1, -1);
        tess.addVertex(1, 1, -1);

        // betweens.
        tess.addVertex(0, 0, 0);
        tess.addVertex(0, 0, -1);

        tess.addVertex(0, 1, 0);
        tess.addVertex(0, 1, -1);

        tess.addVertex(1, 0, 0);
        tess.addVertex(1, 0, -1);

        tess.addVertex(1, 1, 0);
        tess.addVertex(1, 1, -1);

        tess.draw();
    }

    private static void renderBlockBoxTo(Tessellator tess, Pos vec)
    {
        tess.startDrawing(GL11.GL_LINES);

        // FRONT
        tess.addVertex(0, 0, 0);
        tess.addVertex(0, vec.yi(), 0);

        tess.addVertex(0, vec.yi(), 0);
        tess.addVertex(vec.xi(), vec.yi(), 0);

        tess.addVertex(vec.xi(), vec.yi(), 0);
        tess.addVertex(vec.xi(), 0, 0);

        tess.addVertex(vec.xi(), 0, 0);
        tess.addVertex(0, 0, 0);

        // BACK
        tess.addVertex(0, 0, vec.zi());
        tess.addVertex(0, vec.yi(), vec.zi());
        tess.addVertex(0, 0, vec.zi());
        tess.addVertex(vec.xi(), 0, vec.zi());
        tess.addVertex(vec.xi(), 0, vec.zi());
        tess.addVertex(vec.xi(), vec.yi(), vec.zi());
        tess.addVertex(0, vec.yi(), vec.zi());
        tess.addVertex(vec.xi(), vec.yi(), vec.zi());

        // betweens.
        tess.addVertex(0, 0, 0);
        tess.addVertex(0, 0, vec.zi());

        tess.addVertex(0, vec.yi(), 0);
        tess.addVertex(0, vec.yi(), vec.zi());

        tess.addVertex(vec.xi(), 0, 0);
        tess.addVertex(vec.xi(), 0, vec.zi());

        tess.addVertex(vec.xi(), vec.yi(), 0);
        tess.addVertex(vec.xi(), vec.yi(), vec.zi());

        tess.draw();
    }
}
