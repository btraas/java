package ca.bcit.comp2526.a2b;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.bcit.comp2526.a2b.LifeTools.Type;

public class WorldTest {

	static {
		Settings.load();
	}
	
	private static final int WORLD_SIZE_X = 25;
	private static final int WORLD_SIZE_Y = 25;
	private static final int WORLD_SIZE_X_ODD = 50;
	private static final int TURNS = 50;
	
	private static final HashMap<Point, CellEntry> WORLD_MAP;
	
	@SuppressWarnings("serial")
	private static class CellEntry extends SimpleEntry<Terrain, ArrayList<LifeTools.Type>> {

		private CellEntry(final Terrain arg0, final LifeTools.Type arg1) {
			super(arg0, typeToList(arg1));
		}
		
		private CellEntry(final Terrain arg0, final Type[] arg1) {
			super(arg0, new ArrayList<Type>(Arrays.asList(arg1)));
		}
		
		private CellEntry(final Terrain arg0, final ArrayList<Type> arg1) {
			super(arg0, arg1);
		}
		
		private static ArrayList<Type> typeToList(Type in) {
			ArrayList<Type> types = new ArrayList<Type>();
			types.add(in);
			return types;
		}
		
		private Terrain getTerrain() {
			return getKey();
		}
		
		private ArrayList<Type> getLifeTypes() {
			return getValue();
		}
		
	}
	
