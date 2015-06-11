package com.builtbroken.jlib.model;

import com.builtbroken.mc.lib.transform.vector.Point;
import com.builtbroken.mc.lib.transform.vector.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark on 6/10/2015.
 */
public class Mesh
{
    public final List<Pos> vertices = new ArrayList<Pos>();
    public final List<Point> textureCoordinates = new ArrayList<Point>();
    public final List<Pos> normals = new ArrayList<Pos>();
    public final List<Face> faces = new ArrayList<Face>();

    public void addVert(Pos pos)
    {
        this.vertices.add(pos);
    }

    public List<Pos> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() {
        return faces;
    }
}
