package resonant.lib.prefab.turbine;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import resonant.core.ResonantEngine;
import resonant.lib.References;
import resonant.lib.multiblock.IMultiBlockStructure;
import resonant.lib.network.Synced;
import resonant.lib.network.Synced.SyncedInput;
import resonant.lib.network.Synced.SyncedOutput;
import resonant.lib.prefab.tile.TileElectrical;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Turbine TileEntity
 * <p/>
 * 1 cubic meter of steam = 338260 J of energy
 * <p/>
 * The front of the turbine is where the output is. */

public abstract class TileTurbine extends TileElectrical implements IMultiBlockStructure<TileTurbine>, IFluidHandler
{
    /** Amount of energy per liter of steam. Boil Water Energy = 327600 + 2260000 = 2587600 */
    protected final long energyPerSteam = 2647600 / 1000;
    protected final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 100);
    protected final long defaultTorque = 5000;
    protected long torque = defaultTorque;
    /** Radius of large turbine? */
    public int multiBlockRadius = 1;
    /** The power of the turbine this tick. In joules/tick */
    public long power = 0;
    /** Current rotation of the turbine in radians. */
    public float rotation = 0;
    @Synced
    public int tier = 0;
    /** Max power in watts. */
    protected long maxPower;
    protected float prevAngularVelocity = 0;
    @Synced(1)
    protected float angularVelocity = 0;
    /** MutliBlock methods. */
    private TurbineMultiBlockHandler multiBlock;

    public TileTurbine()
    {
        /** We're going to use the EnergyStorageHandler to store power. */
        setEnergyHandler(new EnergyStorageHandler(maxPower * 20));
    }

    @Override
    public ForgeDirection getDirection()
    {
        return ForgeDirection.getOrientation(getBlockMetadata());
    }

    @Override
    public void initiate()
    {
        super.initiate();
        setEnergyHandler(new EnergyStorageHandler(maxPower * 20));
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        getMultiBlock().update();

        if (getMultiBlock().isPrimary())
        {
            if (!worldObj.isRemote)
            {
                /** Increase spin rate and consume steam. */
                if (tank.getFluidAmount() > 0 && power < maxPower)
                {
                    power += tank.drain((int) Math.ceil(Math.min(tank.getFluidAmount() * 0.1, getMaxPower() / energyPerSteam)), true).amount * energyPerSteam;
                }

                /** Set angular velocity based on power and torque. */
                angularVelocity = (float) ((double) (power * 4) / torque);

                if (!worldObj.isRemote && ticks % 3 == 0 && prevAngularVelocity != angularVelocity)
                {
                    sendPowerUpdate();
                    prevAngularVelocity = angularVelocity;
                }

                if (power > 0)
                {
                    onProduce();
                }
            }

            if (angularVelocity != 0)
            {
                playSound();

                /** Update rotation. */
                rotation = (float) ((rotation + angularVelocity / 20) % (Math.PI * 2));
            }
        }
        else if (tank.getFluidAmount() > 0)
        {
            getMultiBlock().get().tank.fill(tank.drain(getMultiBlock().get().tank.fill(tank.getFluid(), false), true), true);
        }

        if (!worldObj.isRemote)
        {
            power = 0;
        }
    }

    protected long getMaxPower()
    {
        if (this.getMultiBlock().isConstructed())
        {
            return maxPower * getArea();
        }

        return maxPower;
    }

    public int getArea()
    {
        return (int) (((multiBlockRadius + 0.5) * 2) * ((multiBlockRadius + 0.5) * 2));
    }

    public void onProduce()
    {
    }

    public void playSound()
    {

    }

    @Override
    public Packet getDescriptionPacket()
    {
        return References.PACKET_ANNOTATION.getPacket(this);
    }

    public void sendPowerUpdate()
    {
        if (!world().isRemote)
        {
            References.PACKET_ANNOTATION.sync(this, 1);
        }
    }

    @Override
    public boolean canConnect(ForgeDirection direction, Object obj)
    {
        return this.getMultiBlock().isPrimary() && direction == ForgeDirection.UP;
    }

    /** Reads a tile entity from NBT. */
    @Override
    @SyncedInput
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        tank.readFromNBT(nbt);
        multiBlockRadius = nbt.getInteger("multiBlockRadius");
        getMultiBlock().load(nbt);
    }

    /** Writes a tile entity to NBT. */
    @Override
    @SyncedOutput
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        tank.writeToNBT(nbt);
        nbt.setInteger("multiBlockRadius", multiBlockRadius);
        getMultiBlock().save(nbt);
    }

    /** Tank Methods */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (resource != null && canFill(from, resource.getFluid()))
        {
            return getMultiBlock().get().tank.fill(resource, doFill);
        }
        return 0;
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
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord - multiBlockRadius, this.yCoord - multiBlockRadius, this.zCoord - multiBlockRadius, this.xCoord + 1 + multiBlockRadius, this.yCoord + 1 + multiBlockRadius, this.zCoord + 1 + multiBlockRadius);
    }

    @Override
    public EnumSet<ForgeDirection> getInputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public EnumSet<ForgeDirection> getOutputDirections()
    {
        return EnumSet.of(ForgeDirection.UP);
    }

    @Override
    public Vector3[] getMultiBlockVectors()
    {
        Set<Vector3> vectors = new HashSet<Vector3>();

        ForgeDirection dir = getDirection();
        int xMulti = dir.offsetX != 0 ? 0 : 1;
        int yMulti = dir.offsetY != 0 ? 0 : 1;
        int zMulti = dir.offsetZ != 0 ? 0 : 1;

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
    public TurbineMultiBlockHandler getMultiBlock()
    {
        if (multiBlock == null)
        {
            multiBlock = new TurbineMultiBlockHandler(this);
        }

        return multiBlock;
    }

    @Override
    public void onMultiBlockChanged()
    {
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType() != null ? getBlockType().blockID : 0);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public World getWorld()
    {
        return worldObj;
    }

}
