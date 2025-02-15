package dev.prangellplays.llgdragons.client.entity.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEggEntity;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class NightfuryEggModel extends GeoModel<NightfuryEggEntity> {
    @Override
    public Identifier getModelResource(NightfuryEggEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "geo/entity/nightfury/nightfury_egg.geo.json");
    }

    @Override
    public Identifier getTextureResource(NightfuryEggEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/nightfury_egg.png");
    }

    @Override
    public Identifier getAnimationResource(NightfuryEggEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "animations/entity/nightfury/nightfury_egg.animation.json");
    }
}
