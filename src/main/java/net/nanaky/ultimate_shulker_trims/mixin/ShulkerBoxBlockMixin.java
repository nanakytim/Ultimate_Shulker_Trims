package net.nanaky.ultimate_shulker_trims.mixin;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.nanaky.ultimate_shulker_trims.ShulkerTrimStorage;
import net.nanaky.ultimate_shulker_trims.TrimmedShulkerBox;
import java.util.List;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin {

  @Inject(method = "getDrops", at = @At("RETURN"))
  private void shulkerTrims$addTrimToDrops(
      BlockState state,
      LootParams.Builder builder,
      CallbackInfoReturnable<List<ItemStack>> cir) {
    List<ItemStack> drops = cir.getReturnValue();
    if (drops.isEmpty()) return;

    var blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
    if (!(blockEntity instanceof ShulkerBoxBlockEntity shulkerBE)) return;
    if (!(shulkerBE instanceof TrimmedShulkerBox trimmed) || !trimmed.shulkerTrims$hasTrim()) return;

    ShulkerTrim trim = trimmed.shulkerTrims$getTrim();
    for (ItemStack stack : drops) {
      if (stack.getItem() instanceof BlockItem blockItem
          && blockItem.getBlock() instanceof ShulkerBoxBlock) {
        ShulkerTrimStorage.writeTrimToItem(stack, trim);
      }
    }
  }
}