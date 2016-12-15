package com.builtbroken.mc.core.network.packet.callback;

import com.builtbroken.mc.api.event.TriggerCauseRegistry;
import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.lib.world.edit.BlockEdit;
import com.builtbroken.mc.lib.world.explosive.ExplosiveRegistry;
import com.builtbroken.mc.prefab.explosive.blast.Blast;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2016.
 */
public class PacketBlast extends AbstractPacket
{
    public Blast blast;
    public BlastPacketType type;
    public IExplosiveHandler handler;
    public BlockEdit edit;

    //Internal data only used to build the blast
    private double x, y, z, size;

    public PacketBlast()
    {
        //Needed for forge to construct the packet
    }

    /**
     * @param blast - used to sync data from
     */
    public PacketBlast(Blast blast, BlastPacketType type)
    {
        this.blast = blast;
        this.handler = blast.explosiveHandler;
        this.type = type;
    }

    public PacketBlast(Blast blast, BlockEdit edit)
    {
        this(blast, BlastPacketType.EDIT_DISPLAY);
        this.edit = edit;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(type.ordinal());
        buffer.writeDouble(blast.x());
        buffer.writeDouble(blast.y());
        buffer.writeDouble(blast.z());
        buffer.writeDouble(blast.size);
        ByteBufUtils.writeUTF8String(buffer, handler.getID());

        NBTTagCompound tag = new NBTTagCompound();
        if (blast.cause != null)
        {
            tag.setTag("trigger", TriggerCauseRegistry.cache(blast.cause));
        }
        if (blast.getAdditionBlastData() != null)
        {
            tag.setTag("explosiveData", blast.getAdditionBlastData());
        }
        if (type == BlastPacketType.EDIT_DISPLAY)
        {
            tag.setTag("edit", edit.toNBT());
        }
        ByteBufUtils.writeTag(buffer, tag);

        blast.writeBytes(buffer);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        type = BlastPacketType.values()[buffer.readInt()];
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        size = buffer.readDouble();

        String id = ByteBufUtils.readUTF8String(buffer);
        handler = ExplosiveRegistry.get(id);

        if (handler == null)
        {
            Engine.logger().error("Failed to load handler[" + id + "] from packet data");
        }

        NBTTagCompound save = ByteBufUtils.readTag(buffer);
        blast = (Blast) handler.createBlastForTrigger(Minecraft.getMinecraft().theWorld, x, y, z, TriggerCauseRegistry.rebuild(save.getCompoundTag("trigger"), Minecraft.getMinecraft().theWorld), size, save.getCompoundTag("explosiveData"));

        if (type == BlastPacketType.EDIT_DISPLAY)
        {
            edit = new BlockEdit(save.getCompoundTag("edit"));
        }

        blast.readBytes(buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        if (blast != null)
        {
            switch (type)
            {
                case PRE_BLAST_DISPLAY:
                    blast.doStartDisplay();
                    break;
                case POST_BLAST_DISPLAY:
                    blast.doEndDisplay();
                    break;
                case EDIT_DISPLAY:
                    blast.displayEffectForEdit(edit);
                    break;
            }
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        //Should be no server sync logic
    }

    public enum BlastPacketType
    {
        PRE_BLAST_DISPLAY,
        POST_BLAST_DISPLAY,
        EDIT_DISPLAY
    }
}
