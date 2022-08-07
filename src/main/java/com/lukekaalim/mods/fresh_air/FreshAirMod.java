package com.lukekaalim.mods.fresh_air;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.midnightdust.midnightcontrols.MidnightControls;
import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.MidnightInput;
import eu.midnightdust.midnightcontrols.client.controller.InputManager;

public class FreshAirMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("FRESH_AIR");
	public static PlayerInteractionRenderer INTERACTION_RENDERER = new PlayerInteractionRenderer();
	
	@Override
	public void onInitialize() {
		WorldRenderable.RENDERABLES.put(Identifier.of("fresh_air", "player_interaction_renderer"), INTERACTION_RENDERER);
		CommandRegistrationCallback.EVENT.register((dispacher, access, environment) -> {
			dispacher.register(CommandManager.literal("reload").executes(context -> {
				return Reload();
			}));
		});
	}

	int Reload() {
		LOGGER.info("RELOADED");
		MidnightControlsClient.get().input.notify();
		return 0;
	}
}
