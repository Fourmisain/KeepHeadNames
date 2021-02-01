package fourmisain.keepheadnames;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeepHeadNames implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("keepheadnames");

    private static final Identifier PLAYER_HEAD_LOOT_TABLE_ID = new Identifier("blocks/player_head");

    @Override
    public void onInitialize() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (id.equals(PLAYER_HEAD_LOOT_TABLE_ID)) {
                // ensure mined player heads keep their display tag
                setter.set(LootTable.builder()
                    .pool(LootPool.builder()
                        .rolls(ConstantLootTableRange.create(1))
                        .with(ItemEntry.builder(Items.PLAYER_HEAD)
                            .apply(CopyNbtLootFunction.builder(CopyNbtLootFunction.Source.BLOCK_ENTITY)
                                .withOperation("SkullOwner", "SkullOwner")
                                .withOperation("display", "display"))
                        )
                    )
                    .build()
                );
            }
        });
    }
}