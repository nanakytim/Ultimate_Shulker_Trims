package net.nanaky.ultimate_shulker_trims.client;

import net.nanaky.ultimate_shulker_trims.UltimateShulkerTrims;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.Nullable;

public class UltimateShulkerTrimsClient implements ClientModInitializer {

  @Nullable
  private static ShulkerBoxRenderer cachedRenderer = null;

  @Override
  public void onInitializeClient() {
    UltimateShulkerTrims.LOGGER.info("Shulker Trims client initializing...");
    TrimSyncNetworkClient.register();
    ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
        .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
          @Override
          public Identifier getFabricId() {
            return Identifier.fromNamespaceAndPath(
                "ultimate_shulker_trims", "renderer_cache");
          }
          @Override
          public void onResourceManagerReload(ResourceManager manager) {
            cachedRenderer = null;
          }
        });
    UltimateShulkerTrims.LOGGER.info("Shulker Trims client initialized");
  }

  public static @Nullable ShulkerBoxRenderer getShulkerBoxRenderer() {
    if (cachedRenderer == null) {
      Minecraft mc = Minecraft.getInstance();
      if (mc == null) return null;
      SpecialModelRenderer.BakingContext ctx = new SpecialModelRenderer.BakingContext() {
        @Override
        public net.minecraft.client.model.geom.EntityModelSet entityModelSet() {
          return mc.getEntityModels();
        }
        @Override
        public net.minecraft.client.resources.model.sprite.SpriteGetter sprites() {
          return mc.getAtlasManager();
        }
        @Override
        public net.minecraft.client.renderer.PlayerSkinRenderCache playerSkinRenderCache() {
          return mc.playerSkinRenderCache();
        }
      };
      cachedRenderer = new ShulkerBoxRenderer(ctx);
    }
    return cachedRenderer;
  }
}