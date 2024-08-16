package io.github.fourmisain.keepheadnames;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.*;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.dynamic.NullOps;

import java.util.Optional;

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
						// search for {SkullOwner:{Name:...}}
						var skullOwner = dynamic.get("SkullOwner").result();
						if (skullOwner.isPresent()) {
							var name = skullOwner.get().get("Name").result();
							if (name.isPresent()) {
								Optional<String> nameAsStr = name.get().asString().result();
								if (nameAsStr.isPresent()) {
									KeepHeadNames.LOGGER.info("fixing skull with SkullOwner Name \"{}\"", nameAsStr.get());

									// convert string to json
									Optional<Object> result = TextCodecs.codec(Integer.MAX_VALUE).encodeStart(JavaOps.INSTANCE, Text.literal(nameAsStr.get())).result();
									if (result.isPresent() && result.get() instanceof String json) {
										// convert to item_name component
										Dynamic<?> components = dynamic.get("components").orElseEmptyMap();
										components = components.set("minecraft:item_name", dynamic.createString(json));
										dynamic = dynamic.set("components", components);
									} else {
										KeepHeadNames.LOGGER.error("couldn't JSON encode name: {}", nameAsStr.get());
									}
								} else {
									KeepHeadNames.LOGGER.error("couldn't convert skull with SkullOwner Name \"{}\"", name.get());
								}
							}
						}

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
