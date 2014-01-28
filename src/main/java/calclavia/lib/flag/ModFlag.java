package calclavia.lib.flag;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.utility.nbt.IVirtualObject;
import calclavia.lib.utility.nbt.NBTUtility;
import calclavia.lib.utility.nbt.SaveManager;

/** @author Calclavia */
public class ModFlag extends FlagBase implements IVirtualObject
{
    /** An array of world flag data. Each representing a world. */
    private final List<FlagWorld> flagWorlds = new ArrayList<FlagWorld>();
    private String name = FlagRegistry.DEFAULT_NAME;

    public ModFlag()
    {
        SaveManager.register(this);
    }

    /** Initiates a new mod flag data and loads everything from NBT into memory. Only exists server
     * side.
     * 
     * @param nbt */
    public ModFlag(String name)
    {
        this();
        this.name = name;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        if (nbt != null)
        {
            // A list containing all dimension ID and data within it.
            Iterator dimensions = nbt.getTags().iterator();

            while (dimensions.hasNext())
            {

                Object tag = dimensions.next();
                if (tag instanceof NBTTagCompound)
                {
                    NBTTagCompound dimensionCompound = (NBTTagCompound) tag;
                    try
                    {
                        int dimensionID = Integer.parseInt(dimensionCompound.getName().replace("dim_", ""));
                        World world = DimensionManager.getWorld(dimensionID);
                        FlagWorld flagWorld = new FlagWorld(world);
                        flagWorld.load(dimensionCompound);
                        this.flagWorlds.add(flagWorld);
                    }
                    catch (Exception e)
                    {
                        System.out.println("Mod Flag: Failed to read dimension data: " + dimensionCompound.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        if (nbt != null)
        {
            for (FlagWorld worldData : this.flagWorlds)
            {
                try
                {
                    nbt.setTag("dim_" + worldData.world.provider.dimensionId, worldData.getNBT());
                }
                catch (Exception e)
                {
                    System.out.println("Mod Flag: Failed to save world flag data: " + worldData.world);
                    e.printStackTrace();
                }
            }
        }
    }

    public FlagWorld getFlagWorld(World world)
    {
        FlagWorld worldData = null;

        if (world != null)
        {
            for (FlagWorld data : this.flagWorlds)
            {
                if (data.world != null && data.world.provider != null)
                {
                    if (data.world.provider.dimensionId == world.provider.dimensionId)
                    {
                        worldData = data;
                        break;
                    }
                }
            }

            // If data is null, create it.
            if (worldData == null)
            {
                worldData = new FlagWorld(world);
                this.flagWorlds.add(worldData);
            }
        }

        return worldData;
    }

    public boolean containsValue(World world, String flagName, String checkValue, Vector3 position)
    {
        return this.getFlagWorld(world).containsValue(flagName, checkValue, position);
    }

    public List<FlagWorld> getFlagWorlds()
    {
        return this.flagWorlds;
    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), this.name + ".dat");
    }

    @Override
    public void setSaveFile(File file)
    {
        // TODO Auto-generated method stub

    }

}
