package org.by1337.blib.nms.v1_17.world.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class SynchedEntityDataCustom extends SynchedEntityData {
    public SynchedEntityDataCustom() {
        super(null);
    }

    public <T> void set(EntityDataAccessor<T> param0, T param1) {
        try {
            Method method = SynchedEntityData.class.getDeclaredMethod("b", EntityDataAccessor.class); //getItem
            method.setAccessible(true);
            SynchedEntityData.DataItem<T> var3 = (DataItem<T>) method.invoke(this, param0);
            if (!Objects.equals(param1, var3.getValue())) {
                var3.setValue(param1);
                var3.setDirty(true);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
