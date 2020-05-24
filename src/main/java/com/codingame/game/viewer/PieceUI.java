package com.codingame.game.viewer;

import com.codingame.game.yinsh.Piece;
import com.codingame.gameengine.module.entities.*;

import static com.codingame.game.viewer.Viewer.*;

public class PieceUI {
    private Piece piece;
    private Group group;
    private Circle circle;
    private Circle circle2;
    public PieceUI(Piece piece) {

        int radius = HEX_RADIUS - 22;
        if (piece.getType().equals(Piece.PieceType.STONE)) {
            radius = HEX_RADIUS - 28;
        }
        group = graphics.createGroup();

        this.piece = piece;

        circle = graphics.createCircle().setRadius(radius);

        circle.setZIndex(1);
        circle.setFillColor(piece.getOwner().getColor()).setZIndex(1);
        //graphics.commitEntityState(0, circle);

        if(piece.getType().equals(Piece.PieceType.RING)) {
            circle.setLineColor(piece.getOwner().getColor());
            circle.setLineWidth(12);
            circle.setFillAlpha(0);
            circle.setZIndex(2);
        } else {
            circle.setLineWidth(3);
            circle.setLineColor(0x000000);
            circle.setFillAlpha(1);
            circle.setLineAlpha(0);
        }

        tooltips.setTooltipText(circle, Character.toString((char) (97 + piece.getHex().getY())) + " " + (piece.getHex().getX() + 1));

        group = graphics.createGroup().setZIndex(3);
        if (piece.getType().equals(Piece.PieceType.RING)) {
            circle2 = graphics.createCircle().setRadius(radius);
            circle2.setFillAlpha(0);
            circle2.setLineWidth(16).setLineColor(0x000000);
            if (piece.getOwner().getIndex() == 1)
                circle2.setVisible(false);

            group.add(circle2);
            graphics.commitEntityState(0, circle2);
        }
        group.setX(piece.getHex().getUi().polygon.getX()).setY(piece.getHex().getUi().polygon.getY()).setZIndex(piece.getType().equals(Piece.PieceType.STONE) ? 1 : 2);
        group.add(circle);
        graphics.commitEntityState(0, circle);
        graphics.commitEntityState(0, group);
    }

    public void move() {
        tooltips.setTooltipText(circle, Character.toString((char) (97 + piece.getHex().getY())) + " " + (piece.getHex().getX() + 1));
        group.setX(piece.getHex().getUi().polygon.getX()).setY(piece.getHex().getUi().polygon.getY());
    }


    public Circle getCircle2() {
        return circle2;
    }

    public Group getGroup() {
        return group;
    }

    public Circle getCircle() {
        return circle;
    }

}