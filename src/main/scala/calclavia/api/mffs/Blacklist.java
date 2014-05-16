package calclavia.api.mffs;

import java.util.HashSet;
import java.util.Set;

public class Blacklist
{
    /** Adds blocks to this black list if you do not wish them to be moved by the following:
     * 
     * The interger is the block ID. */
    public static final Set<Integer> stabilizationBlacklist = new HashSet<Integer>();
    public static final Set<Integer> disintegrationBlacklist = new HashSet<Integer>();
    public static final Set<Integer> forceManipulationBlacklist = new HashSet<Integer>();

}
