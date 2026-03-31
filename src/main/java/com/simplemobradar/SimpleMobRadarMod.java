package com.simplemobradar;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class SimpleMobRadarMod implements ModInitializer {
    public static final String MOD_ID = "simple_mob_radar";
    public static final RegistryKey<Item> MOB_RADAR_KEY = RegistryKey.of(RegistryKeys.ITEM, id("mob_radar"));
    public static final Item MOB_RADAR = Registry.register(
            Registries.ITEM,
            MOB_RADAR_KEY,
            new Item(new Item.Settings().maxCount(1).registryKey(MOB_RADAR_KEY))
    );

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(MOB_RADAR));
    }
}
