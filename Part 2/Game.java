import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Game {
    ArrayList<Card> deck;
    ArrayList<Card> cards;
    ArrayList<Player> players;
    ArrayList<Card> centerCards;
    ArrayList< Map<Card, Player> > highestCard;
    Map<Card, Player> playedCard;
    Card movedCard;
    int trickNum;
    int currentTurn;
    int prevTurn;



    public static void main(String[] args) {
        Game game = new Game();
        game.startGame();
    }

    public void startGame(){
        Scanner input = new Scanner(System.in);

        initializePlayer();

        initializeDeck();

        distributeCards();

        centerCards = new ArrayList<>();

        boolean endGame = false;

        while (!endGame){

            boolean endRound = false;

            // Draw a lead card for the first trick
            centerCards.add(deck.remove(0));

            currentTurn = 0;
            prevTurn = 0;

            getTurn();

            // Track the highest card
            highestCard = new ArrayList<>();

            trickNum = 1;

            while (!endRound){

                int turnsPlayed = 0;

                while (turnsPlayed < 4){

                    displayGame();

                    String playerInput = input.nextLine();
                    playerInput = playerInput.toLowerCase();

                    switch (playerInput){

                        // Player draw Card
                        case "d":
                            boolean validCard = false;

                            while (!validCard) {
                                //checks if there are any cards in deck
                                if (deck.size() > 0) {

                                    players.get(currentTurn).drawCard(deck.get(0));
                                    System.out.println("Drew card " + deck.get(0) + ".");
            
                                    if (deck.get(0).getRank().equals(centerCards.get(0).getRank())
                                    || deck.get(0).getSuit().equals(centerCards.get(0).getSuit())) {
                                        validCard = true;
                                    }
                                    deck.remove(0);
                                }
                                //if no cards, skips player's turn
                                else {
                                    System.out.println("No cards available to be drawn.");
                                    System.out.println("Player " + (players.indexOf(players.get(currentTurn)) + 1) + "\'s turn will be skipped.");
                                    
                                    turnsPlayed += 1;
                                    switch(currentTurn) {
                                        case 0:
                                            currentTurn = 1;
                                            break;
                                        case 1:
                                            currentTurn = 2;
                                            break;
                                        case 2:
                                            currentTurn = 3;
                                            break;
                                        case 3:
                                            currentTurn = 0;
                                            break;
                                        }
            
                                    break;
                                }
                            }
                            break;

                        // Exit Game
                        case "x":
                            System.exit(0);
                            break;

                        case "s":

                            try {
                                //Write the data that you wish to save:
                                //The cards of each player.
                                //The centercards
                                //The deck
                                //The score 
                                // SaveData data = new SaveData();
                                SaveData dStorage = new SaveData();
                                ResourceManager.save(dStorage, "1.save");
                                // FileOutputStream fos = new FileOutputStream("SaveGameFile.dat");
                                // BufferedOutputStream bos = new BufferedOutputStream(fos);
                                // ObjectOutputStream oos = new ObjectOutputStream(bos);

                                // SaveData dStorage = new SaveData();
                                
                                dStorage.cards = cards;
                                dStorage.players = players;
                                dStorage.deck = deck;
                                dStorage.centerCards = centerCards;
                                dStorage.highestCard = highestCard;
                                dStorage.playedCard = playedCard;
                                dStorage.movedCard = movedCard;
    

                                // oos.writeObject(dStorage);
                                // oos.close();

                                System.out.println(" -- Save successful! -- ");
                            }
                            catch (Exception ex){
                                System.out.println(" -- Save unsuccessful! -- " + ex.getMessage()); 
                                
                            }
                            break;

                        
                        //Player loads
                        case "l":
                        //Load the data you last save.
                        //Parse back the data.
                            try {
                                // FileInputStream fis = new FileInputStream("SaveGameFile.dat");
                                // BufferedInputStream bis = new BufferedInputStream(fis);
                                // ObjectInputStream ois = new ObjectInputStream(bis);

                                // SaveData dStorage = (SaveData)ois.readObject();

                                SaveData dStorage = (SaveData) ResourceManager.load("1.save");

                                cards = dStorage.cards;
                                players = dStorage.players;
                                deck = dStorage.deck;
                                centerCards = dStorage.centerCards;
                                highestCard = dStorage.highestCard;
                                playedCard = dStorage.playedCard;
                                movedCard = dStorage.movedCard;

                                // ois.close();

                                System.out.println(" -- Load successful! -- ");

                                
                            }

                            catch (Exception ex){
                                System.out.println(" -- Load unsuccessful! -- " + ex.getMessage()); 
                            }
                            // catch(ClassNotFoundException ex){
                            //     System.out.println(" -- Load unsuccessful! -- " + ex.getMessage());
                            // }
                            
                            break;
                        //No input 
                        case "":
                            System.out.println("Invalid Card. Please Try Again.");
                            break;

                        // Played a card from the deck
                        default:
                            boolean matchCenter = false;
                            boolean inHand = false;
                            playedCard = new HashMap<>();

                            // Card unavailable in hand
                            while (!matchCenter && !inHand){
                                String chosenCard = playerInput;
                                String chosenSuit = Character.toString(chosenCard.charAt(0));
                                chosenSuit = chosenSuit.toLowerCase();

                                String chosenRank = chosenCard.substring(1);
                                chosenRank = chosenRank.toUpperCase();

                                // The card that will transition from the players hand
                                movedCard = new Card(chosenSuit, chosenRank);

                                // Check for the availability of Cards in Hand
                                for (int i = 0; i < players.get(currentTurn).hand.size(); i++) {
                                    if (players.get(currentTurn).hand.get(i).isEqual(movedCard)) {
                                        inHand = true;
                
                                        // Start a new trick
                                        if (centerCards.size() == 0) {
                                            centerCards.add(movedCard);
                                            players.get(currentTurn).playCard(players.get(currentTurn).hand.get(i));
                                            
                                            playedCard.clear();
                                            playedCard.put(movedCard, players.get(currentTurn));
                                            highestCard.add(playedCard);
                
                                            matchCenter = true;
                                            turnsPlayed += 1;
                                            break;
                                        }

                                        movedCard = players.get(currentTurn).hand.get(i);
                                        break;
                                    }

                                    else{
                                        inHand = false;
                                    }
                                }

                                if (!inHand) {
                                    System.out.println("Invalid Card. Try Again.");
                                    break;
                                }

                                if (!matchCenter) {
                                    // Played Card have the same suit as the lead card
                                    if (movedCard.getSuit().equals(centerCards.get(0).getSuit())) {
                                        matchCenter = true;
                                        players.get(currentTurn).playCard(movedCard);
                                        centerCards.add(movedCard);
                                        
                                        playedCard.clear();
                                        playedCard.put(movedCard, players.get(currentTurn));
                                        highestCard.add(playedCard);
                
                                        turnsPlayed += 1;
                                    }
                
                                    // Played Card have the same rank as the lead card
                                    else if (movedCard.getRank().equals(centerCards.get(0).getRank())) {
                                        matchCenter = true;
                                        players.get(currentTurn).playCard(movedCard);
                                        centerCards.add(movedCard);
                                        
                                        playedCard.clear();
                                        playedCard.put(movedCard, players.get(currentTurn));
                                        
                                        turnsPlayed += 1;
                                    }
                
                                    else {
                                        System.out.println("Invalid Card. Try Again.");
                                        matchCenter = false;
                                        break;
                                    }
                                }

                            }

                            prevTurn = currentTurn;
            
                            if (matchCenter && inHand) {
                                switch(currentTurn) {
                                    case 0:
                                        currentTurn = 1;
                                        break;
                                    case 1:
                                        currentTurn = 2;
                                        break;
                                    case 2:
                                        currentTurn = 3;
                                        break;
                                    case 3:
                                        currentTurn = 0;
                                        break;
                                    }
                            }
                
                    } // END OF PLAYER INPUT

                    if (players.get(prevTurn).hand.isEmpty()) {
                        System.out.println("Player " + (players.indexOf(players.get(prevTurn)) + 1) + " Wins The Game");
                        endRound = true;
                        break;
                    }

                    if (endGame == true){
                        break;
                    }

                } // END OF THE TURN LOOP

                if (endGame == true){
                    break;
                }

                Card highestRank = new Card();
                int winnerIndex = 0;
        
                for (var entry: highestCard.get(0).entrySet()){
                    highestRank = entry.getKey();
                }
        
                for (int i = 1; i < highestCard.size(); i++) {
                    for (var entry: highestCard.get(i).entrySet())
                        if (entry.getKey().setValue() > highestRank.setValue()) {
                            highestRank = entry.getKey();
                            winnerIndex = i;
                        }
                }

                if (!endRound){
                        System.out.println("Player " + (players.indexOf(highestCard.get(winnerIndex).get(highestRank)) + 1) + " wins Trick #" + trickNum);
                }

                currentTurn = players.indexOf(highestCard.get(winnerIndex).get(highestRank));
                trickNum += 1;

                highestCard.clear();
                centerCards.clear();
            }// END OF THE TRICK LOOP

            int totalScore = 0;
            //records each player's score at the end of a round
            if (endRound == true) {
                for (int i = 0; i < 4; i++) {
                    totalScore = 0;
                    for (int j = 0; j < players.get(i).hand.size(); j++) {
                        totalScore += players.get(i).hand.get(j).setValue();
                    }
                    players.get(i).setScore(totalScore);
                }
            }

            endGame = false;
        }
    }
        
    public void initializeDeck(){
        // Set an Array to store 52 cards in deck
        deck = new ArrayList<>();

        // SPADES -> s | HEARTS -> h | CLUBS -> c | DIAMONDS -> d
        String[] suits = {"s", "h", "c", "d"};
        // 13 CARDS 1 SUIT
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        // Pair each RANK & SUITS; Adds cards to deck
        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(suit, rank);
                deck.add(card);
            }
        }

        // Shuffle deck
        Collections.shuffle(deck);        
    }

    public void initializePlayer(){
        // Set an array for the players
        players = new ArrayList<Player>();
        
        // Create the players; Since only 4 players i < 4
        for (int i = 0; i < 4; i++){
            players.add(new Player(new ArrayList<Card>(), 0));
        }
    }

    public void distributeCards(){
        for (int i = 0; i < 4; i++) {
            players.get(i).hand.clear();
            for (int j = 0; j < 7; j++) {
                players.get(i).drawCard(deck.get(0));
                deck.remove(0);
            }
        }
    }

    public void displayGame(){
        
        // Display the current trick
        System.out.println();
        System.out.println("Trick #" + trickNum);
        System.out.println();

        // Display current hand
        for (int i = 0; i < 4; i++){
            System.out.println("Player" + (i + 1) + " : " + players.get(i).getHand());
        }
        
        System.out.println();

        // Display Cards in the Center
        System.out.println("Center: " + centerCards);
        System.out.println();

        System.out.println("Deck   : " + deck);
        System.out.println();

        System.out.print("Score: ");
        for (int i = 0; i < 4; i++){
            System.out.print("Player" + (i + 1) + ": " + players.get(i).getScore() + " | ");
        }
        System.out.println("\n");
        System.out.println();

        // Display Player's turn
        System.out.print("Turn : Player " + (currentTurn + 1));
        System.out.println();
        // Get Player Input
        System.out.println("Enter \"s\" to save or \"l\" to load up previous save");
        System.out.print("Play a card from your hand or \"d\" to draw a card. -> ");

    }

    public void getTurn(){
            
        switch (centerCards.get(0).getRank()){
            // Player 1
            case "A": case "5": case "9": case "K":
                currentTurn = 0;
                break;
            
            // Player 2
            case "2": case "6": case "10":
                currentTurn = 1;
                break;

            // Player 3
            case "3": case "7": case "J":
                currentTurn = 2;
                break;

            // Player 4
            case "4": case "8": case "Q":
                currentTurn = 3;
                break;
        }
    }
}

