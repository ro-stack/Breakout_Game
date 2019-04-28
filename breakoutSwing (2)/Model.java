import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Model of the game of breakout
 * @author Mike Smith University of Brighton
 */

public class Model extends Observable
{

    private static final int B              = 6;  // Border offset
    private static final int M              = 40; // Menu offset

    // Size of things
    private static final float BALL_SIZE    = 18; // Ball side
    private static final int BRICK_WIDTH  = 48; // Brick size
    private static final int BRICK_HEIGHT = 20;
    private static final float BAT_WIDTH  = 50; 
    private static final float BAT_HEIGHT = 30;

    private static final int BAT_MOVE       = 5; // Distance to move bat

    // Scores
    private static final int HIT_BRICK      = 50;  // Score
    private static final int HIT_BOTTOM     = -200;// Score

    private GameObj ball;          // The ball
    private ArrayList<GameObj> bricks;  // The bricks
    private GameObj bat;           // The bat

    private boolean runGame = true; // Game running
    private boolean fast = false;   // Sleep in run loop

    private int score = 0;

    private final int W ;       // Width of area
    private final float H;         // Height of area

    private static final int BRICK_SEP = 4; // brick seperation
    private static final int BRICK_Y_OFFSET = 100;
    private static final int BRICK_X_OFFSET = 42;
    private static final int NBRICKS_PER_ROW = 10;
    private static final int NBRICK_ROWS = 2;

    public Model( int width, int height )
    {
        this.W = width; this.H = height;
    }

    /**
     * Create in the model the objects that form the game
     */

    public void createGameObjects()
    {
        synchronized( Model.class )
        {
            ball   = new GameObj(450, 450, BALL_SIZE, BALL_SIZE, Colour.RED);
            bat    = new GameObj(W/2, H - BAT_HEIGHT*1.5f, BAT_WIDTH*3, 
                BAT_HEIGHT/4, Colour.WHITE);
            bricks = new ArrayList<>();

            // pink first 2 rows
            for (int i = 0; i < NBRICK_ROWS; i++){
                int y = BRICK_Y_OFFSET + (i * (BRICK_HEIGHT + BRICK_SEP));

                for (int j = 0; j < NBRICKS_PER_ROW; j++){
                    int x = (BRICK_X_OFFSET) + (j * (BRICK_WIDTH + BRICK_SEP));
                    bricks.add(new GameObj (x, y, BRICK_WIDTH, BRICK_HEIGHT, Colour.PINK));

                }
            }
            // blue second 2 rows // math = 4 + 20 * 2 = 48 
            for (int i = 0; i < NBRICK_ROWS; i++){
                int y = (BRICK_Y_OFFSET+48) + (i * (BRICK_HEIGHT + BRICK_SEP));

                for (int j = 0; j < NBRICKS_PER_ROW; j++){
                    int x = (BRICK_X_OFFSET) + (j * (BRICK_WIDTH + BRICK_SEP));
                    bricks.add(new GameObj (x, y, BRICK_WIDTH, BRICK_HEIGHT, Colour.BLUE) );

                }
            }
            // orange third 2 rows // math = 48 + 48 = 96
            for (int i = 0; i < NBRICK_ROWS; i++){
                int y = (BRICK_Y_OFFSET+96) + (i * (BRICK_HEIGHT + BRICK_SEP));

                for (int j = 0; j < NBRICKS_PER_ROW; j++){
                    int x = (BRICK_X_OFFSET) + (j * (BRICK_WIDTH + BRICK_SEP));
                    bricks.add(new GameObj (x, y, BRICK_WIDTH, BRICK_HEIGHT, Colour.GREEN) );

                }
            }
            // green fourth 2 rows // math = 96 + 48 = 144
            for (int i = 0; i < NBRICK_ROWS; i++){
                int y = (BRICK_Y_OFFSET+144) + (i * (BRICK_HEIGHT + BRICK_SEP));

                for (int j = 0; j < NBRICKS_PER_ROW; j++){
                    int x = (BRICK_X_OFFSET) + (j * (BRICK_WIDTH + BRICK_SEP));
                    bricks.add(new GameObj (x, y, BRICK_WIDTH, BRICK_HEIGHT, Colour.ORANGE) );

                }
            }
            // cyan fith 2 rows // math = 144 + 48 = 192
            for (int i = 0; i < NBRICK_ROWS; i++){
                int y = (BRICK_Y_OFFSET+192) + (i * (BRICK_HEIGHT + BRICK_SEP));

                for (int j = 0; j < NBRICKS_PER_ROW; j++){
                    int x = (BRICK_X_OFFSET) + (j * (BRICK_WIDTH + BRICK_SEP));
                    bricks.add(new GameObj (x, y, BRICK_WIDTH, BRICK_HEIGHT, Colour.CYAN ));

                }
            }
            //magenta sixth 2 rows // math = 192 + 48 
            for (int i = 0; i < NBRICK_ROWS; i++){
                int y = (BRICK_Y_OFFSET+240) + (i * (BRICK_HEIGHT + BRICK_SEP));

                for (int j = 0; j < NBRICKS_PER_ROW; j++){
                    int x = (BRICK_X_OFFSET) + (j * (BRICK_WIDTH + BRICK_SEP));
                    bricks.add(new GameObj (x, y, BRICK_WIDTH, BRICK_HEIGHT, Colour.MAGENTA ));

                }
            }
            //yellow seventh 2 rows // math = 240 + 48 
            for (int i = 0; i < NBRICK_ROWS; i++){
                int y = (BRICK_Y_OFFSET+288) + (i * (BRICK_HEIGHT + BRICK_SEP));

                for (int j = 0; j < NBRICKS_PER_ROW; j++){
                    int x = (BRICK_X_OFFSET) + (j * (BRICK_WIDTH + BRICK_SEP));
                    bricks.add(new GameObj (x, y, BRICK_WIDTH, BRICK_HEIGHT, Colour.YELLOW ));

                }
            }        
        }  
    } 

