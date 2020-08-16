package spinnery.common.utilities;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class Positions {
	public static float mouseX = 0;
	public static float mouseY = 0;

	public static float getMouseX() {
		return mouseX;
	}

	public static void setMouseX(float mouseX) {
		Positions.mouseX = mouseX;
	}

	public static float getMouseY() {
		return mouseY;
	}

	public static void setMouseY(float mouseY) {
		Positions.mouseY = mouseY;
	}

	public static void enableDragCursor() {
		GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR));
	}

	public static void enableArrowCursor() {
		GLFW.glfwSetCursor(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
	}
}
