package resonant.lib.utility;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.LinkedHashMap;

/**
 * Fake player used by machines, also includes utilities for using the fake player better.
 *
 * @author DarkGuardsman
 */
public class MachinePlayer extends FakePlayer
{
	private static final LinkedHashMap<World, MachinePlayer> FAKE_PLAYERS = new LinkedHashMap<World, MachinePlayer>();

	public MachinePlayer(World world, String name, String sufix)
	{
		super(world, new GameProfile("0", name + sufix));
	}

	public MachinePlayer(World world, String sufix)
	{
		this(world, "[Calc-Core]FakePlayer", sufix);
	}

	public MachinePlayer(World world)
	{
		this(world, "(" + world.provider.dimensionId + ")");
	}

	/**
	 * Gets the fake player for the world
	 */
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
		return useItemAt(itemStack, location.world(), location.xi(), location.yi(), location.zi(), direction.ordinal(), 0, 0, 0);
	}

	public static boolean useItemAt(ItemStack itemStack, World world, int x, int y, int z, int side)
	{
		return useItemAt(itemStack, world, x, y, z, side, 0, 0, 0);
	}

	public static boolean useItemAt(ItemStack itemStack, World world, int x, int y, int z, int side, int hitX, int hitY, int hitZ)
	{
		if (itemStack != null && itemStack.getItem() != null)
		{
			return itemStack.getItem().onItemUse(itemStack, get(world), world, x, y, z, side, hitX, hitY, hitZ);
		}
		else
		{
			return false;
		}
	}

}
