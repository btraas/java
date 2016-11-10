 
 ###############################################
 #                                             #
 #       COMP 2526: Assignment 2a README       #
 #          Brayden Traas                      #
 #          A00968178                          #
 #                                             #
 ###############################################


 
 README contents:

 Part A:		Required A2a classes
-------------------------------------------------------------------
	1. (Object) Interface Cell 
	2. (Object) Class Plant (extends Life)
	3. (Object) Class Herbivore (extends Animal (extends Life))
	4. (Data)   Class World
	5. (Setup)  Class Main
	6. (UI)	    Class GameFrame
	7. (UI)	    Class TurnListener	

 Part B: 	Additions to A2a requirements
-------------------------------------------------------------------
	1. (Object) Abstract Class Life
	2. (Object) Interface Moveable<DestinationT>
	3. (Object) Abstract Class Animal (extends Life, implements Moveable)
	4. (Setup)  Class Creator
	5. (Setup)  Class Settings
	6. (AI)	    Class MoveDecision<DestinationT>
	7. (AI)	    Class NearbyDecision (extends MoveDecision<Cell>)
	8. (AI)	    Class NearbyFoodDecision (extends NearbyDecision)
	9. (AI)	    Class DistanceDecision (extends NearbyDecision)
	10.(UI)	    Class Hexagon (extends Polygon)
	11.(UI)	    Class SquareCell (implements Cell, extends JPanel)
	12.(UI)	    Class HexCell (implements Cell, extends Hexagon)
	13.(UI)	    Class HexFrame (extends GameFrame)
	14.(UI)	    Class HexPanel (extends JPanel, inner class of HexFrame)
	 
	 
	 
	 
	 
	 
###############################################
#                                             #
#       A:    Required A2a classes:           #
#                                             #
###############################################

1. Interface Cell - added methods & instance variables. Constants not mentioned.
--------------------------------------------------

Cell[] getMoveToPossibilities(final Class[]<?> types, int distance):	Returns nearby Cells within the specified distance, 
								and containing Life of the specified allowed types.
								For example, a Herbivore can move 1 Cell away, to any
								empty or Plant-occupied cell.
							
Cell closest(final Cell[] haystack): 	Of a given haystack, finds the closest Cell to this one.
double distance(final Cell other): 		Calculates the distance between this Cell and another.
String getText(): 						Finds & returns the short text that represents this Cell. Note that this isn't the same as toString()


2. Class Plant (extends Life) - added methods
--------------------------------------------------
void processTurn():						Empty, does nothing, returns nothing.
								It's required by the Life parent class that processTurn()
								is defined. If a Plant does nothing on its turn, we should (and have to)
								explicitly define the method to do nothing.

3. Class Herbivore (extends Animal) - added methods
Defines required methods
--------------------------------------------------
void eat() (required by Animal):						Defines what happens when a herbivore eats (sets food supply = max)
Class<?>[] getFoodTypes() (required by Animal):			Returns an array of Classes (types) that are types that a Herbivore can eat (aka Plant.class)
Class<?>[] getMoveToLifeTypes() (required by Moveable):	Returns an array of Classes (types) that Herbivores can move to (as well as empty Cells)
public Cell decideMove(final Random seed, final Cell[] options) (required by Moveable<Cell>): Returns a decided destination Cell. 


4. Class World - added instance variables, constructor, methods
--------------------------------------------------

Constructor:							public World(int rows, int columns): Creates a World with this size. Doesn't create any Cells object; this is done later.
											// If a seed is provided in the settings.properties file, it will be used. This can be used to generate
											// and reproduce expected results.(if no settings.properties file exists, a default settings.properties file 
											// will be created, but it has seed=0; a seed of 0 indicates to generate a new random seed.).
											
											// The current settings and World's seed are printed to stdout at program execution.

Random getSeed():						Returns the randomSeed instance variable (type Random). Only one Random object should exist per world.

Cell createCellAt(final Cell newCell, int row, int column): Adds the given Cell to the specified array location, and may create life here.
										Returns the Cell (updated if it now contains life).

										
5. Class Main - Minor modifications (Load settings, variable grid type & world size, checkstyle & magic number/strings fixes).								

6. Class GameFrame - Minor modifications (Variable title by grid type, Choosing an explicit Cell type (SquareCell)).

7. Class TurnListener - Simply calls takeTurn() on the GameFrame on a mouseReleased() event.


 								
###############################################
#                                             #
#       B:    Additional A2a classes:         #
#                                             #
###############################################

