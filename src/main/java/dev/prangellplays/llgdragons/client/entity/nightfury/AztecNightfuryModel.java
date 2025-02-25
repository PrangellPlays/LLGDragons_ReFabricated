package dev.prangellplays.llgdragons.client.entity.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragon.AztecNightfuryEntity;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class AztecNightfuryModel extends GeoModel<AztecNightfuryEntity> {
    @Override
    public Identifier getModelResource(AztecNightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "geo/entity/nightfury/aztec_nightfury.geo.json");
    }

    @Override
    public Identifier getTextureResource(AztecNightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "textures/entity/nightfury/aztec_nightfury.png");
    }

    @Override
    public Identifier getAnimationResource(AztecNightfuryEntity animatable) {
        return new Identifier(LLGDragons.MOD_ID, "animations/entity/nightfury/nightfury.animation.json");
    }
}
