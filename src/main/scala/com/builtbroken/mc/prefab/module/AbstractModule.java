package com.builtbroken.mc.prefab.module;

import com.builtbroken.mc.api.modules.IModule;
import com.builtbroken.mc.api.modules.IModuleHasMass;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

/**
 * Created by robert on 12/28/2014.
 */
public abstract class AbstractModule implements IModule, IModuleHasMass
{
    /** ItemStack that represents this module */
    protected ItemStack item;
    protected String name;

    public AbstractModule(ItemStack item, String name)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Item for module can not be null");
        }
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Name for a module can not be null or empty");
        }
        this.item = item.copy();
        this.name = name;
    }

    @Override
    public double getMass()
    {
        return 1;
    }

    @Override
    public String getUnlocalizedName()
    {
        return "module." + name;
    }

    /** Loads from the item's NBT */
    public final AbstractModule load()
    {
        load(item);
        return this;
    }

    /** Loads from an ItemStack's NBT, can be used to clone modules */
    public void load(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            load(stack.getTagCompound());
        }
    }

    @Override
    public void save(ItemStack stack)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        save(tagCompound);
        if(saveTag())
        {
            tagCompound.setString(ModuleBuilder.SAVE_ID, getSaveID());
        }

        //Only save to item if we have data to save
        if(!tagCompound.hasNoTags())
        {
            stack.setTagCompound(tagCompound);
        }
    }

    protected boolean saveTag()
    {
        return true;
    }

    @Override
    public ItemStack toStack()
    {
        ItemStack stack = item.copy();
        save(stack);
        return stack;
    }

    /** Does the same thing as {@link #toStack()} */
    public final ItemStack save()
    {
        ItemStack stack = item.copy();
        save(stack);
        return stack;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        return nbt;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {

    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@" + hashCode();
    }

}
