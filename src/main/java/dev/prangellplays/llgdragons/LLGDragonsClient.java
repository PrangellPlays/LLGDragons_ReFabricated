package dev.prangellplays.llgdragons;

import dev.prangellplays.llgdragons.client.init.LLGDragonsEntityRenderer;
import dev.prangellplays.llgdragons.init.LLGDragonsBlocks;
import dev.prangellplays.llgdragons.init.LLGDragonsItems;
import dev.prangellplays.llgdragons.item.DragosphereItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.nbt.NbtCompound;

public class LLGDragonsClient implements ClientModInitializer {
    //Keybinding
    public static KeyBinding descend;
    public static KeyBinding dragonAttackPrimary;
    public static KeyBinding dragonAttackSecondary;
    public static KeyBinding bewilderbeastTalk;
    public static KeyBinding bewilderbeastRoar;

    @Override
    public void onInitializeClient() {
        LLGDragonsEntityRenderer.init();

        //Block Render Layer Map
        BlockRenderLayerMap.INSTANCE.putBlock(LLGDragonsBlocks.NIGHTFURY_HEAD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LLGDragonsBlocks.HTTYD_NIGHTFURY_HEAD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LLGDragonsBlocks.ALBINO_NIGHTFURY_HEAD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(LLGDragonsBlocks.WHITE_NIGHTFURY_HEAD, RenderLayer.getCutout());

        //Model Predicate
        ModelPredicateProviderRegistry.register(LLGDragonsItems.DRAGOSPHERE, LLGDragons.id("dragosphere_full"), (stack, world, entity, i) -> {
            return DragosphereItem.hasCapturedDragon(stack) ? 1.0F : 0.0F;
        });

        //Keybinding
        descend = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.llgdragons.descend", InputUtil.Type.KEYSYM, 162, "category.llgdragons"));
        dragonAttackPrimary = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.llgdragons.dragon_attack_primary", InputUtil.Type.KEYSYM, 66, "category.llgdragons"));
        dragonAttackSecondary = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.llgdragons.dragon_attack_secondary", InputUtil.Type.KEYSYM, 78, "category.llgdragons"));
        bewilderbeastTalk = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.llgdragons.bewilderbeast_talk", InputUtil.Type.KEYSYM, 71, "category.llgdragons"));
        bewilderbeastRoar = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.llgdragons.bewilderbeast_roar", InputUtil.Type.KEYSYM, 72, "category.llgdragons"));
    }
}
