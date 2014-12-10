package resonant.api;

import net.minecraft.nbt.NBTTagCompound;

/** Simple interface placed over objects that
 * save data to and from the world.
 *
 * Created by robert on 8/14/2014.
 */
public interface ISave
{
    /** Called to load the object from NBT */
    public void load(NBTTagCompound nbt);

    /** Called to save the object to NBT */
    public void save(NBTTagCompound nbt);
}
