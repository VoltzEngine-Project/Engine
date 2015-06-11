package com.builtbroken.jlib.model;

/**
 * Created by Dark on 6/10/2015.
 */
public class Face
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
}
