package com.codingame.game.viewer;

import com.codingame.game.Referee;
import com.codingame.game.yinsh.Hex;
import com.codingame.gameengine.module.entities.Polygon;
import com.codingame.gameengine.module.entities.Text;

import static com.codingame.game.viewer.Viewer.*;

public class HexUI {
    Polygon polygon;
    Text text;

    public HexUI(Hex hex, int viewerX) {
        if(hex.getType() == Hex.HexType.CELL) {
            polygon = graphics.createPolygon();

            //polygon.setX((START_X - (int)(HEX_RADIUS * 0.875 * (Referee.getWidthDimensions()[hex.getY()] % (Referee.getHEIGHT() + 1))) + (int)(HEX_RADIUS * 1.74 * viewerX))).setY((int)(START_Y + HEX_RADIUS * 1.5 * hex.getY()));
            polygon.setX((int)(START_Y + HEX_RADIUS * 1.5 * hex.getY())).setY((START_X + (int)(HEX_RADIUS * 0.85 * (Referee.getWidthDimensions()[hex.getY()] % (Referee.getHEIGHT() + 1))) - (int)(HEX_RADIUS * 1.70 * viewerX)));

            for (int i = 0; i < 7; i++) {
                int x = (int)(HEX_RADIUS * Math.cos((i * 2) * Math.PI / 6D));
                int y = (int)(HEX_RADIUS * Math.sin((i * 2) * Math.PI / 6D));
                polygon.addPoint(x, y);
            }

            polygon.setFillColor(HEX_COLOR).setLineWidth(4).setLineColor(0x000000).setZIndex(0);
            text = graphics.createText(Character.toString((char) (97 + hex.getY())) + "" + (hex.getX() + 1)).setX(polygon.getX()).setFontFamily("Verdana").setY(polygon.getY()).setAnchorY(0.5).setAnchorX(0.5).setFontSize(FONT_SIZE - 6).setFontWeight(Text.FontWeight.BOLD).setStrokeThickness(4).setStrokeColor(0x000000).setFillColor(0xFFFFFF).setZIndex(2);
            //text = graphics.createText(Character.toString((char) (97 + hex.getY())) + "" + (hex.getX() + 1)).setX(polygon.getX()).setFontFamily("Arial").setY(polygon.getY()).setAnchorY(0.5).setAnchorX(0.5).setFontSize(FONT_SIZE).setFontWeight(Text.FontWeight.BOLD).setStrokeThickness(4).setStrokeColor(0x000000).setFillColor(0xFFFFFF).setZIndex(2);
            //text = graphics.createText(Character.toString((char) (97 + hex.getY())) + " " + (hex.getX() + 1)).setX(polygon.getX()).setY(polygon.getY()).setAnchorY(0.5).setAnchorX(0.5).setFontSize(FONT_SIZE).setFontWeight(Text.FontWeight.BOLD).setZIndex(2);
            toggleModule.displayOnToggleState(text, "coordinatesToggle", true);
            toggleModule.displayOnToggleState(this.polygon, "viewerToggle", false);

            //tooltips.setTooltipText(polygon, Character.toString((char) (97 + hex.getY())) + " " + (hex.getX() + 1));
        }

    }

    public Polygon getPolygon() {
        return polygon;
    }
}