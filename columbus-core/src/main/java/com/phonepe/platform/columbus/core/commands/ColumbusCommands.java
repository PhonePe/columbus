package com.phonepe.platform.columbus.core.commands;

public interface ColumbusCommands {
    default String getCommandClass() {
        return getClass().getName();
    }
}
