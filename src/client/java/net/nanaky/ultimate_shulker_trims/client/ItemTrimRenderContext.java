package net.nanaky.ultimate_shulker_trims.client;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.jetbrains.annotations.Nullable;

/**
 * Context for passing trim data through the item render pipeline.
 *
 * <p>The vanilla item rendering system doesn't pass ItemStack data to special model renderers
 * (ShulkerBoxModelRenderer ignores the stack entirely). This context bridges the gap by storing
 * trim data during ItemModelManager.update() and making it available during
 * ShulkerBoxModelRenderer.render().
 *
 * <p>For GUI items, rendering is deferred, so we use a WeakHashMap keyed by ItemStackRenderState
 * to persist trim data until the actual render call. A thread-local tracks the current
 * ItemStackRenderState being rendered.
 */
public final class ItemTrimRenderContext {
  private ItemTrimRenderContext() {}

  /**
   * Map of ItemStackRenderState to trim data. Uses weak keys so entries are automatically cleaned
   * up when ItemStackRenderState instances are garbage collected.
   */
  private static final Map<ItemStackRenderState, ShulkerTrim> TRIM_MAP = new WeakHashMap<>();

  /**
   * Thread-local tracking the current ItemStackRenderState being rendered. Set by
   * LayerRenderStateMixin just before SpecialModelRenderer.render() is called.
   */
  private static final ThreadLocal<ItemStackRenderState> CURRENT_RENDER_STATE =
      new ThreadLocal<>();

  /**
   * Store trim data for an ItemStackRenderState. Call this during ItemModelManager.update() when
   * processing a trimmed shulker.
   */
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

  /** Get trim data for an ItemStackRenderState without removing it. */
  @Nullable
  public static ShulkerTrim getTrim(ItemStackRenderState state) {
    synchronized (TRIM_MAP) {
      return TRIM_MAP.get(state);
    }
  }

  /**
   * Set the current ItemStackRenderState being rendered. Call this from LayerRenderState.render()
   * before invoking special model render.
   */
  public static void setCurrentRenderState(@Nullable ItemStackRenderState state) {
    if (state != null) {
      CURRENT_RENDER_STATE.set(state);
    } else {
      CURRENT_RENDER_STATE.remove();
    }
  }

  /** Get the current ItemStackRenderState being rendered. */
  @Nullable
  public static ItemStackRenderState getCurrentRenderState() {
    return CURRENT_RENDER_STATE.get();
  }

  /**
   * Get trim for the current render state (convenience method). Returns null if no current render
   * state or no trim for that state.
   */
  @Nullable
  public static ShulkerTrim getCurrentTrim() {
    ItemStackRenderState state = CURRENT_RENDER_STATE.get();
    if (state == null) {
      return null;
    }
    return getTrim(state);
  }

  /** Clear the current render state thread-local. */
  public static void clearCurrentRenderState() {
    CURRENT_RENDER_STATE.remove();
  }
}