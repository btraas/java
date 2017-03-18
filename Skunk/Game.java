
/**
 * Main game class.
 * 
 * @author Brayden
 * @version 1
 */


public class Game
{
   public static final boolean DEBUG = false; // Set this TRUE for verbose testing output
   
   public static final int MIN_WORD_LENGTH = 2;
   public static final int MAX_WORD_LENGTH = 10;
   
   public static int NUM_COLUMNS = 5;
   public static String[] COLUMNS = new String[]{ "S", "K", "U", "N", "K" };
   public static String PLAYER_NAME = "Player";
   
   public static boolean PLAYER_GOES_FIRST = true;   // set to false for CPU to decide first.
   
   public static double UI_NEWLINE_DELAY = 0.03;     // seconds to wait before each newline
   public static double UI_CLEAR_DELAY   = 0.01;     // seconds to wait before each newLine in a Terminal.clear() command
   public static double END_OF_TURN_SECONDS = 1.0;   // seconds to wait at end of turn
   public static boolean NEWLINE_DELAYS_ON = true;   // Turn Newline delays on?
   public static int UI_CHAR_DELAY_MS = 10;
   public static boolean CHAR_DELAYS_ON = false;
   
   public static int PLAYER_WIN_COUNTER = 0;
   public static int CPU_WIN_COUNTER = 0;
   
   public static final int PLAYER_PAD_CHARS = 19;
   
   public static int HIGH_COLUMN_SCORE = 25;
   public static int AI_SCORE_DIFFERENCE_STRATEGY = 10;
   
   public static final int AI_DEFAULT_CHANCES_1 = 2;    // 2 in 3
   public static final int AI_DEFAULT_CHANCES_2 = 3;    // Used staticly for EASY mode.
   
   public static final int AI_VERYUNLIKELY_CHANCES_1 = 1; // 1 in 4
   public static final int AI_VERYUNLIKELY_CHANCES_2 = 4;
   
   public static final int AI_UNLIKELY_CHANCES_1 = 1;   // 1 in 3
   public static final int AI_UNLIKELY_CHANCES_2 = 3;
   
   public static final int AI_5050_CHANCES_1 = 1;       // 1 in 2
   public static final int AI_5050_CHANCES_2 = 2;
   
   public static final int AI_LIKELY_CHANCES_1 = 2;     // 2 in 3
   public static final int AI_LIKELY_CHANCES_2 = 3;
   
   public static final int AI_MORELIKELY_CHANCES_1 = 3; // 3 in 4
   public static final int AI_MORELIKELY_CHANCES_2 = 4;
   
   public static final int AI_VERYLIKELY_CHANCES_1 = 4; // 4 in 5
   public static final int AI_VERYLIKELY_CHANCES_2 = 5;
   
   public static final int AI_MOSTLIKELY_CHANCES_1 = 5; // 5 in 6
   public static final int AI_MOSTLIKELY_CHANCES_2 = 6;
   
   public static final int ROLL_1 = 1;
   public static final int ROLL_2 = 2;
   public static final int ROLL_3 = 3;
   public static final int ROLL_4 = 4;
   public static final int ROLL_5 = 5;
   
   public static boolean MISC_DELAYS_ON = true;
   public static final double UI_WAITTIME = 0.5;
   public static final double UI_WAITTIME_LONG = 1;
   
   private boolean[]    columnsStarted;
   
   private int[]        playerScores;
   private boolean[]    playerFinished;
   
   private int[]        cpuScores;
   private boolean[]    cpuFinished;
   
   public static int DEFAULT_CPU_DIFFICULTY = 1;
   public static boolean DEFAULT_AUTOSHOW_SCOREBOARD = true;
   public static boolean DEFAULT_NO_ONES_FIRST_ROLL = true;
   
   private int cpuDifficulty; // 0 = easy (no AI), 1 = normal (fair AI), 2 = hard (hardest AI, CPU never gets a 1), 3 = insane (CPU cheats 25%)
   public static final int DIFFICULTY_LEVELS = 4;
   public static final String[] DIFFICULTY_MAP = new String[]{ "Easy", "Normal", "Hard", "Insane" };
   public static final int AI_EASY = 0;
   public static final int AI_NORMAL = 1;
   public static final int AI_HARD = 2;
   public static final int AI_INSANE = 3;
   
