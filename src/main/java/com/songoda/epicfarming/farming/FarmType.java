package com.songoda.epicfarming.farming;

import com.songoda.epicfarming.EpicFarming;

public enum FarmType {
    CROPS, LIVESTOCK, BOTH;

    public String translate() {
        return EpicFarming.getPlugin(EpicFarming.class)
                .getLocale()
                .getMessage("general.interface." + name().toLowerCase())
                .toText();
    }

}
