package net.nanaky.ultimate_shulker_trims;

import net.nanaky.ultimate_shulker_trims.recipe.ShulkerTrimsRecipeSerializers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltimateShulkerTrims implements ModInitializer {
  public static final String MOD_ID = "ultimate_shulker_trims";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    LOGGER.info("Shulker Trims initializing...");

    // Register recipe serializers
    ShulkerTrimsRecipeSerializers.register();

    LOGGER.info("Shulker Trims initialized");
  }
}
