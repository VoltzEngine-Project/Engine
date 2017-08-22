package com.builtbroken.mc.core.handler;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 1/25/2015.
 */
public class RenderSelection
{
    public static List<Cube> cube_render_list = new ArrayList();
    public static List<Cube> region_render_list = new ArrayList();
    public static Cube selection = null;


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void render(RenderWorldLastEvent event)
    {
        for (Cube cube : cube_render_list)
        {
            if (cube != null)
                render(cube, Colors.DARK_GREY.color());
        }
        for (Cube cube : region_render_list)
        {
            //TODO hide corner selection renders so only the main box renders for regions
            if (cube != null)
                render(cube, Colors.DARK_RED.color());
        }
        if (selection != null)
            render(selection, new Color(0, 5, 210));
    }

    public static void render(Cube selection, Color box_color)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tess = Tessellator.instance;
        Tessellator.renderingWorldRenderer = false;

        // GL11.glLineWidth(20f);

        boolean render1 = false;

        // render p1
        if (selection.pointOne() != null && selection.pointOne().y() >= 0)
        {
            IPos3D vec1 = selection.pointOne();
            GL11.glTranslated(vec1.x() - RenderManager.renderPosX, vec1.y() + 1 - RenderManager.renderPosY, vec1.z() - RenderManager.renderPosZ);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glColor3f(255, 0, 0);
            renderBlockBox(tess);
            render1 = true;
        }

        // render p2
        if (selection.pointTwo() != null && selection.pointOne().y() >= 0)
        {
            IPos3D p1 = selection.pointOne();
            IPos3D p2 = selection.pointTwo();

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
            GL11.glColor3f(0, 255, 0);
            renderBlockBox(tess);
        }

        if (selection.isValid())
        {
            float x = (float) (selection.min().xf() - selection.pointTwo().x());
            float y = (float) (selection.min().yf() - selection.pointTwo().y());
            float z = (float) (selection.min().zf() - selection.pointTwo().z()) - 1;

            // translate to the low point..
            GL11.glTranslated(x, y, z);

            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glColor3f(box_color.getRed(), box_color.getGreen(), box_color.getBlue());

            renderBlockBoxTo(tess, new Pos(selection.getSizeX(), -selection.getSizeY(), -selection.getSizeZ()));
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
