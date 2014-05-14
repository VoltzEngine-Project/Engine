package resonant.lib.prefab.vector;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import universalelectricity.api.vector.EulerAngle;
import universalelectricity.api.vector.Vector3;

/** A cubical region class to specify a region.
 * 
 * @author Calclavia */
public class Cuboid
{
    public Vector3 min;
    public Vector3 max;

    public Cuboid()
    {
        this(new Vector3(), new Vector3());
    }

    public Cuboid(Cuboid cuboid)
    {
        this(cuboid.min.clone(), cuboid.max.clone());
    }

    public Cuboid(Vector3 min, Vector3 max)
    {
        this.min = min.clone();
        this.max = max.clone();
    }

    public Cuboid(double minx, double miny, double minz, double maxx, double maxy, double maxz)
    {
        min = new Vector3(minx, miny, minz);
        max = new Vector3(maxx, maxy, maxz);
    }

    public Cuboid(AxisAlignedBB aabb)
    {
        this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public Cuboid(Block block)
    {
        this(block.getBlockBoundsMinX(), block.getBlockBoundsMinY(), block.getBlockBoundsMinZ(), block.getBlockBoundsMaxX(), block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ());
    }

    public AxisAlignedBB toAABB()
    {
        return AxisAlignedBB.getAABBPool().getAABB(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public static Cuboid full()
    {
        return new Cuboid(0, 0, 0, 1, 1, 1);
    }

    public Rectangle toRectangle()
    {
        return new Rectangle(min.toVector2(), max.toVector2());
    }

    public Cuboid add(Vector3 vec)
    {
        min.add(vec);
        max.add(vec);
        return this;
    }

    public Cuboid subtract(Vector3 vec)
    {
        min.subtract(vec);
        max.subtract(vec);
        return this;
    }

    public Cuboid rotate(EulerAngle angle)
    {
        min.rotate(angle);
        max.rotate(angle);
        return this;
    }

    public Cuboid setBounds(Block block)
    {
        block.setBlockBounds((float) min.x, (float) min.y, (float) min.z, (float) max.x, (float) max.y, (float) max.z);
        return this;
    }

    /** Checks if a point is located inside a region */
    @Deprecated
    public boolean isIn(Vector3 point)
    {
        return intersects(point);
    }

    /** Returns whether the given region intersects with this one. */
    @Deprecated
    public boolean isIn(Cuboid other)
    {
        return other.max.x > this.min.x && other.min.x < this.max.x ? (other.max.y > this.min.y && other.min.y < this.max.y ? other.max.z > this.min.z && other.min.z < this.max.z : false) : false;
    }

    public boolean intersects(Vector3 point)
    {
        return (point.x > this.min.x && point.x < this.max.x) && (point.y > this.min.y && point.y < this.max.y) && (point.z > this.min.z && point.z < this.max.z);
    }

    public boolean intersects(Cuboid other)
    {
        return max.x - 1E-5 > other.min.x && other.max.x - 1E-5 > min.x && max.y - 1E-5 > other.min.y && other.max.y - 1E-5 > min.y && max.z - 1E-5 > other.min.z && other.max.z - 1E-5 > min.z;
    }

    public Cuboid offset(Cuboid o)
    {
        min.translate(o.min);
        max.translate(o.max);
        return this;
    }

    public Cuboid translate(Vector3 vec)
    {
        min.translate(vec);
        max.translate(vec);
        return this;
    }

    public Vector3 center()
    {
        return min.clone().add(max).scale(0.5);
    }

    public Cuboid expand(Vector3 difference)
    {
        this.min.subtract(difference);
        this.max.add(difference);
        return this;
    }

    public Cuboid expand(double difference)
    {
        this.min.subtract(difference);
        this.max.add(difference);
        return this;
    }

    /** @return List of vectors within this region. */
    public List<Vector3> getVectors()
    {
        List<Vector3> vectors = new ArrayList<Vector3>();

        for (int x = this.min.intX(); x < this.max.intX(); x++)
        {
            for (int y = this.min.intY(); x < this.max.intY(); y++)
            {
                for (int z = this.min.intZ(); x < this.max.intZ(); z++)
                {
                    vectors.add(new Vector3(x, y, z));
                }
            }
        }

        return vectors;
    }

    public List<Vector3> getVectors(Vector3 center, int radius)
    {
        List<Vector3> vectors = new ArrayList<Vector3>();

        for (int x = this.min.intX(); x < this.max.intX(); x++)
        {
            for (int y = this.min.intY(); x < this.max.intY(); y++)
            {
                for (int z = this.min.intZ(); x < this.max.intZ(); z++)
                {
                    Vector3 vector3 = new Vector3(x, y, z);

                    if (center.distance(vector3) <= radius)
                    {
                        vectors.add(vector3);
                    }
                }
            }
        }

        return vectors;
    }

    /** Returns all entities in this region. */
    public List<Entity> getEntities(World world, Class<? extends Entity> entityClass)
    {
        return world.getEntitiesWithinAABB(entityClass, this.toAABB());
    }

    public List<Entity> getEntitiesExlude(World world, Entity entity)
    {
        return world.getEntitiesWithinAABBExcludingEntity(entity, this.toAABB());
    }

    public List<Entity> getEntities(World world)
    {
        return this.getEntities(world, Entity.class);
    }

    @Override
    public String toString()
    {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Cuboid: [" + new BigDecimal(min.x, cont) + ", " + new BigDecimal(min.y, cont) + ", " + new BigDecimal(min.z, cont) + "] -> [" + new BigDecimal(max.x, cont) + ", " + new BigDecimal(max.y, cont) + ", " + new BigDecimal(max.z, cont) + "]";
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Cuboid)
            return min.equals(((Cuboid) o).min) && max.equals(((Cuboid) o).max);
        return false;
    }

    @Override
    public Cuboid clone()
    {
        return new Cuboid(this);
    }

}
