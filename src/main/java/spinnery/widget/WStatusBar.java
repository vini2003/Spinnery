package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import spinnery.client.utilities.Sprites;
import spinnery.widget.api.Size;


 /
@Environment(EnvType.CLIENT)
public class WStatusBar extends WAbstractWidget {
	private static final Sprites ICONS = Sprites.MINECRAFT_ICONS;


	public static final BarConfig HEALTH_DEFAULT = new BarConfig(ICONS.getSprite(0), ICONS.getSprite(4), ICONS.getSprite(5), BarConfig.Direction.LEFT_TO_RIGHT);
	public static final BarConfig HEALTH_WITHER = new BarConfig(ICONS.getSprite(0), ICONS.getSprite(12), ICONS.getSprite(13), BarConfig.Direction.LEFT_TO_RIGHT);
	public static final BarConfig HEALTH_POISON = new BarConfig(ICONS.getSprite(0), ICONS.getSprite(8), ICONS.getSprite(9), BarConfig.Direction.LEFT_TO_RIGHT);


	public static final BarConfig FOOD_DEFAULT = new BarConfig(ICONS.getSprite(54), ICONS.getSprite(58), ICONS.getSprite(59), BarConfig.Direction.RIGHT_TO_LEFT);


	public static final BarConfig FOOD_HUNGER = new BarConfig(ICONS.getSprite(56), ICONS.getSprite(62), ICONS.getSprite(63), BarConfig.Direction.RIGHT_TO_LEFT);
	public static final BarConfig ARMOR_DEFAULT = new BarConfig(ICONS.getSprite(18), ICONS.getSprite(20), ICONS.getSprite(19), BarConfig.Direction.LEFT_TO_RIGHT);

	public static class BarConfig {
		// Always renders, even behind full sprites.
		public final Sprites.Sprite background;
		public final Sprites.Sprite full;
		public final Sprites.Sprite half;
		// Some bars are specified in their atlas using a right-to-left texture, such as the hunger bar icons.
		public final Direction direction;
		// This allows you to flip a right to left bar around to make it left to right.
		public final boolean mirror;
		// These are 9 for all Minecraft bars, but you can change them if you're making a custom bar.
		public final int spriteWidth;
		public final int spriteHeight;

		public enum Direction {
			LEFT_TO_RIGHT,
			RIGHT_TO_LEFT,
		}

		public BarConfig(Sprites.Sprite background, Sprites.Sprite full, Sprites.Sprite half, Direction direction) {
			this(background, full, half, direction, false, 9, 9);
		}

		private BarConfig(Sprites.Sprite background, Sprites.Sprite full, Sprites.Sprite half, Direction direction, boolean mirror, int width, int height) {
			this.background = background;
			this.full = full;
			this.half = half;
			this.direction = direction;
			this.mirror = mirror;
			this.spriteWidth = width;
			this.spriteHeight = height;
		}

		public BarConfig setSize(int width, int height) {
			return new BarConfig(background, full, half, direction, mirror, width, height);
		}

		// Renders the bar backwards, which is useful for hunger bars which are RTL in the atlas.
		public BarConfig mirror() {
			return new BarConfig(background, full, half, direction, !mirror, spriteWidth, spriteHeight);
		}
	}

	private BarConfig config = HEALTH_DEFAULT;
	private int value;
	private int maxValue;

	public BarConfig getConfig() {
		return config;
	}

	public <W extends WStatusBar> W setConfig(BarConfig config) {
		this.config = config;

		updateSize();

		return (W) this;
	}

	public int getValue() {
		return value;
	}



	  @param maxValue The highest possible value, for health bars this would be {@link LivingEntity#getMaxHealth()}.
	 */
	public <W extends WStatusBar> W setValue(int value, int maxValue) {
		this.maxValue = maxValue;
		this.value = Math.min(value, maxValue);

		updateSize();

		return (W) this;
	}

	protected void updateSize() {
		int numContainers = (maxValue + 1) / 2;
		setSize(Size.of(config.spriteWidth * numContainers, config.spriteHeight));
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		int numContainers = (maxValue + 1) / 2;
		for (int i = 0; i < numContainers; i++) {
			boolean flip = config.direction == BarConfig.Direction.RIGHT_TO_LEFT;
			if (config.mirror) {
				flip = !flip;
			}
			int x = i * config.spriteWidth;
			if (flip) {
				x = (numContainers - i - 1) * config.spriteWidth;
			}

			config.background.draw(matrices, provider, getX() + x, getY(), config.spriteWidth, config.spriteHeight, config.mirror);

			if (i * 2 <= value) {
				Sprites.Sprite sprite = config.half;
				if (value - 2 * i > 1.0F) {
					sprite = config.full;
				}
				sprite.draw(matrices, provider, getX() + x, getY() + 1, config.spriteWidth, config.spriteHeight, config.mirror);
			}
		}

		super.draw(matrices, provider);
	}
}
