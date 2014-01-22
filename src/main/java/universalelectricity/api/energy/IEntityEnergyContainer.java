package universalelectricity.api.energy;

/** Applied to entities that store energy but do not need side references
 * 
 * @author Rseifert */
public interface IEntityEnergyContainer
{
    /** Sets the amount of energy this unit stored.
     * 
     * This function is NOT recommended for calling. */
    public void setEnergy(long energy);

    /** * @return Get the amount of energy currently stored in the block. */
    public long getEnergy();

    /** @return Get the max amount of energy that can be stored in the block. */
    public long getEnergyCapacity();
}
