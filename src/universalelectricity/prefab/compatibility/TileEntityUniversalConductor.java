package universalelectricity.prefab.compatibility;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.compatibility.Compatibility;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityConductor;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerProvider;

/**
 * A universal conductor class. 
 * 
 * Extend this class or use as a reference for your own implementation of compatible conductor tiles.
 * 
 * @author micdoodle8
 * 
 */
public abstract class TileEntityUniversalConductor extends TileEntityConductor implements IEnergySink, IPowerReceptor
{
    protected boolean addedToIC2Network = false;
    private DummyPowerProvider powerProvider;
    
    public TileEntityUniversalConductor()
    {
        this.powerProvider = new DummyPowerProvider(this.getNetwork(), this);
        this.powerProvider.configure(0, 0, 100, 0, 100);
    }

    @Override
    public void setNetwork(IElectricityNetwork network)
    {
        super.setNetwork(network);
        this.powerProvider.network = network;
    }

    @Override
    public boolean canUpdate()
    {
        return !this.addedToIC2Network;
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!this.worldObj.isRemote && !this.addedToIC2Network)
        {
            if (Compatibility.isIndustrialCraft2Loaded())
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            }
            
            this.addedToIC2Network = true;
        }
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return false;
    }

    @Override
    public boolean isAddedToEnergyNet()
    {
        return this.addedToIC2Network;
    }

    @Override
    public void setPowerProvider(IPowerProvider provider)
    {
        if (provider instanceof DummyPowerProvider)
        {
            this.powerProvider = (DummyPowerProvider) provider;
        }
    }

    @Override
    public IPowerProvider getPowerProvider()
    {
        return this.powerProvider;
    }

    @Override
    public void doWork()
    {
        ;
    }

    @Override
    public int powerRequest(ForgeDirection from)
    {
        if (this.getNetwork() == null)
        {
            return 0;
        }
        
        return (int)Math.floor(Math.min(this.getNetwork().getRequest(VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), from)).getWatts() * Compatibility.BC3_RATIO, 100));
    }

    @Override
    public int demandsEnergy()
    {
        if (this.getNetwork() == null)
        {
            return 0;
        }
        
        return (int)Math.floor(Math.min(this.getNetwork().getRequest(this).getWatts() * Compatibility.BC3_RATIO, 100));
    }

    @Override
    public int injectEnergy(Direction directionFrom, int amount)
    {
        if (this.getNetwork() == null)
        {
            return amount;
        }
        
        return (int) Math.floor(this.getNetwork().produce(ElectricityPack.getFromWatts(amount, 120), VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), directionFrom.toForgeDirection())));
    }

    @Override
    public int getMaxSafeInput()
    {
        return Integer.MAX_VALUE;
    }
    
    private class DummyPowerProvider extends PowerProvider
    {
        public IElectricityNetwork network;
        private final TileEntityUniversalConductor conductor;
        
        public DummyPowerProvider(IElectricityNetwork network, TileEntityUniversalConductor conductor)
        {
            this.network = network;
            this.conductor = conductor;
        }
        
        @Override
        public void receiveEnergy(float quantity, ForgeDirection from)
        {
            this.network.produce(ElectricityPack.getFromWatts(this.getEnergyStored(), 120), VectorHelper.getTileEntityFromSide(this.conductor.worldObj, new Vector3(this.conductor), from));
        }
    }
}
