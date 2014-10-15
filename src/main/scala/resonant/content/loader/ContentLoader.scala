package resonant.content.loader

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.Item
import resonant.content.spatial.block.SpatialBlock
import resonant.lib.wrapper.StringWrapper._

/**
 * Automatic content registration for all fields extending this trait.
 * @author anti344, Calclavia
 */
trait ContentLoader
{
    self =>

    lazy val manager = new ModManager()

    def preInit() =
    {
        //Automated handler for registering blocks & items vars
        for (field <- self.getClass.getDeclaredFields)
        {
            //Set it so we can access the field
            field.setAccessible(true)

            //Get contents for reference
            val obj = field.get(self)

            if (obj != null)
            {
                // Get Annotation Name if present
                var name : String  = null;
                val annotation = field.getAnnotation(classOf[ExplicitContentName])

                if (annotation != null)
                {
                    if (!annotation.value.isEmpty)
                    {
                        name = annotation.value
                    }
                    else
                    {
                        name = field.getName
                        name = name.replace("Item", "").replace("Block", "")
                        name = name.replace("item", "").replace("block", "")
                    }
                    name = name.decapitalizeFirst
                }

                // Get type of object, then register it if supported
                if (obj.isInstanceOf[Item])
                {
                    if (name != null)
                        field.set(self, manager.newItem(name, obj.asInstanceOf[Item]))
                    else
                        field.set(self, manager.newItem(obj.asInstanceOf[Item]))
                }
                else if (obj.isInstanceOf[DummySpatialBlock])
                {
                    if (name != null)
                        field.set(self, manager.newBlock(name, obj.asInstanceOf[DummySpatialBlock].spatial))
                    else
                        field.set(self, manager.newBlock(obj.asInstanceOf[DummySpatialBlock].spatial))
                }
                else if (obj.isInstanceOf[Block])
                {
                    name = obj.getClass().getSimpleName
                    name = name.replace("Block", "")
                    name = name.decapitalizeFirst

                    field.set(self, manager.newBlock(name, obj.asInstanceOf[Block]))
                }
            }
        }
    }

    /**
     * Creates a dummy block temporarily until the preInit stage is passed
     */
    implicit protected def wrapSpatialToBlock(spatial: SpatialBlock): Block = new DummySpatialBlock(spatial)

    protected class DummySpatialBlock(val spatial: SpatialBlock) extends Block(Material.air)
    {

    }

}