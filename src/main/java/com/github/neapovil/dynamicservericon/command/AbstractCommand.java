package com.github.neapovil.dynamicservericon.command;

import com.github.neapovil.dynamicservericon.DynamicServerIcon;

public abstract class AbstractCommand
{
    protected DynamicServerIcon plugin = DynamicServerIcon.instance();

    public abstract void register();
}
