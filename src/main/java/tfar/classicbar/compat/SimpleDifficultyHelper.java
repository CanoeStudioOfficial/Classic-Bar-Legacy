package tfar.classicbar.compat;

import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import net.minecraft.entity.player.EntityPlayer;

public class SimpleDifficultyHelper {
    public static IThirstCapability getHandler(EntityPlayer player) {
        return player.getCapability(SDCapabilities.THIRST, null);
    };
}
