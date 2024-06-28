package org.by1337.blib.hook.papi;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class PlaceholderTest {
    @Test
    public void run() {
        Placeholder placeholder = new Placeholder("pl");

        placeholder.addSubPlaceholder(
                new Placeholder("test_test_test1").executor(((player, args) -> "ok"))
        );
        placeholder.addSubPlaceholder(
                new Placeholder("test_test_test2").executor(((player, args) -> "ok2"))
        );
        placeholder.addSubPlaceholder(
                new Placeholder("test_test_test1_sub1").executor(((player, args) -> "sub_ok1"))
        );
        placeholder.addSubPlaceholder(
                new Placeholder("test_test_test1_sub2").executor(((player, args) -> "sub_ok2"))
        );
        placeholder.addSubPlaceholder(
                new Placeholder("test_test_test2_sub1").executor(((player, args) -> "sub_ok3"))
        );
        placeholder.addSubPlaceholder(
                new Placeholder("test_test_test2_sub1_123123").executor(((player, args) -> "sub_123123"))
        );


        placeholder.build();
        placeholder.addSubPlaceholder(
                new Placeholder("test1").executor(((player, args) -> "123321"))
        );
        placeholder.build();

        placeholder.addSubPlaceholder(new Placeholder("new")
                .addSubPlaceholder(new Placeholder("new")
                        .addSubPlaceholder(new Placeholder("new")
                        ).addSubPlaceholder(new Placeholder("new")
                        ).addSubPlaceholder(new Placeholder("new")
                                .executor((player, args) -> "ok")
                        ).addSubPlaceholder(new Placeholder("new")
                                .executor((player, args) -> "ok")
                        )
                )
        );
        placeholder.build();


        Assert.assertEquals(placeholder.process(null, "new_new_new_new_new_new".split("_")), "ok");
        Assert.assertEquals(placeholder.process(null, "new_new_new_new_new".split("_")), "ok");
        Assert.assertEquals(placeholder.process(null, "test_test_test1".split("_")), "ok");
        Assert.assertEquals(placeholder.process(null, "test_test_test2".split("_")), "ok2");

        Assert.assertEquals(placeholder.process(null, "test_test_test1_sub1".split("_")), "sub_ok1");
        Assert.assertEquals(placeholder.process(null, "test_test_test1_sub2".split("_")), "sub_ok2");
        Assert.assertEquals(placeholder.process(null, "test_test_test2_sub1".split("_")), "sub_ok3");
        Assert.assertEquals(placeholder.process(null, "test_test_test2_sub1_321321".split("_")), "sub_ok3");
        Assert.assertEquals(placeholder.process(null, "test_test_test2_sub1_123123".split("_")), "sub_123123");
        Assert.assertEquals(placeholder.process(null, "test1".split("_")), "123321");

        for (String placeHolderS : placeholder.getAllPlaceHolders()) {
            Assert.assertNotNull(placeholder.process(
                    null,
                    placeHolderS.substring(placeholder.getName().length() + 1).split("_")
            ));
        }
    }
}