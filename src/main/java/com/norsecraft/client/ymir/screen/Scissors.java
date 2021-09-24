package com.norsecraft.client.ymir.screen;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.stream.Collectors;

public final class Scissors {

    private static final ArrayDeque<Frame> STACK = new ArrayDeque<>();

    private Scissors() {

    }

    public static Frame push(int x, int y, int width, int height) {
        Frame frame = new Frame(x, y, width, height);
        STACK.push(frame);
        refreshScissors();
        return frame;
    }

    public static void pop() {
        if (STACK.isEmpty()) {
            throw new IllegalStateException("No scissors on the stack!");
        }

        STACK.pop();
        refreshScissors();
    }

    public static void refreshScissors() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (STACK.isEmpty()) {
            GL11.glScissor(0, 0, client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            return;
        }

        int x = Integer.MIN_VALUE;
        int y = Integer.MIN_VALUE;
        int width = -1;
        int height = -1;

        for (Frame frame : STACK) {
            if (x < frame.x)
                x = frame.x;
            if (y < frame.y)
                y = frame.y;
            if (width == -1 || x + width > frame.x + frame.width)
                width = frame.width - (x - frame.x);
            if (height == -1 || y + height > frame.y + frame.height)
                height = frame.height - (y - frame.y);
        }

        int windowHeight = client.getWindow().getFramebufferHeight();
        double scale = client.getWindow().getScaleFactor();
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        GL11.glScissor((int) (x * scale), (int) (windowHeight - (y * scale) - scaledHeight), scaledWidth, scaledHeight);
    }

    public static void checkStackIsEmpty() {
        if(!STACK.isEmpty())
            throw new IllegalStateException("Unpopped scissor frames: " + STACK.stream().map(Frame::toString).collect(Collectors.joining(", ")));
    }

    public static final class Frame implements AutoCloseable {

        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private Frame(int x, int y, int width, int height) {
            if (width < 0) throw new IllegalArgumentException("Negative width for a stack frame");
            if (height < 0) throw new IllegalArgumentException("Negative height for a stack frame");

            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public void close() throws Exception {
            if (STACK.peekLast() != this) {
                if (STACK.contains(this)) {
                    throw new IllegalStateException(this + " is not on top of the stack!");
                } else {
                    throw new IllegalStateException(this + " is not on the stack!");
                }
            }

            pop();
        }

        @Override
        public String toString() {
            return "Frame{ at = (" + x + ", " + y + "), size = (" + width + ", " + height + ") }";
        }

    }
}
