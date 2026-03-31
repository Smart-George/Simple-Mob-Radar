package com.simplemobradar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class SimpleMobRadarClient implements ClientModInitializer {
    private static final double MAX_TRACK_DISTANCE = 30.0;
    private static final int MAX_TARGETS = 12;
    private static final List<RadarTarget> TRACKED = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::scanTargets);
        HudRenderCallback.EVENT.register(this::renderHud);
    }

    private void scanTargets(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            TRACKED.clear();
            return;
        }

        if (!isRadarActive(client)) {
            TRACKED.clear();
            return;
        }

        List<MobEntity> nearby = client.world.getEntitiesByClass(
                MobEntity.class,
                client.player.getBoundingBox().expand(MAX_TRACK_DISTANCE),
                mob -> mob.isAlive() && !mob.isSpectator() && mob != client.player
        );

        nearby.sort(Comparator.comparingDouble(mob -> mob.squaredDistanceTo(client.player)));

        TRACKED.clear();
        for (int i = 0; i < Math.min(nearby.size(), MAX_TARGETS); i++) {
            MobEntity mob = nearby.get(i);
            double dist = Math.sqrt(mob.squaredDistanceTo(client.player));
            mob.setGlowing(true);
            TRACKED.add(new RadarTarget(mob.getUuid(), mob.getDisplayName(), (float) dist, mob.getHealth(), mob.getMaxHealth()));
        }
    }

    private void renderHud(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || TRACKED.isEmpty() || !isRadarActive(client)) {
            return;
        }

        int centerX = context.getScaledWindowWidth() / 2;
        int y = 16;

        context.drawTextWithShadow(client.textRenderer, Text.literal("Simple Mob Radar").formatted(Formatting.GREEN), centerX - 44, y, 0x66FF66);
        y += 12;

        for (RadarTarget target : TRACKED) {
            LivingEntity entity = (LivingEntity) client.world.getEntityById(findEntityIdByUuid(client, target.uuid));
            if (entity == null || !entity.isAlive()) {
                continue;
            }

            String name = target.name.getString();
            String distance = String.format("%.1f blocks", target.distance);
            String health = String.format("%.1f / %.1f HP", entity.getHealth(), entity.getMaxHealth());

            int boxX = centerX - 54;
            int boxW = 108;
            int boxH = 40;
            context.fill(boxX, y - 2, boxX + boxW, y + boxH, 0x66000000);
            context.drawTextWithShadow(client.textRenderer, Text.literal(name).formatted(Formatting.GREEN), boxX + 4, y, 0x66FF66);
            context.drawTextWithShadow(client.textRenderer, Text.literal(distance).formatted(Formatting.AQUA), boxX + 4, y + 10, 0x66FFFF);
            context.drawTextWithShadow(client.textRenderer, Text.literal(health).formatted(Formatting.RED), boxX + 4, y + 20, 0xFF6666);
            context.drawTextWithShadow(client.textRenderer, Text.literal("[+]").formatted(Formatting.GREEN), boxX + 89, y + 22, 0x66FF66);

            y += 44;
            if (y > context.getScaledWindowHeight() - 44) {
                break;
            }
        }
    }

    private int findEntityIdByUuid(MinecraftClient client, UUID uuid) {
        if (client.world == null) {
            return -1;
        }

        for (MobEntity mob : client.world.getEntitiesByClass(MobEntity.class, client.player.getBoundingBox().expand(MAX_TRACK_DISTANCE), m -> true)) {
            if (mob.getUuid().equals(uuid)) {
                return mob.getId();
            }
        }

        return -1;
    }

    private boolean isRadarActive(MinecraftClient client) {
        ItemStack main = client.player.getMainHandStack();
        ItemStack off = client.player.getOffHandStack();
        return main.isOf(SimpleMobRadarMod.MOB_RADAR) || off.isOf(SimpleMobRadarMod.MOB_RADAR);
    }

    private record RadarTarget(UUID uuid, Text name, float distance, float health, float maxHealth) {
    }
}
