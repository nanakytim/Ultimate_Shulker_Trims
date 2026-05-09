package net.nanaky.ultimate_shulker_trims.recipe;

import net.nanaky.ultimate_shulker_trims.UltimateShulkerTrims;

import java.security.Identity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class ShulkerTrimsRecipeSerializers {

  public static final RecipeSerializer<ShulkerTrimRecipe> SHULKER_TRIM =
      new RecipeSerializer<>(ShulkerTrimRecipe.CODEC, ShulkerTrimRecipe.STREAM_CODEC);

  private ShulkerTrimsRecipeSerializers() {}

  public static void register() {
    Registry.register(
        BuiltInRegistries.RECIPE_SERIALIZER,
        Identifier.fromNamespaceAndPath(UltimateShulkerTrims.MOD_ID, "shulker_trim"),
        SHULKER_TRIM);
    UltimateShulkerTrims.LOGGER.info("Registered shulker trim recipe serializer");
  }
}