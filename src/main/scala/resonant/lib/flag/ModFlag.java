package resonant.lib.flag;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import resonant.lib.References;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;
import resonant.lib.utility.nbt.SaveManager;
import universalelectricity.api.vector.Vector3;

/** @author Calclavia */
public class ModFlag extends FlagBase implements IVirtualObject
{
    /** An array of world flag data. Each representing a world. */
    private final HashMap<Integer, FlagWorld> flagWorlds = new HashMap<Integer, FlagWorld>();
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
        if (nbt.hasKey("name"))
        {
            this.name = nbt.getString("name");
            NBTTagList nbtList = nbt.getTagList("WorldFlags");

            for (int i = 0; i < nbtList.tagCount(); ++i)
            {
                NBTTagCompound stackTag = (NBTTagCompound) nbtList.tagAt(i);
                FlagWorld flagWorld = new FlagWorld(stackTag);
                this.flagWorlds.put(flagWorld.world.provider.dimensionId, flagWorld);
            }
        }
        else
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
                        this.flagWorlds.put(dimensionID, flagWorld);
                    }
                    catch (Exception e)
                    {
                        References.LOGGER.severe("Mod Flag: Failed to read dimension data: " + dimensionCompound.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setString("name", this.name);

        NBTTagList nbtFlagWorldList = new NBTTagList();

        for (Entry<Integer, FlagWorld> entry : this.flagWorlds.entrySet())
        {
            if (entry.getValue() != null)
            {
                try
                {
                    NBTTagCompound worldFlagNBT = new NBTTagCompound();
                    entry.getValue().save(worldFlagNBT);
                    nbtFlagWorldList.appendTag(worldFlagNBT);
                }
                catch (Exception e)
                {
                    References.LOGGER.severe("Mod Flag: Failed to save world flag data: " + entry.getValue().world.provider.dimensionId);
                    e.printStackTrace();
                }
            }
        }
        nbt.setTag("WorldFlags", nbtFlagWorldList);
    }

    public FlagWorld getFlagWorld(World world)
    {
        FlagWorld worldData = null;

        if (world != null)
        {
            worldData = this.flagWorlds.get(world.provider.dimensionId);

            // If data is null, create it.
            if (worldData == null)
            {
                worldData = new FlagWorld(world);
                this.flagWorlds.put(world.provider.dimensionId, worldData);
            }
        }

        return worldData;
    }

    public boolean containsValue(World world, String flagName, String checkValue, Vector3 position)
    {
        return this.getFlagWorld(world).containsValue(flagName, checkValue, position);
    }

    public HashMap<Integer, FlagWorld> getFlagWorlds()
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
