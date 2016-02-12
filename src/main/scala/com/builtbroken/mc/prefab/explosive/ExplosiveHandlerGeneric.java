package com.builtbroken.mc.prefab.explosive;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.event.TriggerCause;
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
 * Generic way to trigger an explosive without coding an explosive handler.
 * It not meant to be extended as it uses reflection to create the blast.
 * <p/>
 * Created by robert on 11/19/2014.
 */
public final class ExplosiveHandlerGeneric extends AbstractExplosiveHandler
{
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
    public ExplosiveHandlerGeneric(Class<? extends Blast> blastClass)
    {
        this(blastClass.getSimpleName(), blastClass, 1);
    }

    /**
     * Creates an explosive using a blast class, and name
     *
     * @param name       - name to use for registry id
     * @param blastClass - class extending blast
     */
    public ExplosiveHandlerGeneric(String name, Class<? extends Blast> blastClass)
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
    public ExplosiveHandlerGeneric(String name, Class<? extends Blast> blastClass, int multiplier)
    {
        super(name);
        this.blastClass = blastClass;
        this.multiplier = multiplier;
    }

    @Override
    public IWorldChangeAction createBlastForTrigger(World world, double x, double y, double z, TriggerCause triggerCause, double yieldMultiplier, NBTTagCompound tag)
    {
        try
        {
            Blast blast = blastClass.newInstance();
            blast.setLocation(world, (int) x, (int) y, (int) z);
            blast.setYield(yieldMultiplier * multiplier);
            blast.setCause(triggerCause);
            blast.setAdditionBlastData(tag);
            return blast;
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
    }
}