   private int roll;
   private int firstRoll1Counter = 0;
   
   public static void main(String[] args) {
	   new Game();
   }
   
   public Game()
   {
       Terminal.CHAR_DELAY_MS = CHAR_DELAYS_ON    ? UI_CHAR_DELAY_MS    : 0;
       Terminal.NEWLINE_DELAY = NEWLINE_DELAYS_ON ? UI_NEWLINE_DELAY    : 0.0;
       Terminal.CLEAR_DELAY   = NEWLINE_DELAYS_ON ? UI_CLEAR_DELAY      : 0.0;
       
       cpuDifficulty = DEFAULT_CPU_DIFFICULTY; // set from default
       
       playerScores     = new int[NUM_COLUMNS];
       cpuScores        = new int[NUM_COLUMNS];
       playerFinished   = new boolean[NUM_COLUMNS];
       cpuFinished      = new boolean[NUM_COLUMNS];
       columnsStarted   = new boolean[NUM_COLUMNS];
       
       for(int i = 0; i < NUM_COLUMNS; i++) // set default game values
       {
           playerScores[i] = 0;
           cpuScores[i] = 0;
           playerFinished[i] = false;
           cpuFinished[i] = false;
           columnsStarted[i] = false;
       }
       
       // This method displays a prompt and returns on <Enter>
       Terminal.getEnter("Welcome to SKUNK. Press <Enter> to play.");
       
       // This method prints a blank new line (default 1)
       Terminal.newLine();
       Terminal.print("Starting new game with the word: \""); // Same as System.out.print()
       String word = "";
       for(int i = 0; i < NUM_COLUMNS; i++)
       {
           word += COLUMNS[i];
       }
       Terminal.println(word+"\".");        // Same as System.out.println()
       Terminal.newLine();
       Terminal.println("Difficulty is set to "+DIFFICULTY_MAP[cpuDifficulty]+". Type D to change.");
       Terminal.println("Type S at anytime to see the scoreboard");
       Terminal.println("Type R at anytime to see the rules");
       Terminal.println("Type O at anytime to change options (resets current game)");
       Terminal.println("Type Q at anytime to quit.");
           
       play();
       endGame(false, true); // boolean for hiding results, game completed.
   }
  
   private void endGame(boolean silent, boolean complete) // boolean for silent (hiding results, no tally)
   {
       if(complete)
       {
           int difference = getScore(PLAYER_NAME) - getScore("CPU");
           if(difference > 0)       PLAYER_WIN_COUNTER++;
           else if(difference < 0)  CPU_WIN_COUNTER++;
           // otherwise no counters increased!
       }
       if(!silent)
       {
           showScoreboard(true); // TRUE for end-of-game (different grammar).
           Terminal.println("Thank-you for playing!");
           if(Terminal.confirm("Play again?")) new Game(); // Terminal.confirm() returns true or false if user inputs Y or N
           else endGame(true, false); // call self to quit.
       }
       else System.exit(0); // Silently quit
   }
   
   private boolean checkOptions(String in)
   {
       String letter = in.toUpperCase();
       //Terminal.println("DEBUG: TESTING " +letter+" AS COMMAND!");
       if(letter.equals("D") || letter.equals("S") || letter.equals("R") || letter.equals("Q") || letter.equals("O")) return true;
       else return false;
   }
   
   private boolean runCommand(String in) // Method to handle player commands
   {
       switch(in.toUpperCase())
       {
           case "D" : return changeDifficulty();
           case "S" : Terminal.clear(); showScoreboard(false); return false; // Terminal.clear() prints 20 or so lines
           case "R" : showRules(); return false;
           case "O" : return showOptions();
           case "Q" : endGame(false, false); return true;
           default  : Terminal.println("Unknown command... internal error!"); return false;
       }
       
   }
   
   private int getScore(String player) // helper for showScoreBoard() - Calculates total scores
   {
       int score = 0;
       if(player.equals(PLAYER_NAME)) 
       {
           for(int i = 0; i < NUM_COLUMNS; i++)
           {
               score += playerScores[i];
           }
        }
       else if(player.equalsIgnoreCase("CPU")) 
       {
           for(int i = 0; i < NUM_COLUMNS; i++)
           {
               score += cpuScores[i];
           }
        }
        return score;
   }
   
