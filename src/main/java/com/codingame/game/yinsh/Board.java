package com.codingame.game.yinsh;

import com.codingame.game.Player;
import com.codingame.game.Referee;
import com.codingame.game.viewer.LineUI;
import com.codingame.game.viewer.Viewer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Polygon;
import com.codingame.gameengine.module.entities.Text;

import java.util.ArrayList;

import static com.codingame.game.Referee.getBoardLayout;
import static com.codingame.game.Referee.getHEIGHT;
import static com.codingame.game.yinsh.Hex.HexType.VOID;

public class Board {
    private ArrayList<ArrayList<Hex>> hexes;
    private ArrayList<Piece> rings;
    private ArrayList<Piece> stones;

    private final int height;
    private final int [] dimensions;
    private int PIECE_ID;
    private boolean draftEnded;
    int actions = 0;

    public Board() {
        hexes = new ArrayList<>();
        rings = new ArrayList<>();
        stones = new ArrayList<>();
        dimensions = Referee.getWidthDimensions();
        height = Referee.getHEIGHT();
        PIECE_ID = 0;
        draftEnded = false;

        int array[] = {0,0,0,0,0,0,1,2,3,4,5};

        for(int h = 0; h < height; ++h) {
            hexes.add(new ArrayList<>());
            for(int i = 0; i < array[h]; ++i) {
                hexes.get(h).add(null);
            }
            for(int w = 0; w < dimensions[h]; ++w) {
                hexes.get(h).add(array[h] + w, new Hex(array[h] + w, h, getBoardLayout()[h][w], w));
            }
        }

        Direction[] directions = {Direction.RIGHT, Direction.DOWN_LEFT, Direction.DOWN_RIGHT};

        // SETUP LINES
        boolean [] added = new boolean[11];
        for(ArrayList<Hex> lHexes : hexes) {
            boolean first = false;
            for(Hex hex : lHexes) {
                if(hex == null || hex.getType() == VOID)
                    continue;

                for (Direction dir : directions) {
                    int x = hex.getX() + dir.getX();
                    int y = hex.getY() + dir.getY();
                    if(y < 0 || y >= getHEIGHT()) continue;
                    if(x < 0 || x >= hexes.get(y).size()) continue;
                    if(hexes.get(y).get(x) == null) continue;
                    if(hexes.get(y).get(x).getType() == VOID) continue;

                    new LineUI(hex.getUi().getPolygon().getX(), hex.getUi().getPolygon().getY(), hexes.get(y).get(x).getUi().getPolygon().getX(), hexes.get(y).get(x).getUi().getPolygon().getY());

                }

                if(!first) {
                    first = true;
                    Polygon poly = hex.getUi().getPolygon();
                    //(char) (97 + hex.getY())) + " " + (hex.getX() + 1)
                    Text text = Viewer.graphics.createText(Character.toString((char)(97 + hex.getY()))).setFontWeight(Text.FontWeight.BOLD).setFontSize(42).setX(poly.getX()).setY(poly.getY() + 60).setAnchorX(0.5).setAnchorY(0.5);
                    Viewer.toggleModule.displayOnToggleState(text, "viewerToggle", true);
                }

                int x, y;
                x = hex.getX()+ Direction.TOP_RIGHT.getX();
                y = hex.getY()+ Direction.TOP_RIGHT.getY();


                if (y < 0) {
                    Polygon poly = hex.getUi().getPolygon();
                    added[hex.getX()] = true;
                    Text text = Viewer.graphics.createText(Integer.toString(hex.getX() + 1)).setFontWeight(Text.FontWeight.BOLD).setFontSize(42).setX(poly.getX() - 60).setY(poly.getY() - 30).setAnchorX(0.5).setAnchorY(0.5);
                    Viewer.toggleModule.displayOnToggleState(text, "viewerToggle", true);
                } else if(!added[hex.getX()]) {
                    added[hex.getX()] = true;
                    Polygon poly = hex.getUi().getPolygon();
                    Text text = Viewer.graphics.createText(Integer.toString(hex.getX() + 1)).setFontWeight(Text.FontWeight.BOLD).setFontSize(42).setX(poly.getX() - 60).setY(poly.getY() - 40).setAnchorX(0.5).setAnchorY(0.5);
                    Viewer.toggleModule.displayOnToggleState(text, "viewerToggle", true);
                }
            }
        }

    }

    public ArrayList<ArrayList<Hex>> getHexes() {
        return hexes;
    }

