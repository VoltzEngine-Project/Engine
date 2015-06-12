package com.builtbroken.jlib.model;

/**
 * Created by Dark on 6/10/2015.
 */
public class Face implements Cloneable
{
    //Theses are references to the index in the mesh list of Pos objects
    public final int[] vertexIndices = {-1, -1, -1};
    public final int[] normalIndices = {-1, -1, -1};
    public final int[] textureCoordinateIndices = {-1, -1, -1};


    public Face(int p1, int p2, int p3) {
        this.vertexIndices[0] = p1;
        this.vertexIndices[1] = p2;
        this.vertexIndices[2] = p3;
    }

    @Override
    public Face clone()
    {
        Face face = new Face(vertexIndices[0], vertexIndices[1], vertexIndices[2]);
        face.normalIndices[0] = normalIndices[0];
        face.normalIndices[1] = normalIndices[1];
        face.normalIndices[2] = normalIndices[2];
        face.textureCoordinateIndices[0] = textureCoordinateIndices[0];
        face.textureCoordinateIndices[1] = textureCoordinateIndices[1];
        face.textureCoordinateIndices[2] = textureCoordinateIndices[2];
        return face;
    }
}
