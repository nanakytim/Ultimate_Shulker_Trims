package net.nanaky.ultimate_shulker_trims.mixin;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.nanaky.ultimate_shulker_trims.ShulkerTrimStorage;
import net.nanaky.ultimate_shulker_trims.client.ItemTrimRenderContext;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.jspecify.annotations.Nullable;

@Mixin(ItemModelResolver.class)
public class ItemModelManagerMixin {

  @Unique
  private static void shulkerTrims$captureTrim(ItemStackRenderState state, ItemStack stack) {
    if (stack.getItem() instanceof BlockItem blockItem
        && blockItem.getBlock() instanceof ShulkerBoxBlock) {
      ShulkerTrim trim = ShulkerTrimStorage.readTrimFromItem(stack);
      ItemTrimRenderContext.setTrim(state, trim);
    }
  }

  @Inject(
      method = "appendItemLayers",
      at = @At("HEAD"))
  private void shulkerTrims$captureTrimData(
      ItemStackRenderState output,
      ItemStack item,
      ItemDisplayContext displayContext,
      @Nullable Level level,
      @Nullable ItemOwner owner,
      int seed,
      CallbackInfo ci) {
    shulkerTrims$captureTrim(output, item);
  }

    @Inject(method = "updateForTopItem", at = @At("HEAD"))
    private void shulkerTrims$captureTrimTopItem(
        ItemStackRenderState output,
        ItemStack item,
        ItemDisplayContext displayContext,
        @Nullable Level level,
        @Nullable ItemOwner owner,
        int seed,
        CallbackInfo ci) {
      shulkerTrims$captureTrim(output, item);
    }

  @Inject(
      method = "appendItemLayers",
      at = @At("TAIL"))
  private void shulkerTrims$addTrimToModelKey(
      ItemStackRenderState output,
      ItemStack item,
      ItemDisplayContext displayContext,
      @Nullable Level level,
      @Nullable ItemOwner owner,
      int seed,
      CallbackInfo ci) {
    ShulkerTrim trim = ItemTrimRenderContext.getTrim(output);
    if (trim != null) {
      output.appendModelIdentityElement(trim);
    }
  }

    @Inject(method = { "updateForTopItem" }, at = { @At("TAIL") })
    private void shulkerTrims$addTrimToModelKeyTopItem(final ItemStackRenderState output, final ItemStack item, final ItemDisplayContext displayContext, final Level level, final ItemOwner owner, final int seed, final CallbackInfo ci) {
        final ShulkerTrim trim = ItemTrimRenderContext.getTrim(output);
        if (trim != null) {
            output.appendModelIdentityElement((Object)trim);
        }
    }
}