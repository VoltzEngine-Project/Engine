package com.builtbroken.jlib.model;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Point;
import com.builtbroken.mc.lib.transform.vector.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
 */
public class IcoSphereCreator
{
    //I know this only saves milli seconds but meh
    private static final HashMap<Integer, Mesh> SPHERE_CACHE = new HashMap();

    private Mesh geometry;
    private int index;
    private HashMap<Long, Integer> middlePointIndexCache;

    public static void init_cache()
    {
        SPHERE_CACHE.clear();
        for (int i = 0; i < 6; i++)
        {
            IcoSphereCreator creator = new IcoSphereCreator();
            SPHERE_CACHE.put(i, creator.Create(i));
        }
    }

    /**
     * Creates a new mesh sphere
     * @param level - number of times to subdivide the triangles
     * @return mesh ico sphere, if mesh is cached it will clone
     */
    public static Mesh create(int level)
    {
        return create(level, true);
    }

    /**
     * Creates a new mesh sphere
     * @param level - number of times to subdivide the triangles
     * @param cache - should the mesh be cached for faster creation,
     *                  false if you know your not going to need
     *                  the mesh again
     * @return mesh ico sphere, if mesh is cached it will clone
     */
    public static Mesh create(int level, boolean cache)
    {
        if (!SPHERE_CACHE.containsKey(level) || SPHERE_CACHE.get(level) == null)
        {
            IcoSphereCreator creator = new IcoSphereCreator();
            if(cache)
            {
                SPHERE_CACHE.put(level, creator.Create(level));
            }
            else
            {
                return creator.Create(level);
            }
        }
        return SPHERE_CACHE.get(level).clone();

    }

