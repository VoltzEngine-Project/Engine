package com.builtbroken.mc.seven.abstraction;

import com.builtbroken.mc.api.abstraction.data.IItemData;
import com.builtbroken.mc.api.abstraction.data.ITileData;
import com.builtbroken.mc.api.abstraction.entity.IEntityData;
import com.builtbroken.mc.api.abstraction.imp.IMinecraftInterface;
import com.builtbroken.mc.api.abstraction.tile.ITileMaterial;
import com.builtbroken.mc.api.abstraction.world.IWorld;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.seven.abstraction.data.ItemData;
import com.builtbroken.mc.seven.abstraction.data.TileData;
import com.builtbroken.mc.seven.abstraction.entity.EntityData;
import com.builtbroken.mc.seven.abstraction.tile.TileMaterial;
import com.builtbroken.mc.seven.abstraction.world.WorldWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class MinecraftWrapper implements IMinecraftInterface
{
    //Data cache
    public HashMap<String, TileMaterial> nameToMaterial = new HashMap();
    public HashMap<String, ItemData> nameToItem = new HashMap();
    public HashMap<String, TileData> nameToTile = new HashMap();

    //Wrapper cache
    public HashMap<Integer, WorldWrapper> worldToWrapper = new HashMap();
    public HashMap<Material, TileMaterial> materialToWrapper = new HashMap();
    public HashMap<Entity, IEntityData> entityToWrapper = new HashMap();
    public HashMap<Item, ItemData> itemToWrapper = new HashMap();
    public HashMap<Block, TileData> blockToWrapper = new HashMap();

    public static MinecraftWrapper INSTANCE;

    @Override
    public IWorld getWorld(int dim)
    {
        WorldWrapper worldWrapper = worldToWrapper.get(dim);
        if (worldWrapper != null && worldWrapper.isValid())
        {
            return worldWrapper;
        }
        else
        {
            World world = DimensionManager.getWorld(dim);
            if (world != null)
            {
                worldWrapper = newWorldWrapper(world);
                worldToWrapper.put(dim, worldWrapper);
                return worldWrapper;
            }
        }
        return null;
    }

    protected WorldWrapper newWorldWrapper(World world)
    {
        return new WorldWrapper(world);
    }

    @Override
    public ITileMaterial getTileMaterial(String name)
    {
        if (nameToMaterial.isEmpty())
        {
            initMaterials();
        }
        return nameToMaterial.get(name);
    }

    @Override
    public IItemData getItemData(String key)
    {
        //Try cache
        if (nameToItem.containsKey(key))
        {
            return nameToItem.get(key);
        }

        //Get entry if not cached
        Object item = Item.REGISTRY.getObject(new ResourceLocation(key));
        if (item instanceof Item)
        {
            ItemData data = new ItemData((Item) item);
            nameToItem.put(key, data);
            itemToWrapper.put((Item) item, data);
            return data;
        }
        return null;
    }

    @Override
    public ITileData getTileData(String key)
    {
        return getTileData(new ResourceLocation(key));
    }

    public ITileData getTileData(ResourceLocation location)
    {
        //Try cache
        if (nameToTile.containsKey(location.toString()))
        {
            return nameToTile.get(location.toString());
        }

        //Get entry if not cached
        Object block = Block.REGISTRY.getObject(location);
        if (block instanceof Block && block != Blocks.AIR)
        {
            TileData data = new TileData((Block) block);
            nameToTile.put(location.toString(), data);
            blockToWrapper.put((Block) block, data);
            return data;
        }
        return null;
    }

    @Override
    public boolean isShiftHeld()
    {
        return false;
    }

    public ITileMaterial getTileMaterial(Material material)
    {
        if (materialToWrapper.containsKey(material))
        {
            return materialToWrapper.get(material);
        }

        //Self populate the cache to make an entry per material
        TileMaterial tileMaterial = new TileMaterial(material, material.getClass().getSimpleName() + "#" + material.hashCode());
        materialToWrapper.put(material, tileMaterial); //TODO when game closes dump material cache to save file (allows ID'ing new materials)
        return tileMaterial;
    }

    public void initMaterials()
    {
        addMaterial("air", Material.AIR);
        addMaterial("grass", Material.GRASS);
        addMaterial("ground", Material.GROUND);
        addMaterial("wood", Material.WOOD);
        addMaterial("rock", Material.ROCK);
        addMaterial("stone", Material.ROCK);
        addMaterial("iron", Material.IRON);
        addMaterial("metal", Material.IRON);
        addMaterial("anvil", Material.ANVIL);
        addMaterial("water", Material.WATER);
    }

    public void addMaterial(String name, Material material)
    {
        final String key = name.toLowerCase();
        if (nameToMaterial.containsKey(key))
        {
            Engine.logger().warn("MinecraftWrapper#addMaterial(" + name + ", " + material + ") is overriding previous entry of " + nameToMaterial.get(key));
        }
        nameToMaterial.put(key, new TileMaterial(material, key));
    }

    //===========================================
    //========= Wrapper creation ================
    //===========================================

    public IEntityData get(Entity entity)
    {
        if (entityToWrapper.containsKey(entity))
        {
            return entityToWrapper.get(entity); //TODO validate
        }
        EntityData data = new EntityData(entity);
        entityToWrapper.put(entity, data);
        return data;
    }

    public ITileData getTileData(Block block)
    {
        if (block != null)
        {
            if (blockToWrapper.containsKey(block))
            {
                return blockToWrapper.get(block);
            }
            return getTileData(Block.REGISTRY.getNameForObject(block));
        }
        return null;
    }

    public IItemData getItemData(Item item)
    {
        if (item != null)
        {
            if (itemToWrapper.containsKey(item))
            {
                return itemToWrapper.get(item);
            }
            return getItemData(Item.REGISTRY.getNameForObject(item).toString());
        }
        return null;
    }
}
