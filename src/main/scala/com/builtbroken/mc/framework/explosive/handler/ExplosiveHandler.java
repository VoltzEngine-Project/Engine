package com.builtbroken.mc.framework.explosive.handler;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.items.explosives.IExplosiveContainerItem;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.framework.explosive.ExplosiveRegistry;
import com.builtbroken.mc.framework.explosive.blast.Blast;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Basic implementation of an explosive handler
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/30/2016.
 */
public abstract class ExplosiveHandler<B extends Blast> implements IExplosiveHandler
{
    /** Data about the explosive */
    protected final ExplosiveData data;

    public ExplosiveHandler(ExplosiveData data)
    {
        this.data = data;
    }

    /**
     * Creates an explosive using a blast class, and name
     *
     * @param name - name to use for registry id
     */
    @Deprecated
    public ExplosiveHandler(String name)
    {
        this(name, 1);
    }

    /**
     * Creates an explosive using a blast class, and name
     *
     * @param name  - name to use for registry id
     * @param multi - amount to multiply blast size or power by
     */
    @Deprecated
    public ExplosiveHandler(String name, int multi)
    {
        this(new ExplosiveData(null));
        this.data.translationKey = name;
        this.data.multiplier = multi;
    }

    @Override
    public void addInfoToItem(EntityPlayer player, ItemStack stack, List<String> lines)
    {
        lines.add(LanguageUtility.getLocal("info." + References.PREFIX + "explosive.name") + ": " + LanguageUtility.getLocal(getTranslationKey() + ".name"));
        if (stack != null)
        {
            if (stack.getItem() instanceof IExplosiveContainerItem)
            {
                ItemStack ex_stack = ((IExplosiveContainerItem) stack.getItem()).getExplosiveStack(stack);
                if (ex_stack != null)
                {
                    String s = LanguageUtility.getLocal("info." + References.PREFIX + "explosive.item.name");
                    if (s != null && !s.isEmpty())
                    {
                        lines.add(s + ": " + ex_stack.getDisplayName());
                    }
                }
            }
            if (ExplosiveRegistry.getExplosiveSize(stack) > 0)
            {
                String s = LanguageUtility.getLocal("info." + References.PREFIX + "explosive.yield.name");
                if (s != null && !s.isEmpty())
                {
                    String yield = "" + (ExplosiveRegistry.getExplosiveSize(stack) * getYieldModifier(stack));
                    yield = yield.substring(0, Math.min(yield.indexOf(".") + 2, yield.length()));
                    lines.add(String.format(s, yield));
                }
            }
        }
    }

    @Override
    public IWorldChangeAction createBlastForTrigger(World world, double x, double y, double z, TriggerCause triggerCause, double size, NBTTagCompound tag)
    {
        B blast = newBlast(tag);
        if (blast != null)
        {
            blast.setLocation(world, x, y, z);
            blast.setCause(triggerCause);
            blast.setYield(size * getYieldModifier());
            blast.setAdditionBlastData(tag);
            addData(blast);
        }
        return blast;
    }

    protected void addData(B blast)
    {

    }

    /**
     * New Instance of the blast class
     *
     * @return
     */
    protected B newBlast(NBTTagCompound tag)
    {
        return newBlast();
    }

    /**
     * New Instance of the blast class
     *
     * @return
     */
    protected B newBlast()
    {
        return null;
    }

    @Override
    public double getYieldModifier(ItemStack stack)
    {
        return getYieldModifier();
    }

    @Override
    public double getYieldModifier()
    {
        return data.multiplier;
    }

    @Override
    public void onRegistered(String id, String modID)
    {
        this.data.id = id;
        this.data.modID = modID;
    }

    @Override
    public String getTranslationKey()
    {
        return "explosive." + data.modID + ":" + data.translationKey;
    }

    @Override
    public String getID()
    {
        return data.id;
    }

    @Override
    public String toString()
    {
        return "ExHandler[" + getID() + "]";
    }
}
