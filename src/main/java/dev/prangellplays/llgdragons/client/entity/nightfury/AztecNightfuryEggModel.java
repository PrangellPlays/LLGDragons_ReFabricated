package dev.prangellplays.llgdragons.client.entity.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragon.AztecNightfuryEggEntity;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEggEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class AztecNightfuryEggModel extends GeoModel<AztecNightfuryEggEntity> {
    @Override
    public Identifier getModelResource(AztecNightfuryEggEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "geo/entity/nightfury/nightfury_egg.geo.json");
    }

    @Override
    public Identifier getTextureResource(AztecNightfuryEggEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/aztec_nightfury_egg.png");
    }

    @Override
    public Identifier getAnimationResource(AztecNightfuryEggEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "animations/entity/nightfury/nightfury_egg.animation.json");
    }
}
