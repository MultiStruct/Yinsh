package com.codingame.game;
import com.codingame.game.viewer.PlayerUI;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

public class Player extends AbstractMultiplayerPlayer {
    private Player opponent;
    String[] output;
    PlayerUI UI;
    private int color;
    private boolean wantsActions = true;

    void drawUI() {
        UI = new PlayerUI(this);
    }

    @Override
    public int getExpectedOutputLines() {
        return 1;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void increaseScore(boolean changeUI) {
        if(changeUI)
            UI.getRectangles()[getScore()].setFillColor(color);
        setScore(getScore() + 1);
    }

    public void drawMessage(String message) {
        if(message != "") {
            UI.getTexts()[2].setText(message);
        } else if(message == "" && UI.getTexts()[2].getText() != "") {
            UI.getTexts()[2].setText("");
        }
    }

    public void decreaseScore() {
        setScore(getScore() - 1);
    }

    public boolean getWantsActions() {
        return wantsActions;
    }

    public void setWantsActions(boolean wantsActions) {
        this.wantsActions = wantsActions;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
