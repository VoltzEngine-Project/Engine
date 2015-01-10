package com.builtbroken.mc.lib.mod.loadable;

/**
 * Decision based version of ILoadable to allow control over if it should or shouldn't be loaded
 */
public interface ILoadableProxy extends ILoadable
{
    /**
     * Should we load this loadable
     */
	public boolean shouldLoad();
}
