package net.nanaky.ultimate_shulker_trims.client;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public final class ShulkerTrimRenderer {
  private ShulkerTrimRenderer() {}

  public static Identifier getTrimTextureId(ShulkerTrim trim) {
    String patternPath = getPath(trim.pattern());
    String materialPath = getPath(trim.material());
    return Identifier.fromNamespaceAndPath(
        "ultimate_shulker_trims", "trims/entity/shulker/" + patternPath + "_" + materialPath);
  }

  public static SpriteId getTrimSpriteId(ShulkerTrim trim) {
    return new SpriteId(Sheets.ARMOR_TRIMS_SHEET, getTrimTextureId(trim));
  }

  private static String getPath(@Nullable String identifier) {
    if (identifier == null) return "unknown";
    int colonIndex = identifier.indexOf(':');
    if (colonIndex >= 0 && colonIndex < identifier.length() - 1) {
      return identifier.substring(colonIndex + 1);
    }
    return identifier;
  }
}