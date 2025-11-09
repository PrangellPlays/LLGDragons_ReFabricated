package dev.prangellplays.llgdragons.client.entity.layer;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public class RenderDummyPlayer extends OtherClientPlayerEntity {
    public RenderDummyPlayer(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public boolean isSpectator() { return false; }
    @Override
    public boolean isCreative() { return false; }
}
