package bridgeGame;

public final class Dice {
    public Dice() {
        // TODO
    }

    public int rollDice() {
        return (int)(Math.random() * 6) + 1;
    }
}
