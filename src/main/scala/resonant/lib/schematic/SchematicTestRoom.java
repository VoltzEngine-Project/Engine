package resonant.lib.schematic;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;
import resonant.lib.type.Pair;
import universalelectricity.api.vector.Vector3;

/** Basic room to do debug in without stuff getting in or out
 * 
 * @author Darkguardsman */
public class SchematicTestRoom extends Schematic
{

    @Override
    public String getName()
    {
        return "schematic.testroom.name";
    }

    @Override
    public HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Vector3, Pair<Integer, Integer>> returnMap = new HashMap<Vector3, Pair<Integer, Integer>>();

        //Generates a box 5 blocks tall
        for (int x = -size; x < size; x++)
        {
            for (int z = -size; z < size; z++)
            {
                for (int y = -1; y <= 5; y++)
                {
                    if (x == -size || x == size - 1 || z == -size || z == size - 1)
                    {
                        returnMap.put(new Vector3(x, y, z), new Pair(Block.bedrock.blockID, 0));
                    }
                }
            }
        }

        //Generates a bedrock floor
        for (int x = -size; x < size; x++)
        {
            for (int z = -size; z < size; z++)
            {
                if (x == -size || x == size - 1 || z == -size || z == size - 1)
                {
                    returnMap.put(new Vector3(x, -1, z), new Pair(Block.bedrock.blockID, 0));
                }
            }
        }

        size -= 1;
        //Generates a bedrock lip around the top of the box
        for (int x = -size; x < size; x++)
        {
            for (int z = -size; z < size; z++)
            {
                if (x == -size || x == size - 1 || z == -size || z == size - 1)
                {
                    returnMap.put(new Vector3(x, 6, z), new Pair(Block.bedrock.blockID, 0));
                }
            }
        }
        //Generates a glass roof
        for (int x = -size; x < size; x++)
        {
            for (int z = -size; z < size; z++)
            {
                if (!(x == -size || x == size - 1 || z == -size || z == size - 1))
                {
                    returnMap.put(new Vector3(x, 6, z), new Pair(Block.glass.blockID, 0));
                }
            }
        }
        return returnMap;
    }

}
