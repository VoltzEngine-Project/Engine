package com.builtbroken.mc.seven.abstraction;

import com.builtbroken.mc.abstraction.entity.IEntityData;
import com.builtbroken.mc.abstraction.imp.IMinecraftInterface;
import com.builtbroken.mc.abstraction.tile.ITileMaterial;
import com.builtbroken.mc.abstraction.world.IWorld;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.seven.abstraction.entity.EntityData;
import com.builtbroken.mc.seven.abstraction.tile.TileMaterial;
import com.builtbroken.mc.seven.abstraction.world.WorldWrapper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
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

    //Wrapper cache
    public HashMap<Integer, WorldWrapper> worldToWrapper = new HashMap();
    public HashMap<Material, TileMaterial> materialToWrapper = new HashMap();
    public HashMap<Entity, IEntityData> entityToWrapper = new HashMap();

    public static final MinecraftWrapper INSTANCE = new MinecraftWrapper();

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
                worldWrapper = new WorldWrapper(world);
                worldToWrapper.put(dim, worldWrapper);
                return worldWrapper;
            }
        }
        return null;
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
        addMaterial("air", Material.air);
        addMaterial("grass", Material.grass);
        addMaterial("ground", Material.ground);
        addMaterial("wood", Material.wood);
        addMaterial("rock", Material.rock);
        addMaterial("stone", Material.rock);
        addMaterial("iron", Material.iron);
        addMaterial("metal", Material.iron);
        addMaterial("anvil", Material.anvil);
        addMaterial("water", Material.water);
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
}
