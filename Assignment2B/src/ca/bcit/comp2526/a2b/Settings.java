package ca.bcit.comp2526.a2b;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>Static class for Settings.</p>
 * 
 * <p>Settings are loaded from a file, and a default file is created if 
 *  one doesn't exist.</p>
 *  
 *  <p>Outside-of-class usage:
 *   Settings.load();       // before first use
 *   Settings.get(key);     // etc. See below.</p>
 * 
 * @author Brayden Traas
 * @version 2016-10-22
 */
public final class Settings {

    private static final String DIR_STRING = "Working Directory = " 
        + System.getProperty("user.dir");
    private static final String LOADED = "Loaded ";
    private static final String ARROW = " => ";
    private static final String PROPERTY = "property ";
    private static final String NOT_FOUND_IN = " not found in ";
    
    
    private static final HashMap<String, String> DEFAULTS;
    
    
    private static final String filename = "settings.properties";
    private static final int DEFAULT_WORLDSIZE = 25;
    private static final int DEFAULT_INT = 0;
 
    // Assign defaults to DEFAULT HashMap.
    static {
        HashMap<String, String> tmpDefaults = new HashMap<String, String>();
        
        tmpDefaults.put("debug", "false");
        
        // Upper limit to Life sense distance.
        tmpDefaults.put("maxsensedistance", "99");
        
        // How far to keep away from predators.
        tmpDefaults.put("evasivebufferdistance", "1");
        
        // How big to create the world.
        tmpDefaults.put("worldsize", "" + DEFAULT_WORLDSIZE);
        
        // What data to place in the Cells. Options:
        //  'food', 'coordinate', 'moves', 'none'
        tmpDefaults.put("filltext", "food");
        
        // Type of grid: 'hex' or 'square' (default)
        tmpDefaults.put("gridtype", "hex");
        
        // Chance of Plant spawning (x/100)
        tmpDefaults.put("plantchance", "30");
        
        // Chance of Herbivore spawning in Cell (x/100)
        tmpDefaults.put("herbivorechance", "10");
        
        // Are the lines visible in the grid?
        tmpDefaults.put("visiblelines", "true");
        
        // Seed to use for Random events. 0 = Generate new.
        // All settings need to be the same for a repeat scenario.
        tmpDefaults.put("seed", "0");
    
        DEFAULTS = tmpDefaults;
    }
    
    
    
    private static Properties settings = new Properties();
    
    private Settings() {} // prevent instantiation of final class
  
    /**
     * <p>Loads Settings from the properties file.
     * If the properties file doesn't exist, it creates a
     *  default one by calling setDefaults() and save().</p>
     *  
     * <p>Must be called before attempting to get Settings values.</p>
     */
    public static void load() {
     
        setDefaults();
        
        System.out.println(DIR_STRING);
      
        
        try {
            settings.load(new FileInputStream(filename));
        } catch (Exception exception) {
            Settings.setDefaults();
            Settings.save();

        }
        
  
        for (String key : settings.stringPropertyNames()) {
            String value = settings.getProperty(key);
            System.out.println(LOADED + key + ARROW + value);
        }

        
    }
    
    /*
     * Used internally only, sets the program settings to these default values.
     */
    private static void setDefaults() {

        for (Map.Entry<String, String> entry : DEFAULTS.entrySet()) {
            settings.setProperty(entry.getKey(), entry.getValue());
        }
      
    }
    
    /*
     * Currently used internally only, saves the current settings to the properties file.
     */
    private static void save() {
      
        OutputStream output = null;
      
        try {
            output = new FileOutputStream(filename);
            settings.store(output, null);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Gets the value when given a key to lookup.
     * Basically calls Properties().getProperty().
     * 
     * @param key to lookup (case insensitive)
     * @return the String value
     */
    public static String get(final String key) {
        
        String prop = settings.getProperty(key.toLowerCase());
        if (prop == null) {
            System.err.println(PROPERTY + key + NOT_FOUND_IN + filename);
            prop = "";
        }
        return prop;
    }
    
    /**
     * Gets the property and converts to boolean.
     * @param key to lookup
     * @return true if the value == true
     */
    public static boolean getBoolean(final String key) {
        return Boolean.valueOf(get(key));
    }
    
    /**
     * Gets the specified property and converts to int.
     * @param key to lookup
     * @return int value of this value.
     */
    public static int getInt(final String key) {
        if (key.equals("")) {
            return DEFAULT_INT;
        }
        return Integer.parseInt(get(key));
        
    }
    
    /**
     * Gets the double value of the specified property.
     * @param key to lookup.
     * @return double value of this value.
     */
    public static double getDouble(final String key) {
        return Double.parseDouble(get(key));
    }
}