    private ActivePart active  = null;
    /**
     * Start the continuous updates to the game
     */
    public void startGame()
    {
        synchronized ( Model.class )
        {
            stopGame();
            active = new ActivePart();
            Thread t = new Thread( active::runAsSeparateThread );
            t.setDaemon(true);   // So may die when program exits
            t.start();

        }
    }

    /**
     * Stop the continuous updates to the game
     * Will freeze the game, and let the thread die.
     */
    public void stopGame()
    {  
        synchronized ( Model.class )
        {
            if ( active != null ) { active.stop(); active = null; }
        }
    }

    public GameObj getBat()             { return bat; }

    public GameObj getBall()            { return ball; }

    public ArrayList<GameObj> getBricks()    { return bricks; }

    /**
     * Add to score n units
     * @param n units to add to score
     */
    protected void addToScore(int n)    { score += n; }

    public int getScore()               { return score; }

    /**
     * Set speed of ball to be fast (true/ false)
     * @param fast Set to true if require fast moving ball
     */
    public void setFast(boolean fast)   
    { 
        this.fast = fast; 
    }

    /**
     * Move the bat. (-1) is left or (+1) is right
     * @param direction - The direction to move
     */
    public void moveBat( int direction )
    {
        float dist = direction * BAT_MOVE;    // Actual distance to movefinal 
        float MIN_X = 10; 
        final float MAX_X = 440; 

        if(dist > 0 && bat.getX() < MAX_X) 
        { 
            bat.moveX(dist); 
        } 
        else if ( dist < 0 && bat.getX() > MIN_X) 
        { 
            bat.moveX(dist); 
        } 

        Debug.trace( "Model: Move bat = %6.2f", dist );

    }

    /**
     * This method is run in a separate thread
     * Consequence: Potential concurrent access to shared variables in the class
     */
    class ActivePart
    {
        private boolean runGame = true;

        public void stop()
        {
            runGame = false;
        }

        public void runAsSeparateThread()
        {
            final float S = 3; // Units to move (Speed) 
            try
            {
                synchronized ( Model.class ) // Make thread safe 
                {
                    GameObj       ball   = getBall();     // Ball in game 
                    GameObj       bat    = getBat();      // Bat 
                    ArrayList<GameObj> bricks = getBricks();   // Bricks 
                }

                while (runGame)
                {
                    synchronized ( Model.class ) // Make thread safe 
                    {
                        float x = ball.getX();  // Current x,y position 
                        float y = ball.getY();
                        // Deal with possible edge of board hit 
                        if (x >= W - B - BALL_SIZE)  ball.changeDirectionX();
                        if (x <= 0 + B            )  ball.changeDirectionX();
                        if (y >= H - B - BALL_SIZE)  // Bottom 
                        { 
                            ball.changeDirectionY(); addToScore( HIT_BOTTOM ); 
                        }
                        if (y <= 0 + M            )  ball.changeDirectionY();

                        boolean hit = false;
                        ArrayList<GameObj> removeList = new ArrayList<GameObj>();
                        for (int i = 0; i < bricks.size(); i++) 
                        {
                            GameObj brick = bricks.get(i);

                            if (brick.hitBy(ball)) 
                            {

                                removeList.add(brick);
                                ball.changeDirectionY();

                                addToScore(HIT_BRICK); 
                            }
                        }

                        for (int i = 0; i < removeList.size(); i++)

                        {
                            GameObj toRemove = removeList.get(i);

                            bricks.remove(toRemove);
                        }


                        if (hit)
                            ball.changeDirectionY();

                        if ( ball.hitBy(bat) )
                            ball.changeDirectionY();
                    } 
                    modelChanged();      // Model changed refresh screen 
                    Thread.sleep( fast ? 2 : 20 );
                    ball.moveX(S);  ball.moveY(S);
                }
            } catch (Exception e) 
            { 
                Debug.error("Model.runAsSeparateThread - Error\n%s", 
                    e.getMessage() );
            }
        }
    }

    /**
     * Model has changed so notify observers so that they
     *  can redraw the current state of the game
     */
    public void modelChanged()
    {
        setChanged(); notifyObservers();
    }        
}