   private String playerScore(int scoreIn, boolean started) // helper for showScoreBoard()
   {
       if(scoreIn < 0) scoreIn = 0;
       
       if(!started) return "   ";
       else if(scoreIn > 99) return ""   + scoreIn;
       else if(scoreIn > 9)  return " "  + scoreIn;
       else                  return " "  + scoreIn + " ";
   }
   
   private void showScoreboard(boolean endOfGame)
   {
       if(endOfGame)
       {
           Terminal.println("");
           Terminal.println("~ FINAL SCORE ~");
       }
       else
       {
           Terminal.newLine();
           Terminal.println("~ SCOREBOARD ~");
       }
       
       String playerWinString = PLAYER_WIN_COUNTER == 1 ? " win" : " wins";
       String cpuWinString =    CPU_WIN_COUNTER    == 1 ? " win" : " wins";
       
       Terminal.println("");
       Terminal.println(" "+PLAYER_NAME+" ("+PLAYER_WIN_COUNTER+playerWinString+"): ");
       Terminal.print(" ");
       for(int i = 0; i < NUM_COLUMNS; i++)
       {
           Terminal.print(" "+COLUMNS[i]+" |");
       }
       Terminal.println("    Total");
       
       Terminal.print(" ");
       for(int i = 0; i < NUM_COLUMNS; i++)
       {
           Terminal.print(playerScore(playerScores[i], columnsStarted[i]) + "|");
       }
       Terminal.println("    "+getScore(PLAYER_NAME));
       
       Terminal.println("");
       Terminal.println(" CPU ("+CPU_WIN_COUNTER+cpuWinString+"): ");
       Terminal.print(" ");
       for(int i = 0; i < NUM_COLUMNS; i++)
       {
           Terminal.print(" "+COLUMNS[i]+" |");
       }
       Terminal.println("    Total");
       
       Terminal.print(" ");
       for(int i = 0; i < NUM_COLUMNS; i++)
       {
           Terminal.print(playerScore(cpuScores[i], columnsStarted[i]) + "|");
       }
       Terminal.println("    "+getScore("cpu"));
       
       Terminal.println("");
       
       String winString = "Winning!";
       String loseString = "Losing!";
       String tieString = "Tied!";
       
       if(endOfGame)
       {
           winString = "Winner!";
           loseString = "Loser!";
           tieString = "Tie!";
       }
       
       if(getScore(PLAYER_NAME) > getScore("CPU")) Terminal.println(winString);
       else if(getScore(PLAYER_NAME) < getScore("CPU")) Terminal.println(loseString);
       else Terminal.println(tieString);
       
       Terminal.println("~~~   ~~~");
       Terminal.newLine(); 
       
   }
  
   private void showRules()
   {
       // show rules here
       Terminal.clear();
       Terminal.println("~ RULES OF SKUNK ~");
       Terminal.print("There are "+NUM_COLUMNS+" columns, 1 for each letter in ");
       for(int i = 0; i < NUM_COLUMNS; i++)
       {
           Terminal.print(COLUMNS[i]);   // print each string in array
       }
       Terminal.println(".");
       Terminal.println("For each column, two dice are rolled numerous times.");
       Terminal.println("The total count on both dice are added to each participating player's current column.");
       Terminal.println("However, if one of the dice has a value of 1, the current column value is reset to 0.");
       Terminal.println("If both of the dice have a value of 1, all previous and the current columns are reset to 0.");
       Terminal.println("Each player has a choice to stop participating before each roll to 'lock-in' their score");
       Terminal.println("and prevent being affected by a '1' roll and their column score from resetting to 0.");
       Terminal.println("Once a player has stopped participating, they cannot continue collecting points until");
       Terminal.println("the next column.");
       Terminal.println("Once all players stop participating or a '1' is rolled, the game moves to the next column.");
       Terminal.println("~~~          ~~~");
       Terminal.newLine(3);         // Print 3 new (blank) lines
    
   }
   
