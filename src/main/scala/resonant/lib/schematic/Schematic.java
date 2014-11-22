package resonant.lib.schematic;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.lib.type.Pair;
import resonant.lib.transform.vector.Vector3;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to store and generate schematic data for world generating buildings
 *
 * @author Calclavia, Darkguardsman
 */
public abstract class Schematic
{
    /** Registry and unlocalized name "schematic.NAME-OF-SCHEMATIC.name" */
    private String schematicName;

    public Schematic()
    {

    }

    public Schematic(String name)
    {
        this.schematicName = name;
    }
	/**
	 * The name of the schematic that is unlocalized.
	 *
	 * @return "schematic.NAME-OF-SCHEMATIC.name"
	 */
	public String getName()
    {
        return schematicName;
    }

    /** Gets the actual build map to be placed into th world around the location
     *
     * @param corner - lower bottom left front most corner
     * @param facingDirection - facing direction, option & not support by all schematics
     * @param size - - requested size, optional & not support by all schematics
     * @return BuildMap instance to be build into the world
     */
	public BuildMap getBuildMap(Vector3 corner, ForgeDirection facingDirection, int size)
	{
		HashMap<Vector3, Pair<Block, Integer>> map = getStructure(facingDirection, size);
		BuildMap buildMap = new BuildMap(map.size());
		int p = 0;
		for (Map.Entry<Vector3, Pair<Block, Integer>> entry : map.entrySet())
		{
			buildMap.add(p++, entry.getKey().add(corner), new Placement(entry.getValue().left(), entry.getValue().right()));
		}
		return buildMap;
	}

    /** Generates generic placement data to be iterated threw to create the actual object in the world.
     *
     * @param dir - facing direction, optional
     * @param size - requested size, optional
     * @return map of locations to placement data
     */
	public abstract HashMap<Vector3, Pair<Block, Integer>> getStructure(ForgeDirection dir, int size);

	///////////////////////////////////////////////////////////////////
	///             Helper methods for generating schematics        ///
	///                     Mainly for internal use                 ///
	///////////////////////////////////////////////////////////////////

	/**
	 * Creates a map of vectors in the shape of a line
	 *
	 * @param start  - starting point of the line
	 * @param dir    - direction to create it in
	 * @param block  - block to make the line out of
	 * @param meta   - - meta value of the block for placement
	 * @param length - length of the line
	 * @return HashMap of vectors to placement data
	 */
	public HashMap<Vector3, Pair<Block, Integer>> getLine(final Vector3 start, ForgeDirection dir, Block block, int meta, int length)
	{
		HashMap<Vector3, Pair<Block, Integer>> returnMap = new HashMap();
		for (int i = 0; i < length; i++)
		{
			returnMap.put(new Vector3(dir).multiply(i).add(start), new Pair<Block, Integer>(block, meta));
		}
		return returnMap;
	}

	/**
	 * Creates a map of vectors in the shape of a square
	 *
	 * @param center - center to create the box around, controls offset for later if needed
	 * @param block  - block to make the box out of
	 * @param meta   - meta value of the block for placement
	 * @param size   - size from the center to the edge, half of the side
	 * @return hash map of vectors to placement data
	 */
	public HashMap<Vector3, Pair<Block, Integer>> getBox(final Vector3 center, Block block, int meta, int size)
	{
		return getBox(center, block, meta, size, size);
	}

	/**
	 * Creates a map of vectors in the shape of a square
	 *
	 * @param center - center to create the box around, controls offset for later if needed
	 * @param block  - block to make the box out of
	 * @param meta   - meta value of the block for placement
	 * @param sizeX  - size from the center to the edge, half of the side
	 * @param sizeZ  - size from the center to the edge, half of the side
	 * @return hash map of vectors to placement data
	 */
	public HashMap<Vector3, Pair<Block, Integer>> getBox(final Vector3 center, Block block, int meta, int sizeX, int sizeZ)
	{
		HashMap<Vector3, Pair<Block, Integer>> returnMap = new HashMap();
		//zero zero corner of the square
		Vector3 start = new Vector3(-sizeX, 0, -sizeZ).add(center);

		if (sizeX != sizeZ)
		{
			// X sides
			for (int x = 0; x <= sizeX * 2; x++)
			{
				returnMap.put(new Vector3(x, 0, 0).add(start), new Pair<Block, Integer>(block, meta));
				returnMap.put(new Vector3(x, 0, sizeZ * 2).add(start), new Pair<Block, Integer>(block, meta));
			}
			// Z sides
			for (int z = 0; z <= sizeZ * 2; z++)
			{
				returnMap.put(new Vector3(0, 0, z).add(start), new Pair<Block, Integer>(block, meta));
				returnMap.put(new Vector3(sizeX * 2, 0, z).add(start), new Pair<Block, Integer>(block, meta));
			}
		}
		else
		{
			// All sides, used verses the other way as it cuts the time in half
			for (int s = 0; s <= sizeX * 2; s++)
			{
				returnMap.put(new Vector3(s, 0, 0).add(start), new Pair<Block, Integer>(block, meta));
				returnMap.put(new Vector3(s, 0, sizeZ * 2).add(start), new Pair<Block, Integer>(block, meta));
				returnMap.put(new Vector3(0, 0, s).add(start), new Pair<Block, Integer>(block, meta));
				returnMap.put(new Vector3(sizeZ * 2, 0, s).add(start), new Pair<Block, Integer>(block, meta));
			}
		}
		return returnMap;
	}
}
