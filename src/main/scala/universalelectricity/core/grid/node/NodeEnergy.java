package universalelectricity.core.grid.node;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.EnergyStorage;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.api.core.grid.ISave;
import universalelectricity.api.core.grid.IUpdate;
import universalelectricity.api.core.grid.electric.IEnergyNode;
import universalelectricity.compatibility.Compatibility;

import java.util.Map;

/**
 * Basic node designed to store energy
 * @author Darkguardsman
 */
public class NodeEnergy extends NodeConnector implements IEnergyNode, IUpdate, ISave
{
    protected EnergyStorage buffer = null;
    protected boolean shareEnergy = false;

    public NodeEnergy(INodeProvider parent)
    {
        this(parent, 100);
    }

    public NodeEnergy(INodeProvider parent, double cap)
    {
        this(parent, cap, cap);
    }

    public NodeEnergy(INodeProvider parent, double cap, double tran)
    {
        this(parent, cap, tran, tran);
    }

    public NodeEnergy(INodeProvider parent, double cap, double in, double out)
    {
        super(parent);
        buffer = new EnergyStorage(cap, in, out);
    }


    @Override
    public void update(double deltaTime)
    {
        if(canShareEnergy())
            shareEnergy();
    }

    public EnergyStorage buffer()
    {
        return buffer;
    }

    @Override
    public double addEnergy(ForgeDirection from, double wattage, boolean doAdd)
    {
        if(canConnect(from.getOpposite()))
        {
            return buffer.receiveEnergy(wattage, doAdd);
        }
        return 0;
    }

    @Override
    public double removeEnergy(ForgeDirection from, double wattage, boolean doRemove)
    {
        if(canConnect(from.getOpposite()) && canShareEnergy())
        {
            return buffer.extractEnergy(wattage, doRemove);
        }
        return 0;
    }

    @Override
    public double getEnergy(ForgeDirection from)
    {
        if(canConnect(from.getOpposite()))
        {
            return buffer.getEnergy();
        }
        return -1;
    }

    @Override
    public double getEnergyCapacity(ForgeDirection from)
    {
        if(canConnect(from.getOpposite()))
        {
            return buffer.getEnergyCapacity();
        }
        return -1;
    }

    /** Can this node share energy */
    public boolean canShareEnergy()
    {
        return shareEnergy;
    }

    /**
     * Sets the node to share energy with connected nodes
     * @param share - true will share energy
     */
    public void setCanShareEnergy(boolean share)
    {
        this.shareEnergy = share;
    }

    /** called to share energy with all connected nodes */
    public void shareEnergy()
    {
        System.out.println("PowerCheck: " + this);
        if(getEnergy(ForgeDirection.UNKNOWN) > 0)
        {
            System.out.println("\tEnergy: " + getEnergy(ForgeDirection.UNKNOWN));
            int handlers = connections.size();
            for (Map.Entry<Object, ForgeDirection> entry : connections.entrySet())
            {
                double energyToGive = Math.min(buffer.maxExtract(), (getEnergy(entry.getValue()) / handlers) + (getEnergy(ForgeDirection.UNKNOWN) % handlers));
                System.out.println("\tSide: " + entry.getValue() + "  Object: " + entry.getKey() + "   Energy: " + energyToGive);
                //TODO check if direction is correct
                if (Compatibility.isHandler(entry.getKey(), entry.getValue()))
                {
                    System.out.println("\tIsHandler");
                    if (Compatibility.canConnect(entry.getKey(), entry.getValue(), this))
                    {
                        System.out.println("Out:" +	buffer.extractEnergy(Compatibility.fill(entry.getKey(), entry.getValue(), buffer.extractEnergy(energyToGive, false), true), true));
                    }
                }
                handlers--;
            }
        }
    }

    @Override
    public boolean canConnect(ForgeDirection direction, Object object)
    {
        return object != null && Compatibility.isHandler(object, direction) && canConnect(direction) && Compatibility.canConnect(object, direction, this);
    }

    @Override
    public boolean canUpdate()
    {
        return canShareEnergy() && getEnergy(ForgeDirection.UNKNOWN) > 0;
    }

    @Override
    public boolean continueUpdate()
    {
        return true;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        if(nbt.hasKey("energy"))
        {
            NBTTagCompound energy = nbt.getCompoundTag("energy");
            buffer.readFromNBT(energy);
        }
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        NBTTagCompound energy = new NBTTagCompound();
        buffer.writeToNBT(energy);
        nbt.setTag("energy", energy);
    }
}
