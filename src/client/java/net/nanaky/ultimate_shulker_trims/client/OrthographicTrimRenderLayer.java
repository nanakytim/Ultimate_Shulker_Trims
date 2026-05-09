package net.nanaky.ultimate_shulker_trims.client;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

/**
 * Provides render layers for trim overlays on shulker boxes.
 *
 * <p>Note: In 26.1, RenderType construction is no longer accessible from mod code.
 * The custom orthographic projection fix from 1.21.x is no longer implementable.
 * We use the standard armor trim render type which includes VIEW_OFFSET_Z_LAYERING.
 * This works correctly in perspective projection. Orthographic projection (OrthoCamera mod)
 * may exhibit minor Z-fighting but cannot be fixed without access to RenderType internals.
 */
public final class OrthographicTrimRenderLayer {

  private OrthographicTrimRenderLayer() {}

  private static final RenderType ARMOR_TRIMS =
      RenderTypes.armorCutoutNoCull(Sheets.ARMOR_TRIMS_SHEET);

  public static RenderType getArmorTrims() {
    return ARMOR_TRIMS;
  }
}