1. Abstract Class Life
-----------------------------------------------------
This class defines an abstract Life object that includes Plants, Herbivores, and possible future Life types. 

Instance variables: -----------

	Color color: 	Background color that represents this Life type
 	Cell location:	Cell that this Life object resides in

Methods: ----------------------
	
	Necessary methods as defined in the assignment, common across Plant & Herbivore
	
	abstract void processTurn():	Child classes must define what happens to this Life type each turn.
	void destroy(): 		Sets the location of this Life to null, and empties the cell.
	Color getColor():		Returns the Color that this life is represented by.



2. Interface Moveable<DestinationT>
-----------------------------------------------------
	
	This Interface defines the methods below; for a type that is 'Moveable' to a 'DestinationT' type.
	 For example, Animal implements Moveable<Cell> because an Animal is Moveable<Cell>, as in: an Animal is Moveable betweeen Cells.
	
	void move():			Handles what happens when this Life is told to move
	Class<?>[] getMoveToLifeTypes():	Returns an array of Object types (Classes) that this Life type can move to (as well as null / empty)
	int getMoveDistance():		Returns an int representing the max distance that this Life can move per turn
	DestinationT decideMove(final Random seed, DestinationT[] options): Returns a choice from an array of options. The type is assigned when the Interface is implemented.

Other classes can see if a Life is-a "Moveable<?>" object, etc.


3. Abstract Class Animal (extends Life, implements Moveable<Cell>)
-----------------------------------------------------
A child class of Life, this Class implements Moveable<Cell>, and defines a move() method.
Does not provide a color or location that the parent Life class requires; all child classes must provide them.

Provides a default processTurn() method, which moves the Animal, decreases its food, and dies if it runs out of food.

In its move() method, it calls decideMove(). If child classes do not implement a
 decideMove() method, they will inherit Animal's default decideMove() method which is a simple MoveDecision<Cell>: Random between the options.
 
Herbivore defines its own decideMove() method which uses a level of AI: DistanceDecision (more info below).
 This Decision type is wary of nearby positive (food), negative (direct predators), and evasive (nearby predators) move options. 
 If no positive option is in range this turn, it will make a goal, and find make the best decision this turn to bring itself closer to the goal. 

 
4. Class Creator
-----------------------------------------------------
Un-instantiable (not abstract, but only a private constructor).
Contains static methods only; used to create a new life.
This class also defines the spawn rates of each life type.

One public method: createLife(Cell loc, Random seed)
This method will return a Life object with the defined spawn rates, a location reference and a predefined Random object to use as a seed.


5. Class Settings
-----------------------------------------------------
Another un-instantiable Class (not abstract, and only a private constructor).
Called on program load, this class loads settings from a file if it exists (otherwise creates a default one).

A few static methods are used to load specific settings (case insensitive):

String get(final String key)		: returns the key's value. If not found, prints a message to stderr and returns and empty string.
boolean getBoolean(final String key): returns the key's value as a boolean.
int getInt(final String key)		: returns the key's value as an int.
double getDouble(final String key)	: returns the key's value as a double. 


6. Class MoveDecision<DestinationT>
-----------------------------------------------------
A basic decision-making object. Makes moves at random given
a seed (Random object) and possible options (array of parameterized type DestinationT).

Subclasses can be created to implement "smarter" decisions, but more specific use cases.

For example: A MoveDecision<Cell> will create a MoveDecision of Cell objects. From an array of Cell objects, a Cell is returned (in the decide() method). 


Constructor:				public MoveDecision(final Random seed, final DestinationT[] options) 

Public method(s):			public DestinationT decide():			Default decide() method. Calls decide(true) below.
					public Destination decide(boolean forceRandom):		Decides an option at random via the seed & options given in the constructor.


7. Class NearbyDecision (extends MoveDecision<Cell>)
-----------------------------------------------------
A subclass of MoveDecision, this object makes a decision based on its surroundings, and is strictly limited to Cell objects.
The NearbyDecision uses the following static priority for decisions:

Non-negative > Evasive > Positive

Non-negative: 	Any move that doesn't instantly kill the user (animal).
Evaisve: 	Any move that doesn't put the user (animal) in danger for the next round.
Positive:	Any move that has a positive benefit to the user (animal). I.E. provides the user with food.

Eventually, I'd like to implement an even smarter nearby decision maker, favoring positive > evasive IF
 no food this round = death, and possibly 2+ turns as well? I'd have to work out what gives the user the
 best chances.

A further (more advanced) subclass DistanceDecision can make multi-turn decisions.


