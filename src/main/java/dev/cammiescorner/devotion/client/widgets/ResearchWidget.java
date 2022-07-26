package dev.cammiescorner.devotion.client.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.DevotionHelper;
import dev.cammiescorner.devotion.api.research.Research;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Set;

public class ResearchWidget extends PressableWidget {
	public static final Identifier TEXTURE = Devotion.id("textures/gui/scripts_of_devotion_icons.png");
	private final MinecraftClient client = MinecraftClient.getInstance();
	private final Research research;
	private final PressAction pressAction;
	private double offsetX = 0, offsetY = 0;

	public ResearchWidget(int x, int y, Identifier researchId, PressAction pressAction) {
		super(x, y, 30, 30, Text.empty());
		this.research = Research.getById(researchId);
		this.pressAction = pressAction;
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return active && visible && hovered;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return active && visible && hovered;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if(visible) {
			hovered = mouseX >= x + offsetX && mouseY >= y + offsetY && mouseX < x + offsetX + width && mouseY < y + offsetY + height;
			renderButton(matrices, mouseX, mouseY, delta);
		}
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
//		matrices.translate(offsetX, offsetY, 0);

		Set<Identifier> playerResearch = DevotionHelper.getResearchIds(client.player);
		int u;

		if(playerResearch.contains(research.getId())) {
			u = 60;
			active = true;
		}
		else if(playerResearch.containsAll(research.getParentIds())) {
			u = 30;
			active = true;
		}
		else {
			u = 0;
			active = false;
		}

		// x - 131, y - 42
		drawTexture(matrices, (int) (x + offsetX), (int) (y + offsetY), u, 0, width, height);

		if(isHoveredOrFocused())
			renderTooltip(matrices, mouseX, mouseY);
	}

	@Override
	public void onPress() {
		pressAction.onPress(this);
	}

	public void setOffset(double x, double y) {
		offsetX = x;
		offsetY = y;
	}

	public Research getResearch() {
		return research;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		appendDefaultNarrations(builder);
	}

	public interface PressAction {
		void onPress(ResearchWidget widget);
	}
}