   private int isPlayerRunning() // 0 = false, 1 = true, 2 = re-roll
   {
       String input = Terminal.getInput("Participate? Y/N"); // Return Scanner input
       if(DEBUG) Terminal.println("DEBUG: Player chose: "+input+".");
                  
       if(input.equalsIgnoreCase("Y"))
       {
           return 1;
       }
       else if(input.equalsIgnoreCase("N")) 
       {
           return 0;
       }
       else if(checkOptions(input))
       {
           roll--;     // that last roll didn't count. we're trying again.
           if(runCommand(input)) endGame(false, false); // returns true to end game
           return 2;                // Continue while loop (re-start roll)
       }
       else 
       {
           roll--; // that last roll didn't count. we're trying again.
           Terminal.println("Invalid input. Please try again!");
           return 2;
       }
   }
   
   
   private void play() // Play turn
   {
       // OK, use instance vars above to determine progress, and continue.
       
      for(int i = 0; i < NUM_COLUMNS; i++) // for each column
      {   
          roll = 0;
          columnsStarted[i] = true;
          while(!playerFinished[i] || !cpuFinished[i]) // for each dice roll
          {
              
              DieRoll d1 = new DieRoll();   // Generates a random number between 1 and 6
              DieRoll d2 = new DieRoll();
              roll++;
              
              while(DEFAULT_NO_ONES_FIRST_ROLL && roll == 1 && (d1.value() == 1 || d2.value() == 1))
              {
                  d1 = new DieRoll();
                  d2 = new DieRoll();
              }
              
              boolean playerRunning = false;
              boolean cpuRunning    = false;
               
              Terminal.newLine();
              Terminal.println("~~~~~~~~~~~   Column "+COLUMNS[i]+"  ~~~  Roll "+roll+"  ~~~~~~~~~~~~~");
              Terminal.println(Tools.padL(PLAYER_NAME,  PLAYER_PAD_CHARS)+": "+playerScores[i]);
              Terminal.println(Tools.padL(      "CPU",  PLAYER_PAD_CHARS)+": "+cpuScores[i]);
              Terminal.newLine();
              
              if(DEBUG && PLAYER_GOES_FIRST)            Terminal.println("Player going first!");
              else if(DEBUG && !PLAYER_GOES_FIRST)      Terminal.println("CPU going first!");
              
              
              if(!playerFinished[i] && PLAYER_GOES_FIRST)
              {
                  int a = isPlayerRunning();
                  if(a == 2) continue;
                  if(a == 1) playerRunning = true;
                  else if(a == 0) playerFinished[i] = true;// else, player not running(default)
                  else Terminal.println("Error! Unkown isPlayerRunning() response.");
              }
              if(!cpuFinished[i])
              {
                  if(DEBUG) Terminal.println("DEBUGGER: CPU DECIDING IF PARTICIPATING....");
            
                  cpuRunning = isAIPlaying(d1, d2); // IS CPU playing this time
                  cpuFinished[i] = !cpuRunning;     // True if CPU is still playing
                   
                  if(DEBUG) 
                  {
                      Terminal.print("DEBUGGER: CPU IS");
                      if(!cpuRunning) Terminal.print(" NOT");
                      Terminal.print(" PLAYING.");
                  }
              } else if(DEBUG) Terminal.println("DEBUGGER: CPU ALREADY FINISHED FOR COLUMN "+COLUMNS[i]);
              
              if(!playerFinished[i] && !PLAYER_GOES_FIRST) // if Player goes after CPU.
              {
                  Terminal.print("CPU ");
                  if(cpuRunning)    Terminal.print("is");
                  else                 Terminal.print("is not"); 
                  Terminal.println(" participating.");
                  
                  int a = isPlayerRunning();
                  if(a == 2) continue;
                  if(a == 1) playerRunning = true;
                  else if(a == 0) playerFinished[i] = true;// else, player not running(default)
                  else Terminal.println("Error! Unkown isPlayerRunning() response.");
              }
              
              Terminal.clear();
               
              Terminal.print(PLAYER_NAME+" ");
              if(playerRunning)    Terminal.print("is ");
              else                 Terminal.print("is not"); 
              Terminal.println(" participating.");
               
              Terminal.print("CPU ");
              if(cpuRunning)    Terminal.print("is");
              else                 Terminal.print("is not"); 
              Terminal.println(" participating.");
         
              if(!playerRunning && !cpuRunning) 
              {
                  Terminal.println("~ Moving on to next column! ~");
                  break;
              }
               
              Terminal.newLine();
              Terminal.print("Rolling the dice");
              
              for(int j = 0; j < 3; j++)
              {
                  if(MISC_DELAYS_ON) Terminal.sleep(UI_WAITTIME); // wait for half a second
                  Terminal.print(".");
              }
              if(MISC_DELAYS_ON) Terminal.sleep(UI_WAITTIME_LONG);
              Terminal.newLine();
              // Cue dice roll! -- no we do this earlier now, for insane difficulty
              // DieRoll d1 = new DieRoll();
              // DieRoll d2 = new DieRoll();
          
              Terminal.println(d1.value());
              if(MISC_DELAYS_ON) Terminal.sleep(UI_WAITTIME);
              Terminal.println(d2.value());
              if(MISC_DELAYS_ON) Terminal.sleep(UI_WAITTIME); 
              Terminal.newLine();
              
              if(d1.value() == 1 && d2.value() == 1) // reset scores!
              {
                  if(roll == ROLL_1) firstRoll1Counter++;
                  
                  for(int j = 0; j < NUM_COLUMNS; j++)
                  {
                      if(playerRunning) playerScores[j] = 0;
                      if(cpuRunning)    cpuScores[j]    = 0;
                  }
                  playerFinished[i] = true;
                  cpuFinished[i] = true;
                  Terminal.println("All Columns reset for participating players!");
                  
                  if(roll == 1 && firstRoll1Counter > 2)
                  {
                      Terminal.newLine();
                      Terminal.println("Tired of 1s on the first roll? Disable it in the game options! (Type \"O\" + <Enter>)");
                      Terminal.newLine();
                  }
                  Terminal.println("~ Moving on to next column! ~");
              }
              else if(d1.value() == 1 || d2.value() == 1)
              {
                  if(roll == ROLL_1) firstRoll1Counter++;
                  
                  if(playerRunning) playerScores[i] = 0;
                  if(cpuRunning)    cpuScores[i]    = 0;
                  playerFinished[i] = true;
                  cpuFinished[i] = true;
                  Terminal.println("Column "+COLUMNS[i]+" reset for participating players!");
                  if(firstRoll1Counter > 2)
                  {
                      Terminal.newLine();
                      Terminal.println("Tired of 1s on the first roll? Disable it in the game options! (Type \"O\" + <Enter>)");
                      Terminal.newLine();
                  }
                  Terminal.println("~ Moving on to next column! ~");
              }
              else
              {
                  int newval = d1.value() + d2.value();
                  if(playerRunning) playerScores[i] += newval;
                  if(cpuRunning)    cpuScores[i]    += newval; 
                  Terminal.println("Column "+COLUMNS[i]+" increased by "+newval+".");
              }
              if(MISC_DELAYS_ON) Terminal.sleep(END_OF_TURN_SECONDS);
              if(DEFAULT_AUTOSHOW_SCOREBOARD)
              {
                  Terminal.newLine(2);
                  showScoreboard(false);
              }
             
          }
      }
   }
   
