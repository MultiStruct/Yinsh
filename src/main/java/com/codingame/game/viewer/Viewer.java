package com.codingame.game.viewer;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class Viewer {
    public static GraphicEntityModule graphics;
    static TooltipModule tooltips;
    public static ToggleModule toggleModule;
    private static final int VIEWER_HEIGHT = 1080;
    private static final int VIEWER_WIDTH = 1920;
    private final int BACKGROUND_COLOR = 0xC8C8C8;
    static final int HEX_RADIUS = 60;

    static final int START_X = 480;
    static final int START_Y = 500;
    //static final int HEX_COLOR = 0xE7BA7B;0xc0c0c0
    static final int HEX_COLOR = 0x888888;
    static final int FONT_SIZE = 36;

    public static Text stonesAvailable;

    public static Group hexBoard;

    public Viewer(GraphicEntityModule graphics, TooltipModule tooltips, ToggleModule toggleModule) {
        Viewer.graphics = graphics;
        Viewer.tooltips = tooltips;
        Viewer.toggleModule = toggleModule;
        Viewer.hexBoard = graphics.createGroup();

        Viewer.graphics.createRectangle().setWidth(VIEWER_WIDTH).setHeight(VIEWER_HEIGHT).setFillColor(BACKGROUND_COLOR);
        Rectangle b2 = Viewer.graphics.createRectangle().setWidth(VIEWER_WIDTH).setHeight(VIEWER_HEIGHT).setFillColor(BACKGROUND_COLOR);
        //0x585858
        Viewer.toggleModule.displayOnToggleState(b2, "viewerToggle", false);

        Viewer.stonesAvailable = graphics.createText("51").setFontSize(60).setX(500).setY(50);
    }
}
