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
    private final List<Pos> vertices = new ArrayList<Pos>();
    private final List<Point> textureCoordinates = new ArrayList<Point>();
    private final List<Pos> normals = new ArrayList<Pos>();
    private final List<Face> faces = new ArrayList<Face>();

    public boolean hasTextureCoordinates() {
        return getTextureCoordinates().size() > 0;
    }

    public boolean hasNormals() {
        return getNormals().size() > 0;
    }

    public void addVert(Pos pos)
    {
        this.vertices.add(pos);
    }
    public List<Pos> getVertices() {
        return vertices;
    }

    public List<Point> getTextureCoordinates() {
        return textureCoordinates;
    }

    public List<Pos> getNormals() {
        return normals;
    }

    public List<Face> getFaces() {
        return faces;
    }
}