   private boolean showOptions()
   {
       Terminal.clear();
       Terminal.println("~ GAME OPTIONS ~");
       Terminal.newLine();
       DEFAULT_AUTOSHOW_SCOREBOARD = Terminal.confirm("Auto-show scoreboard after each diceroll?");
       Terminal.newLine(2);
       DEFAULT_NO_ONES_FIRST_ROLL = !Terminal.confirm("Allow ones on first roll?"); // flip result
       Terminal.newLine(2);
       
       PLAYER_GOES_FIRST = Terminal.confirm("Player goes first?");
       Terminal.newLine(2);
       
       // getAlNum returns only Alphanumeric Scanner input.
       String newName = Terminal.getAlNum("Enter your player's name between "+MIN_WORD_LENGTH+"-"+MAX_WORD_LENGTH+" characters. (Currently \""+PLAYER_NAME+"\") or C to cancel.");
       while(newName == null || (!newName.equalsIgnoreCase("C") && (newName.length() < MIN_WORD_LENGTH || newName.length() > MAX_WORD_LENGTH)))
       {
           newName = Terminal.getAlNum("Please enter a new name between "+MIN_WORD_LENGTH+"-"+MAX_WORD_LENGTH+" characters or C to cancel.");
       }
       if(newName.equalsIgnoreCase("C")) Terminal.println("Player name unchanged. ("+PLAYER_NAME+")");
       else
       {
           PLAYER_NAME = newName;
           Terminal.println("Player name changed to \""+PLAYER_NAME+"\".");
       }
       
       Terminal.newLine(2);
       if(Terminal.confirm("Show advanced options?")) showAdvancedOptions();
       
       Terminal.println("Changes saved.");
       Terminal.newLine();
       if(MISC_DELAYS_ON) Terminal.sleep(UI_WAITTIME_LONG);
       
       new Game();
       endGame(true, false); // silently end current game
       return true;
   }
   
