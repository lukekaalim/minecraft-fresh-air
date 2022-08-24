package com.lukekaalim.mods.fresh_air;

import org.jetbrains.annotations.NotNull;

import com.lukekaalim.mods.interactions.BlockTarget;
import com.lukekaalim.mods.interactions.EntityTarget;
import com.lukekaalim.mods.interactions.Target;

import dev.lambdaurora.spruceui.hud.Hud;
import dev.lambdaurora.spruceui.hud.component.TextHudComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PlayerInteractionHUD extends Hud {

  TextHudComponent text;
  PlayerInteractionManager interation;
  
  public PlayerInteractionHUD() {
    super(new Identifier(FreshAirMod.NAMESPACE, "player_interaction"));
  }

  @Override
  public void init(@NotNull MinecraftClient client, int screenWidth, int screenHeight) {
    super.init(client, screenWidth, screenHeight);
    this.text = new TextHudComponent(new Identifier(FreshAirMod.NAMESPACE, "player_interaction/text"), 0, 0, Text.of("Hello Ari"));
    components.add(text);
    if (client.cameraEntity instanceof InteractingPlayer player) {
      interation = player.getInteractionManager();
    }
  }

  @Override
  public boolean hasTicks() {
    return true;
  }

  @Override
  public void tick() {
    var entityDescriptions = interation.entityTargets
      .stream()
      .map(et -> et.Describe().getString())
      .reduce((a, b) -> a + ", " + b)
      .orElse("[No Block]");
    var blockDescriptions = interation.blockTargets
      .stream()
      .map(et -> et.Describe().getString())
      .reduce((a, b) -> a + ", " + b)
      .orElse("[No Entity]");
      
    text.setText(Text.literal(entityDescriptions + ", " + blockDescriptions));
  }
}
