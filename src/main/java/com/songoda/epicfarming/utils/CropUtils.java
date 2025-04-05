package com.songoda.epicfarming.utils;

import com.songoda.core.compatibility.crops.CompatibleCrop;
import org.bukkit.block.Block;

public class CropUtils {
    public static boolean isFullyGrown(Block crop) {
        return CompatibleCrop.isCrop(crop) && CompatibleCrop.isCropFullyGrown(crop);
    }

    public static void resetGrowthStage(Block crop) {
        if (!CompatibleCrop.isCrop(crop)) {
            return;
        }
        CompatibleCrop.resetCropAge(crop);
    }

    public static void incrementGrowthStage(Block crop) {
        if (!CompatibleCrop.isCrop(crop)) {
            return;
        }
        CompatibleCrop.incrementCropAge(crop);
    }
}
