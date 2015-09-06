package com.builtbroken.mc.lib.transform.region;

import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 1/12/2015.
 */
public class Sphere extends Shape3D
{
    double r;

    public Sphere(Pos pos, int r)
    {
        super(pos);
        this.r = r;
    }

    @Override
    public double getSizeX() { return r * 2; }

    @Override
    public double getSizeY() { return r * 2; }

    @Override
    public double getSizeZ() { return r * 2; }

    @Override
    public double getArea() { return 4 * Math.PI * (r * r); }

    @Override
    public double getVolume() { return (4 * Math.PI * (r * r * r)) / 3; }

    public boolean isWithin(double x, double y, double z)
    {
        return new Pos(x, y, z).subtract(x, y, z).magnitude() <= this.r;
    }

    public <E extends Entity> List<E> getEntities(World world, Class<E> clazz)
    {
        List<E> list = new ArrayList();
        int minX = MathHelper.floor_double((r - World.MAX_ENTITY_RADIUS) / 16.0D);
        int maxX = MathHelper.floor_double((r + World.MAX_ENTITY_RADIUS) / 16.0D);
        int minZ = MathHelper.floor_double((r - World.MAX_ENTITY_RADIUS) / 16.0D);
        int maxZ = MathHelper.floor_double((r + World.MAX_ENTITY_RADIUS) / 16.0D);

        //world.getEntitiesWithinAABB()
        for (int i1 = minX; i1 <= maxX; i1++)
        {
            for (int j1 = minZ; j1 <= maxZ; j1++)
            {
                if (world.getChunkProvider().chunkExists(i1, j1))
                {
                    Chunk chunk = world.getChunkFromChunkCoords(i1, j1);

                    int i = MathHelper.floor_double((r - World.MAX_ENTITY_RADIUS) / 16.0D);
                    int j = MathHelper.floor_double((r + World.MAX_ENTITY_RADIUS) / 16.0D);
                    i = MathHelper.clamp_int(i, 0, chunk.entityLists.length - 1);
                    j = MathHelper.clamp_int(j, 0, chunk.entityLists.length - 1);

                    for (int k = i; k <= j; k++)
                    {
                        List list1 = chunk.entityLists[k];

                        for (int l = 0; l < list1.size(); l++)
                        {
                            Entity entity = (Entity) list1.get(l);

                            if (ClassIsAssignableFrom.isAssignableFrom(clazz, entity.getClass()) && distance(new Pos(entity)) <= r)
                            {
                                list.add((E)entity);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
