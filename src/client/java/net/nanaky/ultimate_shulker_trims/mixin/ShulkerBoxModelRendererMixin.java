package net.nanaky.ultimate_shulker_trims.mixin;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.nanaky.ultimate_shulker_trims.client.ItemTrimRenderContext;
import net.nanaky.ultimate_shulker_trims.client.ShulkerTrimRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.special.ShulkerBoxSpecialRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxSpecialRenderer.class)
public class ShulkerBoxModelRendererMixin {

  @Shadow @Final private ShulkerBoxRenderer shulkerBoxRenderer;
  @Shadow @Final private float openness;

  @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIZ I)V",
      at = @At("TAIL"))
  private void shulkerTrims$renderTrimOverlay(
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      int lightCoords,
      int overlayCoords,
      boolean hasFoil,
      int outlineColor,
      CallbackInfo ci) {
    ShulkerTrim trim = ItemTrimRenderContext.getCurrentTrim();
    if (trim == null) return;

    SpriteId trimSpriteId = ShulkerTrimRenderer.getTrimSpriteId(trim);
    this.shulkerBoxRenderer.submit(
        poseStack, submitNodeCollector,
        lightCoords, overlayCoords,
        this.openness, null, trimSpriteId, 0);
  }
}