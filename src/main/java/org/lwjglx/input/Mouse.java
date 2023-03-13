package org.lwjglx.input;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import me.eigenraven.lwjgl3ify.core.Config;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.LWJGLException;
import org.lwjglx.Sys;
import org.lwjglx.opengl.Display;

public class Mouse {

    // Fields for reflection compatibility with lwjgl2
    public static final int EVENT_SIZE = 1 + 1 + 4 + 4 + 4 + 8;
    private static ByteBuffer buttons = BufferUtils.createByteBuffer(32);
    private static IntBuffer coord_buffer = BufferUtils.createIntBuffer(32);
    private static ByteBuffer readBuffer = BufferUtils.createByteBuffer(32);

    private static boolean grabbed = false;

    private static int lastX = 0;
    private static int lastY = 0;

    private static int lastEventX = 0;
    private static int lastEventY = 0;

    private static int latestX = 0;
    private static int latestY = 0;

    private static int x = 0;
    private static int y = 0;

    private static EventQueue queue = new EventQueue(128);

    private static int[] buttonEvents = new int[queue.getMaxEvents()];
    private static boolean[] buttonEventStates = new boolean[queue.getMaxEvents()];
    private static int[] xEvents = new int[queue.getMaxEvents()];
    private static int[] yEvents = new int[queue.getMaxEvents()];
    private static int[] wheelEvents = new int[queue.getMaxEvents()];
    private static int[] lastxEvents = new int[queue.getMaxEvents()];
    private static int[] lastyEvents = new int[queue.getMaxEvents()];
    private static long[] nanoTimeEvents = new long[queue.getMaxEvents()];

    private static boolean clipPostionToDisplay = true;
    private static boolean ignoreNextDelta = false;
    private static boolean ignoreNextMove = false;

    public static void addMoveEvent(double mouseX, double mouseY) {
        if (ignoreNextMove) {
            ignoreNextMove = false;
            return;
        }
        latestX = (int) mouseX;
        latestY = Display.getHeight() - (int) mouseY;
        if (ignoreNextDelta) {
            ignoreNextDelta = false;
            x = latestX;
            y = latestY;
            lastX = latestX;
            lastY = latestY;
        }

        lastxEvents[queue.getNextPos()] = lastEventX;
        lastyEvents[queue.getNextPos()] = lastEventY;
        lastEventX = latestX;
        lastEventY = latestY;

        xEvents[queue.getNextPos()] = latestX;
        yEvents[queue.getNextPos()] = latestY;

        wheelEvents[queue.getNextPos()] = 0;

        buttonEvents[queue.getNextPos()] = -1;
        buttonEventStates[queue.getNextPos()] = false;

        nanoTimeEvents[queue.getNextPos()] = Sys.getNanoTime();

        queue.add();
    }

    public static void addButtonEvent(int button, boolean pressed) {
        lastxEvents[queue.getNextPos()] = lastEventX;
        lastyEvents[queue.getNextPos()] = lastEventY;
        lastEventX = latestX;
        lastEventY = latestY;

        xEvents[queue.getNextPos()] = latestX;
        yEvents[queue.getNextPos()] = latestY;

        wheelEvents[queue.getNextPos()] = 0;

        buttonEvents[queue.getNextPos()] = button;
        buttonEventStates[queue.getNextPos()] = pressed;

        nanoTimeEvents[queue.getNextPos()] = Sys.getNanoTime();

        queue.add();
    }

    static double fractionalWheelPosition = 0.0;
    // Used for our config screen for ease of access
    public static double totalScrollAmount = 0.0;

