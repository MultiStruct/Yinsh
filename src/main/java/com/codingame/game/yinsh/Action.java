package com.codingame.game.yinsh;

import java.util.ArrayList;

public class Action {
    private String desc = "null";
    private String reverseDesc = "null";
    private ActionType type;
    private Piece piece;
    private Hex origin;
    private Hex destination;
    private ArrayList<Piece> stones;
    public ArrayList<Action> followingActions = new ArrayList<>();

    public Action(ActionType type) {
        this.type = type;
        this.desc = "STEAL";
    }

    // PLACE
    public Action(ActionType type, Hex destination) {
        this.type = type;
        this.destination = destination;
        this.piece = null;
        stones = new ArrayList<>();
        this.desc = ((char) (97 + destination.getY()))+""+(destination.getX()+1);
    }

    // MOVE WITH REMOVAL?
    Action(ActionType type, Piece piece, Hex destination, ArrayList<Piece> stones) {
        this.stones = new ArrayList<>();
        this.stones = stones;
        this.type = type;
        this.piece = piece;
        this.origin = piece.getHex();
        this.destination = destination;

        if (destination != null)
            this.desc = ((char) (97 + piece.getHex().getY()))+""+(piece.getHex().getX()+1) + "-" + ((char) (97 + destination.getY()))+""+(destination.getX()+1);
        if (this.type == ActionType.REMOVE) {
            this.desc = "x" + ((char) (97 + stones.get(0).getHex().getY())) + "" + (stones.get(0).getHex().getX() + 1) + "-" + ((char) (97 + stones.get(4).getHex().getY())) + "" + (stones.get(4).getHex().getX() + 1) + "x" + ((char) (97 + piece.getHex().getY()))+""+(piece.getHex().getX()+1);
            this.reverseDesc = "x" + ((char) (97 + stones.get(4).getHex().getY())) + "" + (stones.get(4).getHex().getX() + 1) + "-" + ((char) (97 + stones.get(0).getHex().getY())) + "" + (stones.get(0).getHex().getX() + 1) + "x" + ((char) (97 + piece.getHex().getY()))+""+(piece.getHex().getX()+1);
        }
    }

    public Hex getDestination() {
        return destination;
    }

    public ActionType getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public Piece getPiece() {
        return piece;
    }

    public Hex getOrigin() {
        return origin;
    }

    public ArrayList<Piece> getStones() {
        return stones;
    }

    public String getReverseDesc() {
        return reverseDesc;
    }

    public enum ActionType {
        PLACE("PLACE"),
        MOVE("MOVE"),
        REMOVE("REMOVE"),
        STEAL("STEAL");

        private String description;

        public String getDescription() {
            return description;
        }

        public static ActionType fromDescription(String description) {
            for (ActionType type : values()) {
                if (type.description.equals(description)) {
                    return type;
                }
            }
            return null;
        }

        ActionType(String description) {
            this.description = description;
        }
    }
}
