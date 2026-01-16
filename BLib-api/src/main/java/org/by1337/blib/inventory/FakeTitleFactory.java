package org.by1337.blib.inventory;
@Deprecated
public interface FakeTitleFactory {
    FakeTitleFactory INSTANCE = () -> FakeTitle.INSTANCE;
    FakeTitle get();
}