   private void showAdvancedOptions()
   {
       Terminal.clear();
       Terminal.println("~ ADVANCED OPTIONS ~");
       Terminal.newLine();
       String oldGameWord = "";
       String prompt = "Current game word is \"";
       for(int i = 0; i < NUM_COLUMNS; i++)
       {
           oldGameWord += COLUMNS[i];
       }
       prompt += oldGameWord;
       prompt += "\". Enter a new word now (between "+MIN_WORD_LENGTH+"-"+MAX_WORD_LENGTH+" characters), or C to cancel.";
       String newStr = Terminal.getAlphaUC(prompt);
       while(newStr == null || (!newStr.equals("C") && (newStr.length() < MIN_WORD_LENGTH || newStr.length() > MAX_WORD_LENGTH)))
       {
           // getAlphaUC returns only alpha characters, converted to uppercase from Scanner input.
           newStr = Terminal.getAlphaUC("Please enter a new word between "+MIN_WORD_LENGTH+"-"+MAX_WORD_LENGTH+" characters, or C to cancel.");
       }
       if(newStr.equals("C")) Terminal.println("Game word unchanged. ("+oldGameWord+")");
       else
       {
           NUM_COLUMNS = newStr.length();
           COLUMNS = new String[NUM_COLUMNS];
           
           if(DEBUG) Terminal.println("DEBUG: NEW COLUMNS: "+NUM_COLUMNS);
           if(DEBUG) Terminal.println("DEBUG: NEW WORD: "+newStr);
           
           for(int i = 0; i < NUM_COLUMNS; i++)
           {
               COLUMNS[i] = Character.toString(newStr.charAt(i));
           }
           Terminal.println("Game word has been changed to \""+newStr+"\".");
        
       }
       
       Terminal.newLine(2);
       NEWLINE_DELAYS_ON = NEWLINE_DELAYS_ON ? !Terminal.confirm("Turn off newline delays?") : Terminal.confirm("Turn on newline delays?");
       
       Terminal.newLine(2);
       MISC_DELAYS_ON = MISC_DELAYS_ON ? !Terminal.confirm("Turn off diceroll & turn delays?") : Terminal.confirm("Turn on diceroll & turn delays?");
       
       Terminal.newLine(2);
       CHAR_DELAYS_ON = CHAR_DELAYS_ON ? !Terminal.confirm("Turn off character delays?") : Terminal.confirm("Turn on character delays?");
       
       Terminal.newLine(3);
       
       
   }
   
