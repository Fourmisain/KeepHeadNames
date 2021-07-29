package fourmisain.keepheadnames;

import fourmisain.keepheadnames.mixin.LootFunctionTypesAccessor;
import fourmisain.keepheadnames.util.CopyLoreLootFunction;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyNameLootFunction;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class KeepHeadNames implements ModInitializer {
	private static final Identifier PLAYER_HEAD_LOOT_TABLE_ID = new Identifier("blocks/player_head");

	public static LootFunctionType COPY_LORE;

	@Nullable
	public static NbtList getLore(ItemStack itemStack) {
		NbtCompound compound = itemStack.getSubTag("display");

		if (compound != null && compound.contains("Lore", 9)) {
			return compound.getList("Lore", 8);
		}

		return null;
	}

	public static void setLore(ItemStack itemStack, @Nullable NbtList lore) {
		if (lore != null) {
			NbtCompound displayTag = itemStack.getOrCreateSubTag("display");
			displayTag.put("Lore", lore);
		} else {
			NbtCompound displayTag = itemStack.getSubTag("display");

			if (displayTag != null) {
				displayTag.remove("Lore");

				if (displayTag.isEmpty()) {
					itemStack.removeSubTag("display");
				}
			}
		}
	}

	@Override
	public void onInitialize() {
		COPY_LORE = LootFunctionTypesAccessor.register("copy_lore", new CopyLoreLootFunction.Serializer());

		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
			if (id.equals(PLAYER_HEAD_LOOT_TABLE_ID)) {
				// ensure mined player heads keep their display tag and lore
				setter.set(LootTable.builder()
					.pool(LootPool.builder()
						.rolls(ConstantLootTableRange.create(1))
						.with(ItemEntry.builder(Items.PLAYER_HEAD)
							.apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY))
							.apply(CopyLoreLootFunction.builder(CopyLoreLootFunction.Source.BLOCK_ENTITY))
							.apply(CopyNbtLootFunction.builder(CopyNbtLootFunction.Source.BLOCK_ENTITY)
								.withOperation("SkullOwner", "SkullOwner"))
						)
					)
					.build()
				);
			}
		});
	}
}