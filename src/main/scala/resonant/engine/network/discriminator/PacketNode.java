package resonant.engine.network.discriminator;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.tileentity.TileEntity;
import resonant.api.grid.INode;
import resonant.api.grid.INodeProvider;
import resonant.lib.transform.vector.IVector3;

/**
 * Version of PacketTile designed to be used by nodes. Doesn't function any different than the tile packet. However, serves as a way to sort normal tile packets from node generated ones.
 *
 * @author Darkguardsman
 */
public class PacketNode extends PacketTile
{
	public String nodeClassName = "INode";

	public PacketNode()
	{
		super();
	}

	public PacketNode(int x, int y, int z, Object... args)
	{
		super(x, y, z, args);
	}

	public PacketNode(TileEntity tile, Object... args)
	{
		super(tile, args);
	}

	public PacketNode(INode node, Object... args)
	{
		INodeProvider provider = node.getParent();
		nodeClassName = node.getClass().getSimpleName();
		if (node instanceof IVector3)
		{
			x = (int) ((IVector3) node).x();
			y = (int) ((IVector3) node).y();
			z = (int) ((IVector3) node).z();
		}
		else if (provider instanceof TileEntity)
		{
			x = ((TileEntity) provider).xCoord;
			y = ((TileEntity) provider).yCoord;
			z = ((TileEntity) provider).zCoord;
		}
		else if (provider instanceof IVector3)
		{
			x = (int) ((IVector3) provider).x();
			y = (int) ((IVector3) provider).y();
			z = (int) ((IVector3) provider).z();
		}
		else
		{
			throw new IllegalArgumentException("PacketNode needs location date from node: " + node);
		}

		write(args);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		ByteBufUtils.writeUTF8String(buffer, nodeClassName);
		buffer.writeBytes(data());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		nodeClassName = ByteBufUtils.readUTF8String(buffer);
		data_$eq(buffer.slice());
	}
}
