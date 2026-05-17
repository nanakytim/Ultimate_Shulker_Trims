package net.nanaky.ultimate_shulker_trims.mixin;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.nanaky.ultimate_shulker_trims.ShulkerTrimStorage;
import net.nanaky.ultimate_shulker_trims.TrimmedShulkerBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends BlockEntity implements TrimmedShulkerBox {

  private ShulkerBoxBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Unique private @Nullable ShulkerTrim shulkerTrims$cachedTrim;
  @Unique private boolean shulkerTrims$trimLoaded = false;

  @Override
  public @Nullable ShulkerTrim shulkerTrims$getTrim() {
    if (!this.shulkerTrims$trimLoaded) {
      this.shulkerTrims$trimLoaded = true;
      CustomData customData = this.components().get(DataComponents.CUSTOM_DATA);
      if (customData != null) {
        this.shulkerTrims$cachedTrim = ShulkerTrimStorage.readTrim(customData.copyTag());
      }
    }
    return this.shulkerTrims$cachedTrim;
  }

  @Override
  public void shulkerTrims$setTrim(@Nullable ShulkerTrim trim) {
    this.shulkerTrims$cachedTrim = trim;
    this.shulkerTrims$trimLoaded = true;
    this.setChanged();
  }

  @Inject(method = "saveAdditional", at = @At("RETURN"))
  private void shulkerTrims$saveTrim(ValueOutput output, CallbackInfo ci) {
    final ShulkerTrim trim = this.shulkerTrims$cachedTrim;
    if (trim != null) {
    } 
  }

  @Inject(method = "loadAdditional", at = @At("RETURN"))
  private void shulkerTrims$loadTrim(ValueInput input, CallbackInfo ci) {
    
    this.shulkerTrims$trimLoaded = false;
    this.shulkerTrims$cachedTrim = null;
    
    ShulkerTrim syncTrim = ShulkerTrimStorage.readTrimFromData(input);
    if (syncTrim != null) {
      this.shulkerTrims$cachedTrim = syncTrim;
      this.shulkerTrims$trimLoaded = true;
    }
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag nbt = super.getUpdateTag(registries);
    ShulkerTrim trim = this.shulkerTrims$getTrim();
    if (trim != null) {
      ShulkerTrimStorage.writeTrim(nbt, trim);
    }
    return nbt;
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}