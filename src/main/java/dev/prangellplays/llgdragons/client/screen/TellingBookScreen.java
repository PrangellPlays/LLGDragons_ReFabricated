package dev.prangellplays.llgdragons.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.prangellplays.llgdragons.LLGDragons;
import dev.prangellplays.llgdragons.entity.DragonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class TellingBookScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier(LLGDragons.MOD_ID, "textures/gui/telling_book.png");

    public TellingBookScreen() {
        super(Text.translatable("item.llgdragons.telling_book").formatted(Formatting.DARK_GREEN));
    }

    @Override
    protected void init() {
    }

    public Entity getTargetedEntity() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.crosshairTarget instanceof EntityHitResult entityHit) {
            return entityHit.getEntity();
        }

        return null;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Entity target = getTargetedEntity();
        DragonEntity dragon = (DragonEntity) target;
        renderBackground(context);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - 146) / 2;
        int y = (height - 180) / 2;
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        context.drawTexture(TEXTURE, x, y, 0, 0, 146, 180);
        TextRenderer textRenderer = MinecraftClient.getInstance().inGameHud.getTextRenderer();
        //Page
        context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book"), x + 35, y + 15, 43520, false);
        context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book.name").append(":"), x + 14, y + 27, 0, false);
        if (dragon.hasCustomName()) {
            context.drawText(textRenderer, dragon.getCustomName(), x + 26, y + 39, 0, false);
        } else {
            context.drawText(textRenderer, "", x + 26, y + 39, 0, false);
        }

        context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book.species").append(":"), x + 14, y + 51, 0, false);
        context.drawText(textRenderer, Text.translatable(target.getType().getTranslationKey()), x + 26, y + 63, 0, false);
        context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book.owner").append(":"), x + 14, y + 75, 0, false);
        context.drawText(textRenderer, Objects.requireNonNull(dragon.getOwner()).getName(), x + 26, y + 87, 0, false);
        context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book.gender").append(":"), x + 14, y + 99, 0, false);
        if (dragon.isFemale()) {
            context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book.gender.female"), x + 26, y + 111, 0, false);
        } else {
            context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book.gender.male"), x + 26, y + 111, 0, false);
        }
        context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book.age").append(":"), x + 14, y + 123, 0, false);
        if (dragon.getDayCountAge() < 6) {
            context.drawText(textRenderer, Text.literal(String.valueOf(dragon.getDayCountAge())).append(Text.literal(" ").append(Text.translatable("item.llgdragons.telling_book.age.days").append(Text.literal(" (").append(Text.translatable("item.llgdragons.telling_book.age.baby").append(Text.literal(")")))))), x + 26, y + 135, 0, false);
        } else {
            context.drawText(textRenderer, Text.literal(String.valueOf(dragon.getDayCountAge())).append(Text.literal(" ").append(Text.translatable("item.llgdragons.telling_book.age.days").append(Text.literal(" (").append(Text.translatable("item.llgdragons.telling_book.age.adult").append(Text.literal(")")))))), x + 26, y + 135, 0, false);
        }
        context.drawText(textRenderer, Text.translatable("item.llgdragons.telling_book.favourite_food").append(":"), x + 14, y + 147, 0, false);
        context.drawText(textRenderer, dragon.getFavouriteFood().getName(), x + 26, y + 159, 0, false);
    }
}