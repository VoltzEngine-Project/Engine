package universalelectricity.prefab.compatibility;

import ic2.api.Direction;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.compatibility.Compatibility;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.block.IElectricalStorage;
import universalelectricity.core.electricity.ElectricityPack;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerProvider;

/**
 * A universal electricity tile used for tiles that consume or produce electricity.
 * 
 * Extend this class or use as a reference for your own implementation of compatible electrical tiles.
 * 
 * @author micdoodle8
 * 
 */
public abstract class TileEntityUniversalElectrical extends TileEntity implements IElectrical, IEnergySink, IPowerReceptor, IElectricalStorage
{
    protected boolean addedToEnergyNet;
    public IPowerProvider bcPowerProvider;
    public float energyStored = 0;
    public float maxEnergyStored = 0;
    
    public TileEntityUniversalElectrical(float maxEnergy)
    {
        this(0, maxEnergy);
    }
    
    public TileEntityUniversalElectrical(float initialEnergy, float maxEnergy)
    {
        this.energyStored = initialEnergy;
        this.maxEnergyStored = maxEnergy;
        this.bcPowerProvider = new PowerProviderCompat(this);
        this.bcPowerProvider.configure(0, 0, 100, 0, (int)Math.floor(maxEnergy * Compatibility.BC3_RATIO));
    }
    
    /**
     * The electrical input direction.
     * 
     * @return The direction that electricity is entered into the tile. Return null for no input.
     */
    public abstract ForgeDirection getInputDirection();

    /**
     * The electrical output direction.
     * 
     * @return The direction that electricity is output from the tile. Return null for no output.
     */
    public abstract ForgeDirection getOutputDirection();
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction.equals(this.getInputDirection()) || direction.equals(this.getOutputDirection());
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return direction.toForgeDirection().equals(this.getInputDirection());
    }

    @Override
    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    @Override
    public void setPowerProvider(IPowerProvider provider)
    {
        this.bcPowerProvider = provider;
    }

    @Override
    public IPowerProvider getPowerProvider()
    {
        return this.bcPowerProvider;
    }

    @Override
    public void doWork()
    {
        ;
    }

    @Override
    public int powerRequest(ForgeDirection from)
    {
        if (!from.equals(this.getInputDirection()))
        {
            return 0;
        }

        return (int) Math.floor(this.getRequest(this.getInputDirection()) * Compatibility.TO_BC_RATIO);
    }

    @Override
    public int demandsEnergy()
    {
        return (int) Math.floor(this.getRequest(this.getInputDirection()) * Compatibility.TO_IC2_RATIO);
    }

    @Override
    public int injectEnergy(Direction directionFrom, int amount)
    {
        if (!directionFrom.toForgeDirection().equals(this.getInputDirection()))
        {
            return 0;
        }
        
        ElectricityPack toSend = ElectricityPack.getFromWatts(amount * Compatibility.IC2_RATIO, this.getVoltage());
        
        // Return the difference, since injectEnergy returns leftover energy, and receiveElectricity returns energy used.
        return (int) Math.floor((toSend.getWatts() - this.receiveElectricity(directionFrom.toForgeDirection(), toSend, true)) * Compatibility.TO_IC2_RATIO);
    }

    @Override
    public int getMaxSafeInput()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive)
    {
        if (!from.equals(this.getInputDirection()))
        {
            return 0.0F;
        }
        
        if (receive != null)
        {
            float prevEnergyStored = this.getEnergyStored();
            float newStoredEnergy = Math.min(this.getEnergyStored() + receive.getWatts(), this.getMaxEnergyStored());

            if (doReceive)
            {
                this.setEnergyStored(newStoredEnergy);
            }

            return Math.max(newStoredEnergy - prevEnergyStored, 0);
        }

        return 0.0F;
    }

    @Override
    public ElectricityPack provideElectricity(ForgeDirection from, ElectricityPack request, boolean doProvide)
    {
        if (!from.equals(this.getOutputDirection()))
        {
            return new ElectricityPack();
        }
        
        if (request != null)
        {
            float requestedEnergy = Math.min(request.getWatts(), this.energyStored);

            if (doProvide)
            {
                this.setEnergyStored(this.energyStored - requestedEnergy);
            }

            return ElectricityPack.getFromWatts(requestedEnergy, this.getVoltage());
        }

        return new ElectricityPack();
    }

    @Override
    public float getRequest(ForgeDirection direction)
    {
        return 0;
    }

    @Override
    public float getProvide(ForgeDirection direction)
    {
        return 0;
    }

    @Override
    public float getVoltage()
    {
        return 120;
    }

    @Override
    public void setEnergyStored(float energy)
    {
        this.energyStored = energy;
    }

    @Override
    public float getEnergyStored()
    {
        return this.energyStored;
    }

    @Override
    public float getMaxEnergyStored()
    {
        return this.maxEnergyStored;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.energyStored = nbt.getFloat("energyStored");
        this.maxEnergyStored = nbt.getFloat("maxEnergyStored");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("energyStored", this.energyStored);
        nbt.setFloat("maxEnergyStored", this.maxEnergyStored);
    }
    
    public class PowerProviderCompat extends PowerProvider
    {
        public TileEntityUniversalElectrical theTile;
        
        public PowerProviderCompat(TileEntityUniversalElectrical theTile)
        {
            this.theTile = theTile;
        }
        
        @Override
        public float useEnergy(float min, float max, boolean doUse) 
        {
            float result = 0;

            if (this.theTile.getEnergyStored() * Compatibility.TO_BC_RATIO >= min) 
            {
                if (this.theTile.getEnergyStored() * Compatibility.TO_BC_RATIO <= max) 
                {
                    result = (float)(this.theTile.getEnergyStored() * Compatibility.TO_BC_RATIO);
                    
                    if (doUse)
                    {
                        this.theTile.setEnergyStored(0.0F);
                    }
                } 
                else 
                {
                    result = max;
                    
                    if (doUse) 
                    {
                        this.theTile.setEnergyStored(this.theTile.getEnergyStored() - max * Compatibility.BC3_RATIO);
                    }
                }
            }

            return result;
        }
        
        @Override
        public void receiveEnergy(float quantity, ForgeDirection from) 
        {
            this.theTile.setEnergyStored(this.theTile.getEnergyStored() + (quantity * Compatibility.BC3_RATIO));
        }

        @Override
        public boolean update(IPowerReceptor receptor)
        {
            return true;
        }
    }
}
