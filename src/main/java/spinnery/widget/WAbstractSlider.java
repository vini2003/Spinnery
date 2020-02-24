package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import spinnery.widget.api.Padding;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WPadded;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unchecked")
public abstract class WAbstractSlider extends WAbstractWidget implements WPadded {
	protected double min = 0;
	protected double max = 0;
	protected double progress = 0;
	protected double step = 1;
	protected boolean progressVisible = true;
	protected Consumer<WAbstractSlider> runnableOnProgressChange;

	public double getMin() {
		return min;
	}

	public <W extends WAbstractSlider> W setMin(double min) {
		this.min = min;
		if (progress < min) {
			setProgress(min);
		}
		return (W) this;
	}

	public double getMax() {
		return max;
	}

	public <W extends WAbstractSlider> W setMax(double max) {
		this.max = max;
		if (progress > max) {
			setProgress(max);
		}
		return (W) this;
	}

	public double getStep() {
		return step;
	}

	public <W extends WAbstractSlider> W setStep(double step) {
		this.step = step;
		return (W) this;
	}

	public double getProgress() {
		return progress;
	}

	public <W extends WAbstractSlider> W setProgress(double progress) {
		double value = Math.floor(progress / step) * step;
		this.progress = Math.max(min, Math.min(max, value));
		if (runnableOnProgressChange != null) {
			runnableOnProgressChange.accept(this);
		}
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

	public double getPercentComplete() {
		return Math.max(0, (progress - min) / (max - min));
	}

	public Consumer<WAbstractSlider> getOnProgressChange() {
		return runnableOnProgressChange;
	}

	public <W extends WAbstractSlider> W setOnProgressChange(Consumer<WAbstractSlider> runnableOnProgressChange) {
		this.runnableOnProgressChange = runnableOnProgressChange;
		return (W) this;
	}

	@Override
	public Padding getPadding() {
		return getStyle().asPadding("padding");
	}

	public abstract Size getKnobSize();

	@Override
	public void onKeyPressed(int keyPressed, int character, int keyModifier) {
		if (keyPressed == GLFW.GLFW_KEY_KP_ADD || keyPressed == GLFW.GLFW_KEY_EQUAL) {
			progress = Math.min(progress + step, max);
		}
		if (keyPressed == GLFW.GLFW_KEY_KP_SUBTRACT || keyPressed == GLFW.GLFW_KEY_MINUS) {
			progress = Math.max(progress - step, min);
		}
		super.onKeyPressed(keyPressed, character, keyModifier);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		updatePosition(mouseX, mouseY);
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	protected abstract void updatePosition(int mouseX, int mouseY);

	@Override
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		updatePosition(mouseX, mouseY);
		super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}
}
