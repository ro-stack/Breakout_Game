import java.awt.Color;

/**
 * Hide the specific internal representation of colours
 *  from most of the program.
 * Map to Swing color when required.
 */
public enum Colour  
{ 
    RED(Color.RED), 

    BLUE(Color.BLUE), 

    GRAY(Color.GRAY), 

    CYAN(Color.CYAN), 

    PINK(Color.PINK),

    ORANGE(Color.ORANGE), 

    MAGENTA(Color.MAGENTA), 

    GREEN(Color.GREEN), 

    WHITE(Color.WHITE),

    YELLOW(Color.YELLOW), 

    BLACK(Color.BLACK), 

    LIGHT_GRAY(Color.LIGHT_GRAY);

  
    

  
    private final Color c;

    Colour( Color c ) { this.c = c; }

    public Color forSwing() { return c; }
}

