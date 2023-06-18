import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;



public class SaveData implements Serializable {
    
    private static final long serialVersionUID = 1L;

    //The initialisations and data types that are needed to be save
    String Card;
    ArrayList<Card> deck;
    ArrayList<Card> cards;
    ArrayList<Player> players;
    ArrayList<Card> centerCards;
    ArrayList< Map<Card, Player> > highestCard;
    Map<Card, Player> playedCard;
    Card movedCard;

}
