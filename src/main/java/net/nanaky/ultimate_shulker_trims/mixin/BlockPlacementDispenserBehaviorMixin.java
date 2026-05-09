package net.nanaky.ultimate_shulker_trims.mixin;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.nanaky.ultimate_shulker_trims.ShulkerTrimStorage;
import net.nanaky.ultimate_shulker_trims.TrimmedShulkerBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultDispenseItemBehavior.class)
public class BlockPlacementDispenserBehaviorMixin {

  @Unique
  private static final ThreadLocal<ShulkerTrim> shulkerTrims$pendingTrim = new ThreadLocal<>();

  @Inject(method = "execute", at = @At("HEAD"))
  private void shulkerTrims$captureTrimBeforeDispense(
      BlockSource pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
    shulkerTrims$pendingTrim.set(ShulkerTrimStorage.readTrimFromItem(stack));
  }

  @Inject(method = "execute", at = @At("RETURN"))
  private void shulkerTrims$transferTrimAfterDispense(
      BlockSource pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
    ShulkerTrim trim = shulkerTrims$pendingTrim.get();
    shulkerTrims$pendingTrim.remove();
    if (trim == null) return;
    Level level = pointer.level();
    Direction facing = pointer.state().getValue(DispenserBlock.FACING);
    BlockPos targetPos = pointer.pos().relative(facing);
    BlockState targetState = level.getBlockState(targetPos);
    if (!(targetState.getBlock() instanceof ShulkerBoxBlock)) return;
    BlockEntity blockEntity = level.getBlockEntity(targetPos);
    if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBE
        && shulkerBE instanceof TrimmedShulkerBox trimmed) {
      trimmed.shulkerTrims$setTrim(trim);
      blockEntity.setChanged();
      level.sendBlockUpdated(targetPos, targetState, targetState, 3);
    }
  }
}