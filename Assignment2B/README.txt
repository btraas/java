
 ###############################################
 #                                             #
 #       COMP 2526: Assignment 2b README       #
 #          Brayden Traas                      #
 #          A00968178                          #
 #                                             #
 ###############################################

Starting the Game of Life:
1. Copy the settings.properties file and image files to the project root
2. Compile and run as a Java application

Instructions:
1. Choose options as per the initial settings dialog & click save. The game frame will appear.
2. Manually take a turn by clicking the left mouse button anywhere in the frame.
3. Run turns automatically by pressing spacebar. Toggle this on/off by pressing the spacebar.

Notes:
1. The settings.properties file must exist to run. If one doesn't exist, the program will create one for you.
    - This file stores the options you choose on launch, so that chosen options are persistent (unless changed).
2. The game of life can be run with either HexCells or SquareCells, defined in settings.properties (or the options dialog).
    - Gameplay and visuals are different between Hex & Square cells; some requirements (such as minimum reproduction neighbors) 
      needed to be tweaked to work better on a board of hexes. This program was designed primarily for use with hexes.
3. The program attempts to load the two image files: water.png and dirt.png to represent terrain. If these files cannot be loaded,
      blue / brown colored cells will be used to represent water and dirt instead.
4. Any "life" that has moved since the previous turn will be represented by a coloured arrow, representing its last direction of travel.
    - Any life that has not moved in the last turn will be represented as a coloured circle instead (see screenshot.png).

Modified Requirements:
1. All LifeTypes (Plant, Herbivore, Omnivore, Carnivore) have some stats tweaked for balancing with Hex cells. This was necessary because of the smaller number of adjacent cells to each cell in a hex "grid". Stats for LifeTypes should match A2B requirements exactly for SquareCell worlds. (As per LifeType.java and the settings.properties file choosing hex vs. square Cell type)

2. Main.java and GameFrame.java are modified. This is to show the Settings JFrame before creating & displaying the world, and also allowing a variable World type (parameterized Cell type: <CellT extends Cell> either HexCell or SquareCell).

3. UI (GameFrame, PolygonPanel, HexPanel, SquarePanel) has been (mostly) separated from data & logic (Main, World, Cell, Life etc.). I've noticed that this both makes my code easier to read, and run more efficiently. When the PolygonPanel (either SquarePanel or HexPanel) repaints, it iterates over each Cell of the specified World object, and draws / paints its shape at the Cell's data coordinates, with the color/picture of the Cell's Terrain (enum), and then draws a circle or arrow to represent the Cell's Life objects it contains.

4. TurnListener was modified to also implement KeyListener, and listen for the spacebar (start & stop auto turn listener). Runs in a loop with a Thread.sleep() call at the end.