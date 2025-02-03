package dev.prangellplays.llgdragons;

import dev.prangellplays.llgdragons.config.LLGDragonsConfig;
import dev.prangellplays.llgdragons.init.*;
import dev.prangellplays.llgdragons.item.DragosphereItem;
import dev.prangellplays.llgdragons.network.LLGDragonsServerPacket;
import dev.prangellplays.llgdragons.util.LLGDragonsRegistries;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LLGDragons implements ModInitializer {
	public static final String MOD_ID = "llgdragons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final List<String> dragosphereWhitelist = new ArrayList<>();

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, LLGDragonsConfig.class);
		LLGDragonsBlocks.init();
		LLGDragonsItems.init();
		LLGDragonsItemGroups.init();
		LLGDragonsEntities.init();
		LLGDragonsRegistries.init();
		LLGDragonsServerPacket.init();
		LLGDragonsSounds.init();

		DragosphereItem.DragosphereEventHandler.init();
		whitelist();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	private void whitelist() {
		dragosphereWhitelist.add("minecraft:wolf");
		dragosphereWhitelist.add("minecraft:donkey");
		dragosphereWhitelist.add("llgdragons:nightfury");
	}
}