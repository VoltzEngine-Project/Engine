package com.builtbroken.mc.framework.item;

import com.builtbroken.mc.api.items.listeners.IItemEventListener;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
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

    //Subtypes of the item
    public HashMap<Integer, ItemNodeSubType> subTypeHashMap = new HashMap();

    public ItemNode(String owner, String id)
    {
        this.owner = owner;
        this.id = id;
    }

    public boolean hasSubTypes()
    {
        return hasSubTypes;
    }

    @JsonProcessorData(value = "hasSubTypes")
    public void setHasSubTypes(boolean hasSubTypes)
    {
        this.hasSubTypes = hasSubTypes;
        this.item.setHasSubtypes(hasSubTypes);
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
    }

    @JsonProcessorData(value = "subTypes")
    public void setSubTypes(JsonElement data)
    {
        if (data.isJsonArray())
        {
            for (JsonElement element : data.getAsJsonArray())
            {
                JsonObject itemData = element.getAsJsonObject();
                if (element.isJsonObject()) //TODO move to JSON processor?
                {
                    JsonProcessor.ensureValuesExist(itemData, "id", "name", "index");
                    String id = itemData.getAsJsonPrimitive("id").getAsString();
                    String name = itemData.getAsJsonPrimitive("name").getAsString();
                    int index = itemData.getAsJsonPrimitive("index").getAsInt();

                    ItemNodeSubType subType = new ItemNodeSubType(this.item, this, id, name, index);

                    if (subTypeHashMap.containsKey(index))
                    {
                        throw new IllegalArgumentException("ItemNode#setSubTypes(data) >> process subtypes >> duplicate index used for " + subType + " and " + subTypeHashMap.get(index));
                    }
                    subTypeHashMap.put(index, subType);

                    //TODO process extra data using JSON injection handler
                }
                else
                {
                    throw new IllegalArgumentException("ItemNode#setSubTypes(data) >> process subtypes >> sub type entries must be a JsonObject, " + element);
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("ItemNode#setSubTypes(data) requires that the input from JSON be a json array matching '\"subType\":[{values1}, {value2}]");
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

    public String getRenderContentID(int meta)
    {
        return id; //TODO add JSON option to return different render ID per sub type to allow overriding render settings (render pass count)
    }
}
