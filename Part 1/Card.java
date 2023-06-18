public class Card {
    String suit; 
    String rank;

    Card(){}

    // Initialize the Card
    Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return this.suit;
    }

    public String getRank() {
        return this.rank;
    }

    // Set value to cards based on rank
    public int setValue() {
        if (rank.equals("A")) {
            return 14;
        } else if (rank.equals("J") ){
            return 11;
        } else if (rank.equals("Q") ){
            return 12;
        }else if (rank.equals("Q") ){
            return 13;
        }else {
            return Integer.parseInt(rank);    
        }
    }    

    public boolean isEqual(Card card) {
        if (this.suit.equals(card.suit) && this.rank.equals(card.rank))
            return true;
        else
            return false;
    }

    
    public String getCard() {
        return this.suit + this.rank;
    }

    public String toString(){
        return this.suit + this.rank;
    }

}