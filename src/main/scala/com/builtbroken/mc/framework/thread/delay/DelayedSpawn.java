package com.builtbroken.mc.framework.thread.delay;

import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class DelayedSpawn extends DelayedAction
{
    public final Pos center;
    public final Entity entity;
    public final int range;

    int spawnAttemptLimit = 10;
    private int spawnAttempts;

    public DelayedSpawn(World world, Pos center, Entity entity, int range, int ticks)
    {
        super(world, ticks);
        this.center = center;
        this.entity = entity;
        this.range = range;
    }

    @Override
    public boolean trigger()
    {
        if (entity != null)
        {
            final float width = entity.width;
            final float height = entity.height;

            //Code can only handle entities roughly 1 block wide
            if (width <= 1.1)
            {
                spawnAttempts++;

                //Get random spawn point
                Pos spawnPoint = center.addRandom(world.rand, range);
                if (!spawnPoint.isAirBlock(world))
                {
                    spawnPoint = spawnPoint.add(0, 1, 0);
                }

                //Ensure point is in air and is on ground
                if (spawnPoint.isAirBlock(world) && !spawnPoint.sub(0, 1, 0).isAirBlock(world))
                {
                    //Do height check
                    if (height > 1)
                    {
                        int heightChecks = (int) Math.ceil(height - 1);
                        for (int i = 0; i < heightChecks; i++)
                        {
                            if (!spawnPoint.add(0, i, 0).isAirBlock(world))
                            {
                                return spawnAttempts >= spawnAttemptLimit;
                            }
                        }
                    }
                    //Set data
                    entity.setPosition(spawnPoint.xi() + 0.5, spawnPoint.yi() + 0.5, spawnPoint.zi() + 0.5);
                    //TODO rotate to face closest player

                    //Spawn
                    world.spawnEntityInWorld(entity);
                    return true; //done
                }

                return spawnAttempts >= spawnAttemptLimit;
            }
        }

        return true; //done due to bad data
    }
}