package com.builtbroken.mc.lib.render.model;

import com.builtbroken.jlib.model.Face;
import com.builtbroken.jlib.model.Mesh;
import com.builtbroken.jlib.model.UVPoint;
import com.builtbroken.jlib.model.Vert;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark on 6/10/2015.
 * https://github.com/OskarVeerhoek/YouTube-tutorials/blob/master/src/episode_24/ModelDemo.java
 * http://thecodinguniverse.com/wp-content/uploads/2012/07/OpenGL-Shapes.png
 */
public class Model implements Cloneable
{
    public final List<Mesh> meshes = new ArrayList();
    public boolean render_wireframe = false;
    public boolean render_normals = false;

    public Model(Mesh... meshes)
    {
        if (meshes != null)
            for (Mesh mesh : meshes)
                this.meshes.add(mesh);
    }

    public void render()
    {
        if (render_wireframe)
            GL11.glBegin(GL11.GL_LINE_LOOP);
        else
            GL11.glBegin(GL11.GL_TRIANGLES);


        for (Mesh m : meshes)
        {
            for (Face face : m.getFaces())
            {
                try
                {

                    //One
                    renderVert(m, face, 0);

                    //Two
                    renderVert(m, face, 1);

                    //Three
                    renderVert(m, face, 2);
                } catch (ArrayIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
            }
        }
        GL11.glEnd();

        if (render_normals)
        {
            GL11.glColor3f(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue());
            for (Mesh m : meshes)
            {
                for (Face face : m.getFaces())
                {
                    Vert center = center2(m.vertices.get(face.vertexIndices[0]), m.vertices.get(face.vertexIndices[1]), m.vertices.get(face.vertexIndices[2]));
                    Vert out = center.add(m.normals.get(face.normalIndices[0]));

                    GL11.glBegin(GL11.GL_LINE_LOOP);
                    GL11.glVertex3f(center.xf(), center.yf(), center.zf());
                    GL11.glVertex3f(out.xf(), out.yf(), out.zf());
                    GL11.glEnd();
                }
            }
        }
    }

    public void tessellate()
    {
        Tessellator.instance.startDrawing(GL11.GL_TRIANGLES);
        for (Mesh m : meshes)
        {
            for (Face face : m.getFaces())
            {
                tessVert(m, face, 0);
                tessVert(m, face, 1);
                tessVert(m, face, 2);
            }
        }
        Tessellator.instance.draw();
    }

    protected void tessVert(Mesh m, Face face, int i)
    {
        Vert n1 = m.vertices.get(face.vertexIndices[i]);
        UVPoint t1 = m.textureCoordinates.get(face.textureCoordinateIndices[i]);
        Tessellator.instance.addVertexWithUV(n1.xf(), n1.yf(), n1.zf(), t1.xf(), t1.yf());
    }

    protected void renderVert(Mesh m, Face face, int i)
    {
        Vert n1 = m.normals.get(face.normalIndices[i]);
        GL11.glNormal3f(n1.xf(), n1.yf(), n1.zf());

        Vert v1 = m.getVertices().get(face.vertexIndices[i]);
        GL11.glVertex3f(v1.xf(), v1.yf(), v1.zf());

        if (m.textureCoordinates.size() >= face.textureCoordinateIndices[i])
        {
            UVPoint t1 = m.textureCoordinates.get(face.textureCoordinateIndices[i]);
            GL11.glTexCoord2f(t1.xf(), t1.yf());
        }
    }

    static Vert center2(Vert v1, Vert v2, Vert v3)
    {
        return v1.add(v2).add(v3).divide(3);
    }

    @Override
    public Model clone()
    {
        Model model = new Model();
        for(Mesh mesh : meshes)
            model.meshes.add(mesh.clone());
        model.render_normals = render_normals;
        model.render_wireframe = render_wireframe;
        return model;
    }
}
