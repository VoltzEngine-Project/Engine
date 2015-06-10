package com.builtbroken.jlib.model;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Pos;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark on 6/10/2015.
 * https://github.com/OskarVeerhoek/YouTube-tutorials/blob/master/src/episode_24/ModelDemo.java
 */
public class Model
{
    public final List<Mesh> meshes = new ArrayList();

    public void render()
    {
        GL11.glBegin(GL11.GL_LINE_LOOP);
        for(Mesh m : meshes)
        {
            for (Face face : m.getFaces())
            {
                int v1_index = face.getVertexIndices()[0];
                int v2_index = face.getVertexIndices()[1];
                int v3_index = face.getVertexIndices()[2];
                try
                {
                    //Pos n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
                    //GL11.glNormal3f(n1.xf(), n1.yf(), n1.zf());
                    Pos v1 = m.getVertices().get(v1_index);
                    GL11.glVertex3f(v1.xf(), v1.yf(), v1.zf());

                    //Pos n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
                    //GL11.glNormal3f(n2.xf(), n2.yf(), n2.zf());
                    Pos v2 = m.getVertices().get(v2_index);
                    GL11.glVertex3f(v2.xf(), v2.yf(), v2.zf());

                    //Pos n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
                    //GL11.glNormal3f(n3.xf(), n3.yf(), n3.zf());
                    Pos v3 = m.getVertices().get(v3_index);
                    GL11.glVertex3f(v3.xf(), v3.yf(), v3.zf());
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                    Engine.instance.logger().error("Failed to render face " + v1_index +" " + v2_index + " " + v3_index);
                }
            }
        }
        GL11. glEnd();
    }
}
