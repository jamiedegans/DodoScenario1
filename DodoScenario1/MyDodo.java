import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 20-01-2017
 */
public class MyDodo extends Dodo
{
    private int myNrOfEggsHatched;

    public MyDodo() {
        super( EAST );
        myNrOfEggsHatched = 0;
    }

    public void act() {
        eggTrailToNest();
    }

    /**
     * Move one cell forward in the current direction.
     * 
     * <P> Initial: Dodo is somewhere in the world
     * <P> Final: If possible, Dodo has moved forward one cell
     *
     */
    public void move() {
        if (canMove()) {
            step();
        } else {
            showError( "I'm stuck!" );
        }
    }

    /**
     * climbs over a fence  and ends facing right
     * 
     * <P> makes the dodo climb over fence
     */
    public void climbOverFence() {
        if (fenceAhead()) {
            turnLeft();
            move();       
            turnRight();
            move();
            move(); 
            turnRight();
            move(); 
            turnLeft();
        } else {
            showError( "there are no fences" );
        }
    }

    /**
     * Test if Dodo can move forward, (there are no obstructions
     *    or end of world in the cell in front of her).
     * 
     * <p> Initial: Dodo is somewhere in the world
     * <p> Final:   Same as initial situation
     * 
     * @return boolean true if Dodo can move (no obstructions ahead)
     *                 false if Dodo can't move
     *                      (an obstruction or end of world ahead)
     */
    public boolean canMove() {
        if ( borderAhead() || fenceAhead() ){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Hatches the egg in the current cell by removing
     * the egg from the cell.
     * Gives an error message if there is no egg
     * 
     * <p> Initial: Dodo is somewhere in the world. There is an egg in Dodo's cell.
     * <p> Final: Dodo is in the same cell. The egg has been removed (hatched).     
     */    
    public void hatchEgg () {
        if ( onEgg() ) {
            pickUpEgg();
            myNrOfEggsHatched++;
        } else {
            showError( "There was no egg in this cell" );
        }
    }

    /**
     * dodo turns 180 degrees
     */
    public void turn180(){
        turnRight();
        turnRight();

    }

    /**
     * dodo walks over egde and will climbs over fences and lays egg on empty nest
     */
    public void walkToWorldEdgeClimbingOverFences(){
        while(!borderAhead()){
            if(fenceAhead() == true){
                climbOverFence();
                if(onNest() && canLayEgg()){
                    layEgg();
                }
            } else{
                move();
            }
        }
    }
    
    /**
     * dodo walks to the nest if there path to the eggs laid out
     */
       public void eggTrailToNest(){
            while(!onNest()){
            if(eggAhead() || nestAhead() == true){
            move();
            }
            else{
                turnRight();
                if(!eggAhead()){
                    turn180();
                }
            }  
        }
        
    }
    
    /**
     * dodo will walk around the fences stopping onto a egg
     */
    public void walkAroundFencedArea(){
        while(!onEgg()){
            turnRight();
            while(fenceAhead()){
                turnLeft();  
            }
            move();

        }
    }
    
       /**
     * dodo will walk around the fences stopping onto a nest
     */
    public void walkAroundFencedAreaToNest(){
        while(!onNest()){
            turnRight();
            while(fenceAhead()){
                turnLeft();  
            }
            move();
        }
    }

    /**
     * Returns the number of eggs Dodo has hatched so far.
     * 
     * @return int number of eggs hatched by Dodo
     */
    public int getNrOfEggsHatched() {
        return myNrOfEggsHatched;
    }

    /**
     * Move given number of cells forward in the current direction.
     * 
     * <p> Initial:   
     * <p> Final:  
     * makes the dodo move the amount of steps wanted and print it out
     * @param   int distance: the number of steps made
     */
    public void jump( int distance ) {
        int nrStepsTaken = 0;               // set counter to 0
        while ( nrStepsTaken < distance ) { 
            // check if more steps must be taken  
            // take a step

            move();                         
            nrStepsTaken++;
            System.out.println(nrStepsTaken + distance);            // increment the counter
        }
    }

    /**
     * dodo walks to end of the world while picking uop each grain and printing it out
     */
    public void pickUpGrainsAndPrintCoordinates(){
        while(!borderAhead()){
            if(onGrain()){
                System.out.println( getX() + "  ,  " + getY());
                pickUpGrain();

            }
            move();
        }
        if(onGrain()){
            System.out.println( getX() + "  ,  " + getY());
            pickUpGrain();
        } 
    }

    /**
     * dodo turns takes one step and move back turning in end to right
     */
    public void stepOneCellBackwards() {
        turn180();
        step();
        turn180();
    }

    /**
     * the dodo checks if the grain ahead by walking above it 
     * then reurns the boolean
     */
    public boolean grainAhead() {
        move();
        boolean result = onGrain();
        stepOneCellBackwards();
        return result;           
    }

    /**
     * the dodo walks to a egg
     * then reurns the boolean
     */
    public void gotoEgg() {
        while(!onEgg() && !borderAhead()){
            move();

        }

    }    

    /**
     * Walks to edge of the world printing the coordinates at each step
     * 
     * <p> Initial: Dodo is on West side of world facing East.
     * <p> Final:   Dodo is on East side of world facing East.
     *              Coordinates of each cell printed in the console.
     */

    public void walkToWorldEdge(){
        while( ! borderAhead() ){
            // print coordinates
            //System.out.println( getX() + "," + getY());
            move();
        }
    }
    
    /**
     * walks to the egde of the world look at th end left
     */
    public void goBackToStartOfRowAndFaceBack( ) {
        turn180();
        walkToWorldEdge();
        turn180();

    }

    /**
     * walks to the egde and lays egg in empty nest
     */
    public void walkToWorldEdgeLayingEgg(){
        while(!borderAhead()){
            if(onNest() && canLayEgg()){
                layEgg();
            }
            move();

        }
        if(onNest() && canLayEgg()){
            layEgg();
        }
    }

    /**
     * Test if Dodo can lay an egg.
     *          (there is not already an egg in the cell)
     * 
     * <p> Initial: Dodo is somewhere in the world
     * <p> Final:   Same as initial situation
     * 
     * @return boolean true if Dodo can lay an egg (no egg there)
     *                 false if Dodo can't lay an egg
     *                      (already an egg in the cell)
     */

    public boolean canLayEgg( ){
        if( onEgg() == true ){
            return false;
        }else{
            return true;
        }
    }
}
