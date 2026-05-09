package net.nanaky.ultimate_shulker_trims.client;

import net.nanaky.ultimate_shulker_trims.UltimateShulkerTrims;
import net.fabricmc.api.ClientModInitializer;

public class UltimateShulkerTrimsClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    UltimateShulkerTrims.LOGGER.info("Shulker Trims client initializing...");

    // Register network handler for Paper server sync
    TrimSyncNetworkClient.register();

    UltimateShulkerTrims.LOGGER.info("Shulker Trims client initialized");
  }
}
