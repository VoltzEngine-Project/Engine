package resonant.api;

import java.util.List;

import net.minecraft.entity.Entity;

/** An interface applied to a TileEntity that can move entities. An example of this is a conveyor
 * belt.
 * 
 * @Author DarkGuardsman */
public interface IEntityConveyor
{
    /** Used to get a list of entities the belt exerts an effect upon.
     * 
     * @return list of entities in the belts are of effect */
    public List<Entity> getAffectedEntities();

    /** Used by other automation to prevent this tile from effecting the entity. Most cases this is
     * used by robotic arms, and rejectors. Allowing them to pick up the entity without issues. */
    public void ignoreEntity(Entity entity);
}
