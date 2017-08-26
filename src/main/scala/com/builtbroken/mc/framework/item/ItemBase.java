package com.builtbroken.mc.framework.item;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.api.items.listeners.IItemActivationListener;
import com.builtbroken.mc.api.items.listeners.IItemEventListener;
import com.builtbroken.mc.api.items.listeners.IItemWithListeners;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketReceiver;
import com.builtbroken.mc.core.network.packet.PacketPlayerItem;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.framework.json.IJsonGenMod;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Prefab used by JSON driven items
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/9/2017.
 */
public class ItemBase extends Item implements IJsonGenObject<Item>, IItemWithListeners, IPacketReceiver
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
    public List<IItemEventListener> getItemListeners(String key)
    {
        return listeners.get(key);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flagIn)
    {
        super.addInformation(stack, world, list, flagIn);
        if (Engine.runningAsDev)
        {
            list.add("Node: " + node.getClass().getSimpleName());
        }
    }

    //=============================================
    //============== Render code ==================
    //=============================================

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
    public EnumAction getItemUseAction(ItemStack stack)
    {
        //TODO implement json hook
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        //TODO implement json hook, largest duration wins
        return 0;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, onItemRightClick(playerIn.getHeldItem(handIn), worldIn, playerIn, handIn));
    }

    @Deprecated
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand handIn)
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
            player.sendMessage(new TextComponentString(Colors.RED.code + "Unexpected error using item, see logs for error details"));
            Engine.logger().error("ItemBase: Unexpected error triggering listeners during onItemRightClick(" + stack + ", " + player + ", " + world + ")");
        }
        return stack;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
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
                        if (((IItemActivationListener) next).onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ))
                        {
                            clicked = true;
                        }
                    }
                }
                return clicked ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            }
        }
        catch (Exception e)
        {
            player.sendMessage(new TextComponentString(Colors.RED.code + "Unexpected error using item, see logs for error details"));
            Engine.logger().error("ItemBase: Unexpected error triggering listeners during onItemUse(" + stack + ", " + player + ", " + world + ", " + pos + ", " + facing, e);
        }
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
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
                        clicked = ((IItemActivationListener) next).onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
                    }
                }
                return clicked ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            }
        }
        catch (Exception e)
        {
            player.sendMessage(new TextComponentString(Colors.RED.code + "Unexpected error using item, see logs for error details"));
            Engine.logger().error("ItemBase: Unexpected error triggering listeners during onItemUseFirst(" + stack + ", " + player + ", " + world + ", " + pos + ", " + side, e);
        }
        return EnumActionResult.PASS;
    }


    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        boolean clicked = false;
        ItemListenerIterator it = new ItemListenerIterator(this, "activation");
        while (it.hasNext())
        {
            IItemEventListener next = it.next();
            if (next instanceof IItemActivationListener)
            {
                if (((IItemActivationListener) next).doesSneakBypassUse(stack, world, pos, player))
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
    public void register(IJsonGenMod mod, RegistryEvent.Register<Item> reg)
    {
        if (!registered)
        {
            registered = true;
            this.setRegistryName(mod.getDomain() + ":" + node.id);
            reg.getRegistry().register(this);
            Engine.logger().info(this + " has been claimed by " + mod);
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
        return getMod() + ":" + node.id;
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
