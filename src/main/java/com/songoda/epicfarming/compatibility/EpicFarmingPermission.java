package com.songoda.epicfarming.compatibility;

import com.songoda.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.skyblock.permission.BasicPermission;
import com.songoda.skyblock.permission.PermissionType;

public class EpicFarmingPermission extends BasicPermission {
    public EpicFarmingPermission() {
        super("EpicFarming", XMaterial.END_ROD, PermissionType.GENERIC);
    }
}
