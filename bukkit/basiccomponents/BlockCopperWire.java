package basiccomponents;

import java.util.Random;

import net.minecraft.server.Block;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import universalelectricity.Vector3;
import universalelectricity.extend.BlockConductor;
import forge.ITextureProvider;


/**
 * The Class BlockCopperWire.
 */
public class BlockCopperWire extends BlockConductor implements ITextureProvider
{
    
    /**
     * Instantiates a new block copper wire.
     *
     * @param i the i
     */
    public BlockCopperWire(int i)
    {
        super(i, Material.CLOTH);
        a("Copper Wire");
        a(k);
        b(0.2F);
        a(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
        j();
        textureId = 7;
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
        checkHostileBlock(world, i, j, k);
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
        super.doPhysics(world, i, j, k, l);
        checkHostileBlock(world, i, j, k);
    }

    /**
     * Check hostile block.
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     */
    public void checkHostileBlock(World world, int i, int j, int k)
    {
        Vector3 vector3 = new Vector3(i, j, k);

        for (byte byte0 = 0; byte0 < 6; byte0++)
        {
            switch (byte0)
            {
                case 0:
                    j--;
                    break;

                case 1:
                    j++;
                    break;

                case 2:
                    k++;
                    break;

                case 3:
                    k--;
                    break;

                case 4:
                    i++;
                    break;

                case 5:
                    i--;
                    break;
            }

            int l = world.getTypeId(i, j, k);

            if (l == Block.FIRE.id || l == Block.LAVA.id || l == Block.STATIONARY_LAVA.id || l == Block.WATER.id || l == Block.STATIONARY_WATER.id)
            {
                world.setTypeId(vector3.intX(), vector3.intY(), vector3.intZ(), Block.FIRE.id);
                world.a("largesmoke", vector3.intX(), vector3.intY(), vector3.intZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     *
     * @return true, if successful
     */
    public boolean a()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc).
     *
     * @return true, if successful
     */
    public boolean b()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block.
     *
     * @return the int
     */
    public int c()
    {
        return -1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     *
     * @param i the i
     * @param random the random
     * @param j the j
     * @return the drop type
     */
    public int getDropType(int i, Random random, int j)
    {
        return BasicComponents.ItemCopperWire.id;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     *
     * @param random the random
     * @return the int
     */
    public int a(Random random)
    {
        return 1;
    }

    /**
     * Returns the TileEntity used by this block.
     *
     * @return the tile entity
     */
    public TileEntity a_()
    {
        return new TileEntityCopperWire();
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Block#getTextureFile()
     */
    public String getTextureFile()
    {
        return "/basiccomponents/textures/blocks.png";
    }
}
