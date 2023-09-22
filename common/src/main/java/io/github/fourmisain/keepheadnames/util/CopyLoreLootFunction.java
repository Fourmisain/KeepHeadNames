package io.github.fourmisain.keepheadnames.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fourmisain.keepheadnames.KeepHeadNames;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.nbt.LootNbtProvider;
import net.minecraft.loot.provider.nbt.LootNbtProviderTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.List;
import java.util.Set;

import static io.github.fourmisain.keepheadnames.KeepHeadNames.setLore;

public class CopyLoreLootFunction extends ConditionalLootFunction {
	public static final Codec<CopyLoreLootFunction> CODEC = RecordCodecBuilder.create(
		instance -> method_53344(instance)
			.and(LootNbtProviderTypes.CODEC.fieldOf("source").forGetter(lootFunction -> lootFunction.source))
			.apply(instance, CopyLoreLootFunction::new)
	);

	final LootNbtProvider source;

	public CopyLoreLootFunction(List<LootCondition> lootConditions, LootNbtProvider source) {
		super(lootConditions);
		this.source = source;
	}

	@Override
	public LootFunctionType getType() {
		return KeepHeadNames.COPY_LORE;
	}

	@Override
	public Set<LootContextParameter<?>> getRequiredParameters() {
		return source.getRequiredParameters();
	}

	@Override
	public ItemStack process(ItemStack stack, LootContext context) {
		NbtElement nbtElement = source.getNbt(context);

		if (nbtElement != null && nbtElement.getType() == NbtElement.COMPOUND_TYPE) {
			NbtList lore = ((NbtCompound) nbtElement).getList("Lore", NbtElement.STRING_TYPE);
			setLore(stack, lore);
		}

		return stack;
	}
}
