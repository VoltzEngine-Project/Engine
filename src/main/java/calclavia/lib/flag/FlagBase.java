package calclavia.lib.flag;

import java.io.File;

import calclavia.lib.utility.nbt.IVirtualObject;
import net.minecraft.nbt.NBTTagCompound;

/** @author Calclavia */
public abstract class FlagBase implements IVirtualObject
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

    @Override
    public File getSaveFile()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSaveFile(File file)
    {
        // TODO Auto-generated method stub

    }
}
