package org.by1337.blib.factory;

import org.by1337.blib.command.BukkitCommandRegister;

public interface BukkitCommandRegisterFactory {
    BukkitCommandRegisterFactory INSTANCE = BukkitCommandRegister::getInstance;
    BukkitCommandRegister create();
}
