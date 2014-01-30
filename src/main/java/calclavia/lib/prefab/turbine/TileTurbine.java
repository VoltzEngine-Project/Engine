package calclavia.lib.prefab.turbine;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.multiblock.reference.IMultiBlockStructure;
import calclavia.lib.multiblock.reference.MultiBlockHandler;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.network.PacketHandler;
import calclavia.lib.prefab.tile.TileElectrical;
import calclavia.lib.utility.WorldUtility;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Turbine TileEntity
 * 
 * 1 cubic meter of steam = 338260 J of energy
 */
public abstract class TileTurbine extends TileElectrical implements IMultiBlockStructure<TileTurbine>, IPacketReceiver, IFluidHandler
{
	/**
	 * Radius of large turbine?
	 */
	protected int multiBlockRadius = 1;

	protected ForgeDirection placementDir = ForgeDirection.DOWN;

	/**
	 * Max power in watts.
	 */
	protected long maxPower = 800000;

	/**
	 * Turn slow down when not powered
	 */
	protected long powerDamping = 10000;

	/**
	 * Amount of energy per liter of steam.
	 */
	protected long energyPerSteam = 339;

	private final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 100);

	public long power = 0;

	/**
	 * Current rotation of the turbine in radians.
	 */
	public float rotation = 0;

	protected long torque = 1000;
	protected float angularVelocity = 0;

	public TileTurbine()
	{
		this.energy = new EnergyStorageHandler();
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.getMultiBlock().update();

		if (this.getMultiBlock().isPrimary())
		{
			/**
			 * Increase spin rate and consume steam.
			 */
			if (tank.getFluid() != null)
			{
				power += tank.drain(tank.getCapacity(), true).amount * energyPerSteam;
			}

			if (power > 0)
			{
				playSound();

				/**
				 * Set angular velocity based on power and torque.
				 */
				angularVelocity = power / getTorque();
				/**
				 * Update rotation.
				 */
				rotation += angularVelocity;

				if (!worldObj.isRemote && this.ticks % 3 == 0)
				{
					sendPowerUpdate();
				}

				rotation = MathHelper.wrapAngleTo180_float(rotation);
			}

			if (this.getMultiBlock().isConstructed())
			{
				power = Math.max(Math.min(power - powerDamping * 9, getMaxPower()), 0);
			}
			else
			{
				power = Math.max(Math.min(power - powerDamping, getMaxPower()), 0);
			}
		}
	}

	protected long getMaxPower()
	{
		if (this.getMultiBlock().isConstructed())
		{
			return maxPower * 9;
		}

		return maxPower;
	}

	public long getTorque()
	{
		if (this.getMultiBlock().isConstructed())
		{
			return torque * 3;
		}

		return torque;
	}

	public void onProduce()
	{
	}

	public void playSound()
	{

	}

	public abstract void sendPowerUpdate();

	@Override
	public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
	{
		try
		{
			final byte id = data.readByte();

			if (id == 1)
			{
				this.readFromNBT(PacketHandler.readNBTTagCompound(data));
			}
			else if (id == 2)
			{
				this.power = data.readLong();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return this.getMultiBlock().isPrimary() && direction == ForgeDirection.UP;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		power = nbt.getLong("power");
		getMultiBlock().load(nbt);
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		getMultiBlock().save(this, nbt);
	}

	/**
	 * Tank Methods
	 */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return getMultiBlock().get().tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return fluid != null && fluid.getName().equals("steam");
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] { this.tank.getInfo() };
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(this.xCoord - 3, this.yCoord, this.zCoord - 3, this.xCoord + 4, this.yCoord + 1, this.zCoord + 4);
	}

	@Override
	public EnumSet<ForgeDirection> getOutputDirections()
	{
		return EnumSet.of(ForgeDirection.UP);
	}

	/**
	 * MutliBlock methods.
	 */
	private MultiBlockHandler<TileTurbine> multiBlock;

	@Override
	public Vector3[] getMultiBlockVectors()
	{
		Set<Vector3> vectors = new HashSet<Vector3>();

		int xMulti = placementDir.offsetX != 0 ? 0 : 1;
		int yMulti = placementDir.offsetY != 0 ? 0 : 1;
		int zMulti = placementDir.offsetZ != 0 ? 0 : 1;

		for (int x = -multiBlockRadius; x <= multiBlockRadius; x++)
		{
			for (int y = -multiBlockRadius; y <= multiBlockRadius; y++)
			{
				for (int z = -multiBlockRadius; z <= multiBlockRadius; z++)
				{
					vectors.add(new Vector3(x * xMulti, y * yMulti, z * zMulti));
				}
			}
		}

		return vectors.toArray(new Vector3[0]);
	}

	@Override
	public Vector3 getPosition()
	{
		return new Vector3(this);
	}

	@Override
	public MultiBlockHandler<TileTurbine> getMultiBlock()
	{
		if (multiBlock == null)
			multiBlock = new MultiBlockHandler<TileTurbine>(this);

		return multiBlock;
	}

	@Override
	public void onMultiBlockChanged()
	{
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public World getWorld()
	{
		return worldObj;
	}
}
