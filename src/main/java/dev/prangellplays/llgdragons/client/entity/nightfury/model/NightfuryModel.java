package dev.prangellplays.llgdragons.client.entity.nightfury.model;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class NightfuryModel extends GeoModel<NightfuryEntity> {
    @Override
    public Identifier getModelResource(NightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "geo/entity/nightfury/nightfury.geo.json");
    }

    @Override
    public Identifier getTextureResource(NightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/nightfury.png");
    }

    @Override
    public Identifier getAnimationResource(NightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "animations/entity/nightfury/nightfury.animation.json");
    }
}
