package resonant.content.prefab.java;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.engine.References;
import resonant.lib.network.discriminator.PacketNode;
import resonant.lib.network.discriminator.PacketType;
import resonant.lib.network.handle.IPacketIDReceiver;
import universalelectricity.api.core.grid.INode;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.api.core.grid.ISave;
import universalelectricity.api.core.grid.IUpdate;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Prefab designed to automate all node interaction of the time.
 * Does use some reflection to generate a list of all fields holding nodes
 *
 * TODO if timing for reflection proves to take to long replace with getAllNodes() method
 * @author Darkguardsman
 */
public class TileNode extends TileAdvanced implements INodeProvider, IPacketIDReceiver
{
    INode baseNode = null;

    private static final Map<Class<? extends TileNode>, List<Field>> nodeFields = new LinkedHashMap<>();

    public TileNode(Material material)
    {
        super(material);
        generateNodeList();
    }

    @Override
    public void onAdded()
    {
        super.onAdded();
        if(nodeFields.containsKey(getClass()))
        {
            List<Field> fields = nodeFields.get(getClass());
            for(Field field : fields)
            {
                INode node = null;
                try
                {
                    Object object = field.get(this);
                    if(object instanceof INode)
                    {
                        node = (INode) object;
                        node.reconstruct();
                    }
                }
                catch (Exception e)
                {
                    System.out.println("\n=========================================================================================");
                    System.out.println("[" +References.NAME +"] A node contained in a tile has thrown an exception during build");
                    System.out.println("[" +References.NAME +"] Tile: " + this);
                    System.out.println("[" +References.NAME +"] Node: " + node);
                    e.printStackTrace();
                    System.out.println("=========================================================================================\n");
                }
            }
        }
    }

    @Override
    public void update()
    {
        super.update();
        if(nodeFields.containsKey(getClass()))
        {
            List<Field> fields = nodeFields.get(getClass());
            for(Field field : fields)
            {
                INode node = null;
                try
                {
                    Object object = field.get(this);
                    if(object instanceof INode)
                    {
                        node = (INode) object;
                        if(node instanceof IUpdate)
                        {
                            if(((IUpdate) node).canUpdate()) {
                                ((IUpdate) node).update(20);
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("\n=========================================================================================");
                    System.out.println("[" +References.NAME +"] A node contained in a tile has thrown an exception during update");
                    System.out.println("[" +References.NAME +"] Tile: " + this);
                    System.out.println("[" +References.NAME +"] Node: " + node);
                    e.printStackTrace();
                    System.out.println("=========================================================================================\n");
                    if(node != null)
                    {
                        //TODO maybe create new instance of node
                        node.reconstruct();
                    }
                }
            }
        }
    }

    @Override
    public void invalidate()
    {
        if(nodeFields.containsKey(getClass()))
        {
            List<Field> fields = nodeFields.get(getClass());
            for(Field field : fields)
            {
                INode node = null;
                try
                {
                    Object object = field.get(this);
                    if(object instanceof INode)
                    {
                        node = (INode) object;
                        node.deconstruct();
                    }
                    field.set(this, null);
                }
                catch (Exception e)
                {
                    System.out.println("\n=========================================================================================");
                    System.out.println("[" +References.NAME +"] A node contained in a tile has thrown an exception invalidate process");
                    System.out.println("[" +References.NAME +"] Tile: " + this);
                    System.out.println("[" +References.NAME +"] Node: " + node);
                    e.printStackTrace();
                    System.out.println("=========================================================================================\n");
                }
            }
        }
        super.invalidate();
    }

    @Override
    public INode getNode(Class<? extends INode> nodeType, ForgeDirection from)
    {
        return baseNode;
    }

    //TODO improve and turn into a helper class
    public void generateNodeList()
    {
        if(!nodeFields.containsKey(getClass()))
        {
            Class clazz = getClass();
            List<Field> fields = new LinkedList<Field>();
            for(Field field : clazz.getFields())
            {
                if(!fields.contains(field) && field.getType().isAssignableFrom(INode.class))
                {
                    fields.add(field);
                }
            }
            for(Field field : clazz.getDeclaredFields())
            {
                if(!fields.contains(field) && field.getType().isAssignableFrom(INode.class))
                {
                    fields.add(field);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
       super.readFromNBT(nbt);
        if(nodeFields.containsKey(getClass()))
        {
            List<Field> fields = nodeFields.get(getClass());
            for(Field field : fields)
            {
                INode node = null;
                try
                {
                    Object object = field.get(this);
                    if(object instanceof INode)
                    {
                        node = (INode) object;
                        if(node instanceof ISave)
                        {
                            ((ISave) node).load(nbt);
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("\n=========================================================================================");
                    System.out.println("[" +References.NAME +"] A node contained in a tile has thrown an exception while loading");
                    System.out.println("[" +References.NAME +"] Tile: " + this);
                    System.out.println("[" +References.NAME +"] Node: " + node);
                    e.printStackTrace();
                    System.out.println("=========================================================================================\n");
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if(nodeFields.containsKey(getClass()))
        {
            List<Field> fields = nodeFields.get(getClass());
            for(Field field : fields)
            {
                INode node = null;
                try
                {
                    Object object = field.get(this);
                    if(object instanceof INode)
                    {
                        node = (INode) object;
                        if(node instanceof ISave)
                        {
                            ((ISave) node).save(nbt);
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("\n=========================================================================================");
                    System.out.println("[" +References.NAME +"] A node contained in a tile has thrown an exception while saving");
                    System.out.println("[" +References.NAME +"] Tile: " + this);
                    System.out.println("[" +References.NAME +"] Node: " + node);
                    e.printStackTrace();
                    System.out.println("=========================================================================================\n");
                }
            }
        }
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if(type instanceof PacketNode && nodeFields.containsKey(getClass()))
        {
            List<Field> fields = nodeFields.get(getClass());
            for(Field field : fields)
            {
                INode node = null;
                try
                {
                    Object object = field.get(this);
                    if(object instanceof INode && (((PacketNode)type).nodeClassName.equalsIgnoreCase("INode") || object.getClass().getSimpleName().equalsIgnoreCase(((PacketNode)type).nodeClassName)))
                    {
                        node = (INode) object;
                        if(node instanceof IPacketIDReceiver)
                        {
                            if(((IPacketIDReceiver) node).read(buf, id, player, type))
                            {
                                return true;
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("\n=========================================================================================");
                    System.out.println("[" +References.NAME +"] A node contained in a tile has thrown an exception while saving");
                    System.out.println("[" +References.NAME +"] Tile: " + this);
                    System.out.println("[" +References.NAME +"] Node: " + node);
                    e.printStackTrace();
                    System.out.println("=========================================================================================\n");
                }
            }
        }
        return false;
    }
}
