package net.fabricmc.example;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BoxScreen extends HandledScreen<BoxScreenHandler> {
  //A path to the gui texture. In this example we use the texture from the dispenser
  private static final Identifier TEXTURE = new Identifier("modid", "romance_ui.png");

  public BoxScreen(BoxScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
  }

  @Override
  protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);
    int x = (width - backgroundWidth) / 2;
    int y = (height - backgroundHeight) / 2;
    drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    //super.render(matrices, mouseX, mouseY, delta);
    renderBackground(matrices);
    if (handler.item != null)
        this.itemRenderer.renderGuiItemIcon(new ItemStack(handler.item), 0, 0);
        
    this.drawBackground(matrices, delta, mouseX, mouseY);
    //this.drawForeground(matrices, mouseX, mouseY);
    //drawMouseoverTooltip(matrices, mouseX, mouseY);
  }
}

