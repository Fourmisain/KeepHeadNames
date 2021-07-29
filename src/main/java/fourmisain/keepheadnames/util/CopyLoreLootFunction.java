package fourmisain.keepheadnames.util;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import fourmisain.keepheadnames.KeepHeadNames;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.JsonHelper;

import java.util.Set;

import static fourmisain.keepheadnames.KeepHeadNames.setLore;

public class CopyLoreLootFunction extends ConditionalLootFunction {
	final CopyLoreLootFunction.Source source;

	public CopyLoreLootFunction(LootCondition[] lootConditions, CopyLoreLootFunction.Source source) {
		super(lootConditions);
		this.source = source;
	}

	@Override
	public LootFunctionType getType() {
		return KeepHeadNames.COPY_LORE;
	}

	@Override
	public Set<LootContextParameter<?>> getRequiredParameters() {
		return ImmutableSet.of(this.source.parameter);
	}

	@Override
	public ItemStack process(ItemStack stack, LootContext context) {
		Object object = context.get(this.source.parameter);

		if (object instanceof Loreable loreable) {
			setLore(stack, loreable.getLore());
		}

		return stack;
	}

	public static Builder<?> builder(CopyLoreLootFunction.Source source) {
		return builder((conditions) -> new CopyLoreLootFunction(conditions, source));
	}

	public static class Serializer extends ConditionalLootFunction.Serializer<CopyLoreLootFunction> {
		public void toJson(JsonObject jsonObject, CopyLoreLootFunction copyNameLootFunction, JsonSerializationContext jsonSerializationContext) {
			super.toJson(jsonObject, copyNameLootFunction, jsonSerializationContext);
			jsonObject.addProperty("source", copyNameLootFunction.source.name);
		}

		public CopyLoreLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
			CopyLoreLootFunction.Source source = CopyLoreLootFunction.Source.valueOf(JsonHelper.getString(jsonObject, "source"));
			return new CopyLoreLootFunction(lootConditions, source);
		}
	}

	public enum Source {
		THIS("this", LootContextParameters.THIS_ENTITY),
		KILLER("killer", LootContextParameters.KILLER_ENTITY),
		KILLER_PLAYER("killer_player", LootContextParameters.LAST_DAMAGE_PLAYER),
		BLOCK_ENTITY("block_entity", LootContextParameters.BLOCK_ENTITY);

		public final String name;
		public final LootContextParameter<?> parameter;

		Source(String name, LootContextParameter<?> parameter) {
			this.name = name;
			this.parameter = parameter;
		}
	}
}
