package com.builtbroken.mc.framework.item;

import com.builtbroken.mc.api.items.listeners.IItemEventListener;
import com.builtbroken.mc.api.items.listeners.IItemWithListeners;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.item.logic.ItemNode;
import com.builtbroken.mc.lib.json.IJsonGenMod;
import com.builtbroken.mc.lib.json.imp.IJsonGenObject;
import com.builtbroken.mc.lib.json.processors.item.processor.JsonItemProcessor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.ChestGenHooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Prefab used by JSON driven items
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class ItemBase extends Item implements IJsonRenderStateProvider, IJsonGenObject, IItemWithListeners
{
    /** Handles item properties and main logic */
    public final ItemNode node;

    /** Event listeners for this item */
    public final HashMap<String, List<IItemEventListener>> listeners = new HashMap();

    /** Was the content registered */
    protected boolean registered = false;

    /**
     * @param node - logic controller and property call back for the item
     */
    public ItemBase(ItemNode node)
    {
        this.node = node;
        this.node.item = this;
    }

    /**
     * @param id    - unique ID that the item was register with
     * @param owner - domain of the mod that owners the content
     * @param name  - unlocalized name to use for translations
     */
    public ItemBase(String id, String owner, String name)
    {
        this(new ItemNode(owner, id));
        node.setUnlocalizedName(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        super.addInformation(stack, player, list, b);
        if (Engine.runningAsDev)
        {
            list.add("RenderID: " + getRenderContentID(stack.getItemDamage()));
            list.add("RenderS: " + getRenderKey(stack));
            list.add("RenderE: " + getRenderKey(stack, player, player.getItemInUseCount()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        super.registerIcons(reg);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        return getIconFromState(ClientDataHandler.INSTANCE.getRenderData(getRenderContentID(meta)), meta, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true; //Fix for getting ItemStack calls
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(int metadata)
    {
        RenderData data = ClientDataHandler.INSTANCE.getRenderData(getRenderContentID(metadata));
        if (data != null)
        {
            return data.itemRenderLayers;
        }
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass)
    {
        return getIconFromState(ClientDataHandler.INSTANCE.getRenderData(getRenderContentID(meta)), meta, pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        String gunState = getRenderKey(stack, player, useRemaining);
        if (gunState != null)
        {
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(getRenderContentID(stack.getItemDamage()));
            if (data != null)
            {
                IRenderState state = data.getState(RenderData.INVENTORY_RENDER_KEY + "." + gunState);
                if (state != null)
                {
                    IIcon icon = state.getIcon(renderPass);
                    if (icon != null)
                    {
                        return icon;
                    }
                }
            }
        }
        return getIconFromDamageForRenderPass(stack.getItemDamage(), renderPass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        String renderKey = getRenderKey(stack);
        if (renderKey != null)
        {
            RenderData data = ClientDataHandler.INSTANCE.getRenderData(getRenderContentID(stack.getItemDamage()));
            if (data != null)
            {
                IRenderState state = data.getState(RenderData.INVENTORY_RENDER_KEY + "." + renderKey);
                if (state != null)
                {
                    IIcon icon = state.getIcon(pass);
                    if (icon != null)
                    {
                        return icon;
                    }
                }
            }
        }
        return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
    }

    /**
     * Called to ge the render key for the stack
     * <p>
     * Keep in mind key is prefixed by render type
     * <p>
     * Ex: item.inventory.key
     * item.entity.key
     *
     * @param stack - stack being rendered
     * @return key for the render ID
     */
    public String getRenderKey(ItemStack stack)
    {
        return null;
    }

    /**
     * Called to ge the render key for the stack
     * <p>
     * Keep in mind key is prefixed by render type
     * <p>
     * Ex: item.inventory.key
     * item.entity.key
     *
     * @param stack        - stack being rendered
     * @param entity       - entity holding the item
     * @param useRemaining - how many uses are left
     * @return key for the render ID
     */
    public String getRenderKey(ItemStack stack, Entity entity, int useRemaining)
    {
        return null;
    }

    /**
     * Called to get the icon from the state
     *
     * @param data - render data
     * @param meta - metadata or damage of the item
     * @param pass - render pass, 0 by default
     * @return icon, can not be null or will crash
     */
    protected IIcon getIconFromState(RenderData data, int meta, int pass)
    {
        if (data != null)
        {
            //Attempt to get meta
            IRenderState state = data.getState(RenderData.INVENTORY_RENDER_KEY + "." + meta);
            if (state != null)
            {
                state = data.getState(RenderData.INVENTORY_RENDER_KEY);
                if (state != null)
                {
                    IIcon icon = state.getIcon(pass);
                    if (icon != null)
                    {
                        return icon;
                    }
                }
            }
            //Attempt to do non-meta
            state = data.getState(RenderData.INVENTORY_RENDER_KEY);
            if (state != null)
            {
                IIcon icon = state.getIcon(0);
                if (icon != null)
                {
                    return icon;
                }
            }
        }
        return getFallBackIcon();
    }

    /**
     * Called to get a fallback icon to display
     * when an icon can not be retrieved from
     * the json render system
     *
     * @return icon, can not be null
     */
    protected IIcon getFallBackIcon()
    {
        return itemIcon != null ? itemIcon : Items.stick.getIconFromDamage(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getRenderContentID(IItemRenderer.ItemRenderType renderType, Object objectBeingRendered)
    {
        return getRenderContentID(0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public List<String> getRenderContentIDs()
    {
        List<String> list = new ArrayList();
        list.add(getRenderContentID(0));
        return list;
    }

    @Override
    public WeightedRandomChestContent getChestGenBase(ChestGenHooks chest, Random rnd, WeightedRandomChestContent original)
    {
        //TODO implement json hook
        return original;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        //TODO implement json hook
        return EnumAction.none;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        //TODO implement json hook
        return 0;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int uses)
    {
        //TODO implement listener hook
    }

    @Override
    public void register(IJsonGenMod mod, ModManager manager)
    {
        if (!registered)
        {
            registered = true;
            manager.newItem(node.id, this);
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
        return node.owner;
    }

    @Override
    public String getContentID()
    {
        return node.id;
    }

    public String getRenderContentID(int meta)
    {
        return "item." + getMod() + ":" + node.id;
    }

    @Override
    public List<IItemEventListener> getItemListeners(String key)
    {
        return listeners.get(key);
    }


    //=============================================
    //============== to string ====================
    //=============================================

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getClassDisplayName());
        builder.append("(");
        toStringData(builder);
        builder.append(")@");
        builder.append(hashCode());
        return builder.toString();
    }

    /**
     * Gets the debug display name of the class.
     * Normally this is just the class name
     * but is designed to be overridden in
     * the code generator for simplicity
     *
     * @return name
     */
    protected String getClassDisplayName()
    {
        return getClass().getSimpleName();
    }

    /**
     * Called to build data about the class.
     * By default this outputs if the
     * tile is on the server or client,
     * the world,
     * the position,
     * and the tile being hosted
     *
     * @param builder - builder to append data to
     */
    protected void toStringData(StringBuilder builder)
    {

    }
}
