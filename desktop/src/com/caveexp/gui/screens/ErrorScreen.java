package com.caveexp.gui.screens;

import com.caveexp.Main;
import com.caveexp.gui.component.PositionAnchor;
import com.caveexp.gui.font.Font;
import com.caveexp.gui.component.GUIButtonComponent;
import com.caveexp.util.Renderer;

public class ErrorScreen extends CloseableScreen {
    private final String exception;
    private int width;
    private int height;
    private int x;
    private int y;
    private String[] lines;
    public ErrorScreen(Exception exception) {
        StringBuilder e = new StringBuilder();
        e.append(exception.toString()).append("\nStack Trace:");
        for (StackTraceElement element : exception.getStackTrace()) {
            e.append("\n").append(element);
        }
        this.exception = e.toString();
        System.out.println(this.exception);
    }
    public void init() {
        String text = "$r$cFF0000$bERROR\n" + exception;
        lines = text.split("\n");
        for (String line : lines) {
            width = Math.max(width, Font.stringWidth(line));
        }
        width += 10;
        height = 39 + Font.getHeight() * lines.length;
        x = -width / 2;
        y = -height / 2;
        addComponent(GUIButtonComponent.build(x + 5, y + height - 29, width - 10, 24, "Close", Main.screenStack::pop).anchored(PositionAnchor.CENTER));
    }
    public void render(Renderer renderer) {
        renderer.setColor(0x0000007F);
        renderer.rectfill(x, y, width, height);
        renderer.setColor(0xFFFFFFFF);
        renderer.rectdraw(x, y, width, height);
        int i = 0;
        for (String line : lines) {
            Font.render(renderer, Main.windowWidth / 2 - width / 2 + 5, Main.windowHeight / 2 - height / 2 + 5 + i * Font.getHeight(), "$n$c000000" + line);
            i++;
        }
    }
}
