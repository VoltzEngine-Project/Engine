package resonant.lib.multiblock;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.core.ResonantEngine;
import resonant.lib.network.IPacketReceiver;
import resonant.lib.network.PacketTile;
import universalelectricity.core.transform.vector.Vector3;

/**
 * This is a multiblock to be used for blocks that are bigger than one block.
 *
 * @author Calclavia
 */
public class TileMultiBlockPart extends TileEntity implements IPacketReceiver
{
	// The the position of the main block. Relative to this block's position.
	private Vector3 mainBlockPosition;

	public Vector3 getMainBlock()
	{
		if (this.mainBlockPosition != null)
		{
			return new Vector3(this).add(this.mainBlockPosition);
		}

		return null;
	}

	public void setMainBlock(Vector3 mainBlock)
	{
		this.mainBlockPosition = mainBlock.clone().add(new Vector3(this).multiply(-1));

		if (!this.worldObj.isRemote)
		{
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		if (this.mainBlockPosition != null)
		{
			final PacketTile packetTile = new PacketTile(this, new Object[] { this.mainBlockPosition.xi(), this.mainBlockPosition.yi(), this.mainBlockPosition.zi() });
			return ResonantEngine.INSTANCE.packetHandler.toMCPacket(packetTile);
		}

		return null;
	}

	@Override
	public void onReceivePacket(ByteBuf data, EntityPlayer player, Object... obj)
	{
		try
		{
			this.mainBlockPosition = new Vector3(data.readInt(), data.readInt(), data.readInt());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onBlockRemoval(BlockMultiBlockPart block)
	{
		if (this.getMainBlock() != null)
		{
			TileEntity tileEntity = this.getMainBlock().getTileEntity(this.worldObj);

			if (tileEntity instanceof IMultiBlock)
			{
				block.destroyMultiBlockStructure((IMultiBlock) tileEntity);
			}
		}
	}

	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer entityPlayer)
	{
		if (this.getMainBlock() != null)
		{
			TileEntity tileEntity = this.getMainBlock().getTileEntity(this.worldObj);

			if (tileEntity instanceof IBlockActivate)
			{
				return ((IBlockActivate) tileEntity).onActivated(entityPlayer);
			}
		}

		return false;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		if (nbt.hasKey("mainBlockPosition"))
		{
			this.mainBlockPosition = new Vector3(nbt.getCompoundTag("mainBlockPosition"));
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		if (this.mainBlockPosition != null)
		{
			nbt.setTag("mainBlockPosition", this.mainBlockPosition.writeToNBT(getWorldObj(), new NBTTagCompound()));
		}
	}

	/**
	 * Determines if this TileEntity requires update calls.
	 *
	 * @return True if you want updateEntity() to be called, false if not
	 */
	@Override
	public boolean canUpdate()
	{
		return false;
	}
}