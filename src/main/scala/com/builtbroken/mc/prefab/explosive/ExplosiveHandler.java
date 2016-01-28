package com.builtbroken.mc.prefab.explosive;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.prefab.explosive.blast.Blast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.List;

/**
 * Prefab explosive container for generating blasts when triggered
 * <p/>
 * Created by robert on 11/19/2014.
 */
public class ExplosiveHandler implements IExplosiveHandler
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
    public ExplosiveHandler(Class<? extends Blast> blastClass)
    {
        this(blastClass.getSimpleName(), blastClass, 1);
    }

    /**
     * Creates an explosive using a blast class, and name
     *
     * @param name       - name to use for registry id
     * @param blastClass - class extending blast
     */
    public ExplosiveHandler(String name, Class<? extends Blast> blastClass)
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
    public ExplosiveHandler(String name, Class<? extends Blast> blastClass, int multiplier)
    {
        this.translationKey = name;
        this.blastClass = blastClass;
        this.multiplier = multiplier;
    }

    @Override
    public IWorldChangeAction createBlastForTrigger(World world, double x, double y, double z, TriggerCause triggerCause, double yieldMultiplier, NBTTagCompound tag)
    {
        try
        {
            return blastClass.newInstance().setLocation(world, (int) x, (int) y, (int) z).setYield(yieldMultiplier * multiplier).setCause(triggerCause);
        } catch (InstantiationException | IllegalAccessException e)
        {
            Engine.instance.logger().log(Level.ERROR, "Failed to create blast object");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addInfoToItem(EntityPlayer player, ItemStack stack, List<String> lines)
    {
        lines.add(LanguageUtility.getLocal("info." + References.PREFIX + "explosive.size.name") + ": " + multiplier);
        addInfoToItem(stack, lines);
    }

    @Deprecated
    protected void addInfoToItem(ItemStack stack, List<String> lines)
    {

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

    @Override
    public String toString()
    {
        return "ExHandler[" + getID() + "]";
    }
}