   private boolean changeDifficulty()
   {
       Terminal.clear();
       Terminal.println("~ CHANGING DIFFICULTY ~");
       if(playerFinished[0]) Terminal.println("WARNING: This will reset your current game. Press C to cancel...");
       Terminal.println("Current difficulty is: "+cpuDifficulty+" ("+DIFFICULTY_MAP[cpuDifficulty]+"). Type any of the following numbers below to change.");
       
       for(int i = 0; i < DIFFICULTY_LEVELS; i++)
       {
           Terminal.println(i+": "+DIFFICULTY_MAP[i]);
       }
       Terminal.newLine();
       
       
       String input = Terminal.getAlNumUC(); // Get Alphanumeric scanner input, convert to uppercase
           
       while( !input.equalsIgnoreCase("C") && (Integer.parseInt(input) < 0 || Integer.parseInt(input) > DIFFICULTY_LEVELS) )
       {
           input = Terminal.getAlNumUC("Please choose a number listed above or C to cancel.");
       }
       // Continue here if acceptable above
       if(input.equalsIgnoreCase("C"))
       {
           Terminal.clear();
           return false; // return to game
       }
       else if(DIFFICULTY_MAP[Integer.parseInt(input)] != null)
       {
           int inputInt = Integer.parseInt(input);
           DEFAULT_CPU_DIFFICULTY = inputInt;  // change default for future games
           cpuDifficulty = inputInt;           // change this difficulty
           Terminal.clear();
           Terminal.println("Difficulty changed to "+inputInt+" ("+DIFFICULTY_MAP[inputInt]+").");
               
           Terminal.newLine(3);
                
           new Game();
           endGame(true, false); // end old game
               
           return true;
       }
       else Terminal.println("INTERNAL ERROR. CONTACT THE DEVELOPER...");
           
       
       return false; 
   }
    
   private boolean isAIPlaying(DieRoll d1, DieRoll d2) // Decide if AI is in or out
   {
        if(cpuDifficulty < 3) return doAI(cpuDifficulty, d1, d2);
        else
        {
            boolean lvl3AI = doAI(2, d1, d2);
            
            if(d1.value() == 1 || d2.value() == 1) return false;
            else return lvl3AI;
        }
   }
    
