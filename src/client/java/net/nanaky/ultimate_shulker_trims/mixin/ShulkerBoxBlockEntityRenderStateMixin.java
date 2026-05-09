package net.nanaky.ultimate_shulker_trims.mixin;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.nanaky.ultimate_shulker_trims.client.TrimmedShulkerRenderState;
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ShulkerBoxRenderState.class)
public class ShulkerBoxBlockEntityRenderStateMixin implements TrimmedShulkerRenderState {

  @Unique private @Nullable ShulkerTrim shulkerTrims$trim;

  @Override
  public @Nullable ShulkerTrim shulkerTrims$getTrim() {
    return this.shulkerTrims$trim;
  }

  @Override
  public void shulkerTrims$setTrim(@Nullable ShulkerTrim trim) {
    this.shulkerTrims$trim = trim;
  }
}