package dev.prangellplays.llgdragons.client.entity.nightfury;

import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.dragon.NightfuryEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

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
