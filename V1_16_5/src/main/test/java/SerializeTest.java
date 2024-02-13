import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import org.by1337.v1_16_5.inventory.ItemStackSerializeV1_16_5;

import org.junit.Assert;
import org.junit.Test;

/**
 * The SerializeTest class contains unit tests for serialization and deserialization
 * of ItemStacks using the ItemStackSerializeV1_16_R3 class. It ensures that serialized
 * ItemStacks can be correctly deserialized and that the serialized strings math the
 * expected values.
 */
public class SerializeTest {

    /**
     * Runs unit tests for serialization and deserialization of ItemStacks.
     * It initializes a mock Bukkit Server and tests the serialization and
     * deserialization of different ItemStacks.
     */
    @Test
    public void run() {
        SetServer.setServer(); // Initialize the mock server

        ItemStackSerializeV1_16_5 itemStackSerialize = new ItemStackSerializeV1_16_5();

        // Test serialization and deserialization of an Enchanted Golden Apple
        {
            String item = "e2lkOiJtaW5lY3JhZnQ6ZW5jaGFudGVkX2dvbGRlbl9hcHBsZSIsQ291bnQ6MmJ9";
            ItemStack itemStack = itemStackSerialize.deserialize(item);

            Assert.assertEquals(itemStack.getType(), Material.ENCHANTED_GOLDEN_APPLE);
            Assert.assertEquals(itemStack.getAmount(), 2);
            Assert.assertEquals(item, itemStackSerialize.serialize(itemStack));
        }
        // Test serialization and deserialization of a custom named Diamond ItemStack
        {
            String item = "e2lkOiJtaW5lY3JhZnQ6ZGlhbW9uZCIsQ291bnQ6MWIsdGFnOntkaXNwbGF5OntOYW1lOid7ImV4dHJhIjpbeyJib2xkIjpmYWxzZSwiaXRhbGljIjpmYWxzZSwidW5kZXJsaW5lZCI6ZmFsc2UsInN0cmlrZXRocm91Z2giOmZhbHNlLCJvYmZ1c2NhdGVkIjpmYWxzZSwiY29sb3IiOiJncmF5IiwidGV4dCI6ItCw0LHQstCz0JDQkdCS0JNhYmNkQUJDRMOKw4vDjMONw47Dj+KaoeKbj+KclOKdhOKdjOKdpOKtkCJ9XSwidGV4dCI6IiJ9J319fQ==";
            ItemStack itemStack = itemStackSerialize.deserialize(item);

            Assert.assertEquals(itemStack.getType(), Material.DIAMOND);
            Assert.assertEquals(itemStack.getAmount(), 1);
            Assert.assertEquals(itemStack.getItemMeta().getDisplayName(), "§7абвгАБВГabcdABCDÊËÌÍÎÏ⚡⛏✔❄❌❤⭐");
            Assert.assertEquals(item, itemStackSerialize.serialize(itemStack));
        }


    }
}
