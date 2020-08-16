package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import spinnery.widget.api.Padding;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WPadded;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unchecked")
public abstract class WAbstractSlider extends WAbstractWidget implements WPadded {
	private float minimum = 0;
	private float maximum = 0;
	private float progress = 0;
	private float step = 1;

	private boolean progressVisible = true;

	public float getMinimum() {
		return minimum;
	}

	public <W extends WAbstractSlider> W setMinimum(float minimum) {
		this.minimum = minimum;
		if (progress < minimum) {
			setProgress(minimum);
		}
		return (W) this;
	}

	public float getMaximum() {
		return maximum;
	}

	public <W extends WAbstractSlider> W setMaximum(float maximum) {
		this.maximum = maximum;
		if (progress > maximum) {
			setProgress(maximum);
		}
		return (W) this;
	}

	public float getStep() {
		return step;
	}

	public <W extends WAbstractSlider> W setStep(float step) {
		this.step = step;
		return (W) this;
	}

	public float getProgress() {
		return progress;
	}

	public <W extends WAbstractSlider> W setProgress(float progress) {
		float value = (float) (Math.floor(progress / step) * step);
		this.progress = Math.max(minimum, Math.min(maximum, value));
		return (W) this;
	}

	public boolean isProgressVisible() {
		return progressVisible;
	}

	public <W extends WAbstractSlider> W setProgressVisible(boolean progressVisible) {
		this.progressVisible = progressVisible;
		return (W) this;
	}

	public String getFormattedProgress() {
		return String.valueOf(progress);
	}

	abstract public Position getProgressTextAnchor();

	public float getPercentComplete() {
		return Math.max(0, (progress - minimum) / (maximum - minimum));
	}

	public abstract Size getKnobSize();

	@Override
	public Padding getPadding() {
		return getStyle().asPadding("padding");
	}

	@Override
	public void onKeyPressed(int keyCode, int character, int keyModifier) {
		if (keyCode == GLFW.GLFW_KEY_KP_ADD || keyCode == GLFW.GLFW_KEY_EQUAL) {
			progress = Math.min(progress + step, maximum);
		}
		if (keyCode == GLFW.GLFW_KEY_KP_SUBTRACT || keyCode == GLFW.GLFW_KEY_MINUS) {
			progress = Math.max(progress - step, minimum);
		}
		super.onKeyPressed(keyCode, character, keyModifier);
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		updatePosition(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	protected abstract void updatePosition(float mouseX, float mouseY);

	@Override
	public void onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY) {
		updatePosition(mouseX, mouseY);
		super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}
}
