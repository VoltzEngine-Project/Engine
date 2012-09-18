package universalelectricity.extend;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.network.IPacketReceiver;
import universalelectricity.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

/**
 * REQUIRED
 * This tile entity is for all conductors.
 * @author Calclavia
 *
 */
public abstract class TileEntityConductor extends TileEntity implements IPacketReceiver
{
    public int connectionID = 0;

    /**
     * Stores information on the blocks that this conductor is connected to
     */
    public TileEntity[] connectedBlocks = {null, null, null, null, null, null};

    public TileEntityConductor()
    {
        this.reset();
    }

    /**
     * Adds a connection between this conductor and a UE unit
     * @param tileEntity - Must be either a producer, consumer or a conductor
     * @param side - side in which the connection is coming from
     */
    public void updateConnection(TileEntity tileEntity, ForgeDirection side)
    {
        if (tileEntity instanceof TileEntityConductor || tileEntity instanceof IElectricUnit)
        {
            this.connectedBlocks[side.ordinal()] = tileEntity;

            if (tileEntity instanceof TileEntityConductor)
            {
                ElectricityManager.instance.mergeConnection(this.connectionID, ((TileEntityConductor)tileEntity).connectionID);
            }
        }
        else
        {
            if (this.connectedBlocks[side.ordinal()] != null)
            {
                if (this.connectedBlocks[side.ordinal()] instanceof TileEntityConductor)
                {
                    ElectricityManager.instance.splitConnection(this, (TileEntityConductor)this.connectedBlocks[side.ordinal()]);
                }
            }

            this.connectedBlocks[side.ordinal()] = null;
        }
        
        if(!this.worldObj.isRemote)
        {
        	PacketManager.sendTileEntityPacket(this, "BasicComponents", 1);
        }
    }

    public void updateConnectionWithoutSplit(TileEntity tileEntity, ForgeDirection side)
    {
        if(tileEntity instanceof TileEntityConductor || tileEntity instanceof IElectricUnit)
        {
            this.connectedBlocks[side.ordinal()] = tileEntity;

            if (tileEntity instanceof TileEntityConductor)
            {
                ElectricityManager.instance.mergeConnection(this.connectionID, ((TileEntityConductor)tileEntity).connectionID);
            }
        }
        else
        {
            this.connectedBlocks[side.ordinal()] = null;
        }
        
        if(!this.worldObj.isRemote)
        {
        	PacketManager.sendTileEntityPacket(this, "BasicComponents", 1);
        }
    }
    
    @Override
	public void handlePacketData(NetworkManager network, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
        {
            int ID = dataStream.readInt();
            
            if(ID == 1)
            {
                this.refreshConnectedBlocks();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}

    /**
     * Determines if this TileEntity requires update calls.
     * @return True if you want updateEntity() to be called, false if not
     */
    @Override
    public boolean canUpdate()
    {
        return false;
    }

    public void reset()
    {
        this.connectionID = 0;
        ElectricityManager.instance.registerConductor(this);
    }

    public void refreshConnectedBlocks()
    {
        if (this.worldObj != null)
        {
            BlockConductor.updateConductorTileEntity(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
    }

    /**
     * Gets the resistance of the conductor. Used to calculate energy loss.
     * A higher resistance means a higher energy loss.
     * @return The amount of Ohm's. E.g 1.2Ohms or 3.0Ohms
     */
    public abstract double getResistance();
    
    /**
     * The maximum amount of voltage this conductor can handle before exploding
     * @return The amount of voltage in volts
     */
    public abstract double getVoltage();
    
    public World getWorld()
    {
    	return this.worldObj;
    }
}
