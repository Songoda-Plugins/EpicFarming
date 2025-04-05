package com.songoda.epicfarming.listeners;

import com.songoda.epicfarming.EpicFarming;
import com.songoda.epicfarming.farming.Farm;
import com.songoda.epicfarming.farming.levels.modules.Module;
import com.songoda.epicfarming.farming.levels.modules.ModuleAutoCollect;
import com.songoda.ultimatestacker.api.events.entity.StackedItemSpawnEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class UltimateStackerListener implements Listener {

    private final EpicFarming plugin;

    public UltimateStackerListener(EpicFarming plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onStackedItemSpawn(StackedItemSpawnEvent event) {
        Location farmLocation = (Location) event.getExtraData().get("EFA-TAGGED");

        if (farmLocation == null) {
            return;
        }

        Farm farm = this.plugin.getFarmManager().getFarm(farmLocation);

        boolean autoCollect = false;
        for (Module module : farm.getLevel().getRegisteredModules()) {
            if (module instanceof ModuleAutoCollect && ((ModuleAutoCollect) module).isEnabled(farm)) {
                autoCollect = true;
            }
        }

        if (autoCollect) {
            long amount = event.getAmount();
            ItemStack itemStack = event.getItemStack();

            while (amount > 0) {
                ItemStack clone = itemStack.clone();
                clone.setAmount((int) Math.min(amount, clone.getMaxStackSize()));
                amount -= clone.getAmount();
                farm.addItem(clone);
            }
            event.setCancelled(true);
        }
    }
}
