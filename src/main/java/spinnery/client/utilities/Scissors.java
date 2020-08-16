package spinnery.client.utilities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import spinnery.widget.implementation.WAbstractWidget;

import static org.lwjgl.opengl.GL11.*;

public class Scissors {
	private static final int max = 512;
	private static final Scissors[] objects = new Scissors[max];
	private static int lastObject = -1;

	private int index;

	private int left;
	private int right;
	private int top;
	private int bottom;

	public Scissors(VertexConsumerProvider provider, int x, int y, int width, int height) {
		if (provider instanceof VertexConsumerProvider.Immediate) {
			((VertexConsumerProvider.Immediate) provider).draw();
		}

		lastObject++;
		if (lastObject < max) {
			index = lastObject;
			objects[index] = this;

			left = x;
			right = x + width - 1;
			top = y;
			bottom = y + height - 1;

			if (index > 0) {
				Scissors parent = objects[index - 1];

				if (left < parent.left) left = parent.left;
				if (right > parent.right) right = parent.right;
				if (top < parent.top) top = parent.top;
				if (bottom > parent.bottom) bottom = parent.bottom;
			}

			resume();
		}
	}

	public Scissors(VertexConsumerProvider provider, WAbstractWidget element) {
		this(provider, (int) (element.getX() * MinecraftClient.getInstance().getWindow().getScaleFactor()),
				(int) (MinecraftClient.getInstance().getWindow().getHeight() - ((element.getY() + element.getHeight()) * MinecraftClient.getInstance().getWindow().getScaleFactor())),
				(int) (element.getWidth() * MinecraftClient.getInstance().getWindow().getScaleFactor()),
				(int) (element.getHeight() * MinecraftClient.getInstance().getWindow().getScaleFactor()));
	}

	private static void glScissor(int x, int y, int width, int height) {
		if (width < 0) width = 0;
		if (height < 0) height = 0;

		org.lwjgl.opengl.GL11.glScissor(x, y, width, height);
	}

	private void resume() {
		glEnable(GL_SCISSOR_TEST);

		glScissor(left, top, right - left + 1, bottom - top + 1);
	}

	public void destroy(VertexConsumerProvider provider) {
		if (provider instanceof VertexConsumerProvider.Immediate) {
			((VertexConsumerProvider.Immediate) provider).draw();
		}

		glDisable(GL_SCISSOR_TEST);

		objects[index] = null;
		lastObject--;

		if (lastObject > -1)
			objects[lastObject].resume();
	}
}