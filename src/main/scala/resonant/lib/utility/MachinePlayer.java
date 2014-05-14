package resonant.lib.utility;

import java.util.LinkedHashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayer;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.VectorWorld;

/** Fake player used by machines, also includes utilities for using the fake player better.
 * 
 * @author DarkGuardsman */
public class MachinePlayer extends FakePlayer
{
    private static final LinkedHashMap<World, MachinePlayer> FAKE_PLAYERS = new LinkedHashMap<World, MachinePlayer>();

    public MachinePlayer(World world, String name, String sufix)
    {
        super(world, name + sufix);
    }

    public MachinePlayer(World world, String sufix)
    {
        this(world, "[Calc-Core]FakePlayer", sufix);
    }

    public MachinePlayer(World world)
    {
        this(world, "(" + world.provider.dimensionId + ")");
    }

    /** Gets the fake player for the world */
    public static MachinePlayer get(World world)
    {
        if (!FAKE_PLAYERS.containsKey(world) || FAKE_PLAYERS.get(world) == null)
        {
            FAKE_PLAYERS.put(world, new MachinePlayer(world));
        }
        return FAKE_PLAYERS.get(world);
    }

    public static boolean useItemAt(ItemStack itemStack, VectorWorld location, ForgeDirection direction)
    {
        return useItemAt(itemStack, location.world, location.intX(), location.intY(), location.intZ(), direction.ordinal(), 0, 0, 0);
    }

    public static boolean useItemAt(ItemStack itemStack, World world, int x, int y, int z, int side)
    {
        return useItemAt(itemStack, world, x, y, z, side, 0, 0, 0);
    }

    public static boolean useItemAt(ItemStack itemStack, World world, int x, int y, int z, int side, int hitX, int hitY, int hitZ)
    {
        if (itemStack != null && itemStack.getItem() != null)
            return itemStack.getItem().onItemUse(itemStack, get(world), world, x, y, z, side, hitX, hitY, hitZ);
        else
            return false;
    }

}
