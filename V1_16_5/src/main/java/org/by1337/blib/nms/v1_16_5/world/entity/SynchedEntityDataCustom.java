package org.by1337.blib.nms.v1_16_5.world.entity;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class SynchedEntityDataCustom extends DataWatcher {
    public SynchedEntityDataCustom() {
        super(null);
    }
    public <T> void set(DataWatcherObject<T> datawatcherobject, T t0) {
        try {
            Method method = DataWatcher.class.getDeclaredMethod("b", DataWatcherObject.class);
            method.setAccessible(true);
            DataWatcher.Item<T> datawatcher_item = (DataWatcher.Item<T>)method.invoke(this, datawatcherobject);
            if (!Objects.equals(t0, datawatcher_item.b())) {
                datawatcher_item.a(t0);
                datawatcher_item.a(true);
            }

        }catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean a() {
        return true;
    }
}
