package com.builtbroken.mc.api.energy;

/** Simple interface applied to objects that store energy
 * Created by Dark on 8/15/2015.
 */
public interface IEnergyBuffer
{
    /**
     * Added energy to the buffer
     *
     * @param energy - energy to add
     * @return energy stored
     */
    int addEnergyToStorage(int energy, boolean doAction);

    /**
     * Removes energy from the buffer
     *
     * @param energy - energy to remove
     * @return energy actually removed
     */
    int removeEnergyFromStorage(int energy, boolean doAction);

    /**
     * @return Max limit of storage for the buffer
     */
    int getMaxBufferSize();

    /**
     * @return Actual energy stored in buffer
     */
    int getEnergyStored();
}
