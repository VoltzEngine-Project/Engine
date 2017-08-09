package com.builtbroken.mc.lib.data.mass;

import com.builtbroken.mc.api.IHasMass;
import com.builtbroken.mc.api.IMassRegistry;
import com.builtbroken.mc.api.items.IItemHasMass;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.data.item.ItemStackWrapper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/28/2016.
 */
public class MassRegistry implements IMassRegistry
{
    private HashMap<Class<? extends Entity>, Double> entityMass = new HashMap();
    private HashMap<Item, Double> itemMass = new HashMap();
    private HashMap<Block, Double> blockMass = new HashMap();
    private HashMap<ItemStackWrapper, Double> stackMass = new HashMap();

    @Override
    public void setMass(Class<? extends Entity> entity, double mass)
    {
        if (entity != null)
        {
            if (mass < 0)
            {
                Engine.logger().error("Mass is negative, setting to positive", new RuntimeException());
            }
            entityMass.put(entity, Math.abs(mass));
        }
        else
        {
            Engine.logger().error("Class can not be null", new RuntimeException());
        }
    }

    @Override
    public void setMass(Item item, double mass)
    {
        if (item != null)
        {
            if (mass < 0)
            {
                Engine.logger().error("Mass is negative, setting to positive", new RuntimeException());
            }
            itemMass.put(item, Math.abs(mass));
        }
        else
        {
            Engine.logger().error("Item can not be null", new RuntimeException());
        }
    }

    @Override
    public void setMass(Item item, int meta, double mass)
    {
        if (item != null)
        {
            setMass(new ItemStack(item, 1, meta), mass);
        }
        else
        {
            Engine.logger().error("Item can not be null", new RuntimeException());
        }
    }

    @Override
    public void setMass(Block block, double mass)
    {
        if (block != null)
        {
            blockMass.put(block, Math.abs(mass));
        }
        else
        {
            Engine.logger().error("Item can not be null", new RuntimeException());
        }
    }

    @Override
    public void setMass(Block block, int meta, double mass)
    {
        if (block != null)
        {
            setMass(new ItemStack(block, 1, meta), mass);
        }
        else
        {
            Engine.logger().error("Item can not be null", new RuntimeException());
        }
    }

    @Override
    public void setMass(ItemStack stack, double mass)
    {
        if (stack != null)
        {
            ItemStackWrapper wrapper = new ItemStackWrapper(stack);
            if (mass < 0)
            {
                Engine.logger().error("Mass is negative, setting to positive", new RuntimeException());
            }
            stackMass.put(wrapper, Math.abs(mass));
        }
        else
        {
            Engine.logger().error("Item can not be null", new RuntimeException());
        }
    }

    @Override
    public double getMass(World world, int x, int y, int z)
    {
        double mass;

        if (world != null)
        {
            //Checks if the chunk is loaded
            if (world instanceof WorldServer)
            {
                ChunkProviderServer providerServer = ((WorldServer) world).theChunkProviderServer;
                if (!providerServer.chunkExists(x >> 4, z >> 4))
                {
                    return -1;
                }
            }
            Block block = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile instanceof IHasMass)
            {
                mass = ((IHasMass) tile).getMass();
                if (mass >= 0)
                {
                    return mass;
                }
            }

            mass = getMass(block, meta);
            return mass >= 0 ? mass : getMass(block);
        }
        return -1;
    }

    @Override
    public double getMass(Entity entity)
    {
        double mass;
        if (entity instanceof IHasMass)
        {
            mass = ((IHasMass) entity).getMass();
            if (mass >= 0)
            {
                return mass;
            }
        }
        Class<? extends Entity> clazz = entity.getClass();
        if(entityMass.containsKey(clazz))
        {
            return entityMass.get(clazz);
        }
        for(Class<? extends Entity> cl : entityMass.keySet())
        {
            //TODO look for lowest level class
            //TODO unit test
            if(clazz.isAssignableFrom(cl))
            {
                return entityMass.get(cl);
            }
        }
        return -1;
    }

    @Override
    public double getMass(ItemStack stack)
    {
        if(stack != null)
        {
            double mass;
            Item item = stack.getItem();
            if(item instanceof IItemHasMass)
            {
                mass = ((IItemHasMass) item).getMass(stack);
                if(mass >= 0)
                {
                    return mass;
                }
            }
            ItemStackWrapper wrapper = new ItemStackWrapper(stack);
            mass = stackMass.get(wrapper);
            return mass >= 0 ? mass : getMass(item);
        }
        return -1;
    }

    @Override
    public double getMass(Block block)
    {
        if(block != null)
        {
            return blockMass.get(block);
        }
        return -1;
    }

    @Override
    public double getMass(Block block, int meta)
    {
        if(block != null)
        {
            return getMass(new ItemStack(block, 1, meta));
        }
        return -1;
    }

    @Override
    public double getMass(Item item)
    {
        if(item != null)
        {
            return blockMass.get(item);
        }
        return -1;
    }

    @Override
    public double getMass(Item item, int meta)
    {
        if(item != null)
        {
            return getMass(new ItemStack(item, 1, meta));
        }
        return -1;
    }
}
