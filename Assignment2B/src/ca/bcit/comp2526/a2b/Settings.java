package ca.bcit.comp2526.a2b;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

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
@SuppressWarnings("serial")
public final class Settings extends JFrame {

    private static final String TITLE = "A2B Settings";

    private static final String DIR_STRING = "Working Directory = " 
        + System.getProperty("user.dir");
    private static final String LOADED = "Loaded ";
    private static final String ARROW = " => ";
    private static final String PROPERTY = "property ";
    private static final String NOT_FOUND_IN = " not found in ";
    
    
    private static final ArrayList<Setting> DEFAULTS = new ArrayList<Setting>();
    
    
    private static final String filename = "settings.properties";
    private static final int DEFAULT_WORLDSIZE = 25;
    private static final int DEFAULT_CELLSIZE = 20;
    private static final int DEFAULT_INT = 0;
 
    // Assign defaults to DEFAULT HashMap.
    static {
         
        DEFAULTS.add(new Setting("debug", "false", "Debug Application?"));
        
        // Upper limit to Life sense distance.
        DEFAULTS.add(new Setting(   "maxsensedistance", 
                                    "99", 
                                    "Upper limit to Life sense (predators & food)"));
        
        // How far to keep away from predators.
        DEFAULTS.add(new Setting("evasivebufferdistance", "1", "Distance to keep from predators"));
        
        // How big to create the world.
        DEFAULTS.add(new Setting("worldcolumns", "" + DEFAULT_WORLDSIZE, "Number of columns"));
        DEFAULTS.add(new Setting("worldrows", "" + DEFAULT_WORLDSIZE, "Number of rows"));
        DEFAULTS.add(new Setting("cellradius", "" + DEFAULT_CELLSIZE, "Cell size (px)"));
        
        // What data to place in the Cells. Options:
        //  'food', 'coordinate', 'moves', 'none'
        DEFAULTS.add(new Setting("filltext", "none", 
                "What to show in the Cells: 'food', 'coordinate', 'moves', or 'none'"));
        
        // Type of grid: 'hex' or 'square' (default)
        DEFAULTS.add(new Setting("gridtype", "hex", "Type of grid: 'hex' or 'square' (default)"));
        
        // Chance of Plant spawning (x/100)
        DEFAULTS.add(new Setting("plantchance", "30", "Chance of Plant spawning (x/100)"));
        
        // Chance of Herbivore spawning in Cell (x/100)
        DEFAULTS.add(new Setting("herbivorechance", "25", "Chance of Herbivore spawning (x/100)"));
        
        // Chance of Carnivore spawning in Cell (x/100)
        DEFAULTS.add(new Setting("carnivorechance", "10", "Chance of Carnivore spawning (x/100)"));
        
        // Chance of Omnivore spawning in Cell (x/100)
        DEFAULTS.add(new Setting("omnivorechance", "10", "Chance of Omnivore spawning (x/100)"));
        
        // Chance of Cell being water terrain;
        DEFAULTS.add(new Setting("waterchance", "3", "Chance of a Cell containing water (x/100)"));
        
        // Are the lines visible in the grid?
        DEFAULTS.add(new Setting("visiblelines", "false", "Show borders between Cells?"));
        
        // Time between auto-turns.
        DEFAULTS.add(new Setting(   "turnspeedms", 
                                    "50", 
                                    "Time between turns (Spacebar to activate) in ms"));
        
        // Percent to darken each turn.
        DEFAULTS.add(new Setting(   "darkenpercent", 
                                    "80", 
                                    "Percent of previous color retained on Life aging"));
        
        // Seed to use for Random events. 0 = Generate new.
        // All settings need to be the same for a repeat scenario.
        DEFAULTS.add(new Setting(   "seed", 
                                    "0", 
                                    "Seed to use for Random events. 0 = Generate new."));
    
    }
    
    
    private JPanel panel;
    private static LinkedHashMap<String, Setting> settings = new LinkedHashMap<String, Setting>();
    
    protected Settings() {
    }
    
    // Used internally only, like a struct. Key/Value/Name variables.
    private static class Setting {
        private String key;
        private String value;
        private String name;
        
        
        private Setting() {
        }
        
        private Setting(final String key, final String value, final String name) {
            this.key = key;
            this.value = value;
            this.name = name;
            
            if (name == null) {
                this.name = key;
            }
        }
    }
    
    // Create a Settings panel
    protected void init() {
        setTitle(TITLE);
        
        load();
        
        
        SpringLayout layout = new SpringLayout();
        panel = new JPanel(layout);
        //panel.setLayout(new GridLayout(settings.size(), 2));
        
        int pos = 0;
        for (String key : settings.keySet()) {
            Setting setting = settings.get(key);
            JLabel label = new JLabel("<html>" + setting.name + "</html>");
            panel.add(label);
            
            JTextField text = new JTextField(setting.value, 10);
            label.setLabelFor(text);
            label.setName(setting.key);
            panel.add(text);
            
            layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel);
            layout.putConstraint(SpringLayout.NORTH, label, 25 + pos, SpringLayout.NORTH, panel);
            layout.putConstraint(SpringLayout.NORTH, text, 25 + pos, SpringLayout.NORTH, panel);
            layout.putConstraint(SpringLayout.WEST, text, 20, SpringLayout.EAST, label);
            
            pos += 25;
        }
        JButton saveButton = new JButton("Save");
        
        JFrame settingsFrame = this;
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                submit();
                settingsFrame.dispose();
            }
        });
        panel.add(saveButton);
        
        layout.putConstraint(SpringLayout.WEST, saveButton, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, saveButton, 25 + pos, SpringLayout.NORTH, panel);
        
        add(panel);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                submit();
                event.getWindow().dispose();
            }
        });
    
    }
    
    // Save settings
    private void submit() {
        setFromPanel();
        save();
        Main.createWorld();

    }
    
    // Set settings from the panel
    private void setFromPanel() {
        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel)component;
                
                Setting setting = settings.get(label.getName());
                
                Component labelFor = label.getLabelFor();
                String value = labelFor == null || !(labelFor instanceof JTextField) ? "ERROR" 
                        : ((JTextField)labelFor).getText();
                
                setting.value = value;

            }
        }
    }
    
  
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
      
        Properties properties = new Properties();
        
        try {
            properties.load(new FileInputStream(filename));
        } catch (Exception exception) {
            Settings.setDefaults();
            Settings.save();

        }
        
  
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            
            Setting setting = settings.get(key);
            if (setting == null) {
                settings.put(key, new Setting(key, value, null));
            } else {
                setting.value = value;
            }
            System.out.println(LOADED + key + ARROW + value);
        }

        
    }
    
    /*
     * Used internally only, sets the program settings to these default values.
     */
    private static void setDefaults() {

        for (Setting setting : DEFAULTS) {
            settings.put(setting.key, setting);
        }
      
    }
    
    /*
     * Currently used internally only, saves the current settings to the properties file.
     */
    private static void save() {
      
        OutputStream output = null;
      
        try {
            output = new FileOutputStream(filename);
            
            Properties properties = new Properties();
            for (String key : settings.keySet()) {
                Setting setting = settings.get(key);
                properties.setProperty(setting.key, setting.value);
            }
            
            properties.store(output, null);
            
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
        
        Setting prop = settings.get(key.toLowerCase());
        if (prop == null || prop.value == null) {
            System.err.println(PROPERTY + key + NOT_FOUND_IN + filename);
            prop = new Setting();
        }
        return prop.value;
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
