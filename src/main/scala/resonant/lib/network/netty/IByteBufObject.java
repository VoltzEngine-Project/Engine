package resonant.lib.network.netty;

import io.netty.buffer.ByteBuf;

/**
 * Implement this on Objects you want to be able to
 *
 * @author tgame14
 * @since 26/05/14
 */
public interface IByteBufObject
{
	/**
	 * @param buf a {@link io.netty.buffer.ByteBuf} to write to.
	 */
	public void toBytes(ByteBuf buf);

	/**
	 * @param buf a {@link io.netty.buffer.ByteBuf} to read from
	 */
	public void fromBytes(ByteBuf buf);
}
