package resonant.test.grid;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import resonant.content.wrapper.BlockDummy;
import resonant.junit.TestRegistry;
import resonant.lib.prefab.TileConductor;
import resonant.lib.schematic.StringSchematic;

/**
 * Created by robert on 11/21/2014.
 */
public class WireMap extends StringSchematic
{
    private static Block wire;
    int numberOfWires;
    int numberOfBranches;
    int numberOfJunctions;

    public WireMap(String[][] map, int numberOfWires, int numberOfBranches, int numberOfJunctions)
    {
        super(map);
        this.numberOfWires = numberOfWires;
        this.numberOfBranches = numberOfBranches;
        this.numberOfJunctions = numberOfJunctions;
        addBlock('-', wire());
    }

    public static Block wire()
    {
        if (wire == null)
        {
            if (Block.getBlockFromName("wire") == null)
            {
                TestRegistry.registerBlock(new BlockDummy("JUnit", null, new TileConductor()), "wire");
            }
            wire = Block.getBlockFromName("wire");
        }
        return wire;
    }


    public static enum WireTests
    {
        SIMPLE(new String[][]{
                new String[]{
                    /*0*/ "0123456789ABCDEF",
                    /*1*/ "0123456789ABCDEF",
                    /*2*/ "0123456789ABCDEF",
                    /*3*/ "0123456789ABCDEF",
                    /*4*/ "0123456789ABCDEF",
                    /*5*/ "0123456789ABCDEF",
                    /*6*/ "01234567-9ABCDEF",
                    /*7*/ "01234567-9ABCDEF",
                    /*8*/ "012345-----BCDEF",
                    /*9*/ "01234567-9ABCDEF",
                    /*10*/"01234567-9ABCDEF",
                    /*11*/"0123456789ABCDEF",
                    /*12*/"0123456789ABCDEF",
                    /*13*/"0123456789ABCDEF",
                    /*14*/"0123456789ABCDEF",
                    /*15*/"0123456789ABCDEF"}},
                9, 4, 1
        ),
        JUNCTION_FIVE(new String[][]{
                new String[]{
                    /*0*/ "0123456789ABCDEF",
                    /*1*/ "0123456789ABCDEF",
                    /*2*/ "0123456789ABCDEF",
                    /*3*/ "012345-----BCDEF",
                    /*4*/ "01234-67-9A-CDEF",
                    /*5*/ "01234-67-9A-CDEF",
                    /*6*/ "01234-------CDEF",
                    /*7*/ "01234-67-9A-CDEF",
                    /*8*/ "01234-67-9A-CDEF",
                    /*9*/ "012345-----BCDEF",
                    /*10*/"0123456789ABCDEF",
                    /*11*/"0123456789ABCDEF",
                    /*12*/"0123456789ABCDEF",
                    /*13*/"0123456789ABCDEF",
                    /*14*/"0123456789ABCDEF",
                    /*15*/"0123456789ABCDEF"}},
                27, 12, 5
        );

        String[][] map;
        int numberOfWires;
        int numberOfBranches;
        int numberOfJunctions;

        private WireTests(String[][] map, int numberOfWires, int numberOfBranches, int numberOfJunctions)
        {
            this.map = map;
            this.numberOfWires = numberOfWires;
            this.numberOfBranches = numberOfBranches;
            this.numberOfJunctions = numberOfJunctions;
        }

        public WireMap create()
        {
            return new WireMap(map, numberOfWires, numberOfBranches, numberOfJunctions);
        }

        public void build(World world, int x, int y, int z)
        {
            create().build(world, x, y, z);
        }

    }
}
