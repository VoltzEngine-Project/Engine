package resonant.lib.flag;

import net.minecraft.nbt.NBTTagCompound;
import resonant.lib.utility.nbt.ISaveObj;

/** @author Calclavia */
public abstract class FlagBase implements ISaveObj
{
    /** Gets the NBT data that would normally be saved by the object */
    public NBTTagCompound getNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        try
        {
            this.save(nbt);
        }
        catch (Exception e)
        {
            System.out.println("[Caclavia-Core]FlagBase: Failed to read flag");
            e.printStackTrace();
        }

        return nbt;
    }
}
