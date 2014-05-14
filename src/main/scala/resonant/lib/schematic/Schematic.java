package resonant.lib.schematic;

import java.util.HashMap;

import net.minecraftforge.common.ForgeDirection;
import resonant.lib.type.Pair;
import universalelectricity.api.vector.Vector3;

/** Stores building structure data.
 * 
 * TODO: MFFS integration for saving and loading custom modes.
 * 
 * @author Calclavia */
public abstract class Schematic
{
    /** The name of the schematic that is unlocalized.
     * 
     * @return "schematic.NAME-OF-SCHEMATIC.name" */
    public abstract String getName();

    /** Gets the structure of the schematic.
     * 
     * @param size - The size multiplier.
     * @return A Hashmap of positions and block IDs with metadata. */
    public abstract HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size);
}
