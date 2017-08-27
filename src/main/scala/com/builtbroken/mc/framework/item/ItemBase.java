package com.builtbroken.mc.framework.item;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.api.items.listeners.IItemActivationListener;
import com.builtbroken.mc.api.items.listeners.IItemEventListener;
import com.builtbroken.mc.api.items.listeners.IItemWithListeners;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.IJsonRenderStateProvider;
import com.builtbroken.mc.client.json.imp.IRenderState;
import com.builtbroken.mc.client.json.render.RenderData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketReceiver;
import com.builtbroken.mc.core.network.packet.PacketPlayerItem;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.json.IJsonGenMod;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;

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
public class ItemBase extends Item implements IJsonRenderStateProvider, IJsonGenObject, IItemWithListeners, IPacketReceiver
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
    public String getUnlocalizedName(ItemStack stack)
    {
        if (getHasSubtypes() && !node.subTypeHashMap.isEmpty())
        {
            if (node.subTypeHashMap.containsKey(stack.getItemDamage()))
            {
                return "item." + this.unlocalizedName + "." + node.subTypeHashMap.get(stack.getItemDamage()).unlocalizedName;
            }
            return "item." + this.unlocalizedName + "." + stack.getItemDamage(); //TODO maybe check if has translation
        }
        return "item." + this.unlocalizedName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List items)
    {
        boolean added = false;
        if (getHasSubtypes())
        {
            for (ItemNodeSubType type : node.subTypeHashMap.values())
            {
                items.add(new ItemStack(item, 1, type.index));
                added = true;
            }
        }

        //Backup for if getHashSubTypes is false or didn't add items
        if (!added)
        {
            items.add(new ItemStack(item, 1, 0));
        }
    }

    @Override
    public Item setUnlocalizedName(String name)
    {
        this.unlocalizedName = name;
        if (iconString == null)
        {
            setTextureName(name);
        }
        return this;
    }

    @Override
    public List<IItemEventListener> getItemListeners(String key)
    {
        return listeners.get(key);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        super.addInformation(stack, player, list, b);
        if (Engine.runningAsDev)
        {
            list.add("Node: " + node.getClass().getSimpleName());
            list.add("RenderID: " + getRenderContentID(stack));
            list.add("RenderS: " + getRenderKey(stack));
            list.add("RenderE: " + getRenderKey(stack, player, player.getItemInUseCount()));

            int[] ids = OreDictionary.getOreIDs(stack);
            boolean first = true;
            if (ids != null && ids.length > 0)
            {
                for (int id : ids)
                {
                    if (first)
                    {
                        first = false;
                        list.add("Ore: " + Colors.GREY.code + OreDictionary.getOreName(id));
                    }
                    else
                    {
                        list.add("     " + Colors.GREY.code + OreDictionary.getOreName(id));
                    }
                }
            }
        }
        //TODO add listener support
    }

    //=============================================
    //============== Render code ==================
    //=============================================

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
        final String contentID = getRenderContentID(stack);
        final RenderData data = ClientDataHandler.INSTANCE.getRenderData(contentID);
        if (data != null)
        {
            final String renderKey = getRenderKey(stack, player, useRemaining);
            if (renderKey != null)
            {
                IRenderState state = data.getState(RenderData.INVENTORY_RENDER_KEY + "." + renderKey);  //TODO add render pass & use remaining
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
        return getIcon(stack, renderPass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        //Attempt to render using stack -> content ID
        final String contentID = getRenderContentID(stack);
        final RenderData data = ClientDataHandler.INSTANCE.getRenderData(contentID);
        if (data != null)
        {
            //Build key set to attempt to get icon TODO cache to improve performance
            List<String> keys = new ArrayList();
            String recommendedKey = getRenderKey(stack);
            if (recommendedKey != null && !recommendedKey.isEmpty())
            {
                keys.add(RenderData.INVENTORY_RENDER_KEY + "." + recommendedKey + "." + pass);
                keys.add(RenderData.INVENTORY_RENDER_KEY + "." + recommendedKey);
                keys.add(recommendedKey);
            }
            keys.add(RenderData.INVENTORY_RENDER_KEY + "." + pass);
            keys.add(RenderData.INVENTORY_RENDER_KEY);

            //Loop through keys until we find a valid match
            for (String key : keys)
            {
                IRenderState state = data.getState(key);
                if (state != null)
                {
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
        }
        //If all else fails fall back to using metadata
        return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
    }

    /**
     * Called to get the render key for the stack
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
        if (getHasSubtypes() && node.subTypeHashMap.containsKey(stack.getItemDamage()))
        {
            return node.subTypeHashMap.get(stack.getItemDamage()).id;
        }
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
            //TODO cache list for faster runtime
            List<String> keys = new ArrayList();
            if (node.hasSubTypes() && node.subTypeHashMap.containsKey(meta))
            {
                keys.add(RenderData.INVENTORY_RENDER_KEY + "." + node.subTypeHashMap.get(meta).id + "." + pass);
                keys.add(RenderData.INVENTORY_RENDER_KEY + "." + node.subTypeHashMap.get(meta).id);
                keys.add(node.subTypeHashMap.get(meta).id);
            }
            keys.add(RenderData.INVENTORY_RENDER_KEY + "." + meta + "." + pass);
            keys.add(RenderData.INVENTORY_RENDER_KEY + "." + meta);
            keys.add(RenderData.INVENTORY_RENDER_KEY + "." + pass);
            keys.add(RenderData.INVENTORY_RENDER_KEY);

            //Attempt to get meta
            for (String key : keys)
            {
                IRenderState state = data.getState(key);
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
        if (objectBeingRendered instanceof ItemStack)
        {
            return getRenderContentID((ItemStack) objectBeingRendered);
        }
        else if (objectBeingRendered instanceof Item)
        {
            return getRenderContentID(new ItemStack((Item) objectBeingRendered));
        }
        else if (objectBeingRendered instanceof Block)
        {
            return getRenderContentID(new ItemStack((Block) objectBeingRendered));
        }
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

    //=============================================
    //============== Wrapper ====================
    //=============================================

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
        //TODO implement json hook, largest duration wins
        return 0;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int uses)
    {
        //TODO implement listener hook
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        try
        {
            if (stack != null)
            {
                ItemStack copy = stack.copy();
                ItemListenerIterator it = new ItemListenerIterator(this, "activation");
                while (it.hasNext())
                {
                    IItemEventListener next = it.next();
                    if (next instanceof IItemActivationListener)
                    {
                        copy = ((IItemActivationListener) next).onItemRightClick(copy, world, player);
                    }
                }
                return copy;
            }
        }
        catch (Exception e)
        {
            player.addChatComponentMessage(new ChatComponentText(Colors.RED.code + "Unexpected error using item, see logs for error details"));
            Engine.logger().error("ItemBase: Unexpected error triggering listeners during onItemRightClick(" + stack + ", " + player + ", " + world + ")");
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hit_x, float hit_y, float hit_z)
    {
        try
        {
            if (stack != null)
            {
                boolean clicked = false;
                ItemListenerIterator it = new ItemListenerIterator(this, "activation");
                while (it.hasNext())
                {
                    IItemEventListener next = it.next();
                    if (next instanceof IItemActivationListener)
                    {
                        if (((IItemActivationListener) next).onItemUse(stack, player, world, x, y, z, side, hit_x, hit_y, hit_z))
                        {
                            clicked = true;
                        }
                    }
                }
                return clicked;
            }
        }
        catch (Exception e)
        {
            player.addChatComponentMessage(new ChatComponentText(Colors.RED.code + "Unexpected error using item, see logs for error details"));
            Engine.logger().error("ItemBase: Unexpected error triggering listeners during onItemUse(" + stack + ", " + player + ", " + world + ", " + x + ", " + y + ", " + z + ", " + side, e);
        }
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hit_x, float hit_y, float hit_z)
    {
        try
        {
            if (stack != null)
            {
                boolean clicked = false;
                ItemListenerIterator it = new ItemListenerIterator(this, "activation");
                while (it.hasNext())
                {
                    IItemEventListener next = it.next();
                    if (next instanceof IItemActivationListener)
                    {
                        clicked = ((IItemActivationListener) next).onItemUseFirst(stack, player, world, x, y, z, side, hit_x, hit_y, hit_z);
                    }
                }
                return clicked;
            }
        }
        catch (Exception e)
        {
            player.addChatComponentMessage(new ChatComponentText(Colors.RED.code + "Unexpected error using item, see logs for error details"));
            Engine.logger().error("ItemBase: Unexpected error triggering listeners during onItemUseFirst(" + stack + ", " + player + ", " + world + ", " + x + ", " + y + ", " + z + ", " + side, e);
        }
        return false;
    }


    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        boolean clicked = false;
        ItemListenerIterator it = new ItemListenerIterator(this, "activation");
        while (it.hasNext())
        {
            IItemEventListener next = it.next();
            if (next instanceof IItemActivationListener)
            {
                if (((IItemActivationListener) next).doesSneakBypassUse(world, x, y, z, player))
                {
                    clicked = true;
                }
            }
        }
        return clicked;
    }

    //=============================================
    //============== JSON data ====================
    //=============================================

    @Override
    public void register(IJsonGenMod mod, ModManager manager)
    {
        if (!registered)
        {
            registered = true;
            manager.newItem(node.id, this);
            Engine.logger().info(this + " has been registered to " + mod);

            //Handle subtype ore names
            if (node.hasSubTypes())
            {
                for (ItemNodeSubType type : node.subTypeHashMap.values())
                {
                    if (type.oreName != null && !type.oreName.isEmpty())
                    {
                        OreDictionary.registerOre(type.oreName, new ItemStack(this, 1, type.index));
                    }
                }
            }
        }
    }

    @Override
    public String getLoader()
    {
        return "item";
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

    public String getRenderContentID(ItemStack stack)
    {
        return getRenderContentID(stack.getItemDamage());
    }

    public String getRenderContentID(int meta)
    {
        return getMod() + ":" + node.getRenderContentID(meta);
    }

    //=============================================
    //============== Network ====================
    //=============================================

    @Override
    public void read(ByteBuf buf, EntityPlayer player, PacketType packet)
    {
        if (packet instanceof PacketPlayerItem)
        {
            int slot = ((PacketPlayerItem) packet).slotId;
            ItemStack stack = player.inventory.getStackInSlot(slot);
            if (stack != null)
            {
                if (stack.getItem() == this)
                {
                    try
                    {
                        node.readPacketData(buf, player, stack);
                    }
                    catch (Exception e)
                    {
                        Engine.logger().error("ItemBase#read() >> Unexpected error while handling packet on stack[" + stack + "] item[" + stack.getItem() + "] from packet[" + packet + "]", e);
                    }
                }
                else if (Engine.runningAsDev)
                {
                    Engine.logger().error("ItemBase#read() >> stack in slot[" + slot + "] item is not an instance of " + this + " can not read packet.");
                }
            }
            else if (Engine.runningAsDev)
            {
                Engine.logger().error("ItemBase#read() >> stack in slot[" + slot + "] item is null preventing packet reading.");
            }
        }
    }

    public PacketPlayerItem getPacket(int slotID, Object... args)
    {
        return new PacketPlayerItem(slotID, args);
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
