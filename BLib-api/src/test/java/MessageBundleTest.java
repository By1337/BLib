
import org.by1337.blib.lang.Lang;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MessageBundleTest {
    @Test
    public void testSupportedMessages() {
        for (String s : Lang.LANGUAGES){
            Lang.loadTranslations(s);
            assertNotNull(Lang.getMessage("unsupported-version"));
            assertNotNull(Lang.getMessage("detect-version"));
            assertNotNull(Lang.getMessage("unexpected-character"));
            assertNotNull(Lang.getMessage("unexpected-token"));
            assertNotNull(Lang.getMessage("expected"));
            assertNotNull(Lang.getMessage("too-many-arguments"));
            assertNotNull(Lang.getMessage("number-too-big"));
            assertNotNull(Lang.getMessage("number-too-small"));
            assertNotNull(Lang.getMessage("nan"));
            assertNotNull(Lang.getMessage("unknown-player"));
            assertNotNull(Lang.getMessage("constant-not-found-more"));
            assertNotNull(Lang.getMessage("constant-not-found"));
            assertNotNull(Lang.getMessage("invalid-characters"));
            assertNotNull(Lang.getMessage("missing-argument"));
            assertNotNull(Lang.getMessage("language-changed"));
            assertNotNull(Lang.getMessage("reload"));
        }
    }
}
