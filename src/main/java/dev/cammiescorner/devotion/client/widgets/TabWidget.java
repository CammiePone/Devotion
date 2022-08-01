package dev.cammiescorner.devotion.client.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.screens.GuideBookScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TabWidget extends PressableWidget {
	public static final Identifier TEXTURE = Devotion.id("textures/gui/scripts_of_devotion_icons.png");
	public static final MinecraftClient client = MinecraftClient.getInstance();
	private final boolean top;
	private final Identifier tabId;
	private final Item item;
	private final PressAction pressAction;
	private double yPos = 0;

	public TabWidget(int x, int y, boolean top, Identifier tabId, Item item, PressAction action) {
		super(x, y, 24, 40, Text.translatable("tab." + tabId.method_42094()));
		this.top = top;
		this.tabId = tabId;
		this.item = item;
		this.pressAction = action;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		MatrixStack modelViewMatrix = RenderSystem.getModelViewStack();
		int screenX = (client.getWindow().getScaledWidth() - 378) / 2;
		int screenY = (client.getWindow().getScaledHeight() - 250) / 2;
		int u = 0;

		if(client.currentScreen instanceof GuideBookScreen guideBookScreen) {
			if(!guideBookScreen.tabId.equals(tabId)) {
				u = 24;

				if(isHoveredOrFocused())
					yPos = Math.min(17, yPos + 1);
				else
					yPos = Math.max(0, yPos - 1);
			}
			else {
				yPos = 17;
			}
		}

		matrices.push();

		if(top) {
			matrices.translate(0, yPos, 0);
			drawTexture(matrices, x, y - 14, 104 + u, 0, width, height);
		}
		else {
			matrices.translate(0, -yPos, 0);
			drawTexture(matrices, x, y + 16, 152 + u, 0, width, height);
		}

		matrices.pop();

		modelViewMatrix.push();

		if(top) {
			modelViewMatrix.translate(0, yPos, 0);
			client.getItemRenderer().renderGuiItemIcon(new ItemStack(item), x - screenX + 4, y - screenY - 1);
		}
		else {
			modelViewMatrix.translate(0, -yPos, 0);
			client.getItemRenderer().renderGuiItemIcon(new ItemStack(item), x - screenX + 4, y - screenY + 24);
		}

		modelViewMatrix.pop();
		RenderSystem.applyModelViewMatrix();
	}

	@Override
	public void onPress() {
		pressAction.onPress(this);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		appendDefaultNarrations(builder);
	}

	public Identifier getTabId() {
		return tabId;
	}

	public interface PressAction {
		void onPress(TabWidget widget);
	}
}
