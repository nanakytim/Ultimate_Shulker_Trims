package net.nanaky.ultimate_shulker_trims;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import org.jetbrains.annotations.Nullable;


public interface TrimmedShulkerBox {
  
  @Nullable
  ShulkerTrim shulkerTrims$getTrim();

  
  void shulkerTrims$setTrim(@Nullable ShulkerTrim trim);

  
  default boolean shulkerTrims$hasTrim() {
    return shulkerTrims$getTrim() != null;
  }
}
