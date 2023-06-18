import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand;
    int score;
    String name;

    Player(ArrayList<Card> hand, int score) {
        this.hand = hand;
        this.score = score;
    }

    // TRACKING THE CARDS IN HAND //
    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    //  OPERATION REGARDING CARDS IN HAND //
    public void drawCard(Card card) {
        hand.add(card);
    }

    public void playCard(Card card) {
        hand.remove(card);
    }

    // TALLY THE SCORES //
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}