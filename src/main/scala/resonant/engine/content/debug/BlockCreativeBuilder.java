package resonant.engine.content.debug;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.engine.ResonantEngine;
import resonant.lib.network.IPacketReceiver;
import resonant.lib.schematic.Schematic;
import resonant.lib.type.Pair;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.core.transform.vector.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Automatically set up structures to allow easy debugging in creative mode.
 */
public class BlockCreativeBuilder extends BlockRotatable implements IPacketReceiver
{
	public static final List<Schematic> REGISTRY = new ArrayList<Schematic>();

	public BlockCreativeBuilder(int id)
	{
		super(id, UniversalElectricity.machine);
		setCreativeTab(CreativeTabs.tabTools);
		rotationMask = Byte.parseByte("111111", 2);
	}

	public static int register(Schematic schematic)
	{
		REGISTRY.add(schematic);
		return REGISTRY.size() - 1;
	}

	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (REGISTRY.size() > 0)
		{
			par5EntityPlayer.openGui(ResonantEngine.instance, -1, par1World, x, y, z);
			return true;
		}

		return false;
	}

	@Override
	public void onReceivePacket(ByteBuf data, EntityPlayer player, Object... extra)
	{
		World world = player.worldObj;

		if (!world.isRemote)
		{
			// Only allow operators.
			if (MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.getCommandSenderName()))
			{
				try
				{
					int schematicID = data.readInt();
					int size = data.readInt();
					Vector3 position = new Vector3((Integer) extra[0], (Integer) extra[1], (Integer) extra[2]);

					if (size > 0)
					{
						HashMap<Vector3, Pair<Block, Integer>> map = REGISTRY.get(schematicID).getStructure(ForgeDirection.getOrientation(position.getBlockMetadata(world)), size);

						for (Entry<Vector3, Pair<Block, Integer>> entry : map.entrySet())
						{
							Vector3 placePos = entry.getKey().clone();
							placePos.$plus(position);
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