    // add vertex to mesh, fix position to be on unit sphere, return index
    private int addVertex(Pos p)
    {
        double length = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ());
        geometry.addVert(new Pos(p.getX() / length, p.getY() / length, p.getZ() / length));
        return index++;
    }

    // return index of point in the middle of p1 and p2
    private int getMiddlePoint(int p1, int p2)
    {
        // first check if we have it already
        boolean firstIsSmaller = p1 < p2;
        long smallerIndex = firstIsSmaller ? p1 : p2;
        long greaterIndex = firstIsSmaller ? p2 : p1;
        long key = (smallerIndex << 32) + greaterIndex;


        if (this.middlePointIndexCache.containsKey(key))
        {
            return this.middlePointIndexCache.get(key);
        }

        // not in cache, calculate it
        Pos point1 = this.geometry.getVertices().get(p1);
        Pos point2 = this.geometry.getVertices().get(p2);
        Pos middle = new Pos(
                (point1.x() + point2.x()) / 2.0,
                (point1.y() + point2.y()) / 2.0,
                (point1.z() + point2.z()) / 2.0);

        // add vertex makes sure point is on unit sphere
        int i = addVertex(middle);

        // store it, return index
        this.middlePointIndexCache.put(key, i);
        return i;
    }

    /**
     * Generates an Iso Sphere using a sub divide process
     * 1 - > 12 vert
     * 2 - > 42 vert
     * 3 - > 62
     * 4 - > 642
     * 5 - > 2562
     * 6 - > 10242
     * 7 - > 40962
     *
     * @param recursionLevel - number of times to sub divide the triangles into 4 more triangles
     * @return Mesh containing the data for the sphere
     */
    private Mesh Create(int recursionLevel)
    {
        this.geometry = new Mesh();
        this.middlePointIndexCache = new HashMap();
        this.index = 0;

        // create 12 vertices of a icosahedron
        double t = (1.0 + Math.sqrt(5.0)) / 2.0;

        addVertex(new Pos(-1, t, 0));
        addVertex(new Pos(1, t, 0));
        addVertex(new Pos(-1, -t, 0));
        addVertex(new Pos(1, -t, 0));

        addVertex(new Pos(0, -1, t));
        addVertex(new Pos(0, 1, t));
        addVertex(new Pos(0, -1, -t));
        addVertex(new Pos(0, 1, -t));

        addVertex(new Pos(t, 0, -1));
        addVertex(new Pos(t, 0, 1));
        addVertex(new Pos(-t, 0, -1));
        addVertex(new Pos(-t, 0, 1));


        // create 20 triangles of the icosahedron
        List<Face> faces = new ArrayList();

        // 5 faces around point 0
        faces.add(new Face(0, 11, 5));
        faces.add(new Face(0, 5, 1));
        faces.add(new Face(0, 1, 7));
        faces.add(new Face(0, 7, 10));
        faces.add(new Face(0, 10, 11));

        // 5 adjacent faces 
        faces.add(new Face(1, 5, 9));
        faces.add(new Face(5, 11, 4));
        faces.add(new Face(11, 10, 2));
        faces.add(new Face(10, 7, 6));
        faces.add(new Face(7, 1, 8));

        // 5 faces around point 3
        faces.add(new Face(3, 9, 4));
        faces.add(new Face(3, 4, 2));
        faces.add(new Face(3, 2, 6));
        faces.add(new Face(3, 6, 8));
        faces.add(new Face(3, 8, 9));

        // 5 adjacent faces 
        faces.add(new Face(4, 9, 5));
        faces.add(new Face(2, 4, 11));
        faces.add(new Face(6, 2, 10));
        faces.add(new Face(8, 6, 7));
        faces.add(new Face(9, 8, 1));


        // refine triangles
        for (int i = 0; i < recursionLevel; i++)
        {
            List<Face> faces2 = new ArrayList();
            for (Face tri : faces)
            {
                // replace triangle by 4 triangles
                int a = getMiddlePoint(tri.vertexIndices[0], tri.vertexIndices[1]);
                int b = getMiddlePoint(tri.vertexIndices[1], tri.vertexIndices[2]);
                int c = getMiddlePoint(tri.vertexIndices[2], tri.vertexIndices[0]);

                faces2.add(new Face(tri.vertexIndices[0], a, c));
                faces2.add(new Face(tri.vertexIndices[1], b, a));
                faces2.add(new Face(tri.vertexIndices[2], c, b));
                faces2.add(new Face(a, b, c));
            }
            faces = faces2;
        }



        /* TODO implement vertex normals
         * vertex v1, v2, v3, ....
         * triangle tr1, tr2, tr3 // all share vertex v1
         * v1.normal = normalize( tr1.normal + tr2.normal + tr3.normal )
         */
        // done, now add triangles to mesh
        this.geometry.getFaces().addAll(faces);
        this.geometry.textureCoordinates.add(new Point(0, 0));
        this.geometry.textureCoordinates.add(new Point(0.5, 1));
        this.geometry.textureCoordinates.add(new Point(1, 0));

        for (Face face : this.geometry.getFaces())
        {
            Pos v1 = geometry.getVertices().get(face.vertexIndices[0]);
            Pos v2 = geometry.getVertices().get(face.vertexIndices[1]);
            Pos v3 = geometry.getVertices().get(face.vertexIndices[2]);

            //Generate normals
            //Special thanks to this site https://www.opengl.org/wiki/Calculating_a_Surface_Normal
            Pos u = v2.sub(v1);
            Pos v = v3.sub(v1);
            double x = (u.y() * v.z()) - (u.z() * v.y());
            double y = (u.z() * v.x()) - (u.x() * v.z());
            double z = (u.x() * v.y()) - (u.y() * v.x());
            geometry.normals.add(new Pos(x, y, z).normalize());

            face.normalIndices[0] = geometry.normals.size() - 1;
            face.normalIndices[1] = geometry.normals.size() - 1;
            face.normalIndices[2] = geometry.normals.size() - 1;

            face.textureCoordinateIndices[0] = 0;
            face.textureCoordinateIndices[1] = 1;
            face.textureCoordinateIndices[2] = 2;
        }

        return this.geometry;
    }
}
