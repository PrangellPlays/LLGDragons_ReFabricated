package dev.prangellplays.llgdragons.client.entity.nightfury.model.plasma_blast;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragonability.nightfury.PlasmaBlastEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class PlasmaBlastModel extends GeoModel<PlasmaBlastEntity> {
    @Override
    public Identifier getModelResource(PlasmaBlastEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "geo/entity/nightfury/plasma_blast.geo.json");
    }

    @Override
    public Identifier getTextureResource(PlasmaBlastEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/plasma_blast.png");
    }

    @Override
    public Identifier getAnimationResource(PlasmaBlastEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "animations/entity/nightfury/plasma_blast.animation.json");
    }
}