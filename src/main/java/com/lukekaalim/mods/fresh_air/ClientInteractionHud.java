package com.lukekaalim.mods.fresh_air;

import org.jetbrains.annotations.NotNull;

import dev.lambdaurora.spruceui.hud.Hud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ClientInteractionHud extends Hud {

  public ClientInteractionHud() {
    super(new Identifier(FreshAirMod.NAMESPACE, "hud/client_interaction"));
  }
  
  @Override
	public void init(@NotNull MinecraftClient client, int screenWidth, int screenHeight) {
		this.components.clear();
	}
}
