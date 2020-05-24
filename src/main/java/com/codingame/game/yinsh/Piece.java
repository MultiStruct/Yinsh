package com.codingame.game.yinsh;

import com.codingame.game.Player;
import com.codingame.game.viewer.PieceUI;

public class Piece {
    private int id;
    private PieceType type;
    private Hex hex;
    private Player owner;
    private boolean active;
    private PieceUI UI;

    Piece(Hex hex, Player owner, int id, PieceType type, boolean changeUI) {
        this.hex = hex;
        this.owner = owner;
        this.id = id;
        this.type = type;
        this.active = true;
        hex.setPiece(this);
        if (changeUI)
            this.UI = new PieceUI(this);
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public PieceType getType() {
        return type;
    }

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }

    public Player getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    void move(Hex destination, boolean changeUI) {
        this.hex = destination;
        destination.setPiece(this);
        if (changeUI && this.getUI() != null)
            this.getUI().move();
    }

    void undoMove(Hex origin, Hex destination) {
        this.hex = origin;
        hex.setPiece(this);
        destination.setPiece(null);
    }

    PieceUI getUI() {
        return UI;
    }

    public enum PieceType {
        RING("RING"),
        STONE("STONE");

        private String description;

        PieceType(String description) {
            this.description = description;
        }
    }
    @Override
    public String toString() {
        return Integer.toString(this.id);
    }
}