	static {
		HashMap<Point, CellEntry> tmpMap = new HashMap<Point, CellEntry>();
		
		tmpMap.put(new Point(1, 1), new CellEntry(Terrain.WATER, (Type)null));
		tmpMap.put(new Point(3, 3), new CellEntry(Terrain.WATER, (Type)null));
		tmpMap.put(new Point(3, 4), new CellEntry(Terrain.WATER, (Type)null));
		tmpMap.put(new Point(3, 6), new CellEntry(Terrain.WATER, (Type)null));
		
		tmpMap.put(new Point(4, 1), new CellEntry(Terrain.DEFAULT, Type.CARNIVORE));
		tmpMap.put(new Point(4, 2), new CellEntry(Terrain.DEFAULT, Type.HERBIVORE));
		tmpMap.put(new Point(4, 3), new CellEntry(Terrain.DEFAULT, Type.OMNIVORE));
		tmpMap.put(new Point(4, 4), new CellEntry(Terrain.DEFAULT, Type.PLANT));
		
		tmpMap.put(new Point(5, 1), new CellEntry(Terrain.WATER, Type.CARNIVORE));
		tmpMap.put(new Point(5, 2), new CellEntry(Terrain.WATER, Type.HERBIVORE));
		tmpMap.put(new Point(5, 3), new CellEntry(Terrain.WATER, Type.OMNIVORE));
		tmpMap.put(new Point(5, 4), new CellEntry(Terrain.WATER, Type.PLANT));
		
		
		
		tmpMap.put(new Point(6,  1), new CellEntry(Terrain.DEFAULT, new Type[] {Type.PLANT, Type.PLANT}));
		tmpMap.put(new Point(6,  2), new CellEntry(Terrain.DEFAULT, new Type[] {Type.PLANT, Type.HERBIVORE}));
		tmpMap.put(new Point(6,  3), new CellEntry(Terrain.DEFAULT, new Type[] {Type.PLANT, Type.OMNIVORE}));
		tmpMap.put(new Point(6,  4), new CellEntry(Terrain.DEFAULT, new Type[] {Type.PLANT, Type.CARNIVORE}));
		
		tmpMap.put(new Point(6,  5), new CellEntry(Terrain.DEFAULT, new Type[] {Type.HERBIVORE, Type.PLANT}));
		tmpMap.put(new Point(6,  6), new CellEntry(Terrain.DEFAULT, new Type[] {Type.HERBIVORE, Type.HERBIVORE}));
		tmpMap.put(new Point(6,  7), new CellEntry(Terrain.DEFAULT, new Type[] {Type.HERBIVORE, Type.OMNIVORE}));
		tmpMap.put(new Point(6,  8), new CellEntry(Terrain.DEFAULT, new Type[] {Type.HERBIVORE, Type.CARNIVORE}));
		
		tmpMap.put(new Point(6,  9), new CellEntry(Terrain.DEFAULT, new Type[] {Type.OMNIVORE, Type.PLANT}));
		tmpMap.put(new Point(6, 10), new CellEntry(Terrain.DEFAULT, new Type[] {Type.OMNIVORE, Type.HERBIVORE}));
		tmpMap.put(new Point(6, 11), new CellEntry(Terrain.DEFAULT, new Type[] {Type.OMNIVORE, Type.OMNIVORE}));
		tmpMap.put(new Point(6, 12), new CellEntry(Terrain.DEFAULT, new Type[] {Type.OMNIVORE, Type.CARNIVORE}));
		
		tmpMap.put(new Point(6, 13), new CellEntry(Terrain.DEFAULT, new Type[] {Type.CARNIVORE, Type.PLANT}));
		tmpMap.put(new Point(6, 14), new CellEntry(Terrain.DEFAULT, new Type[] {Type.CARNIVORE, Type.HERBIVORE}));
		tmpMap.put(new Point(6, 15), new CellEntry(Terrain.DEFAULT, new Type[] {Type.CARNIVORE, Type.OMNIVORE}));
		tmpMap.put(new Point(6, 16), new CellEntry(Terrain.DEFAULT, new Type[] {Type.CARNIVORE, Type.CARNIVORE}));
		
		
		// Water testing
		
		tmpMap.put(new Point(7,  1), new CellEntry(Terrain.WATER, new Type[] {Type.PLANT, Type.PLANT}));
		tmpMap.put(new Point(7,  2), new CellEntry(Terrain.WATER, new Type[] {Type.PLANT, Type.HERBIVORE}));
		tmpMap.put(new Point(7,  3), new CellEntry(Terrain.WATER, new Type[] {Type.PLANT, Type.OMNIVORE}));
		tmpMap.put(new Point(7,  4), new CellEntry(Terrain.WATER, new Type[] {Type.PLANT, Type.CARNIVORE}));
		
		tmpMap.put(new Point(7,  5), new CellEntry(Terrain.WATER, new Type[] {Type.HERBIVORE, Type.PLANT}));
		tmpMap.put(new Point(7,  6), new CellEntry(Terrain.WATER, new Type[] {Type.HERBIVORE, Type.HERBIVORE}));
		tmpMap.put(new Point(7,  7), new CellEntry(Terrain.WATER, new Type[] {Type.HERBIVORE, Type.OMNIVORE}));
		tmpMap.put(new Point(7,  8), new CellEntry(Terrain.WATER, new Type[] {Type.HERBIVORE, Type.CARNIVORE}));
		
		tmpMap.put(new Point(7,  9), new CellEntry(Terrain.WATER, new Type[] {Type.OMNIVORE, Type.PLANT}));
		tmpMap.put(new Point(7, 10), new CellEntry(Terrain.WATER, new Type[] {Type.OMNIVORE, Type.HERBIVORE}));
		tmpMap.put(new Point(7, 11), new CellEntry(Terrain.WATER, new Type[] {Type.OMNIVORE, Type.OMNIVORE}));
		tmpMap.put(new Point(7, 12), new CellEntry(Terrain.WATER, new Type[] {Type.OMNIVORE, Type.CARNIVORE}));
		
		tmpMap.put(new Point(7, 13), new CellEntry(Terrain.WATER, new Type[] {Type.CARNIVORE, Type.PLANT}));
		tmpMap.put(new Point(7, 14), new CellEntry(Terrain.WATER, new Type[] {Type.CARNIVORE, Type.HERBIVORE}));
		tmpMap.put(new Point(7, 15), new CellEntry(Terrain.WATER, new Type[] {Type.CARNIVORE, Type.OMNIVORE}));
		tmpMap.put(new Point(7, 16), new CellEntry(Terrain.WATER, new Type[] {Type.CARNIVORE, Type.CARNIVORE}));
		
		
		WORLD_MAP = tmpMap;
		
	}
	
	private ArrayList<World<?>> worlds;
	
