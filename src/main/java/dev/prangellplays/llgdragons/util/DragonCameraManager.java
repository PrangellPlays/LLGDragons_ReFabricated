package dev.prangellplays.llgdragons.util;

import dev.prangellplays.llgdragons.entity.dragon.nightfury.NightfuryEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;

public class DragonCameraManager {
    private static Perspective previousPerspective = Perspective.FIRST_PERSON;

    public static void onDragonMount() {
        previousPerspective = MinecraftClient.getInstance().options.getPerspective();
        MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

    public static void onDragonDismount() {
        MinecraftClient.getInstance().options.setPerspective(previousPerspective);
    }

    @SuppressWarnings("ConstantConditions") // player should never be null at time of calling
    public static void setMountCameraAngles(Camera camera) {
        if (MinecraftClient.getInstance().player.getVehicle() instanceof NightfuryEntity && !MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
            //Perspective offset = MinecraftClient.getInstance().options.getPerspective() == Perspective.THIRD_PERSON_BACK ? Perspective.THIRD_PERSON_BACK : Perspective.THIRD_PERSON_FRONT;
            camera.moveBy(0, 2, 0);
            camera.moveBy(-camera.clipToSpace(6), 0, 0); // do distance calcs AFTER our new position is set
        }
    }
}
