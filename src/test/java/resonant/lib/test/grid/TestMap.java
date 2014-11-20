package resonant.lib.test.grid;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import resonant.lib.type.Pair;

import java.util.HashMap;

/**
 * Created by robert on 11/20/2014.
 */
public class TestMap
{
    public static String[][] blankTestMap =
            {
                    new String[] {/*0*/ "0123456789ABCDEF",
                    /*1*/ "0123456789ABCDEF",
                    /*2*/ "0123456789ABCDEF",
                    /*3*/ "0123456789ABCDEF",
                    /*4*/ "0123456789ABCDEF",
                    /*5*/ "0123456789ABCDEF",
                    /*6*/ "0123456789ABCDEF",
                    /*7*/ "0123456789ABCDEF",
                    /*8*/ "0123456789ABCDEF",
                    /*9*/ "0123456789ABCDEF",
                    /*10*/"0123456789ABCDEF",
                    /*11*/"0123456789ABCDEF",
                    /*12*/"0123456789ABCDEF",
                    /*13*/"0123456789ABCDEF",
                    /*14*/"0123456789ABCDEF",
                    /*15*/"0123456789ABCDEF"}
            };

    public static String[][] simpleTestMap =
            {
                    new String[] {/*0*/ "0123456789ABCDEF",
                    /*1*/ "0123456789ABCDEF",
                    /*2*/ "0123456789ABCDEF",
                    /*3*/ "0123456---ABCDEF",
                    /*4*/ "01234567-9ABCDEF",
                    /*5*/ "01234-67-9A-CDEF",
                    /*6*/ "01234-------CDEF",
                    /*7*/ "01234-67-9A-CDEF",
                    /*8*/ "01234567-9ABCDEF",
                    /*9*/ "0123456---ABCDEF",
                    /*10*/"0123456789ABCDEF",
                    /*11*/"0123456789ABCDEF",
                    /*12*/"0123456789ABCDEF",
                    /*13*/"0123456789ABCDEF",
                    /*14*/"0123456789ABCDEF",
                    /*15*/"0123456789ABCDEF"}
            };


    private HashMap<Character, Pair<Block, Integer>> charMap = new HashMap();
    private String[][] mapToBuild;

    /**
     * Creates a test map object used to parse and build a map from a String matrix
     * @param map - 2 dim matrix of Y layers and Z rows of blocks to place.
     *            "0123456789ABCDEF" are used by default as blank spaces. You
     *            can override this behavior at your own risk
     */
    public TestMap(String[][] map)
    {
        mapToBuild = map.clone();
    }

    /** Adds a mapping for a block
     * @param c - character
     * @param block - block, can't be null
     * @return this
     */
    public TestMap addBlock(Character c, Block block)
    {
       return addBlock(c, block, 0);
    }

    /** Adds a mapping for a block
     * @param c - character
     * @param block - block, can't be null
     * @param meta - block placement meta
     * @return this
     */
    public TestMap addBlock(Character c, Block block, int meta)
    {
        charMap.put(c, new Pair<Block, Integer>(block, 0 & 15));
        return this;
    }

    /** Builds the map into the world using xyz as the lower corner */
    public void build(World world, int xi, int yi, int zi)
    {
        for(int y = 0; y < mapToBuild.length; y++ )
        {
            for(int z = 0; z < mapToBuild[y].length; z++)
            {
                char[] chars = mapToBuild[y][z].toCharArray();
                for(int x = 0; x < chars.length; x++)
                {
                    if (charMap.containsKey(chars[x]))
                    {
                        world.setBlock(xi + x, yi + y, zi + z, charMap.get(chars[x]).left(), charMap.get(chars[x]).right(), 0);
                    }
                }
            }
        }
    }
}
