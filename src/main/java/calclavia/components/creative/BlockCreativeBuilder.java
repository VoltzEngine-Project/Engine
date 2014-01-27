package calclavia.components.creative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import universalelectricity.api.vector.Vector3;
import calclavia.components.BlockCC;
import calclavia.components.CalclaviaLoader;
import calclavia.lib.Calclavia;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.schematic.Schematic;

import com.builtbroken.common.Pair;
import com.google.common.io.ByteArrayDataInput;

/**
 * Automatically set up structures to allow easy debugging in creative mode.
 */
public class BlockCreativeBuilder extends BlockCC implements IPacketReceiver
{
	public static final List<Schematic> REGISTRY = new ArrayList<Schematic>();

	public static int register(Schematic schematic)
	{
		REGISTRY.add(schematic);
		return REGISTRY.size() - 1;
	}

	public BlockCreativeBuilder()
	{
		super("creativeBuilder", CalclaviaLoader.idManager.getNextItemID());
		setCreativeTab(CreativeTabs.tabTools);
	}

	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (REGISTRY.size() > 0)
		{
			par5EntityPlayer.openGui(CalclaviaLoader.INSTANCE, -1, par1World, x, y, z);
			return true;
		}

		return false;
	}

	@Override
	public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
	{
		World world = player.worldObj;
		
		if (!world.isRemote)
		{
			// Only allow operators.
			if (MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.username))
			{
				try
				{
					int schematicID = data.readInt();
					int size = data.readInt();
					Vector3 position = new Vector3((Integer) extra[0], (Integer) extra[1], (Integer) extra[2]);

					if (size > 0)
					{
						HashMap<Vector3, Pair<Integer, Integer>> map = REGISTRY.get(schematicID).getStructure(size);

						for (Entry<Vector3, Pair<Integer, Integer>> entry : map.entrySet())
						{
							Vector3 placePos = entry.getKey().clone();
							placePos.translate(position);
							placePos.setBlock(world, entry.getValue().left(), entry.getValue().right());
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
