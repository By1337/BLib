package org.by1337.blib.hook.papi;


import org.junit.Assert;
import org.junit.Test;

public class PapiEscaperTest {

    @Test
    public void test() {
        Assert.assertEquals(
                "123123 %%math_10+99%% 123 %%value+123%% %%rel_somevalue_123%%  %123",
                PapiEscaper.escape("123123 %math_10+99% 123 %value+123% %rel_somevalue_123%  %123")
        );
    }
}