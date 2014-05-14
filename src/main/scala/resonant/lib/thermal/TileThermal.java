package resonant.lib.thermal;

import net.minecraft.nbt.NBTTagCompound;
import resonant.lib.prefab.tile.TileAdvanced;

public abstract class TileThermal extends TileAdvanced
{
    private int temperature = 273 + 20;

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    public int addThermalEnergy(long energy)
    {
        float mass = getMass();
        // c = Q/(mT); Therefore: Temperature (in Kelvin) = Q/mc
        int changeInTemperature = (int) (energy / (mass * specificHeatCapacity()));
        temperature += energy;
        onTemperatureChanged();
        return changeInTemperature;
    }

    protected void onTemperatureChanged()
    {
        if (temperature > boilingPoint())
        {
            boil();
        }
        else if (temperature < meltingPoint())
        {
            melt();
        }
    }

    /** Gets the mass in KG Mass (KG) = Volume (Cubic Meters) * Densitry (kg/m-cubed) */
    public float getMass()
    {
        return 1;
    }

    public abstract int boilingPoint();

    protected void boil()
    {

    }

    public abstract int meltingPoint();

    protected void melt()
    {

    }

    public abstract int specificHeatCapacity();

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        temperature = nbt.getInteger("temperature");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("temperature", temperature);
    }
}
