package resonant.api;

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerUsing
{
    public HashSet<EntityPlayer> getPlayersUsing();
}
