package resonant.lib.prefab.tile;

import java.util.EnumSet;

import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import resonant.api.IIO;
import resonant.lib.content.module.TileBase;

public abstract class TileIO extends TileBase implements IIO
{
    /** IO METHODS. Default: Connect from all sides. "111111" Output all sides: 728 0 - Nothing 1 -
     * Input 2 - Output */
    protected short ioMap = 364;
    protected boolean saveIOMap = false;

    public TileIO(Material material)
    {
        super(material);
    }

    /** The electrical input direction.
     * 
     * @return The direction that electricity is entered into the tile. Return null for no input. By
     * default you can accept power from all sides. */
    @Override
    public EnumSet<ForgeDirection> getInputDirections()
    {
        EnumSet<ForgeDirection> dirs = EnumSet.noneOf(ForgeDirection.class);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            if (getIO(direction) == 1)
            {
                dirs.add(direction);
            }
        }

        return dirs;
    }

    /** The electrical output direction.
     * 
     * @return The direction that electricity is output from the tile. Return null for no output. By
     * default it will return an empty EnumSet. */
    @Override
    public EnumSet<ForgeDirection> getOutputDirections()
    {
        EnumSet<ForgeDirection> dirs = EnumSet.noneOf(ForgeDirection.class);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            if (getIO(direction) == 2)
            {
                dirs.add(direction);
            }
        }

        return dirs;
    }

    @Override
    public void setIO(ForgeDirection dir, int type)
    {
        String currentIO = getIOMapBase3();
        StringBuilder str = new StringBuilder(currentIO);
        str.setCharAt(dir.ordinal(), Integer.toString(type).charAt(0));
        this.ioMap = Short.parseShort(str.toString(), 3);
    }

    @Override
    public int getIO(ForgeDirection dir)
    {
        String currentIO = getIOMapBase3();
        return Integer.parseInt("" + currentIO.charAt(dir.ordinal()));
    }

    public String getIOMapBase3()
    {
        String currentIO = Integer.toString(ioMap, 3);

        while (currentIO.length() < 6)
        {
            currentIO = "0" + currentIO;
        }

        return currentIO;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (saveIOMap && nbt.hasKey("ioMap"))
        {
            this.ioMap = nbt.getShort("ioMap");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        if (saveIOMap)
        {
            nbt.setShort("ioMap", this.ioMap);
        }
    }
}