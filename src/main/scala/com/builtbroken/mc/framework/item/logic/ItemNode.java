package com.builtbroken.mc.framework.item.logic;

import com.builtbroken.mc.api.items.listeners.IItemEventListener;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.item.ItemBase;
import com.builtbroken.mc.lib.json.loading.JsonProcessorData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Node to handle logic for an item
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2017.
 */
public class ItemNode implements IItemEventListener
{
    private static final List<String> empty_list = new ArrayList()
    {
        @Override
        public boolean add(Object s)
        {
            return false;
        }
    };

    /** Domain of the mod that owners this content */
    public final String owner;
    /** Unique ID of the content */
    public final String id;

    public ItemBase item;

    private boolean hasSubTypes = false;
    private int maxStackSize = 64;
    private String unlocalizedName;

    public ItemNode(String owner, String id)
    {
        this.owner = owner;
        this.id = id;
    }

    public boolean isHasSubTypes()
    {
        return hasSubTypes;
    }

    @JsonProcessorData(value = "hasSubTypes")
    public void setHasSubTypes(boolean hasSubTypes)
    {
        this.hasSubTypes = hasSubTypes;
    }

    public int getMaxStackSize()
    {
        return maxStackSize;
    }

    @JsonProcessorData(value = "maxStackSize", type = "int")
    public void setMaxStackSize(int maxStackSize)
    {
        this.maxStackSize = maxStackSize;
    }

    @Override
    public List<String> getListenerKeys()
    {
        return empty_list;
    }

    @JsonProcessorData(value = "name")
    public void setUnlocalizedName(String name)
    {
        this.unlocalizedName = name;
        item.setUnlocalizedName(owner + ":" + name);
        if (item.iconString == null)
        {
            item.setTextureName(owner + ":" + name);
        }
    }

    /**
     * Called to read packet data
     *
     * @param buf
     * @param player
     * @param stack
     */
    public void readPacketData(ByteBuf buf, EntityPlayer player, ItemStack stack)
    {
        if (Engine.runningAsDev)
        {
            throw new RuntimeException("Received packet on item without being coded to handle packets.");
        }
    }
}
