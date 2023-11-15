import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import org.junit.Test;

public class Nbt {
    @Test
    public void run() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putString("key", "value");
        compoundTag.putInt("intTag", 10);
        compoundTag.putDouble("dou", 10.5);
        compoundTag.putDouble("dou 2", 10.5);


        System.out.println(compoundTag.toString());

        /*

       [
       value: {value: 10, type: int},
       value1: {
        [value: 10, type: int], [value: 10, type: int]
        },
         "vectors":{
         [
         {x:10,y:14,z:-1, type: vector},
         {x:10,y:14,z:-1, type: vector},
         {x:10,y:14,z:-1, type: vector},
         {x:10,y:14,z:-1, type: vector}
         ]
         }
        ]

        * */
    }
}
