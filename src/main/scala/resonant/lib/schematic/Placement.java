package resonant.lib.schematic;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import universalelectricity.core.transform.vector.Vector3;
import universalelectricity.core.transform.vector.VectorWorld;

/**
 * Placement data for the schematics.
 * Defaults mainly to block and meta but allows for additional data
 * <p/>
 * Created n 10/20/2014.
 *
 * @author Darkguardsman
 */
public class Placement
{
	private Block block = null;
	private int meta = 0;

	public Placement(Block block)
	{
		this.block = block;
	}

	public Placement(Block block, int meta)
	{
		this(block);
		this.meta = meta;
	}

	public boolean place(VectorWorld spot)
	{
		return place(spot.world(), spot);
	}

	public boolean place(World world, Vector3 vec)
	{
		return place(world, vec.xi(), vec.yi(), vec.zi());
	}

	public boolean place(World world, double x, double y, double z)
	{
		return place(world, (int) x, (int) y, (int) z);
	}

	public boolean place(World world, int x, int y, int z)
	{
		return world.setBlock(x, y, z, block, meta, 2);
	}

}
