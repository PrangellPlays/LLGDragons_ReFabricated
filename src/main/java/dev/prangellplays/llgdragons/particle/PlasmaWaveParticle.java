package dev.prangellplays.llgdragons.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PlasmaWaveParticle extends SpriteBillboardParticle {
    SpriteProvider basespriteSet;
    protected PlasmaWaveParticle(ClientWorld level, double xCoord, double yCoord, double zCoord, SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0.6F;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.scale = 1f;
        this.maxAge = 20;
        this.angle = (float) MathHelper.atan2(xd, zd);
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
        this.basespriteSet = spriteSet;
    }

    @Override
    public void tick() {
        super.tick();
        this.scale(0.01f + scale);
        if (this.scale > 30) {
            this.markDead();
        }
        this.setSpriteForAge(this.basespriteSet);
        this.fadeOut();
    }

    private void fadeOut() {
        this.scale = 6.0F / (float)this.maxAge * (float)this.age + 1.0F;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d camPos = camera.getPos();
        float x = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - camPos.getX());
        float y = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - camPos.getY());
        float z = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - camPos.getZ());

        Quaternionf q = new Quaternionf(
                0,
                MathHelper.sin(this.angle / 2),
                0,
                MathHelper.cos(this.angle / 2)
        );
        Vector3f[] pos = new Vector3f[]{new Vector3f(-1, 0, -1), new Vector3f(-1, 0, 1), new Vector3f(1, 0, 1), new Vector3f(1, 0, -1)};
        float i = this.getSize(tickDelta);

        for (int j = 0; j < 4; ++j) {
            Vector3f vec3f = pos[j];
            vec3f.rotate(q);
            vec3f.mul(i);
            vec3f.add(x, y, z);
        }

        float k = this.getMinU();
        float l = this.getMaxU();
        float n = this.getMaxV();
        float m = this.getMinV();
        int o = this.getBrightness(tickDelta);
        vertexConsumer.vertex(pos[0].x, pos[0].y, pos[0].z).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(pos[1].x, pos[1].y, pos[1].z).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(pos[2].x, pos[2].y, pos[2].z).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(pos[3].x, pos[3].y, pos[3].z).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
    }

    public int getBrightness(float tint) {
        return 240;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z, double dx, double dy, double dz) {
            return new PlasmaWaveParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}