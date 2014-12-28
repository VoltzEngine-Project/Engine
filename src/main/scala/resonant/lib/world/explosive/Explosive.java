package resonant.lib.world.explosive;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import resonant.api.TriggerCause;
import resonant.api.explosive.IExplosive;
import resonant.engine.References;
import resonant.lib.type.Pair;
import resonant.lib.utility.LanguageUtility;
import resonant.lib.world.edit.IWorldChangeAction;

import java.util.List;

/**
 * Prefab explosive container for generating blasts when triggered
 * <p/>
 * Created by robert on 11/19/2014.
 */
public class Explosive implements IExplosive
{
    /**
     * unlocalized and registry name
     */
    protected String translationKey;
    protected String id;
    protected String modID;
    /**
     * Class to generate explosives from
     */
    protected Class<? extends Blast> blastClass;
    /**
     * Size multiplier
     */
    int multiplier = 1;

    /**
     * Creates an explosive using a blast class, uses the blast class's name as the registry id
     *
     * @param blastClass - class extending blast
     */
    public Explosive(Class<? extends Blast> blastClass)
    {
        this(blastClass.getSimpleName(), blastClass, 1);
    }

    /**
     * Creates an explosive using a blast class, and name
     *
     * @param name       - name to use for registry id
     * @param blastClass - class extending blast
     */
    public Explosive(String name, Class<? extends Blast> blastClass)
    {
        this(name, blastClass, 1);
    }

    /**
     * Creates an explosive using a blast class, and name
     *
     * @param name       - name to use for registry id
     * @param blastClass - class extending blast
     * @param multiplier - value to mutliply the size by
     */
    public Explosive(String name, Class<? extends Blast> blastClass, int multiplier)
    {
        this.translationKey = name;
        this.blastClass = blastClass;
        this.multiplier = multiplier;
    }

    @Override
    public IWorldChangeAction createBlastForTrigger(World world, double x, double y, double z, TriggerCause triggerCause, int yieldMultiplier, NBTTagCompound tag)
    {
        try
        {
            return blastClass.newInstance().setLocation(world, (int) x, (int) y, (int) z).setYield(yieldMultiplier * multiplier).setCause(triggerCause);
        } catch (InstantiationException e)
        {
            References.LOGGER.log(Level.ERROR, "Failed to create blast object");
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            References.LOGGER.log(Level.ERROR, "Failed to create blast object");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addInfoToItem(ItemStack stack, List<String> lines)
    {
        lines.add(LanguageUtility.getLocal("info." + References.PREFIX + "explosive.size.name") + ": " + multiplier);
    }

    @Override
    public void onRegistered(String id, String modID)
    {
        this.id = id;
        this.modID = modID;
    }

    @Override
    public String getTranslationKey()
    {
        return "explosive." + modID + ":" +translationKey;
    }

    @Override
    public String getID()
    {
        return id;
    }
}