    public void undoAction(Action action, Player player) {
        if (action.getType() == Action.ActionType.MOVE) {
            Piece piece = action.getPiece();
            PIECE_ID--;

            stones.removeIf(stone -> stone.getHex() == action.getOrigin());
            //stones.remove((stones.size() -1));
            piece.undoMove(action.getOrigin(), action.getDestination());


            if (action.getStones() != null) {
                for (Piece p : action.getStones()) {
                    p.setOwner(p.getOwner().getOpponent());
                }
            }
        } else if (action.getType() == Action.ActionType.REMOVE) {
            for (Piece stone : action.getStones()) {
                stone.getHex().setPiece(stone);
                stones.add(stone);
            }

            action.getPiece().getHex().setPiece(action.getPiece());
            this.rings.add(action.getPiece());
            player.decreaseScore();
        }
    }

    public ArrayList<Action> computeRemoveActions(Player player) {
        ArrayList<Action> moves = new ArrayList<>();

        Direction[] directions = {Direction.RIGHT, Direction.DOWN_LEFT, Direction.DOWN_RIGHT};

        for (Piece stone : stones) {
            if (stone.getOwner() != player) continue;

            for (Direction dir : directions) {
                ArrayList<Piece> stones = new ArrayList<>();
                stones.add(stone);
                int y = stone.getHex().getY();
                int x = stone.getHex().getX();

                while (true) {
                    y += dir.getY();
                    x += dir.getX();
                    if(y < 0 || y >= getHEIGHT()) break;
                    if(x < 0 || x >= hexes.get(y).size()) break;
                    if(hexes.get(y).get(x) == null) break;
                    if(hexes.get(y).get(x).getType() == VOID) break;

                    if(hexes.get(y).get(x).getPiece() != null && hexes.get(y).get(x).getPiece().getType() == Piece.PieceType.STONE && hexes.get(y).get(x).getPiece().getOwner() == player) {
                        stones.add(hexes.get(y).get(x).getPiece());
                        if (stones.size() == 5) {
                            for (Piece ring : rings) {
                                if (ring.getOwner() != player) continue;

                                Action action = new Action(Action.ActionType.REMOVE, ring, null, stones);
                                actions++;
                                moves.add(action);
                            }
                            break;
                        }
                        else
                            continue;
                    }
                    break;

                }
            }
        }


        return moves;
    }

    public ArrayList<Action> computeMoveActions(Player player) {
        ArrayList<Action> moves = new ArrayList<>();

        for(Piece ring : rings) {
            if(ring.getOwner() != player) continue;

            for (Direction dir : Direction.values()) {
                ArrayList<Piece> stones = new ArrayList<>();
                int y = ring.getHex().getY();
                int x = ring.getHex().getX();

                while (true) {
                    y += dir.getY();
                    x += dir.getX();
                    if(y < 0 || y >= getHEIGHT()) break;
                    if(x < 0 || x >= hexes.get(y).size()) break;
                    if(hexes.get(y).get(x) == null) break;
                    if(hexes.get(y).get(x).getType() == VOID) break;
                    if(hexes.get(y).get(x).getPiece() != null && hexes.get(y).get(x).getPiece().getType() == Piece.PieceType.RING) break;


                    if(stones.size() > 0 && hexes.get(y).get(x).getPiece() == null) {
                        Action act = new Action(Action.ActionType.MOVE, ring, hexes.get(y).get(x), stones);
                        moves.add(act);
                        actions++;
                        break;
                    } else if(stones.size() == 0 && hexes.get(y).get(x).getPiece() == null) {
                        Action act = new Action(Action.ActionType.MOVE, ring, hexes.get(y).get(x), null);
                        moves.add(act);
                        actions++;
                    } else if(hexes.get(y).get(x).getPiece() != null && hexes.get(y).get(x).getPiece().getType() == Piece.PieceType.STONE) {
                        stones.add(hexes.get(y).get(x).getPiece());
                    }
                }
            }
        }


        return moves;
    }

