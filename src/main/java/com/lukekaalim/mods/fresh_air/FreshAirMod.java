package com.lukekaalim.mods.fresh_air;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lukekaalim.mods.interactions.registry.GlobalInteractionRegistry;
import com.lukekaalim.mods.interactions.vanilla.VanillaInteractionRegistry;
import com.lukekaalim.mods.rendering_hooks.v0.EntityRendererRegistry;

import dev.lambdaurora.spruceui.hud.HudManager;
import eu.midnightdust.midnightcontrols.MidnightControls;
import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.MidnightInput;
import eu.midnightdust.midnightcontrols.client.controller.InputManager;

public class FreshAirMod implements ModInitializer {
	public static final String NAMESPACE = "fresh_air";
	public static final Logger LOGGER = LoggerFactory.getLogger("FRESH_AIR");

	public static PlayerInteractionRenderer INTERACTION_RENDERER = new PlayerInteractionRenderer();
	public static EntityRendererRegistry ENTITY_RENDERER_REGISTRY = new EntityRendererRegistry();
	
	@Override
	public void onInitialize() {
		ENTITY_RENDERER_REGISTRY.Register(EntityType.PLAYER, new PlayerEntityInteractionRenderer());
		WorldRenderable.RENDERABLES.put(Identifier.of("fresh_air", "player_interaction_renderer"), INTERACTION_RENDERER);

		CommandRegistrationCallback.EVENT.register((dispacher, access, environment) -> {
			dispacher.register(CommandManager.literal("reload").executes(context -> {
				return Reload();
			}));
			dispacher.register(CommandManager.literal("toggle_lock").executes(context -> {
				var client = MinecraftClient.getInstance();
				if (client.cameraEntity instanceof InteractingPlayer player) {
					var im = player.getInteractionManager();
					im.ToggleLock();
					return 0;
				}
				return 1;
			}));
		});
		
		HudManager.register(new PlayerInteractionHUD());
		//EntityRendererRegistry.register(entityType, entityRendererFactory);
		Reload();
	}

	int Reload() {
		LOGGER.info("RELOADED");
    GlobalInteractionRegistry.RegisterInteractionProvider(new VanillaInteractionRegistry());

		return 0;
	}
}