   private boolean doAI(int difficulty, DieRoll d1, DieRoll d2)
   {
       // getChances() is a randomizer that "gets the chances of 'x' in 'y'".
       // It chooses a random number between 1 and y; if it's <= x, return true. Else return false.
        
        if(difficulty == AI_EASY) return Tools.getChances(AI_DEFAULT_CHANCES_1,AI_DEFAULT_CHANCES_2); //  this has a 2/3 (default) chance of being TRUE.
        
        int difference = getScore("CPU") - getScore(PLAYER_NAME); // positive if CPU is winning
        if(DEBUG) Terminal.println("DEBUGGER: Difficulty: "+DIFFICULTY_MAP[difficulty]+". roll: "+roll+" difference: "+difference);
            
        if(difficulty == AI_NORMAL)
        {
           if(roll == ROLL_1) return true;    // Always participate on first roll
            if(DEBUG) Terminal.println("DEBUGGER: Not first roll, let's do some real AI!");
            
            if(difference < (0 - AI_SCORE_DIFFERENCE_STRATEGY))
            {
                if(roll == ROLL_2) return   true;
                if(roll == ROLL_3) return   Tools.getChances(AI_MOSTLIKELY_CHANCES_1,   AI_MOSTLIKELY_CHANCES_2);
                if(roll == ROLL_4) return   Tools.getChances(AI_VERYLIKELY_CHANCES_1,   AI_VERYLIKELY_CHANCES_2); 
                if(roll == ROLL_5) return   Tools.getChances(AI_MORELIKELY_CHANCES_1,   AI_MORELIKELY_CHANCES_2); 
                return                      Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2);
            }
            else if(difference > AI_SCORE_DIFFERENCE_STRATEGY)
            {
                if(roll == ROLL_2) return   Tools.getChances(AI_VERYLIKELY_CHANCES_1,   AI_VERYLIKELY_CHANCES_2);
                if(roll == ROLL_3) return   Tools.getChances(AI_MORELIKELY_CHANCES_1,   AI_MORELIKELY_CHANCES_2);
                if(roll == ROLL_4) return   Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2);
                return                      Tools.getChances(AI_5050_CHANCES_1,         AI_5050_CHANCES_2);
            }
            else
            {
                if(roll == ROLL_2) return   Tools.getChances(AI_MOSTLIKELY_CHANCES_1,   AI_MOSTLIKELY_CHANCES_2);
                if(roll == ROLL_3) return   Tools.getChances(AI_VERYLIKELY_CHANCES_1,   AI_VERYLIKELY_CHANCES_2);
                if(roll == ROLL_4) return   Tools.getChances(AI_MORELIKELY_CHANCES_1,   AI_MORELIKELY_CHANCES_2);
                return                      Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2);
            }
            
        }
        
        int column = 0;
        for(int i = 0; i < NUM_COLUMNS; i++)
        {
            column = i;
            if(!cpuFinished[i]) break; // then this is current column!
        }
        int columnScore = cpuScores[column];
        
        if(difficulty == AI_HARD)
        {
            if(roll == ROLL_1) return true;    // Always participate on first roll
            if(columnScore > HIGH_COLUMN_SCORE)
            {
                if(difference < (0 - AI_SCORE_DIFFERENCE_STRATEGY)) // if losing overall
                {
                    if(roll == ROLL_2)  return  true;
                    if(roll == ROLL_3)  return  Tools.getChances(AI_MOSTLIKELY_CHANCES_1,   AI_MOSTLIKELY_CHANCES_2);
                    if(roll == ROLL_4)  return  Tools.getChances(AI_VERYLIKELY_CHANCES_1,   AI_VERYLIKELY_CHANCES_2);
                    if(roll == ROLL_5)  return  Tools.getChances(AI_MORELIKELY_CHANCES_1,   AI_MORELIKELY_CHANCES_2);
                    return                      Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2); // else return this
                }
                else if(difference > AI_SCORE_DIFFERENCE_STRATEGY) // if overall winning
                {
                    if(roll == ROLL_2)  return  Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2);
                    if(roll == ROLL_3)  return  Tools.getChances(AI_5050_CHANCES_1,         AI_5050_CHANCES_2);
                    if(roll == ROLL_4)  return  Tools.getChances(AI_UNLIKELY_CHANCES_1,     AI_UNLIKELY_CHANCES_2);
                    return                      Tools.getChances(AI_VERYUNLIKELY_CHANCES_1, AI_VERYUNLIKELY_CHANCES_2);
                }
                else
                {
                    if(roll == ROLL_2)  return  Tools.getChances(AI_MORELIKELY_CHANCES_1,   AI_MORELIKELY_CHANCES_2);
                    if(roll == ROLL_3)  return  Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2);
                    if(roll == ROLL_4)  return  Tools.getChances(AI_5050_CHANCES_1,         AI_5050_CHANCES_2);
                    return                      Tools.getChances(AI_UNLIKELY_CHANCES_1,     AI_UNLIKELY_CHANCES_2);
                }
            }
            else
            {
                if(difference < (0 - AI_SCORE_DIFFERENCE_STRATEGY)) // if losing overall
                {
                    if(roll == ROLL_2)  return true;
                    if(roll == ROLL_3)  return  Tools.getChances(AI_MOSTLIKELY_CHANCES_1,   AI_MOSTLIKELY_CHANCES_2);
                    if(roll == ROLL_4)  return  Tools.getChances(AI_VERYLIKELY_CHANCES_1,   AI_VERYLIKELY_CHANCES_2);
                    if(roll == ROLL_5)  return  Tools.getChances(AI_MORELIKELY_CHANCES_1,   AI_MORELIKELY_CHANCES_2);
                    return                      Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2);
                }
                else if(difference > AI_SCORE_DIFFERENCE_STRATEGY) // if overall winning
                {
                    if(roll == ROLL_2)  return  Tools.getChances(AI_MORELIKELY_CHANCES_1,   AI_MORELIKELY_CHANCES_2);
                    if(roll == ROLL_3)  return  Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2);
                    if(roll == ROLL_4)  return  Tools.getChances(AI_5050_CHANCES_1,         AI_5050_CHANCES_2);
                    return                      Tools.getChances(AI_UNLIKELY_CHANCES_1,     AI_UNLIKELY_CHANCES_2);
                }
                else
                {
                    if(roll == ROLL_2)  return  Tools.getChances(AI_VERYLIKELY_CHANCES_1,   AI_VERYLIKELY_CHANCES_2);
                    if(roll == ROLL_3)  return  Tools.getChances(AI_MORELIKELY_CHANCES_1,   AI_MORELIKELY_CHANCES_2);
                    if(roll == ROLL_4)  return  Tools.getChances(AI_LIKELY_CHANCES_1,       AI_LIKELY_CHANCES_2);
                    return                      Tools.getChances(AI_5050_CHANCES_1,         AI_5050_CHANCES_2);
                }
            }
        }
        else return Tools.getChances(AI_DEFAULT_CHANCES_1,  AI_DEFAULT_CHANCES_2);
   }
}
