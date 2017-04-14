package com.builtbroken.mc.lib.json.processors.item;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.item.ItemBase;
import com.builtbroken.mc.lib.json.IJsonGenMod;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.item.processor.JsonItemProcessor;

/**
 * Container/Wrapper for data representing an item
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class ItemJson extends ItemBase implements IJsonGenObject
{
    /** ID used to register this item to the game */
    public final String ID;
    /** Was the content registered */
    protected boolean registered = false;

    /**
     * @param id   - unique id for the item to be registered
     * @param name - used to localize the item
     */
    public ItemJson(String id, String owner, String name)
    {
        super(owner, name);
        this.ID = id;
    }

    @Override
    public void register(IJsonGenMod mod, ModManager manager)
    {
        if (!registered)
        {
            registered = true;
            manager.newItem(ID, this);
            Engine.logger().info(this + " has been claimed by " + mod);
        }
    }

    @Override
    public String getLoader()
    {
        return JsonItemProcessor.KEY;
    }

    @Override
    public String getMod()
    {
        return owner;
    }

    @Override
    public String getContentID()
    {
        return ID;
    }

    @Override
    public String getRenderContentID(int meta)
    {
        return "item." + owner + ":" + ID;
    }
}
