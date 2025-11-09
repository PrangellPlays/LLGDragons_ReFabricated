package dev.prangellplays.llgdragons;

import dev.prangellplays.llgdragons.data.datagen.*;
import dev.prangellplays.llgdragons.data.nightfury.eggcombination.NightfuryEggCombination;
import dev.prangellplays.llgdragons.data.nightfury.eggcombination.NightfuryEggCombinations;
import dev.prangellplays.llgdragons.data.nightfury.variant.NightfuryVariants;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class LLGDragonsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(LLGDragonsBuilderProvider::new);

        pack.addProvider(LLGDragonsModelProvider::new);
        pack.addProvider(LLGDragonsItemTagProvider::new);
        pack.addProvider(LLGDragonsBlockTagProvider::new);
        pack.addProvider(LLGDragonsLootTableProvider::new);
        pack.addProvider(LLGDragonsRecipeProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(LLGDragons.NIGHTFURY_VARIANT_KEY, NightfuryVariants::bootstrap);
		registryBuilder.addRegistry(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY, NightfuryEggCombinations::bootstrap);
	}

	@Override
	public void addJsonKeySortOrders(JsonKeySortOrderCallback callback) {
		// For variants
		callback.add("entity", 1);
		callback.add("name", 2);
		callback.add("texture", 3);

		// For egg combinations
		callback.add("parent_a", 4);
		callback.add("parent_b", 5);
		callback.add("possible_offspring", 6);
		callback.add("chance", 7);
	}

	public static class LLGDragonsBuilderProvider extends FabricDynamicRegistryProvider {
		public LLGDragonsBuilderProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
			entries.addAll(registries.getWrapperOrThrow(LLGDragons.NIGHTFURY_VARIANT_KEY));
			entries.addAll(registries.getWrapperOrThrow(LLGDragons.NIGHTFURY_EGG_COMBINATION_KEY));
		}

		@Override
		public String getName() {
			return "LLGDragons Variants";
		}
	}
}