    public void applyAction(Action action, Player player, boolean changeUI) {
        if (action.getType() == Action.ActionType.STEAL) {
            rings.get(0).setOwner(player);
            rings.get(0).getUI().getCircle().setLineColor(player.getColor());
            rings.get(0).getUI().getCircle2().setVisible(false);
        } else if (action.getType() == Action.ActionType.PLACE) {
            rings.add(new Piece(action.getDestination(), player, PIECE_ID, Piece.PieceType.RING, changeUI));
            PIECE_ID++;
        } else if (action.getType() == Action.ActionType.MOVE) {

            Hex origin = hexes.get(action.getPiece().getHex().getY()).get(action.getPiece().getHex().getX());
            Piece piece = action.getPiece();
            piece.move(action.getDestination(), changeUI);
            if (changeUI) {
                if (action.getStones() != null)
                    Viewer.graphics.commitEntityState(0.5, piece.getUI().getGroup());
                else
                    Viewer.graphics.commitEntityState(1, piece.getUI().getGroup());

            }
            if (action.getStones() != null) {
                for (Piece p : action.getStones()) {
                    p.setOwner(p.getOwner().getOpponent());

                    if(changeUI) {
                        //p.getUI().getCircle().setFillColor(p.getOwner().getColorToken());
                        // p.getUI().getCircle().setLineColor(p.getOwner().getColorToken());
                        Circle circle = p.getUI().getCircle();
                        Viewer.graphics.commitEntityState(0.51, circle);

                        circle.setSkewY(circle.getSkewY() + Math.toRadians(90));
                        Viewer.graphics.commitEntityState(0.75, circle);
                        circle.setFillColor(p.getOwner().getColor());
                        //circle.setLineColor(p.getOwner().getOpponent().getColorToken());
                        Viewer.graphics.commitEntityState(0.76, circle);
                        circle.setSkewY(circle.getSkewY() + Math.toRadians(90));
                        Viewer.graphics.commitEntityState(1, circle);
                    }
                }
            }

            stones.add(new Piece(origin, player, PIECE_ID, Piece.PieceType.STONE, changeUI));
            PIECE_ID++;
        } else if (action.getType() == Action.ActionType.REMOVE) {
            for (Piece stone : action.getStones()) {

                Piece s = null;
                for(Piece st : stones) {
                    if(st.getId() == stone.getId()) {
                        s = st;
                        break;
                    }
                }
                s.getHex().setPiece(null);

                stones.remove(s);
                
                // TODO FIX THIS HAD TO MOVE THIS TO HERE

                if(changeUI)
                    s.getUI().getGroup().setAlpha(0).setVisible(false);
            }

            if(changeUI) {
                //action.getPiece().getUI().getCircle().setFillAlpha(1);
                action.getPiece().getUI().getCircle2().setVisible(false);
                Viewer.graphics.commitEntityState(0, action.getPiece().getUI().getCircle2());
                action.getPiece().getUI().getGroup().setAlpha(0).setVisible(false);
            }
            action.getPiece().getHex().setPiece(null);
            this.rings.remove(action.getPiece());
            player.increaseScore(changeUI);
        }

        if (changeUI) {
            Viewer.stonesAvailable.setText(Integer.toString(51 - stones.size()));
        }
    }

    public void computeLegalActions(Player player, ArrayList<Action> current, boolean moved, int turn) {
        if(player.getScore() >= 3)
            return;
        ArrayList<Action> removalMoves = computeRemoveActions(player);

        if(removalMoves.size() > 0) {
            for(Action act : removalMoves) {
                current.add(act);
                applyAction(act, player, false);
                computeLegalActions(player, act.followingActions, moved, turn);
                undoAction(act, player);
            }
        } else if(!moved && stones.size() < 51) {
            ArrayList<Action> moves = computeMoveActions(player);
            for(Action act : moves) {
                current.add(act);
                applyAction(act, player, false);
                computeLegalActions(player, act.followingActions, true, turn);
                undoAction(act, player);
            }
        }
    }

    public ArrayList<String> getInString(ArrayList<Action> actions) {
        ArrayList<String> strings = new ArrayList<>();
        for (Action action : actions) {

            ArrayList<String> child = getInString(action.followingActions);
            if(child.size() > 0) {
                for(String s : child)
                    strings.add(action.getDesc() + ";" + s);
            } else {
                strings.add(action.getDesc());
            }
        }

        return strings;
    }


    public ArrayList<Action> getLegalActions(Player player, int turn) {
        ArrayList<Action> moves = new ArrayList<>();

        if(!draftEnded && rings.size() == 10)
            draftEnded = true;

        if(!draftEnded) {
            if (rings.size() == 1 && player.getIndex() == 1) {
                moves.add(new Action(Action.ActionType.STEAL));
            }

            for(int h = 0; h < height; ++h) {
                for(int w = 0; w < hexes.get(h).size(); ++w) {
                    if (hexes.get(h).get(w) == null) continue;
                    if (hexes.get(h).get(w).getType() == VOID) continue;
                    if (hexes.get(h).get(w).getPiece() != null) continue;
                    moves.add(new Action(Action.ActionType.PLACE, hexes.get(h).get(w)));
                }
            }
        } else {
            actions  = 0;

            computeLegalActions(player, moves, false, turn);
        }

        return moves;
    }

    public int getStonesSize() {
        return stones.size();
    }
}
