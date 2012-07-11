package universalelectricity.extend;

import net.minecraft.server.BlockContainer;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import universalelectricity.UniversalElectricity;
import universalelectricity.Vector3;


/**
 * The Class BlockConductor.
 */
public abstract class BlockConductor extends BlockContainer
{
    
    /**
     * Instantiates a new block conductor.
     *
     * @param i the i
     * @param material the material
     */
    public BlockConductor(int i, Material material)
    {
        super(i, material);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     */
    public void onPlace(World world, int i, int j, int k)
    {
        super.onPlace(world, i, j, k);
        updateConductorTileEntity(world, i, j, k);
        world.applyPhysics(i, j, k, id);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     * @param l the l
     */
    public void doPhysics(World world, int i, int j, int k, int l)
    {
        updateConductorTileEntity(world, i, j, k);
    }

    /**
     * Update conductor tile entity.
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     */
    public static void updateConductorTileEntity(World world, int i, int j, int k)
    {
        for (byte byte0 = 0; byte0 < 6; byte0++)
        {
            TileEntity tileentity = world.getTileEntity(i, j, k);

            if (tileentity != null && (tileentity instanceof TileEntityConductor))
            {
                TileEntityConductor tileentityconductor = (TileEntityConductor)tileentity;
                tileentityconductor.updateConnection(UniversalElectricity.getUEUnitFromSide(world, new Vector3(i, j, k), byte0), byte0);
            }
        }
    }

    /**
     * Returns the TileEntity used by this block.
     *
     * @return the tile entity
     */
    public TileEntity a_()
    {
        return null;
    }
}
