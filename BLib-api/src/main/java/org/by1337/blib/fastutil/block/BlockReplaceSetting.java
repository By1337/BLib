package org.by1337.blib.fastutil.block;

import org.bukkit.block.Block;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BlockReplaceSetting {
    protected boolean applyPhysics = false;
    protected boolean deobfuscatePaperAntiXRay = true;
    protected boolean recalculateLight = true;
    protected boolean sendPacketsOnBlockChange = true;
    protected Consumer<Block> blockBreakCallBack;
    protected boolean perfectPhysics = false;
    protected Predicate<Block> filter;
    protected long customTimeOut;
    protected boolean debug = false;
    protected boolean doPlace;

    public boolean isDebug() {
        return debug;
    }

    public BlockReplaceSetting setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public long getCustomTimeOut() {
        return customTimeOut;
    }

    public BlockReplaceSetting setCustomTimeOut(long customTimeOut) {
        this.customTimeOut = customTimeOut;
        return this;
    }

    public boolean isApplyPhysics() {
        return applyPhysics;
    }

    public BlockReplaceSetting setApplyPhysics(boolean applyPhysics) {
        this.applyPhysics = applyPhysics;
        return this;
    }

    public boolean isDeobfuscatePaperAntiXRay() {
        return deobfuscatePaperAntiXRay;
    }

    public BlockReplaceSetting setDeobfuscatePaperAntiXRay(boolean deobfuscatePaperAntiXRay) {
        this.deobfuscatePaperAntiXRay = deobfuscatePaperAntiXRay;
        return this;
    }

    public boolean isRecalculateLight() {
        return recalculateLight;
    }

    public BlockReplaceSetting setRecalculateLight(boolean recalculateLight) {
        this.recalculateLight = recalculateLight;
        return this;
    }

    public boolean isSendPacketsOnBlockChange() {
        return sendPacketsOnBlockChange;
    }

    public BlockReplaceSetting setSendPacketsOnBlockChange(boolean sendPacketsOnBlockChange) {
        this.sendPacketsOnBlockChange = sendPacketsOnBlockChange;
        return this;
    }


    public boolean isDoPlace() {
        return doPlace;
    }

    public BlockReplaceSetting setDoPlace(boolean doPlace) {
        this.doPlace = doPlace;
        return this;
    }

    public Consumer<Block> getBlockBreakCallBack() {
        return blockBreakCallBack;
    }

    public BlockReplaceSetting setBlockBreakCallBack(Consumer<Block> blockBreakCallBack) {
        this.blockBreakCallBack = blockBreakCallBack;
        return this;
    }

    public boolean isPerfectPhysics() {
        return perfectPhysics;
    }

    public BlockReplaceSetting setPerfectPhysics(boolean perfectPhysics) {
        this.perfectPhysics = perfectPhysics;
        return this;
    }

    public Predicate<Block> getFilter() {
        return filter;
    }

    public BlockReplaceSetting setFilter(Predicate<Block> filter) {
        this.filter = filter;
        return this;
    }
}
