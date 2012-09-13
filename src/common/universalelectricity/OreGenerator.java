package universalelectricity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.ChunkProviderEnd;
import net.minecraft.src.ChunkProviderGenerate;
import net.minecraft.src.ChunkProviderHell;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class OreGenerator implements IWorldGenerator
{
	/**
	 * Add your ore data to this list of ores for it to automatically generate! No hassle indeed!
	 */
	private static final List<OreGenData> ORES_TO_GENERATE = new ArrayList<OreGenData>();
	
	/**
	 * Adds an ore to the ore generate list. Do this in pre-init.
	 */
	public static void addOre(OreGenData data)
	{
		ORES_TO_GENERATE.add(data);
	}
	
	/**
	 * Removes an ore to the ore generate list. Do this in init.
	 */
	public static void removeOre(OreGenData data)
	{
		ORES_TO_GENERATE.remove(data);
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		chunkX = chunkX << 4;
		chunkZ = chunkZ << 4;
		
		//Checks to make sure this is the normal world 
		for(OreGenData oreData : ORES_TO_GENERATE)
        {
            if(oreData.shouldGenerate && 
            	((oreData.generateSurface && chunkGenerator instanceof ChunkProviderGenerate) ||
            	(oreData.generateNether && chunkGenerator instanceof ChunkProviderHell) ||
            	(oreData.generateEnd && chunkGenerator instanceof ChunkProviderEnd)
            	))
            {
            	WorldGenMinable worldGenMinable = new WorldGenMinable(oreData.oreStack.itemID, oreData.oreStack.getItemDamage(), oreData.amountPerBranch);

                for (int i = 0; i < oreData.amountPerChunk; i++)
                {
                    int x = chunkX + rand.nextInt(16);
                    int y = rand.nextInt(oreData.maxGenerateLevel) + oreData.minGenerateLevel;
                    int z = chunkZ + rand.nextInt(16);
                    
        			int randAmount = rand.nextInt(8);

                	worldGenMinable.generate(world, rand, x, y, z);
                }
            }
     
        }
	}
}
