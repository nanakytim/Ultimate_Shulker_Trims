package net.nanaky.ultimate_shulker_trims.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.nanaky.ultimate_shulker_trims.ShulkerTrimStorage;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public class ShulkerTrimRecipe implements SmithingRecipe {

  public static final MapCodec<ShulkerTrimRecipe> CODEC =
      RecordCodecBuilder.mapCodec(
          instance ->
              instance
                  .group(
                      Ingredient.CODEC.fieldOf("template").forGetter(r -> r.template),
                      Ingredient.CODEC.fieldOf("base").forGetter(r -> r.base),
                      Ingredient.CODEC.fieldOf("addition").forGetter(r -> r.addition))
                  .apply(instance, ShulkerTrimRecipe::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, ShulkerTrimRecipe> STREAM_CODEC =
      StreamCodec.composite(
          Ingredient.CONTENTS_STREAM_CODEC, r -> r.template,
          Ingredient.CONTENTS_STREAM_CODEC, r -> r.base,
          Ingredient.CONTENTS_STREAM_CODEC, r -> r.addition,
          ShulkerTrimRecipe::new);

  private final Ingredient template;
  private final Ingredient base;
  private final Ingredient addition;

  public ShulkerTrimRecipe(Ingredient template, Ingredient base, Ingredient addition) {
    this.template = template;
    this.base = base;
    this.addition = addition;
  }

  @Override
  public ItemStack assemble(SmithingRecipeInput input) {
    ItemStack baseStack = input.base();
    if (baseStack.isEmpty()) return ItemStack.EMPTY;

    Holder<TrimMaterial> material = input.addition().get(DataComponents.PROVIDES_TRIM_MATERIAL);
    if (material == null) return ItemStack.EMPTY;

    Identifier itemId = BuiltInRegistries.ITEM.getKey(input.template().getItem());
    String patternName = extractPatternFromTemplateId(itemId);
    if (patternName == null) return ItemStack.EMPTY;

    String pattern = itemId.getNamespace() + ":" + patternName;
    String materialId = material.getRegisteredName();
    ShulkerTrim trim = new ShulkerTrim(pattern, materialId);

    ItemStack result = baseStack.copyWithCount(1);
    ShulkerTrimStorage.writeTrimToItem(result, trim);
    return result;
  }

  private static String extractPatternFromTemplateId(Identifier itemId) {
    String path = itemId.getPath();
    String suffix = "_armor_trim_smithing_template";
    if (path.endsWith(suffix)) {
      return path.substring(0, path.length() - suffix.length());
    }
    return null;
  }

  @Override
  public Optional<Ingredient> templateIngredient() {
    return Optional.of(template);
  }

  @Override
  public Ingredient baseIngredient() {
    return base;
  }

  @Override
  public Optional<Ingredient> additionIngredient() {
    return Optional.of(addition);
  }

  @Override
  public boolean showNotification() {
    return true;
  }

  @Override
  public String group() {
    return "";
  }

  @Override
  public RecipeSerializer<ShulkerTrimRecipe> getSerializer() {
    return ShulkerTrimsRecipeSerializers.SHULKER_TRIM;
  }

  @Override
  public PlacementInfo placementInfo() {
    return PlacementInfo.create(List.of(template, base, addition));
  }
}