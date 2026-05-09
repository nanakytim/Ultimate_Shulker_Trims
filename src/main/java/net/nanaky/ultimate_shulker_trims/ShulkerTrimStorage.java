package net.nanaky.ultimate_shulker_trims;

import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.ValueInput;
import org.jspecify.annotations.Nullable;

public final class ShulkerTrimStorage {
  private ShulkerTrimStorage() {}

  public static final String TRIM_KEY = "ultimate_shulker_trims:trim";
  public static final String PATTERN_KEY = "pattern";
  public static final String MATERIAL_KEY = "material";

  /** Write trim data into a CompoundTag (for NBT persistence). */
  public static void writeTrim(CompoundTag nbt, @Nullable ShulkerTrim trim) {
    if (trim == null) {
      nbt.remove(TRIM_KEY);
      return;
    }
    CompoundTag trimNbt = new CompoundTag();
    trimNbt.putString(PATTERN_KEY, trim.pattern());
    trimNbt.putString(MATERIAL_KEY, trim.material());
    nbt.put(TRIM_KEY, trimNbt);
  }

  /** Read trim data from a CompoundTag. */
  @Nullable
  public static ShulkerTrim readTrim(CompoundTag nbt) {
    CompoundTag trimNbt = nbt.getCompound(TRIM_KEY).orElse(null);
    if (trimNbt == null || trimNbt.isEmpty()) return null;

    Optional<String> pattern = trimNbt.getString(PATTERN_KEY);
    Optional<String> material = trimNbt.getString(MATERIAL_KEY);

    if (pattern.isEmpty() || material.isEmpty()) {
      UltimateShulkerTrims.LOGGER.warn("Invalid trim NBT: missing pattern or material");
      return null;
    }

    ShulkerTrim trim = new ShulkerTrim(pattern.get(), material.get());
    if (!trim.isValid()) {
      UltimateShulkerTrims.LOGGER.warn(
          "Invalid trim identifiers: pattern={}, material={}", pattern.get(), material.get());
      return null;
    }
    return trim;
  }

  /** Read trim from an ItemStack's CUSTOM_DATA component. */
  @Nullable
  public static ShulkerTrim readTrimFromItem(ItemStack stack) {
    CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
    if (customData == null) return null;
    return readTrim(customData.copyTag());
  }

  /** Write trim to an ItemStack's CUSTOM_DATA component. */
  public static void writeTrimToItem(ItemStack stack, @Nullable ShulkerTrim trim) {
    if (trim == null) {
      CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(TRIM_KEY));
      return;
    }
    CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
      CompoundTag trimNbt = new CompoundTag();
      trimNbt.putString(PATTERN_KEY, trim.pattern());
      trimNbt.putString(MATERIAL_KEY, trim.material());
      tag.put(TRIM_KEY, trimNbt);
    });
  }

  /** Read trim from a ValueInput (block entity persistence). */
  @Nullable
  public static ShulkerTrim readTrimFromData(ValueInput data) {
    Optional<ValueInput> trimData = data.child(TRIM_KEY);
    if (trimData.isEmpty()) return null;

    ValueInput trimView = trimData.get();
    Optional<String> pattern = trimView.getString(PATTERN_KEY);
    Optional<String> material = trimView.getString(MATERIAL_KEY);

    if (pattern.isEmpty() || material.isEmpty()) {
      UltimateShulkerTrims.LOGGER.warn("Invalid trim data: missing pattern or material");
      return null;
    }

    ShulkerTrim trim = new ShulkerTrim(pattern.get(), material.get());
    if (!trim.isValid()) {
      UltimateShulkerTrims.LOGGER.warn(
          "Invalid trim identifiers: pattern={}, material={}", pattern.get(), material.get());
      return null;
    }
    return trim;
  }
}