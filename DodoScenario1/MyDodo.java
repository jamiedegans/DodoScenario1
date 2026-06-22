import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;  

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

    }
    
    public void _getScore(int score1, int score2){
       ((Mauritius)getWorld()).updateScore();
    
    }
    
    /**
     * dodo wil move 20 steps randomly
     */
    public void moveRandomly(){

        for(int nrStepsTaken = 0; nrStepsTaken < Mauritius.MAXSTEPS;) { 
            move();                         
            nrStepsTaken-;
            faceDirection(randomDirection());
            if(borderAhead() || fenceAhead()){
                turnLeft();
            }
        }
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
    public void hatchEgg() {
        if ( onEgg() ) {
            pickUpEgg();
            myNrOfEggsHatched++;
        } else {
            showError( "There was no egg in this cell" );
        }
    }

    /**
     * dodo will go to X and Y location 
     */
    public void goToLocation(int x, int y){
        int moveX = x - getX();
        int moveY = y - getY();
        if(moveX > 0){
            setDirection(0);
            turnRight();
            jump(moveX);
        }else{
            moveX = moveX *-1;
            setDirection(0);
            turnLeft();
            jump(moveX); 
        }

        if(moveY < 0){
            moveY = moveY *-1;
            setDirection(0);
            jump(moveY);
        }else{
            setDirection(0);
            turn180();
            jump(moveY); 
        }
    }

    /**
     * dodo will check is he can move
     */
    public boolean validCoordinates(int x, int y){
        int moveX = x - getX();
        int moveY = y - getY();

        World currentWorld = getWorld();
        boolean isYValid = moveY >= 0 && moveY < currentWorld.getHeight();
        boolean isXValid = moveX >= 0 && moveX < currentWorld.getWidth();
        if( isXValid && isYValid){
            return true;
        }  
        showError("Invalid coordinates");
        return false;
    }

    /**
     * dodo will turn to the asked deriction
     */
    public void faceDirection(int deriction){
        if (deriction >= 0 && deriction <= 3){
            while(getDirection() != deriction){
                turnLeft();
            }  
        }
    }

    /**
     * dodo walk around world counting the eggs and calculate the avarage
     */
    public double averageEggsPerRow() {
        int totalEggs = 0;
        int totalRows = getWorld().getHeight();

        for (int row = 0; row < totalRows; row++) {
            goToLocation(0, row);
            faceDirection(1);
            totalEggs += countEggsInRow();
        }
        return (double) totalEggs / totalRows;
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
     * dodo will lay the eggs  filling the map up in stairway like way
     */
    public void layThemEggs(){
        int rowsLaid = 0;
        for(;rowsLaid < getWorld().getHeight(); rowsLaid++ ){
            goToLocation(0, rowsLaid); 
            faceDirection(1);
            layTrailOfEggs(rowsLaid+1);
        }
    }

    /**
     * dodo will lay the double amount of eggs
     */
    public void layDoubleAmountEggs(){
        int rowsLaid = 0;
        while(!borderAhead()){
            goToLocation(0, rowsLaid); 
            faceDirection(1);
            layTrailOfEggs((int)Math.pow(2 ,rowsLaid));
            rowsLaid++;
        }
    }

    /**
     * Lays a pyramid of eggs starting from current postion
     */

    public void layEggPyramid() {
        int centerX = getX();
        int startY = getY();
        int eggsInRow = 1;;
        int rowStartX;
        int row = 0;
        for (; row + startY < getWorld().getHeight(); row++) {
            rowStartX = centerX - row;
            goToLocation(rowStartX, startY + row);
            faceDirection(EAST);
            layTrailOfEggs(eggsInRow);
            eggsInRow += 2;
        }
    }

    /**
     * dodo will walk around the world counting eggs
     */
    public int countEggsInWorld(){
        goToLocation(0, 0);
        faceDirection(1);
        int eggs = 0;
        int locationXY = 1;
        while(validCoordinates(0, locationXY)){
            goToLocation(0, locationXY);
            faceDirection(1);
            eggs = countEggsInRow() + eggs;
            locationXY++;
        }
        return eggs;
    }

    /**
     * counts the egg in the row straight forward
     */
    public int countEggsInRow(){
        int eggInRow = 0;
        goBackToStartOfRowAndFaceBack();
        if(onEgg()){
            eggInRow++;
        }
        while(!borderAhead()){
            move();
            if(onEgg()){
                eggInRow++;
            }
        }
        return eggInRow; 
    }

    /**
     * dodo wil count each row and then lay an egg in a uneven row
     */
    public void makeEggsEven(){
        goToLocation(0, 0);
        faceDirection(1);

        int startY = 0;
        int startX = 0;
        int errorLineY = -1;
        int errorLineX = -1;

        while(startX < getWorld().getHeight()){
            goToLocation(0, startX);
            faceDirection(1);
            int eggs = countEggsInRow();

            if(eggs % 2 == 1){
                errorLineX = getY(); 
                System.out.println("Y: "+errorLineX);
            }
            startX++;
        }

        while(startY < getWorld().getWidth()){
            goToLocation(startY, 0);
            faceDirection(0);
            int eggs = countEggsInRow();
            if(eggs % 2 == 1){
                errorLineY = getX();
                System.out.println("X:"+errorLineY);
            }
            startY++;  
        }

        if(errorLineX != -1 && errorLineY != -1){
            goToLocation(errorLineY, errorLineX);
            layEgg();
        }
    }

    /**
     * dodo will make the world even without using any derictional functions
     */
    public void makeEggsEvenVersionAfterHittingHead(){
        walkToWorldEdge();
        turnLeft();
        walkToWorldEdge();
        turn180();
        int moved = 0;

        while(!borderAhead()){
            int eggs = countEggsInRow();
            goBackToStartOfRowAndFaceBack();        

            if(eggs % 2 == 1){
                walkToWorldEdge();
                turnLeft();
                walkToWorldEdge();
                turn180();
                while(!borderAhead()){
                    eggs = countEggsInRow();
                    goBackToStartOfRowAndFaceBack();

                    if(eggs % 2 == 1){
                        jump(moved);
                        layEgg();
                        break;
                    }
                    turnRight();
                    if(borderAhead()){
                        break;
                    }
                    move();
                    turnLeft();
                }
                break;
            }
            turnRight();
            if(borderAhead()){
                break;
            }
            move();
            moved++;
            turnLeft();
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
     * dodo will walk to edgde world laying the asked amount of eggs
     */
    public void layTrailOfEggs(int number) {
        int eggsLaid = 0;
        for (; eggsLaid < number; eggsLaid++) {
            layEgg();
            if (eggsLaid < number - 1) 
                move();
        }
    }

    /**
     * dodo will change values of the eggs 
     */
    public void changeEggValue(){
        BlueEgg blueEgg = new BlueEgg(); //1
        GoldenEgg goldenEgg = new GoldenEgg(); //5

        int temporaryValueEgg = blueEgg.getValue(); //1

        blueEgg.setValue(goldenEgg.getValue()); // 1
        goldenEgg.setValue(temporaryValueEgg);

        System.out.println(goldenEgg.getValue());
        System.out.println(blueEgg.getValue());
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
        while (nrStepsTaken < distance ) { 

            move();                         
            nrStepsTaken++;
            //System.out.println(nrStepsTaken + distance);            // increment the counter
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
     * then returns the boolean
     */
    public void gotoEgg() {
        while(!onEgg() && !borderAhead()){
            move();
        }
    }

    /**
     * dodo will lay 10 randoms surpiseEggs on random places
     */
    public List<SurpriseEgg> makeListOfSurpriseEggs(){
        return SurpriseEgg.generateListOfSurpriseEggs(10, getWorld());
    }

    /**
     * dodo wil print the coordinates of the egg selected
     */
    public void printCoordinatesOfEgg(Egg egg){
        System.out.println(egg.getX() + " "+ egg.getY());  
    }

    /**
     * dodo will lay 10 randoms surpiseEggs on random places and print it coordinates
     */
    public void makeListOfSurpriseEggsAndPrintCoordinates(){
        for (Egg egg: makeListOfSurpriseEggs()){
            printCoordinatesOfEgg(egg);
        }
    }

    /**
     * dodo will lay 10 eggs then count value of all eggs and print out most valuable
     */
    public void selectTheWithTheMostValueEgg(){
        int MostValueEgg = -1;
        Egg highestEgg = null;
        for (Egg egg: makeListOfSurpriseEggs()){
            printCoordinatesOfEgg(egg);
            System.out.println(egg.getValue());
            if(MostValueEgg < egg.getValue()){
                highestEgg = egg;
                MostValueEgg = egg.getValue();
            }
        }
        printCoordinatesOfEgg(highestEgg);
        System.out.println(highestEgg.getValue());
    }

    /**
     * dodo wil lay 10 eggs random valua and then calculate then average value eggs
     */
    public void averageValueEggs(){   
        int valueEggs = 0;
        double average = 00.00;
        List<SurpriseEgg> eggs = makeListOfSurpriseEggs();
        int size = eggs.size();

        for (Egg egg: eggs){
            System.out.println(valueEggs = valueEggs + egg.getValue());
        }
        average = (double)valueEggs / size;
        System.out.println("tha avarage is");
        System.out.println(average);
    }

    /**
     * Places all the Egg objects in the world in a list.
     * 
     * @return List of Egg objects in the world
     */
    public List<Egg> getListOfEggsInWorld() {
        return getWorld().getObjects(Egg.class);
    }

    public List<Integer> createListOfNumbers() {
        return new ArrayList<> (Arrays.asList( 2, 43, 7, -5, 12, 7 ));
    }

    /**
     * Method for praciticing with lists.
     */
    public void practiceWithLists( ){
        List<Integer> listOfNumbers = createListOfNumbers();

        //the following is incorrect and is to be fixed in challenge 6.1c
        System.out.println("First element: " + listOfNumbers.get(1) ); 
    }

    public void practiceWithListsOfSurpriseEggs( ){
        List<SurpriseEgg>  listOfEgss = SurpriseEgg.generateListOfSurpriseEggs( 12, getWorld() );
    }

    /**
     * Walks to edge of the world printing the coordinates at each step
     * 
     * <p> Initial: Dodo is on West side of world facing East.
     * <p> Final:   Dodo is on East side of world facing East.
     *              Coordinates of each cell printed in the console.
     */

    public void walkToWorldEdge(){
        while(!borderAhead() ){
            move();
        }
    }

    /**
     * walks to the egde of the world look at th end left
     */
    public void goBackToStartOfRowAndFaceBack() {
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
