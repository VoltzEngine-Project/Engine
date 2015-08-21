package com.builtbroken.mc.lib.modflags;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.lib.access.AccessUser;
import com.builtbroken.mc.lib.helper.NBTUtility;
import com.builtbroken.mc.lib.modflags.events.PlayerRegionEvent;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Controls all protected regions for a single world
 * Created by robert on 2/16/2015.
 */
public class RegionController implements IVirtualObject
{
    public final int dim;
    protected HashMap<String, Region> regions = new HashMap(); //TODO break into segments in case we get a lot of regions per world

    static
    {
        SaveManager.registerClass("regionController", RegionController.class);
    }

    public RegionController(int dim)
    {
        this.dim = dim;
        load(NBTUtility.loadData(getSaveFile()));
        SaveManager.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public Region getRegion(String name)
    {
        if(regions.containsKey(name))
            return regions.get(name);
        return null;
    }

    public List<Region> getRegionsAtLocation(IPos3D pos)
    {
        List<Region> list = new ArrayList();
        for(Region region: regions.values())
        {
            if(region.doesContainPoint(pos))
            {
                list.add(region);
            }
        }
        return list;
    }

    public List<Region> getRegionsNear(EntityPlayerMP player, int distance)
    {
        List<Region> regions = new ArrayList();
        Location location = new Location(player);

        for(Region region : this.regions.values())
        {
            if(region.isCloseToAnyCorner(location, distance))
            {
                regions.add(region);
            }
        }
        return  regions;
    }

    public boolean removeRegion(Region region)
    {
        return regions.containsKey(region.name) && regions.remove(region.name) != null;
    }

    public Region createNewRegion(String name, Cube cube)
    {
        if(getRegion(name) == null && cube.isValid())
        {
            Region region = new Region(name);
            region.segments.add(cube);
            regions.put(name, region);
            return region;
        }
        return null;
    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "ve/modflags/region_dim_" + dim +".dat");
    }

    @Override
    public void setSaveFile(File file)
    {

    }

    @Override
    public boolean shouldSaveForWorld(World world)
    {
        return world != null && world.provider.dimensionId == dim;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        if(nbt.hasKey("regions"))
        {
            regions.clear();
            NBTTagList list = nbt.getTagList("regions", 10);
            for(int i = 0; i < list.tagCount(); i++)
            {
                String name = list.getCompoundTagAt(i).getString("region_name");
                Region region = new Region(name);
                region.load(list.getCompoundTagAt(i));
                regions.put(name, region);
            }
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        if(!regions.isEmpty())
        {
            NBTTagList list = new NBTTagList();
            for(Map.Entry<String, Region> entry : regions.entrySet())
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("region_name", entry.getKey());
                list.appendTag(entry.getValue().save(tag));
            }
            nbt.setTag("regions", list);
        }
        return nbt;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(!event.world.isRemote && event.world.provider.dimensionId == dim)
        {
            EntityPlayer player = event.entityPlayer;
            List<Region> regionList = getRegionsAtLocation(new Pos(event.x, event.y, event.z));
            if(regionList != null && regionList.size() > 0)
            {
                for(Region region : regionList)
                {
                    AccessUser user = region.getAccessProfile().getUserAccess(player);
                    if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
                    {
                        if(!user.hasNode(RegionManager.leftClick.toString()))
                        {
                            event.setCanceled(true);
                            player.addChatComponentMessage(new ChatComponentText("You do not have the permission to do that here!"));
                        }
                    }
                    else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
                    {
                        if(!user.hasNode(RegionManager.rightClick.toString()))
                        {
                            event.setCanceled(true);
                            player.addChatComponentMessage(new ChatComponentText("You do not have the permission to do that here!"));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END && !event.world.isRemote && event.world.provider.dimensionId == dim)
        {
            long ticks = event.world.getWorldInfo().getWorldTime();
            List<Event> events = new ArrayList();

            if(ticks % 3 == 0)
            {
                for(Object object : event.world.playerEntities)
                {
                    if(object instanceof EntityPlayer)
                    {
                        EntityPlayer player = (EntityPlayer) object;
                        Location location = new Location(player);
                        for(Region region : regions.values())
                        {
                            if(region.doesContainPoint(location) && !region.players_in_region.contains(player))
                            {
                                events.add(new PlayerRegionEvent.PlayerEnterRegionEvent(player, region));
                            }
                            else if(!region.doesContainPoint(location) && region.players_in_region.contains(player))
                            {
                                events.add(new PlayerRegionEvent.PlayerExitRegionEvent(player, region));
                            }
                        }
                    }
                }
            }

            for(Event e : events)
            {
                MinecraftForge.EVENT_BUS.post(e);
            }
        }
    }
}
