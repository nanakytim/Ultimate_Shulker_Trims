package net.nanaky.ultimate_shulker_trims.mixin;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.nanaky.ultimate_shulker_trims.TrimmedShulkerBox;
import net.nanaky.ultimate_shulker_trims.client.ShulkerTrimRenderer;
import net.nanaky.ultimate_shulker_trims.client.TrimmedShulkerRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.jspecify.annotations.Nullable;

@Mixin(ShulkerBoxRenderer.class)
public abstract class ShulkerBoxBlockEntityRendererMixin {

  @Inject(method = "extractRenderState", at = @At("TAIL"))
  private void shulkerTrims$copyTrimToRenderState(
      ShulkerBoxBlockEntity entity,
      ShulkerBoxRenderState state,
      float partialTicks,
      Vec3 cameraPos,
      @Nullable CrumblingOverlay breakProgress,
      CallbackInfo ci) {
    if (entity instanceof TrimmedShulkerBox trimmed
        && state instanceof TrimmedShulkerRenderState trimmedState) {
      trimmedState.shulkerTrims$setTrim(trimmed.shulkerTrims$getTrim());
    }
  }

  @Inject(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/ShulkerBoxRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At("TAIL"))
  private void shulkerTrims$renderTrimOverlay(
      ShulkerBoxRenderState state,
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      CameraRenderState cameraState,
      CallbackInfo ci) {
    if (!(state instanceof TrimmedShulkerRenderState trimmedState)) return;
    ShulkerTrim trim = trimmedState.shulkerTrims$getTrim();
    if (trim == null) return;

    SpriteId trimSpriteId = ShulkerTrimRenderer.getTrimSpriteId(trim);

    poseStack.pushPose();
    
    ShulkerBoxRenderer self = (ShulkerBoxRenderer)(Object)this;
    com.mojang.math.Transformation transform = ShulkerBoxRenderer.modelTransform(state.direction);
    com.mojang.blaze3d.vertex.PoseStack.Pose pose = poseStack.last();
    pose.pose().mul(transform.getMatrix());

    self.submit(poseStack, submitNodeCollector,
        state.lightCoords, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
        state.progress, null, trimSpriteId, 0);

    poseStack.popPose();
  }
}