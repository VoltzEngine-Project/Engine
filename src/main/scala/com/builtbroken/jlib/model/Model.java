package com.builtbroken.jlib.model;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Point;
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
    public boolean line = false;

    public void render()
    {
        if (line)
            GL11.glBegin(GL11.GL_LINE_LOOP);
        else
            GL11.glBegin(GL11.GL_TRIANGLES);
        for (Mesh m : meshes)
        {
            for (Face face : m.getFaces())
            {
                int v1_index = face.vertexIndices[0];
                int v2_index = face.vertexIndices[1];
                int v3_index = face.vertexIndices[2];

                int n1_index = face.normalIndices[0];
                int n2_index = face.normalIndices[1];
                int n3_index = face.normalIndices[2];

                int t1_index = face.textureCoordinateIndices[0];
                int t2_index = face.textureCoordinateIndices[1];
                int t3_index = face.textureCoordinateIndices[2];

                try
                {

                    //One
                    Pos n1 = m.normals.get(n1_index);
                    GL11.glNormal3f(n1.xf(), n1.yf(), n1.zf());

                    Pos v1 = m.getVertices().get(v1_index);
                    GL11.glVertex3f(v1.xf(), v1.yf(), v1.zf());

                    if(m.textureCoordinates.size() >= t1_index)
                    {
                        Point t1 = m.textureCoordinates.get(t1_index);
                        GL11.glTexCoord2f(t1.xf(), t1.yf());
                    }

                    //Two
                    Pos n2 = m.normals.get(n2_index);
                    GL11.glNormal3f(n2.xf(), n2.yf(), n2.zf());

                    Pos v2 = m.getVertices().get(v2_index);
                    GL11.glVertex3f(v2.xf(), v2.yf(), v2.zf());

                    if(m.textureCoordinates.size() >= t2_index)
                    {
                        Point t2 = m.textureCoordinates.get(t2_index);
                        GL11.glTexCoord2f(t2.xf(), t2.yf());
                    }

                    //Three
                    Pos n3 = m.normals.get(n3_index);
                    GL11.glNormal3f(n3.xf(), n3.yf(), n3.zf());

                    Pos v3 = m.getVertices().get(v3_index);
                    GL11.glVertex3f(v3.xf(), v3.yf(), v3.zf());

                    if(m.textureCoordinates.size() >= t3_index)
                    {
                        Point t3 = m.textureCoordinates.get(t3_index);
                        GL11.glTexCoord2f(t3.xf(), t3.yf());
                    }
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                    Engine.instance.logger().error("Failed to render face " + v1_index + " " + v2_index + " " + v3_index);
                    Engine.instance.logger().error("Failed to render normal " + n1_index + " " + n2_index + " " + n3_index);
                }
            }
        }
        GL11.glEnd();
    }
}
