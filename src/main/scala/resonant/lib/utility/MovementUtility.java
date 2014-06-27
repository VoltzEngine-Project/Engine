package resonant.lib.utility;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import universalelectricity.core.transform.vector.Vector3;

import java.lang.reflect.Method;

/**
 * Helper class for the Force Manipulator
 *
 * @author Calclavia
 * @author Based off of Jakj's code. Licensed under WTFPL.
 */
public class MovementUtility
{
	/**
	 * Obfuscation names for reflection
	 */
	public static final String[] CHUNK_RELIGHT_BLOCK = { "relightBlock", "func_76615_h" };
	public static final String[] CHUNK_PROPOGATE_SKY_LIGHT_OCCLUSION = { "propagateSkylightOcclusion", "func_76595_e" };

	/**
	 * Sets a block in a sneaky way to bypass some restraints.
	 */
	public static void setBlockSneaky(World world, Vector3 position, Block block, int metadata, TileEntity tileEntity)
	{
		Chunk chunk = world.getChunkFromChunkCoords(position.xi() >> 4, position.zi() >> 4);
		Vector3 chunkPosition = new Vector3(position.xi() & 0xF, position.yi() & 0xF, position.zi() & 0xF);

		int heightMapIndex = chunkPosition.zi() << 4 | chunkPosition.xi();

		if (position.yi() >= chunk.precipitationHeightMap[heightMapIndex] - 1)
		{
			chunk.precipitationHeightMap[heightMapIndex] = -999;
		}

		int heightMapValue = chunk.heightMap[heightMapIndex];

		world.removeTileEntity(position.xi(), position.yi(), position.zi());

		ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[position.yi() >> 4];

		if (extendedBlockStorage == null)
		{
			extendedBlockStorage = new ExtendedBlockStorage((position.yi() >> 4) << 4, !world.provider.hasNoSky);

			chunk.getBlockStorageArray()[position.yi() >> 4] = extendedBlockStorage;
		}

		extendedBlockStorage.func_150818_a(chunkPosition.xi(), chunkPosition.yi(), chunkPosition.zi(), block);
		extendedBlockStorage.setExtBlockMetadata(chunkPosition.xi(), chunkPosition.yi(), chunkPosition.zi(), metadata);

		if (position.yi() >= heightMapValue)
		{
			chunk.generateSkylightMap();
		}
		else
		{
			//chunk.getBlockLightOpacity(chunkPosition.xi(), position.yi(), chunkPosition.zi())
			if (chunk.getBlockLightValue(chunkPosition.xi(), position.yi(), chunkPosition.zi(), 0) > 0)
			{
				if (position.yi() >= heightMapValue)
				{
					relightBlock(chunk, chunkPosition.clone().add(new Vector3(0, 1, 0)));
				}
			}
			else if (position.yi() == heightMapValue - 1)
			{
				relightBlock(chunk, chunkPosition);
			}

			propagateSkylightOcclusion(chunk, chunkPosition);
		}

		chunk.isModified = true;
		//updateAllLightTypes
		world.func_147451_t(position.xi(), position.yi(), position.zi());

		if (tileEntity != null)
		{
			world.setTileEntity(position.xi(), position.yi(), position.zi(), tileEntity);
		}

		world.markBlockForUpdate(position.xi(), position.yi(), position.zi());
	}

	/**
	 * Re-lights the block in a specific position.
	 *
	 * @param chunk
	 * @param position
	 */
	public static void relightBlock(Chunk chunk, Vector3 position)
	{
		try
		{
			Method m = ReflectionHelper.findMethod(Chunk.class, null, CHUNK_RELIGHT_BLOCK, int.class, int.class, int.class);
			m.invoke(chunk, position.xi(), position.yi(), position.zi());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Propogates skylight occlusion in a specific chunk's position.
	 *
	 * @param chunk
	 * @param position
	 */
	public static void propagateSkylightOcclusion(Chunk chunk, Vector3 position)
	{
		try
		{
			Method m = ReflectionHelper.findMethod(Chunk.class, null, CHUNK_PROPOGATE_SKY_LIGHT_OCCLUSION, int.class, int.class);
			m.invoke(chunk, position.xi(), position.zi());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
