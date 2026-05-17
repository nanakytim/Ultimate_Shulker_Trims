package net.nanaky.ultimate_shulker_trims.client;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;


public final class OrthographicTrimRenderLayer {

  private OrthographicTrimRenderLayer() {}

  private static final RenderType ARMOR_TRIMS =
      RenderTypes.armorCutoutNoCull(Sheets.ARMOR_TRIMS_SHEET);

  public static RenderType getArmorTrims() {
    return ARMOR_TRIMS;
  }
}