    public static void addWheelEvent(double dwheel) {
        if (Config.INPUT_INVERT_WHEEL) {
            dwheel = -dwheel;
        }
        dwheel *= Config.INPUT_SCROLL_SPEED;

        final int lastWheel = (int) fractionalWheelPosition;
        fractionalWheelPosition += dwheel;
        totalScrollAmount += dwheel;
        final int newWheel = (int) fractionalWheelPosition;
        if (newWheel != lastWheel) {
            lastxEvents[queue.getNextPos()] = lastEventX;
            lastyEvents[queue.getNextPos()] = lastEventY;
            lastEventX = latestX;
            lastEventY = latestY;

            xEvents[queue.getNextPos()] = latestX;
            yEvents[queue.getNextPos()] = latestY;

            wheelEvents[queue.getNextPos()] = newWheel - lastWheel;

            buttonEvents[queue.getNextPos()] = -1;
            buttonEventStates[queue.getNextPos()] = false;

            nanoTimeEvents[queue.getNextPos()] = Sys.getNanoTime();

            queue.add();
        }
        fractionalWheelPosition = fractionalWheelPosition % 1;
    }

    public static void poll() {
        lastX = x;
        lastY = y;

        if (!grabbed && clipPostionToDisplay) {
            if (latestX < 0) latestX = 0;
            if (latestY < 0) latestY = 0;
            if (latestX > Display.getWidth() - 1) latestX = Display.getWidth() - 1;
            if (latestY > Display.getHeight() - 1) latestY = Display.getHeight() - 1;
        }

        x = latestX;
        y = latestY;
    }

    public static void create() throws LWJGLException {}

    public static boolean isCreated() {
        return Display.isCreated();
    }

    public static void setGrabbed(boolean grab) {
        GLFW.glfwSetInputMode(
                Display.getWindow(),
                GLFW.GLFW_CURSOR,
                grab ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
        grabbed = grab;
        if (!grab) {
            setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        }
        ignoreNextDelta = true;
    }

    public static boolean isGrabbed() {
        return grabbed;
    }

    public static boolean isButtonDown(int button) {
        return GLFW.glfwGetMouseButton(Display.getWindow(), button) == GLFW.GLFW_PRESS;
    }

    public static boolean next() {
        return queue.next();
    }

    public static int getEventX() {
        return xEvents[queue.getCurrentPos()];
    }

    public static int getEventY() {
        return yEvents[queue.getCurrentPos()];
    }

    public static int getEventDX() {
        return xEvents[queue.getCurrentPos()] - lastxEvents[queue.getCurrentPos()];
    }

    public static int getEventDY() {
        return yEvents[queue.getCurrentPos()] - lastyEvents[queue.getCurrentPos()];
    }

    public static long getEventNanoseconds() {
        return nanoTimeEvents[queue.getCurrentPos()];
    }

    public static int getEventButton() {
        return buttonEvents[queue.getCurrentPos()];
    }

    public static boolean getEventButtonState() {
        return buttonEventStates[queue.getCurrentPos()];
    }

    public static int getEventDWheel() {
        return wheelEvents[queue.getCurrentPos()];
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static int getDX() {
        return x - lastX;
    }

    public static int getDY() {
        return y - lastY;
    }

    public static int getDWheel() {
        return getEventDWheel();
    }

    public static int getButtonCount() {
        return 8; // max mouse buttons supported by GLFW
    }

    public static void setClipMouseCoordinatesToWindow(boolean clip) {
        clipPostionToDisplay = clip;
    }

    public static void setCursorPosition(int new_x, int new_y) {
        if (grabbed) {
            return;
        }
        GLFW.glfwSetCursorPos(Display.getWindow(), new_x, new_y);
        addMoveEvent(new_x, new_y);
        ignoreNextMove = true;
    }

    public static Cursor setNativeCursor(Cursor cursor) throws LWJGLException {
        // no-op
        return null;
    }

    public static void destroy() {}

    public static int getButtonIndex(String buttonName) {
        if (buttonName.matches("BUTTON[0-9]+")) {
            return Integer.parseInt(StringUtils.removeStart(buttonName, "BUTTON"));
        } else {
            return -1;
        }
    }

    public static String getButtonName(int button) {
        return "BUTTON" + button;
    }

    public static Cursor getNativeCursor() {
        return null;
    }

    public static boolean hasWheel() {
        return true;
    }

    public static boolean isClipMouseCoordinatesToWindow() {
        return clipPostionToDisplay;
    }

    public static boolean isInsideWindow() {
        return Display.isVisible();
    }

    public static void updateCursor() {
        // no-op
    }
}
