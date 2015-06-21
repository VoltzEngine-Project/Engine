package com.builtbroken.mc.core.network;

import io.netty.buffer.ByteBuf;

/** Applied to objects that can read data from a byte buf to
 * update there own contents.
 * Created by robert on 1/11/2015.
 */
public interface IByteBufReader
{
    /** Called to read an object from byte buf
     * @param buf a {@link io.netty.buffer.ByteBuf} to read from
     * @return instance of the object
     */
    Object readBytes(ByteBuf buf);
}
