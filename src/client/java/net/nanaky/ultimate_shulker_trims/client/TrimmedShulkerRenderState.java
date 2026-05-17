package net.nanaky.ultimate_shulker_trims.client;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import org.jetbrains.annotations.Nullable;


public interface TrimmedShulkerRenderState {
  @Nullable
  ShulkerTrim shulkerTrims$getTrim();

  void shulkerTrims$setTrim(@Nullable ShulkerTrim trim);
}
