package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class ExampleClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ExampleMod.BOX_SCREEN_HANDLER, BoxScreen::new);
    }
}

