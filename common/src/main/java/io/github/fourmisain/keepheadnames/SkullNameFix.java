package io.github.fourmisain.keepheadnames;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.TypeReferences;

public class SkullNameFix extends DataFix {
	public SkullNameFix(Schema outputSchema) {
		super(outputSchema, false);
	}

	@Override
	protected TypeRewriteRule makeRule() {
		return fixTypeEverywhereTyped(
			"Keep Head Names skull name fix",
			getInputSchema().getType(TypeReferences.BLOCK_ENTITY),
			typed -> typed.updateTyped(
				DSL.namedChoice("minecraft:skull", getInputSchema().getChoiceType(TypeReferences.BLOCK_ENTITY, "minecraft:skull")),
				getOutputSchema().getChoiceType(TypeReferences.BLOCK_ENTITY, "minecraft:skull"),
				typedx -> typedx.update(
					DSL.remainderFinder(),
					dynamic -> {
						dynamic.get("CustomName").result().ifPresent(name -> KeepHeadNames.LOGGER.info("fixing skull with name {}...", name.asString().result().orElse(name.toString())));

						dynamic = dynamic.renameField("CustomName", "custom_name");

						// move Lore to components -> minecraft:lore
						var oldLore = dynamic.get("Lore").result();
						if (oldLore.isPresent()) {
							Dynamic<?> components = dynamic.get("components").orElseEmptyMap();
							components = components.set("minecraft:lore", oldLore.get());

							dynamic = dynamic.set("components", components);
							dynamic = dynamic.remove("Lore");
						}

						return dynamic;
					}
				)
			)
		);
	}
}
