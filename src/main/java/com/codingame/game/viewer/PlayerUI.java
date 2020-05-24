package com.codingame.game.viewer;

import com.codingame.game.Player;
import com.codingame.game.yinsh.Action;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Text;

import static com.codingame.game.viewer.Viewer.*;
import static com.codingame.game.viewer.Viewer.graphics;

public class PlayerUI {
    private Player player;
    private Group hud;
    private Rectangle[] rectangles;
    private Text[] texts;


    public PlayerUI(Player player) {
        this.player = player;
        rectangles = new Rectangle[3];
        texts = new Text[3];

        hud = graphics.createGroup(
                graphics.createRectangle().setFillColor(HEX_COLOR).setX(60 + 1500 * player.getIndex()).setY(200).setHeight(620).setWidth(300).setLineColor(player.getColor()).setLineWidth(5),
                graphics.createText(player.getNicknameToken()).setStrokeThickness(1).setStrokeColor(0x000000).setX(210 + 1500 * player.getIndex()).setY(220).setAnchorX(0.5).setFontWeight(Text.FontWeight.BOLD).setFontSize(40).setFillColor(player.getColorToken()),
                graphics.createSprite().setImage(player.getAvatarToken()).setX(210 + 1500 * player.getIndex()).setY(300).setBaseWidth(200).setBaseHeight(200).setZIndex(2).setAnchorX(0.5)
        );



        for (int i = 0; i < 3; ++i) {
            rectangles[i] = graphics.createRectangle().setFillColor(HEX_COLOR).setLineWidth(2).setLineColor(player.getColor()).setWidth(65).setHeight(65).setX(105  + i * 75 + 1500 * player.getIndex()).setY(550);
            hud.add(rectangles[i]);
        }

        texts[0] = graphics.createText("").setFontSize(FONT_SIZE + 10).setFillColor(player.getColor()).setAnchorX(0.5).setFontWeight(Text.FontWeight.BOLD).setX(210 + 1500 * player.getIndex()).setY(650);
        texts[1] = graphics.createText("").setFontSize(FONT_SIZE).setAnchorX(0.5).setFontWeight(Text.FontWeight.BOLD).setX(210 + 1500 * player.getIndex()).setY(705);
        texts[2] = graphics.createText("").setFontSize(FONT_SIZE).setAnchorX(0.5).setFontWeight(Text.FontWeight.BOLD).setX(210 + 1500 * player.getIndex()).setY(755);

        hud.add(texts);
        if (player.getIndex() == 1)
            hud.setAlpha(0.5);
    }

    public void setTexts(Action action) {
        /*
        texts[0].setText(action.getType().getDescription());
        if (action.getPiece() != null) {
            texts[1].setText("Piece: " + action.getPiece().getId());
        } else {
            texts[1].setText("");
        }

        if (action.getDestination() != null) {
            texts[2].setText("x: " + action.getDestination().getX() + " y: " + action.getDestination().getY());
        } else if(action.getStones() != null && action.getStones().size() > 0) {
            texts[2].setText(action.getStones().toString());
        } else {
            texts[2].setText("");
        }
         */
        texts[0].setText(action.getDesc());
        graphics.commitEntityState(0, texts);
    }

    public Rectangle[] getRectangles() {
        return rectangles;
    }

    public Group getHud() {
        return hud;
    }
}
