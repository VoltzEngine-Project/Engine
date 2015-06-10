package com.builtbroken.jlib.model;

/**
 * Created by Dark on 6/10/2015.
 */
public class Face
{
    private final int[] vertexIndices = {-1, -1, -1};
    private final int[] normalIndices = {-1, -1, -1};
    private final int[] textureCoordinateIndices = {-1, -1, -1};


    public boolean hasNormals() {
        return normalIndices[0] != -1;
    }

    public boolean hasTextureCoordinates() {
        return textureCoordinateIndices[0] != -1;
    }

    public int[] getVertexIndices() {
        return vertexIndices;
    }

    public int[] getTextureCoordinateIndices() {
        return textureCoordinateIndices;
    }

    public int[] getNormalIndices() {
        return normalIndices;
    }

    public Face(int p1, int p2, int p3) {
        this.vertexIndices[0] = p1;
        this.vertexIndices[1] = p2;
        this.vertexIndices[2] = p3;
    }

    public Face(int[] vertexIndices, int[] normalIndices) {
        this.vertexIndices[0] = vertexIndices[0];
        this.vertexIndices[1] = vertexIndices[1];
        this.vertexIndices[2] = vertexIndices[2];
        this.normalIndices[0] = normalIndices[0];
        this.normalIndices[1] = normalIndices[1];
        this.normalIndices[2] = normalIndices[2];
    }

    public Face(int[] vertexIndices, int[] normalIndices, int[] textureCoordinateIndices) {
        this.vertexIndices[0] = vertexIndices[0];
        this.vertexIndices[1] = vertexIndices[1];
        this.vertexIndices[2] = vertexIndices[2];
        this.textureCoordinateIndices[0] = textureCoordinateIndices[0];
        this.textureCoordinateIndices[1] = textureCoordinateIndices[1];
        this.textureCoordinateIndices[2] = textureCoordinateIndices[2];
        this.normalIndices[0] = normalIndices[0];
        this.normalIndices[1] = normalIndices[1];
        this.normalIndices[2] = normalIndices[2];
    }
}
