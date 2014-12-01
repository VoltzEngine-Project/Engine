package resonant.lib.schematic;

import resonant.lib.transform.vector.Vector3;
import resonant.lib.transform.vector.VectorWorld;

import java.util.HashMap;
import java.util.Map;

/**
 * Instance of a schematic used to build a schematic a few blocks at a time
 * <p/>
 * Created on 10/20/2014.
 *
 * @author Darkguardsman
 */
public class BuildMap
{
	private Vector3[] coords;
	private Placement[] blocks;
	private int lastBlockPos = 0;

	public BuildMap(int size)
	{
		coords = new Vector3[size];
		blocks = new Placement[size];
	}

	public BuildMap(HashMap<Vector3, Placement> map)
	{
		this(map.size());
		int p = 0;
		for (Map.Entry<Vector3, Placement> entry : map.entrySet())
		{
			coords[p] = entry.getKey();
			blocks[p] = entry.getValue();
			p++;
		}
	}

	public void build(VectorWorld spot)
	{
		build(spot, coords.length - lastBlockPos);
	}

	public void build(VectorWorld spot, int numberOfBlocks)
	{
		for (int i = 0; i < numberOfBlocks; i++)
		{
			blocks[lastBlockPos + i].place((VectorWorld) coords[i].add(spot));
		}
		lastBlockPos += numberOfBlocks;
	}

	public void add(int p, Vector3 key, Placement placement)
	{
		coords[p] = key;
		blocks[p] = placement;
	}
}
