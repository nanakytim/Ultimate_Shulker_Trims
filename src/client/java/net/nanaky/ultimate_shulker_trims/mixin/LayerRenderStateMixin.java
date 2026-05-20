package net.nanaky.ultimate_shulker_trims.mixin;

import net.nanaky.ultimate_shulker_trims.client.ItemTrimRenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.client.renderer.item.ItemStackRenderState$LayerRenderState")
public class LayerRenderStateMixin {

    @Shadow
    private ItemStackRenderState this$0;

    @Inject(method = "submit", at = @At("HEAD"))
    private void shulkerTrims$setCurrentRenderState(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        int lightCoords,
        int overlayCoords,
        int outlineColor,
        CallbackInfo ci) {
        ItemTrimRenderContext.setCurrentRenderState(this.this$0);
    }

    @Inject(method = "submit", at = @At("RETURN"))
    private void shulkerTrims$clearCurrentRenderState(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        int lightCoords,
        int overlayCoords,
        int outlineColor,
        CallbackInfo ci) {
        ItemTrimRenderContext.clearCurrentRenderState();
    }
}