package com.builtbroken.mc.lib.data;

import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.core.Engine;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * Used to track block & meta pairs for simple boolean checks
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/22/2017.
 */
public class BlockStateEntry extends Pair<Block, Integer>
{
    public String registryName;

    public BlockStateEntry(String name, int meta)
    {
        super(null, meta);
        this.registryName = name;
    }

    public boolean matches(Block block, int meta)
    {
        return block != null && block == getBlock() && (getMeta() == -1 || getMeta() == meta);
    }

    public Block getBlock()
    {
        if (left() == null && registryName != null)
        {
            setLeft((Block) Block.blockRegistry.getObject(registryName));
            if (left() == Blocks.air && !registryName.equalsIgnoreCase("minecraft:air"))
            {
                //Reset in case a mod has not registered the block
                if(Loader.instance().isInState(LoaderState.PREINITIALIZATION) || Loader.instance().isInState(LoaderState.INITIALIZATION) || Loader.instance().isInState(LoaderState.POSTINITIALIZATION))
                {
                    setLeft(null);
                }
                Engine.logger().error("BlockMeta#getBlock() >> failed to find block '" + registryName + "'");
            }
        }
        return left();
    }

    public int getMeta()
    {
        return right();
    }
}
