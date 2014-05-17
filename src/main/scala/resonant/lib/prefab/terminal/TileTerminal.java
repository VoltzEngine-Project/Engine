package resonant.lib.prefab.terminal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import resonant.api.IPlayerUsing;
import resonant.api.IScroll;
import resonant.api.ITerminal;
import resonant.lib.network.IPacketReceiverWithID;
import resonant.lib.network.PacketHandler;
import resonant.lib.prefab.tile.TileAdvanced;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/** @author Calclavia, DarkGuardsman */
public abstract class TileTerminal extends TileAdvanced implements ITerminal, IScroll, IPacketReceiverWithID, IPlayerUsing
{
    public final HashSet<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();

    /** A list of everything typed inside the terminal */
    private final List<String> terminalOutput = new ArrayList<String>();

    /** The amount of lines the terminal can store. */
    public static final int SCROLL_SIZE = 15;

    /** Used on client side to determine the scroll of the terminal. */
    private int scroll = 0;

    /** Packet Code */
    public abstract Packet getTerminalPacket();

    public abstract Packet getCommandPacket(String username, String cmdInput);

    protected static final int COMMAND_PACKET_ID = 0;
    protected static final int TERMINAL_PACKET_ID = 1;
    protected static final int NBT_PACKET_ID = 2;

    /** Sends all Terminal data Server -> Client */
    public void sendTerminalOutputToClients()
    {
        Packet packet = getTerminalPacket();

        for (EntityPlayer player : this.getPlayersUsing())
        {
            PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
        }
    }

    /** Send a terminal command Client -> server */
    public void sendCommandToServer(EntityPlayer entityPlayer, String cmdInput)
    {
        PacketDispatcher.sendPacketToServer(getCommandPacket(entityPlayer.username, cmdInput));
    }

    /** Retrieves the data needed to generate a packet for the data type. Does not encode the type as
     * this is done with PacketTile.getPacketWithID(). Command packet must be manually generated as
     * it needs the username and command */
    public ArrayList getPacketData(int type)
    {
        ArrayList data = new ArrayList();
        switch (type)
        {
            case NBT_PACKET_ID:
            {
                // Server: Description
                NBTTagCompound nbt = new NBTTagCompound();
                this.writeToNBT(nbt);
                data.add(nbt);
                break;
            }
            case TERMINAL_PACKET_ID:
            {
                // Server: Terminal Packet
                data.add(this.getTerminalOuput().size());
                data.addAll(this.getTerminalOuput());
                break;
            }
        }

        return data;
    }

    @Override
    public boolean onReceivePacket(int id, ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        try
        {
            switch (id)
            {
                case NBT_PACKET_ID:
                {
                    this.readFromNBT(PacketHandler.readNBTTagCompound(data));
                    return true;
                }
                case TERMINAL_PACKET_ID:
                {
                    // Server: Description
                    int size = data.readInt();

                    List<String> oldTerminalOutput = new ArrayList(this.terminalOutput);
                    this.terminalOutput.clear();

                    for (int i = 0; i < size; i++)
                    {
                        this.terminalOutput.add(data.readUTF());
                    }

                    if (!this.terminalOutput.equals(oldTerminalOutput) && this.terminalOutput.size() != oldTerminalOutput.size())
                    {
                        this.setScroll(this.getTerminalOuput().size() - SCROLL_SIZE);
                    }
                    return true;
                }
                case COMMAND_PACKET_ID:
                {
                    // Client: Command Packet
                    CommandRegistry.onCommand(this.worldObj.getPlayerEntityByName(data.readUTF()), this, data.readUTF());
                    this.sendTerminalOutputToClients();
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("[CalclaviaCore]Terminal-Prefab: Failed to read packet");
            e.printStackTrace();
            return true;
        }
        return false;
    }

    @Override
    public List<String> getTerminalOuput()
    {
        return this.terminalOutput;
    }

    @Override
    public boolean addToConsole(String msg)
    {
        if (!this.worldObj.isRemote)
        {
            int usedLines = 0;

            msg.trim();
            if (msg.length() > 23)
            {
                msg = msg.substring(0, 22);
            }

            this.getTerminalOuput().add(msg);
            this.sendTerminalOutputToClients();
            return true;
        }

        return false;
    }

    @Override
    public void addToConsole(List<String> output_list)
    {
        if (output_list != null && !output_list.isEmpty())
        {
            for (String string : output_list)
            {
                this.addToConsole(string);
            }
        }
    }

    @Override
    public void scroll(int amount)
    {
        this.setScroll(this.scroll + amount);
    }

    @Override
    public void setScroll(int length)
    {
        this.scroll = Math.max(Math.min(length, this.getTerminalOuput().size()), 0);
    }

    @Override
    public int getScroll()
    {
        return this.scroll;
    }

    @Override
    public boolean canUse(String node, EntityPlayer player)
    {
        return true;
    }

    @Override
    public HashSet<EntityPlayer> getPlayersUsing()
    {
        return this.playersUsing;
    }

}
