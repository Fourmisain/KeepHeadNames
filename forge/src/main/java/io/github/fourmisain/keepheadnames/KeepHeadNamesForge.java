package io.github.fourmisain.keepheadnames;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod(KeepHeadNames.MOD_ID)
public class KeepHeadNamesForge {
	public KeepHeadNamesForge() {
		// this mod is server-only
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
			() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		FMLJavaModLoadingContext.get().getModEventBus().addListener(KeepHeadNamesForge::commonSetup);
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(KeepHeadNames::init);
	}
}
