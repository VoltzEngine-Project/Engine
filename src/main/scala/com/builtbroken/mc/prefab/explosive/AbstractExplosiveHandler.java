package com.builtbroken.mc.prefab.explosive;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/30/2016.
 */
public abstract class AbstractExplosiveHandler implements IExplosiveHandler
{
    /**
     * unlocalized and registry name
     */
    protected String translationKey;
    protected String id;
    protected String modID;

    /**
     * Creates an explosive using a blast class, and name
     *
     * @param name       - name to use for registry id
     */
    public AbstractExplosiveHandler(String name)
    {
        this.translationKey = name;
    }

    @Override
    public void addInfoToItem(EntityPlayer player, ItemStack stack, List<String> lines)
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
