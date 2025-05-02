package dev.prangellplays.llgdragons;

import dev.prangellplays.llgdragons.config.LLGDragonsConfig;
import dev.prangellplays.llgdragons.data.nightfury.NightfuryVariant;
import dev.prangellplays.llgdragons.util.LLGDragonsRegistries;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LLGDragons implements ModInitializer {
	public static final String MOD_ID = "llgdragons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final List<String> dragosphereWhitelist = new ArrayList<>();

	public static final RegistryKey<Registry<NightfuryVariant>> NIGHTFURY_VARIANT_KEY = RegistryKey.ofRegistry(Identifier.of(MOD_ID, "variant/nightfury_variant"));

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, LLGDragonsConfig.class);
		LLGDragonsRegistries.init();

		DynamicRegistries.registerSynced(NIGHTFURY_VARIANT_KEY, NightfuryVariant.CODEC);

		whitelist();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	private void whitelist() {
		dragosphereWhitelist.add("llgdragons:nightfury");
		dragosphereWhitelist.add("llgdragons:aztec_nightfury");
	}
}