	private ArrayList<World<? extends Cell>> allWorlds() {
		
		/*
		ArrayList<World<? extends Cell>> all = new ArrayList<World<? extends Cell>>();
		
		for (SimpleEntry<Class<? extends Cell>, World<? extends Cell>> world : worlds) {
			all.add(world.getValue());
		}
		return all;
		*/
		
		return worlds;
		
	}
	
	
	@Before
	public void setUp() throws Exception {
		
		worlds = new ArrayList<World<? extends Cell>>();
		
		worlds.add(new World<SquareCell>(SquareCell.class, WORLD_SIZE_X, WORLD_SIZE_Y));
		worlds.add(new World<SquareCell>(SquareCell.class, WORLD_SIZE_X_ODD, WORLD_SIZE_Y));
		worlds.add(new World<HexCell>(HexCell.class, WORLD_SIZE_X, WORLD_SIZE_Y));
		worlds.add(new World<HexCell>(HexCell.class, WORLD_SIZE_X_ODD, WORLD_SIZE_Y));
		
		for (World<?> world : allWorlds()) {
			world.init();			
		}
		
	}

	@After
	public void tearDown() throws Exception {
	}




	@Test
	public void testCreateCellAt() {
	
		for (World<? extends Cell> world : worlds) {
			for (int i = 0; i < world.getColumnCount(); i++) {
				for (int j = 0; j < world.getRowCount(); j++) {
					
					CellEntry cellData = WORLD_MAP.get(new Point(i, j));
					ArrayList<LifeTools.Type> types = new ArrayList<LifeTools.Type>();
					
					// Set terrain manually.
					if (cellData != null) {
						Creator.forceTerrain = cellData.getTerrain();
						types = cellData.getLifeTypes();
						if (types.size() == 0 || types.get(0) == null) {
							Creator.forceLifeType = LifeTools.Type.NULL;
						} else {
							Creator.forceLifeType = types.get(0);
							types.remove(0);
						}
					} else {
						Creator.forceTerrain = Terrain.DEFAULT;
						Creator.forceLifeType = LifeTools.Type.NULL;
					}
					
					
					Cell newCell;
					if (world.getCellType().equals(SquareCell.class)) {
						
						newCell = new SquareCell((World<SquareCell>)world, i, j);
					} else if (world.getCellType().equals(HexCell.class)) {
						
						newCell = new HexCell((World<HexCell>)world, i, j);
					} else {
						newCell = null;
						fail(world.getCellType() + " Not yet implemented!");
					}
					
					// Set additional lives manually.
					for (LifeTools.Type type : types) {
						if (type != null && type != Type.NULL) {
							Creator.forceLifeType = type;
							newCell.addLife(new Life(type, newCell));
						}
					}
					
					if (world.getCellType().equals(SquareCell.class)) {
						((World<SquareCell>)world).addCell((SquareCell)newCell);
					} else if (world.getCellType().equals(HexCell.class)) {
						
						((World<HexCell>)world).addCell((HexCell)newCell);
					}
	
				}
			}			
		}
	}
	
	@Test
	public void testGetCells() {
		for(World<? extends Cell> world : allWorlds()) {
			assertNotNull(world.getCells());
		}
	}

	@Test
	public void testGetLives() {
		for(World<? extends Cell> world : allWorlds()) {
			assertNotNull(world.getLives());
		}
	}

	@Test
	public void testGetRowCount() {
		for (World<?> world: allWorlds()) {
			world.getRowCount();
		}
	}

	@Test
	public void testGetColumnCount() {
		for (World<?> world: allWorlds()) {
			world.getColumnCount();
		}
	}
	
	@Test
	public void testGetCellAt() {
		for (World<?> world : allWorlds()) {
			for (Point point : WORLD_MAP.keySet()) {
				System.out.println("Checking "+point+" "+world.getCellAt(point.x, point.y)+" on world "+world);
				assertNotNull(world.getCellAt(point.x, point.y));
			}
			assertNotNull(world.getCellAt(0, 0));
			assertNotNull(world.getCellAt(0, world.getRowCount()-1));
			assertNotNull(world.getCellAt(world.getColumnCount()-1, 0));
			assertNotNull(world.getCellAt(world.getColumnCount()-1, world.getRowCount()-1));
		}
	}

	@Test
	public void testTakeTurn() {
		for (World<?> world : allWorlds()) {
			for (int i = 0; i < TURNS; i++) {
				world.takeTurn();
			}
		}
	}

}
