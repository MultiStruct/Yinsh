package com.codingame.game;
import java.util.*;
import java.util.concurrent.TimeoutException;

import com.codingame.game.viewer.Viewer;
import com.codingame.game.yinsh.*;
import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

import static com.codingame.game.yinsh.Hex.HexType.CELL;
import static com.codingame.game.yinsh.Hex.HexType.VOID;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphics;
    @Inject private TooltipModule tooltips;
    @Inject private ToggleModule toggleModule;
    @Inject private EndScreenModule endScreenModule;

    private static final int HEIGHT = 11;
    private static final int[] widthDimensions = {6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6};
    private static final Hex.HexType[][] boardLayout = {
            {VOID, CELL, CELL, CELL, CELL, VOID},
            {CELL, CELL, CELL, CELL, CELL, CELL, CELL},
            {CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL},
            {CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL},
            {CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL},
            {VOID, CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL, VOID},
            {CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL},
            {CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL},
            {CELL, CELL, CELL, CELL, CELL, CELL, CELL, CELL},
            {CELL, CELL, CELL, CELL, CELL, CELL, CELL},
            {VOID, CELL, CELL, CELL, CELL, VOID},
    };
    private Viewer viewer;
    private Board board;
    private Player currentPlayer;
    private Queue<Action> actionsQueue;
    private ArrayList<String> str;

    @Override
    public void init() {
        gameManager.getPlayer(0).setColor(0xFFFFFF);
        gameManager.getPlayer(1).setColor(0x000000);
        gameManager.setFirstTurnMaxTime(1000);
        gameManager.setTurnMaxTime(100);
        gameManager.getPlayer(0).setOpponent(gameManager.getPlayer(1));
        gameManager.getPlayer(1).setOpponent(gameManager.getPlayer(0));
        gameManager.setMaxTurns(200);
        actionsQueue = new LinkedList<Action>();;

        viewer = new Viewer(graphics, tooltips, toggleModule);
        board = new Board();




        for (Player player : gameManager.getPlayers())
            player.drawUI();


        currentPlayer = gameManager.getPlayer(0);
    }

    @Override
    public void gameTurn(int turn) {
        if(turn == 1) {
            for(Player player : gameManager.getPlayers()) {
                player.sendInputLine(Integer.toString(player.getIndex() + 1));
                player.execute();
            }
            for(Player player : gameManager.getPlayers()) {
                try {
                    String opt = player.getOutputs().get(0);
                    if(opt.equals("no")) {
                        player.setWantsActions(false);
                    }
                } catch (AbstractPlayer.TimeoutException e) {
                    gameManager.addToGameSummary(gameManager.formatErrorMessage(currentPlayer.getNicknameToken() + " did not output in time!"));
                    currentPlayer.deactivate(currentPlayer.getNicknameToken() + " timeout!");
                    currentPlayer.setScore(-1);
                    gameManager.endGame();
                }catch (IndexOutOfBoundsException e) {
                    gameManager.addToGameSummary("Invalid action!");
                    currentPlayer.deactivate(currentPlayer.getNicknameToken() + " did not provide a legal action!");
                    currentPlayer.setScore(-1);
                    gameManager.endGame();
                }
            }
            return;
        }

        if(!actionsQueue.isEmpty()) {
            Action action = actionsQueue.remove();
            board.applyAction(action, currentPlayer, true);
            if (action.getType() == Action.ActionType.MOVE && action.getStones()  != null) {
                gameManager.setFrameDuration(2000);
            } else {
                gameManager.setFrameDuration(1000);
            }

            if(action.getType() == Action.ActionType.REMOVE) {
                gameManager.addTooltip(currentPlayer, "Removed 1 ring");
            }
            currentPlayer.UI.setTexts(action);
            if(currentPlayer.getScore() >= 3 || board.getStonesSize() >= 51) {
                gameManager.endGame();
            }
            if (actionsQueue.isEmpty()) {
                currentPlayer = currentPlayer.getOpponent();
            }
            return;
        }
        currentPlayer.UI.getHud().setAlpha(1);
        graphics.commitEntityState(0, currentPlayer.UI.getHud());
        currentPlayer.getOpponent().UI.getHud().setAlpha(0.5);
        graphics.commitEntityState(0, currentPlayer.getOpponent().UI.getHud());

        ArrayList<Action> actions = board.getLegalActions(currentPlayer, 1);
        // IF NO MORE LEGAL ACTIONS FOR CURRENT PLAYER, END GAME
        if (actions.size() == 0) {
            gameManager.addToGameSummary(gameManager.formatErrorMessage(currentPlayer.getNicknameToken() + " does not have legal actions!"));
            currentPlayer.deactivate(currentPlayer.getNicknameToken() + " no legal actions!");
            currentPlayer.setScore(-1);
            gameManager.endGame();
        }

        str = board.getInString(actions);

        sendInputs(currentPlayer, turn);
        currentPlayer.execute();

        try {
            List<String> out = currentPlayer.getOutputs();
            if(out.get(0).charAt(out.get(0).length() - 1) == ';') {
                out.set(0, out.get(0).substring(0, out.get(0).length() - 1));
            }
            String[] split = out.get(0).split(";");
            currentPlayer.output = split;
            ArrayList<Action> current = actions;
            // TODO ILL NEED A BETTER THING THAN THIS
            for(String s : split) {
                s = s.toLowerCase();
                if(s.equals("null"))
                    continue;
                for(Action act : current) {
                    // TODO IF DOESN'T FIND MAKE PLAYER LOSE
                    if(act.getDesc().toLowerCase().equals(s) || act.getReverseDesc().equals(s)) {
                        actionsQueue.add(act);
                        current = act.followingActions;
                        break;
                    }
                }
            }

            if(actionsQueue.size() != split.length) {
                throw new InvalidAction(String.format("Player %s played an illegal action.", currentPlayer.getNicknameToken()));
            }

            Action action = actionsQueue.remove();
            if (action.getType() == Action.ActionType.MOVE && action.getStones() != null) {
                gameManager.setFrameDuration(2000);
            } else {
                gameManager.setFrameDuration(1000);
            }
            board.applyAction(action, currentPlayer, true);
            if(action.getType() == Action.ActionType.REMOVE) {
                gameManager.addTooltip(currentPlayer, "Removed 1 ring");
            }
            currentPlayer.UI.setTexts(action);

            if(actionsQueue.isEmpty() && (currentPlayer.getScore() >= 3 || board.getStonesSize() >= 51)) {
                gameManager.endGame();
                //return;
            }

            if(!actionsQueue.isEmpty()) {
                return;
            }

        } catch (AbstractPlayer.TimeoutException e) {
            gameManager.addToGameSummary(gameManager.formatErrorMessage(currentPlayer.getNicknameToken() + " did not output in time!"));
            currentPlayer.deactivate(currentPlayer.getNicknameToken() + " timeout!");
            currentPlayer.setScore(-1);
            gameManager.endGame();
        } catch (InvalidAction e) {
            gameManager.addToGameSummary(e.getMessage());
            gameManager.addToGameSummary(gameManager.formatErrorMessage("Action was: " + currentPlayer.output[0]));
            currentPlayer.deactivate(e.getMessage());
            currentPlayer.setScore(-1);
            gameManager.endGame();
        } catch (IndexOutOfBoundsException e) {
            gameManager.addToGameSummary("Invalid action!");
            currentPlayer.deactivate(currentPlayer.getNicknameToken() + " did not provide a legal action!");
            currentPlayer.setScore(-1);
            gameManager.endGame();
        }

        if(actionsQueue.isEmpty())
            currentPlayer = currentPlayer.getOpponent();
    }

    private void sendInputs(Player player, int turn) {
        player.sendInputLine(Integer.toString(getHEIGHT()));
        ArrayList<ArrayList<Hex>> hexes = board.getHexes();
        for (ArrayList<Hex> row : hexes) {
            StringBuilder s = new StringBuilder();
            for (Hex hex : row) {
                if (hex == null) continue;
                if (hex.getType() != CELL) continue;

                if (hex.getPiece() == null)
                    s.append(".");
                else if (hex.getPiece().getType() == Piece.PieceType.RING) {
                    if(hex.getPiece().getOwner() == player)
                        s.append("R");
                    else
                        s.append("r");
                } else if (hex.getPiece().getType() == Piece.PieceType.STONE) {
                    if(hex.getPiece().getOwner() == player)
                        s.append("S");
                    else
                        s.append("s");
                }
            }
            player.sendInputLine(s.toString());
        }

        // GET OPPONENT LAST MOVE

        player.sendInputLine(player.getWantsActions() ? Integer.toString(str.size()) : "0");
        for (String s : str) {
            player.sendInputLine(s);
        }

    }

    @Override
    public void onEnd() {
        int[] scores = { gameManager.getPlayer(0).getScore(), gameManager.getPlayer(1).getScore() };
        String[] text = new String[2];
        if(scores[0] > scores[1]) {
            text[0] = "Captured " + scores[0] + " rings";
            text[1] = "Captured " + scores[1] + " rings";
        } else if(scores[1] > scores[0]) {
            text[0] = "Captured " + scores[0] + " rings";
            text[1] = "Captured " + scores[1] + " rings";
        } else {
            text[0] = "Captured " + scores[0] + " rings";
            text[1] = "Captured " + scores[1] + " rings";
        }
        endScreenModule.setScores(scores, text);
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int[] getWidthDimensions() {
        return widthDimensions;
    }

    public static Hex.HexType[][] getBoardLayout() {
        return boardLayout;
    }
}
