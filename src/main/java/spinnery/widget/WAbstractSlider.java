package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import spinnery.widget.api.Padding;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WPadded;

import java.util.function.Consumer;


  general slider-like widget, like a {@link WHorizontalSlider}
 /
@Environment(EnvType.CLIENT)
@SuppressWarnings("unchecked")
public abstract class WAbstractSlider extends WAbstractWidget implements WPadded {
	protected float min = 0;
	protected float max = 0;
	protected float progress = 0;
	protected float step = 1;
	protected boolean progressVisible = true;
	protected Consumer<WAbstractSlider> runnableOnProgressChange;




	public float getMin() {
		return min;
	}




	public <W extends WAbstractSlider> W setMin(float min) {
		this.min = min;
		if (progress < min) {
			setProgress(min);
		}
		return (W) this;
	}




	public float getMax() {
		return max;
	}




	public <W extends WAbstractSlider> W setMax(float max) {
		this.max = max;
		if (progress > max) {
			setProgress(max);
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




	public float getPercentComplete() {
		return Math.max(0, (progress - min) / (max - min));
	}




	public Consumer<WAbstractSlider> getOnProgressChange() {
		return runnableOnProgressChange;
	}




	public <W extends WAbstractSlider> W setOnProgressChange(Consumer<WAbstractSlider> runnableOnProgressChange) {
		this.runnableOnProgressChange = runnableOnProgressChange;
		return (W) this;
	}




	public abstract Size getKnobSize();

	@Override
	public Padding getPadding() {
		return getStyle().asPadding("padding");
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}

	@Override
	public boolean isFocusedKeyboardListener() {
		return true;
	}

	@Override
	public void onKeyPressed(int keyCode, int character, int keyModifier) {
		if (keyCode == GLFW.GLFW_KEY_KP_ADD || keyCode == GLFW.GLFW_KEY_EQUAL) {
			progress = Math.min(progress + step, max);
		}
		if (keyCode == GLFW.GLFW_KEY_KP_SUBTRACT || keyCode == GLFW.GLFW_KEY_MINUS) {
			progress = Math.max(progress - step, min);
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
