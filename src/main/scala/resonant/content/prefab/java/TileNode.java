package resonant.content.prefab.java;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.lib.network.discriminator.PacketNode;
import resonant.lib.network.discriminator.PacketType;
import resonant.lib.network.handle.IPacketIDReceiver;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.api.core.grid.ISave;
import universalelectricity.api.core.grid.IUpdate;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Prefab designed to automate all node interaction of the time.
 * Does use some reflection to generate a list of all fields holding nodes
 * <p/>
 * TODO if timing for reflection proves to take to long replace with getAllNodes() method
 *
 * @author Darkguardsman
 */
public class TileNode extends TileAdvanced implements INodeProvider, IPacketIDReceiver
{
	private static final Map<Class<? extends TileNode>, List<Field>> nodeFields = new LinkedHashMap<Class<? extends TileNode>, List<Field>>();
	INode baseNode = null;

	public TileNode(Material material)
	{
		super(material);
		generateNodeList();
	}

	@Override
	public void onNeighborChanged(Block block)
	{
		super.onNeighborChanged(block);
		reconstructNode();
	}

	@Override
	public void onAdded()
	{
		super.onAdded();
		reconstructNode();
	}

	private void reconstructNode()
	{
		for (INode node : getNodes())
		{
			if (node != null)
			{
				node.reconstruct();
			}
		}
	}

	@Override
	public void start()
	{
		super.start();
		reconstructNode();
	}

	@Override
	public void update()
	{
		super.update();
		for (INode node : getNodes())
		{
			if (node instanceof IUpdate)
			{
				if (((IUpdate) node).canUpdate())
				{
					((IUpdate) node).update(20);
				}
			}
		}
	}

	@Override
	public void invalidate()
	{
		for (INode node : getNodes())
		{
			if (node != null)
			{
				node.deconstruct();
			}
		}
		super.invalidate();
	}

	@Override
	public INode getNode(Class<? extends INode> nodeType, ForgeDirection from)
	{
		return baseNode;
	}

	//TODO improve and turn into a helper class
	public void generateNodeList()
	{
		if (!nodeFields.containsKey(getClass()))
		{
			Class clazz = getClass();
			List<Field> fields = new LinkedList<Field>();
			List<Field> allFields = new LinkedList<Field>();

			//Get all fields both inherited and declared
			allFields.addAll(Arrays.asList(clazz.getFields()));
			allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));

			for (Field field : allFields)
			{
				if (!fields.contains(field) && INode.class.isAssignableFrom(field.getType()))
				{
					fields.add(field);
				}
			}
			nodeFields.put(clazz, fields);
		}
	}

	protected List<INode> getNodes()
	{
		List<INode> nodes = new LinkedList<INode>();
		getNodes(nodes);
		return nodes;
	}

	/**
	 * Grabs the list of nods from the class using reflection, to improve performance override this and manually add nodes to the list
	 */
	public void getNodes(List<INode> nodes)
	{
		if (nodeFields.containsKey(getClass()))
		{
			List<Field> fields = nodeFields.get(getClass());
			for (Field field : fields)
			{
				INode node = null;
				try
				{
					field.setAccessible(true);
					Object object = field.get(this);
					if (object instanceof INode)
					{
						nodes.add(node);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		for (INode node : getNodes())
		{
			if (node instanceof ISave)
			{
				((ISave) node).load(nbt);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		for (INode node : getNodes())
		{
			if (node instanceof ISave)
			{
				((ISave) node).save(nbt);
			}
		}
	}

	@Override
	public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
	{
		if (type instanceof PacketNode && nodeFields.containsKey(getClass()))
		{
			for (INode node : getNodes())
			{
				if ((((PacketNode) type).nodeClassName.equalsIgnoreCase("INode") || node.getClass().getSimpleName().equalsIgnoreCase(((PacketNode) type).nodeClassName)))
				{
					if (node instanceof IPacketIDReceiver)
					{
						if (((IPacketIDReceiver) node).read(buf, id, player, type))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
