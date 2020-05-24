package com.codingame.game.yinsh;

import com.codingame.game.viewer.HexUI;

public class Hex {
    private int x, y;
    private HexType type;
    private Piece piece;
    private HexUI ui;


    Hex(int x, int y, HexType type, int viewerX) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.piece = null;
        if (viewerX >= 0)
           this.ui = new HexUI(this, viewerX);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getPiece() {
        return piece;
    }

    void setPiece(Piece piece) {
        this.piece = piece;
    }

    public HexType getType() {
        return type;
    }

    public HexUI getUi() {
        return ui;
    }

    public enum HexType {
        VOID("Void"),
        CELL("Cell");

        private String description;

        HexType(String description) {
            this.description = description;
        }
    }
}
