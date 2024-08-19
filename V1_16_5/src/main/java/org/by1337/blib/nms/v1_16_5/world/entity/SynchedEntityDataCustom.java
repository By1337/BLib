package org.by1337.blib.nms.v1_16_5.world.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;

public class SynchedEntityDataCustom extends SynchedEntityData {
    public SynchedEntityDataCustom() {
        super(null);
    }

    public <T> void set(EntityDataAccessor<T> datawatcherobject, T t0) {
        try {
            Method method = SynchedEntityData.class.getDeclaredMethod("b", EntityDataAccessor.class);
            method.setAccessible(true);
            DataItem<T> datawatcher_item = (DataItem)method.invoke(this, datawatcherobject);
            if (!Objects.equals(t0, datawatcher_item.getValue())) {
                datawatcher_item.setValue(t0);
                datawatcher_item.setDirty(true);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException var5) {
            throw new RuntimeException(var5);
        }
    }

    public boolean isDirty_() {
        return true;
    }
}
