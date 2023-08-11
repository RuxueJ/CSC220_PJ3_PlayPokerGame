package PJ3;
import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. Jacks or Better: a pair pays out only if the cards in the pair are Jacks, 
 * 	Queens, Kings, or Aces. Lower pairs do not pay out. 
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */


/* This is the video poker game class.
 * It uses OneDeck and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */



public class VideoPoker {

    // default constant values
    private static final int defaultBalance=100;
    private static final int numberCards=5;

    // default constant payout value and playerHand types
    private static final int[]    winningMultipliers={1,2,3,5,6,9,25,50,250};
    private static final String[] winningHands={ 
	  "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	  "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

    // use one deck of cards
    private final OneDeck thisDeck;

    // holding current player 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    /** default constructor, set balance = defaultBalance */
    public VideoPoker()
    {
	this(defaultBalance);
    }

    /** constructor, set given balance */
    public VideoPoker(int balance)
    {
	this.playerBalance= balance;
        thisDeck = new OneDeck();
    }

    /** This display the payout table based on winningMultipliers and 
      * winningHands arrays */
    private void showPayoutTable()
    { 
	System.out.println("\n\n");
	System.out.println("Payout Table   	      Multiplier   ");
	System.out.println("=======================================");
	int size = winningMultipliers.length;
	for (int i=size-1; i >= 0; i--) {
		System.out.println(winningHands[i]+"\t|\t"+winningMultipliers[i]);
	}
	System.out.println("\n\n");
    }

    /** Check current playerHand using winningMultipliers and winningHands arrays
     *  Must print yourHandType (default is "Sorry, you lost") at the end of function.
     *  This can be checked by testCheckHands() and main() method.
     */
    private void checkHands()
    {
	// implement this method
		String yourHandType= "Sorry, you lost";
		int indexOfCheckHands = -1;
		int betMultiplier = 0;

		if(isStraight()){
			if(isRoyalFlush()){
				indexOfCheckHands = 8;
			} else if (isStraightFlush()) {
				indexOfCheckHands = 7;
			}else{
				indexOfCheckHands = 3; // straight
			}
		}else if (isFlush()){
			indexOfCheckHands = 4;  // flush
		} else if (isFourOfAKind()) {
			indexOfCheckHands = 6;
		} else if (isFullHouse()) {
			indexOfCheckHands = 5;
		} else if (isThreeOfAKind()) {
			indexOfCheckHands = 2;
		} else if (isTwoPair()) {
			indexOfCheckHands = 1;
		} else if (isRoyalPair()) {
			indexOfCheckHands = 0;
		}
		if(indexOfCheckHands > -1){
			yourHandType = winningHands[indexOfCheckHands];
			betMultiplier = winningMultipliers[indexOfCheckHands];
		}
		playerBalance = playerBalance + playerBet * betMultiplier;
		System.out.println(yourHandType);

    }
	private boolean isRoyalFlush(){
		boolean result1 = isFlush();
		List<Integer> rank = getRankList();
		boolean result2 = (rank.get(0) == 1) && (rank.get(1) == 10) && (rank.get(4) == 13);

		return result1 && result2;
	}
	private boolean isStraight(){
		List<Integer> rank = getRankList();
//		System.out.println(rank);
		boolean result = true, result1, result2;
		result1 = (rank.get(0) == 1);
		result2 = (rank.get(1)==10) || (rank.get(4)==5);
		if(!result1){
			for(int index = 1; index < rank.size(); index++){
				if(rank.get(index-1) != (rank.get(index)-1)){
					result = false;
					break;
				}
			}
		}else{
			result = result2;
		}
		return result;
	}
	private boolean isStraightFlush(){
		// check if it is flush
		// check if it is straight
		boolean flush = isFlush();
		boolean straight = isStraight();

		return straight && flush;
	}
	private List<Integer> getRankList(){
		List<Integer> rank = new ArrayList<>();
		for(Card card:playerHand){
			rank.add(card.getRank());
		}
		Collections.sort(rank);
		return rank;
	}
	private boolean isFourOfAKind(){
		boolean result = false;
		List<Integer> rank = getRankList();

		// if ABBBB or AAAAB, result = true
		if((rank.get(0) == rank.get(3)) || (rank.get(1) == rank.get(4))){
			result = true;
		}
		return result;
	}
	private boolean isThreeOfAKind(){
		boolean result = false;
		List<Integer> rank = getRankList();

		// if ABCCC or ABBBC or AAABC, result = true
		boolean result1 = (rank.get(0) == rank.get(2)) || (rank.get(1) == rank.get(3) || (rank.get(2) == rank.get(4)));
		result = result1 && (!isFourOfAKind()); // is three of all, but not four of all
		return result;
	}
	private boolean isTwoPair(){

		List<Integer> rank = getRankList();
//		System.out.println(rank);

		// if AABBC or ABBCC or AABCC, result = true
		//AABBC
		boolean result1 = (rank.get(0) == rank.get(1)) && (rank.get(2) == rank.get(3));
		// ABBCC
		boolean result2 = (rank.get(1) == rank.get(2)) && (rank.get(3) == rank.get(4));
		//AABBC
		boolean result3 = (rank.get(0) == rank.get(1)) && (rank.get(3) == rank.get(4));

		boolean result = result1 || result2 || result3;

//		result = result && (!isFourOfAKind()) && (!isThreeOfAKind()); // is neither three of all nor four of all
		return result;
	}
	private int getFrequencyOf(Integer n){
		List rank = getRankList();
		int counter = 0;
		for(int i = 0; i < rank.size(); i++){
			if(rank.get(i) == n){
				counter++;
			}
		}
		return counter;
	}
	private boolean isRoyalPair(){
		int frequencyOfA = getFrequencyOf(1);
		int frequencyOfJ = getFrequencyOf(11);
		int frequencyOfQ = getFrequencyOf(12);
		int frequencyOfK = getFrequencyOf(13);
//		System.out.println("fre"+frequencyOfA);
		return (frequencyOfA == 2) || (frequencyOfJ == 2) || (frequencyOfQ == 2) || (frequencyOfK == 2);

	}
	private boolean isFullHouse(){

		List<Integer> rank = getRankList();

		// if AABBB or AAABB, result = true
		boolean result1 = (rank.get(0) == rank.get(1)) && (rank.get(2) == rank.get(4)); //AABBB
		boolean result2 = (rank.get(0) == rank.get(2)) && (rank.get(3) == rank.get(4)); //AAABB

		return result1 || result2;
	}
	private boolean isFlush(){
		int suit = playerHand.get(0).getSuit();
		boolean result = true;
		for(Card card:playerHand){
			if(card.getSuit()!=suit ){  // if there is a different suit, then result = false,and break, return
				result =  false;
				break;
			}
		}
		return result;
	}

//	private boolean isOrdinaryFlush(){
//
//		boolean result;
//		if(isFlush() && (!isStraightFlush()) && (!isRoyalFlush())){ // if it is flush, exclude straightFlush and RoyalFlush,
//			result = true;
//		}else {
//			result = false;
//		}
//		return result;
//	}

    /*************************************************
     *   add additional private methods here ....
     *
     *************************************************/


    public void play() {
    /** The main algorithm for single player poker game 
     *
     * Steps:
     * 		showPayoutTable()
     *
     * 		++	
     * 		show balance, get bet 
     *		verify bet value, update balance
     *		reset deck, shuffle deck,
     *		deal cards and display cards
     *		ask for positions of cards to keep  
     *          get positions in one input line
     *		update cards
     *		check hands, display proper messages
     *		update balance if there is a payout
     *		if balance = O:
     *			end of program 
     *		else
     *			ask if the player wants to play a new game
     *			if the answer is "no" : end of program
     *			else : showPayoutTable() if user wants to see it
     *			goto ++
     */

	// implement this method
		boolean flag = true;
		showPayoutTable();
		while(flag){

			thisDeck.reset();
			thisDeck.shuffle();
			playerBalance = defaultBalance;

			System.out.println("Balance:" + playerBalance);

			while(playerBalance > 0){
				// get bet

				boolean validBet = false;
				System.out.println("Enter in a bet:");
				Scanner scanner = new Scanner(System.in);
				while(!validBet){
					try {
						playerBet = scanner.nextInt();
						validBet = true;
					}catch (InputMismatchException e){
						System.out.println("Please enter integer.Please try again:");
						scanner.nextLine();
					}
				}



				// verify bet value
				while(playerBet > playerBalance || playerBet < 0){
					System.out.println("Bet must be less than balance. No less than 0. try again:");
					playerBet = scanner.nextInt();
				}

				playerBalance -= playerBet;

				try {
					playerHand = thisDeck.deal(5);
				}catch (PlayingCardException e){
					System.out.println("No enough cards.");
				}


				for(int i = 0; i < playerHand.size(); i++){
					System.out.print("[ "+playerHand.get(i)+" ] ");
				}

				System.out.println("\nEnter position of cards to remove in one line (1-5)");
				System.out.println("Leave blank & press enter if you don't want to change any");

				boolean validIndex = false;
				while(!validIndex){
					Scanner inputIndexes = new Scanner(System.in);
					String line = inputIndexes.nextLine();
					if(!line.isEmpty()){
						String[] indexesAsString = line.split(" ");
						for(String indexAsString : indexesAsString ){
							try {
								int index = Integer.parseInt(indexAsString);
								playerHand.set(index - 1, thisDeck.deal(1).get(0));
								validIndex = true;
							}catch (NumberFormatException e){
								validIndex = false;
								System.out.println("Position must be integer. Please enter again:");
//								inputIndexes.nextLine();
								break;
							}catch (IndexOutOfBoundsException e){
								validIndex = false;
								System.out.println("position must between 1 and 5. Please enter again:");
//								inputIndexes.nextLine();
								break;
							}catch (PlayingCardException e){
								validIndex = false;
								System.out.println("no enough cards");
								break;
							}
						}
					} else{
						validIndex = true; // line is empty(user press enter)
					}

				}
				System.out.println("Remaining cards: " + thisDeck.remainSize());
				for(int i = 0; i < playerHand.size(); i++){
					System.out.print("[ "+playerHand.get(i)+" ] ");
				}
				checkHands();
				System.out.println("Balance:" + playerBalance);

			}

			if(playerBalance == 0){
				System.out.println("You lost all your money!");
				System.out.println("Do you want to play again? Y/N");
				boolean validPlayAgain = false;
				while(!validPlayAgain){
					Scanner flagScan = new Scanner(System.in);
					String flagInput = flagScan.next();
					if(flagInput.equals("n") || flagInput.equals("N")){   // cannot use "=="
						flag = false;
						validPlayAgain = true;
					}else if (flagInput.equals("y") || flagInput.equals("Y")){
						flag = true;
						validPlayAgain = true;
					}else{
						System.out.println("invalid input! Please enter again. ");
					}
				}

			}

		}
	}

    /*************************************************
     *   do not modify methods below
     *   methods are used for testing your program.
     *
     *************************************************/

    /** testCheckHands is used to test checkHands() method 
     *  checkHands() should print your current hand type
     */ 
    public void testCheckHands()
    {
      	try {
    		playerHand = new ArrayList<Card>();

		// set Royal Flush
		playerHand.add(new Card(1,4));
		playerHand.add(new Card(10,4));
		playerHand.add(new Card(12,4));
		playerHand.add(new Card(11,4));
		playerHand.add(new Card(13,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight Flush
		playerHand.set(0,new Card(9,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight
		playerHand.set(4, new Card(8,2));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Flush 
		playerHand.set(4, new Card(5,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	 	// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

		// set Four of a Kind
		playerHand.clear();
		playerHand.add(new Card(8,4));
		playerHand.add(new Card(8,1));
		playerHand.add(new Card(12,4));
		playerHand.add(new Card(8,2));
		playerHand.add(new Card(8,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Three of a Kind
		playerHand.set(4, new Card(11,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Full House
		playerHand.set(2, new Card(11,2));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Two Pairs
		playerHand.set(1, new Card(9,2));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Royal Pair
		playerHand.set(0, new Card(3,2));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// non Royal Pair
		playerHand.set(2, new Card(3,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");
      	}
      	catch (Exception e)
      	{
		System.out.println(e.getMessage());
      	}
    }

    /** testOneDeck() is used to test OneDeck class  
     *  testOneDeck() should execute OneDeck's main()
     */ 
    public void testOneDeck()
    {
    	OneDeck tmp = new OneDeck();
        tmp.main(null);
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) 
    {
	VideoPoker pokergame = new VideoPoker();
	pokergame.testCheckHands();
    }
}
