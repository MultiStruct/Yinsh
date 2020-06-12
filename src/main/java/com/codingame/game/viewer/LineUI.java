package com.codingame.game.viewer;

import com.codingame.gameengine.module.entities.Line;

import static com.codingame.game.viewer.Viewer.*;
public class LineUI {
    Line line;

    public LineUI(int startX, int startY, int endX, int endY) {
        line = graphics.createLine().setX(startX).setY(startY).setX2(endX).setY2(endY);
        line.setLineWidth(3);
        line.setLineColor(0x000000);
        toggleModule.displayOnToggleState(line, "viewerToggle", true);

    }
}
