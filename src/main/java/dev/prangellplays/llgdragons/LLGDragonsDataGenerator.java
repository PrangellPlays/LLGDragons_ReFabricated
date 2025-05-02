package dev.prangellplays.llgdragons;

import dev.prangellplays.llgdragons.data.nightfury.NightfuryVariants;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LLGDragonsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(LLGDragonsBuilderProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(LLGDragons.NIGHTFURY_VARIANT_KEY, NightfuryVariants::bootstrap);
	}

	@Override
	public void addJsonKeySortOrders(JsonKeySortOrderCallback callback) {
		callback.add("entity", 1);
		callback.add("name", 2);
		callback.add("texture", 3);
	}

	public static class LLGDragonsBuilderProvider extends FabricDynamicRegistryProvider {
		public LLGDragonsBuilderProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
			entries.addAll(registries.getWrapperOrThrow(LLGDragons.NIGHTFURY_VARIANT_KEY));
		}

		@Override
		public String getName() {
			return "LLGDragons Variants";
		}
	}
}