Constructor:				public NearbyDecision(final Random seed, final Cell[] options, final Class<?>[] positiveTypes, final Class<?>[] negativeTypes)

Public static methods:			public static Cell[] getPositiveOptions(Cell[] options, Class<?>[] positiveTypes)
					public static Cell[] getNotNegativeOptions(final Cell[] options, Class<?>[] negativeTypes) 
					public static Cell[] getNonNegativeOptions(final Cell[] options, Class<?>[] negativeTypes) (alias)
					public static Cell[] getEvasiveOptions(final Cell[] options, Class<?>[] negativeTypes, int distance) 
						(distance = minimum distance to leave between predator & self)

Public instance methods:		public Cell decide(): See Class description.


8. Class NearbyFoodDecision (extends NearbyDecision)
---------------------------------------------------
A subclass of NearbyDecision.
Unlike NearbyDecision, NearbyFoodDecision is ONLY concerned about food. Not used at this point; it's mostly for testing.

Constructor:				public NearbyFoodDecision(final Random seed, final Cell[] options, final Class<?>[] positiveTypes)

Public methods:				public Cell decide(): See Class description


9. Class DistanceDecision (extends NearbyDecision)
---------------------------------------------------
A subclass of NearbyDecision.
Reduces options just like a NearbyDecision, but if there's no positive target, it will find the next closest target (it can reach within its lifetime),
 use that as a goal, and decide on a move this turn that is closest to this target. 
 
Essentially moves towards the nearest food on the board (provided it's in range), and there's no consequences for moving to that Cell (no concequences yet).

Constructor:				public DistanceDecision(final Random seed, final Cell[] options, 
								final Class<?>[] positiveLifeTypes,
								final Class<?>[] negativeLifeTypes, final Cell current, int maxDistance)
								
Public methods:				public Cell decide(): See Class description.


10. Class Hexagon (extends Polygon)
---------------------------------------------------
A subclass of java.awt.Polygon, written by a StackOverflow user: Mr. Polywhirl
http://stackoverflow.com/questions/20734438/algorithm-to-generate-a-hexagonal-grid-with-coordinate-system

Slightly modified to meet Checkstyle & A2a requirements. 

Creates and draws a Hexagon, when given coordinates.

Constructor:				public Hexagon(int valueX, int valueY, int radius)

Public methods:				public void draw(final Graphics2D graphics, int valueX, int valueY, 
									int lineThickness, final Color colorValue, boolean filled)

							public void paint(final Color newColor) (added by myself, a simpler call to draw() to repaint.)
							
11. Class SquareCell (extends JPanel, implements Cell)
------------------------------------------------------
A subclass of JPanel, and implements Cell.

A SquareCell is a Cell used on a square grid (A2a requirements). Separated from Cell so that we can distinguish HexCells and SquareCells (although only one type per World is allowed).

Constructor:				public SquareCell(final World world, int row, int column)

Public methods:				All method implementations of Cell
							public void setText(final String text): sets the text within this Cell (JPanel). Technically sets the text of a JLabel within this JPanel.
							public void setText(): sets the text of this JPanel (see above) with the current value of this Cell (using the implemented getText() method).

							
12. Class HexCell (extends Hexagon, implements Cell)
-----------------------------------------------------
A subclass of Hexagon, and implements Cell.

Constructor:				public HexCell(final World world, int row, int col, int valueX, int valueY, int radius)

Public methods:				All method implementations of Cell
							public void paint() - Calls super.paint() with this occupier's color (or EMPTY_COLOR if there's no occupier). 

							
13. Class HexFrame (extends GameFrame)
-----------------------------------------------------
A subclass of GameFrame, adds a HexPanel instead of a GridLayout to itself.

Constructor:				public HexFrame(final World world)

Public methods:				(inherited from GameFrame)
							public void init(): Sets the title to mention using Hexes, adds a HexPanel insetad of a GridLayout, and adds a Mouse/TurnListener.

							
14. private Class HexPanel (extends JPanel, inner class of HexFrame)
------------------------------------------------------
This class is partially built from code by Mr. Polywhirl at Stackoverflow.com, but substantially modified.
http://stackoverflow.com/questions/20734438/algorithm-to-generate-a-hexagonal-grid-with-coordinate-system

Constructor:			private HexPanel(final World world, int size, int width)

Public methods:			public paintComponent(final Graphics graphics) // (called by Java internally when it decides a paint is needed.)
							// This method calls numerous private methods to redraw the a grid of HexCells. However if a HexCell already exists on the World object here,
							// it calls world.getCellAt() to use the existing Cell object(s) instead of creating new ones each turn.








