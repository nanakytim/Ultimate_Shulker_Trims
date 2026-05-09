package net.nanaky.ultimate_shulker_trims.client;

import net.nanaky.ultimate_shulker_trims.UltimateShulkerTrims;
import net.nanaky.ultimate_shulker_trims.TrimmedShulkerBox;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

public class TrimSyncNetworkClient {

  public static void register() {
    PayloadTypeRegistry.clientboundPlay().register(
        TrimSyncPayload.TYPE, TrimSyncPayload.STREAM_CODEC);

    ClientPlayNetworking.registerGlobalReceiver(
        TrimSyncPayload.TYPE,
        (payload, context) -> {
          UltimateShulkerTrims.LOGGER.info(
              "Received trim sync via Fabric API at ({}, {}, {})",
              payload.x(), payload.y(), payload.z());

          context.client().execute(() -> {
            var level = context.client().level;
            if (level == null) return;

            BlockPos pos = new BlockPos(payload.x(), payload.y(), payload.z());
            var blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof ShulkerBoxBlockEntity
                && blockEntity instanceof TrimmedShulkerBox trimmed) {
              trimmed.shulkerTrims$setTrim(payload.trim());
              UltimateShulkerTrims.LOGGER.info(
                  "Applied trim at {}: {}", pos, payload.trim());
            }
          });
        });

    UltimateShulkerTrims.LOGGER.info("Trim sync channel registered");
  }
}