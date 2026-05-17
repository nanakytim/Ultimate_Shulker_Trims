package net.nanaky.ultimate_shulker_trims.client;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.jetbrains.annotations.Nullable;


public final class ItemTrimRenderContext {
  private ItemTrimRenderContext() {}

  
  private static final Map<ItemStackRenderState, ShulkerTrim> TRIM_MAP = new HashMap<>();

  
  private static final ThreadLocal<ItemStackRenderState> CURRENT_RENDER_STATE =
      new ThreadLocal<>();

  
  public static void setTrim(ItemStackRenderState state, @Nullable ShulkerTrim trim) {
    if (trim != null) {
      synchronized (TRIM_MAP) {
        TRIM_MAP.put(state, trim);
      }
    } else {
      synchronized (TRIM_MAP) {
        TRIM_MAP.remove(state);
      }
    }
  }

  
  @Nullable
  public static ShulkerTrim getTrim(ItemStackRenderState state) {
    synchronized (TRIM_MAP) {
      return TRIM_MAP.get(state);
    }
  }

  
  public static void setCurrentRenderState(@Nullable ItemStackRenderState state) {
    if (state != null) {
      CURRENT_RENDER_STATE.set(state);
    } else {
      CURRENT_RENDER_STATE.remove();
    }
  }

  
  @Nullable
  public static ItemStackRenderState getCurrentRenderState() {
    return CURRENT_RENDER_STATE.get();
  }

  
  @Nullable
  public static ShulkerTrim getCurrentTrim() {
    ItemStackRenderState state = CURRENT_RENDER_STATE.get();
    if (state == null) {
      return null;
    }
    return getTrim(state);
  }

  
  public static void clearCurrentRenderState() {
    CURRENT_RENDER_STATE.remove();
  }
}