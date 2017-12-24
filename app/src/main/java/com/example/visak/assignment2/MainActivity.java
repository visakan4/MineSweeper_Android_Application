package com.example.visak.assignment2;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /**
     * Declaration:
     *
     * a) gridView - Grid view
     * b) pauseTime - Button - To pause the game
     * c) reset - Button - To reset the game
     * d) beginnerButton - Button - for Beginner level
     * e) intermediateButton - Button - for Intermediate level
     * f) expertButton - Button - for Expert level
     * g) pause - Boolean - Used to stop and start the countDown
     * h) successFlag - Boolean - To determine whether the game is won
     * i) firstClick - Boolean - To determine the first click in the grid
     * j) counter - TextView - To display the seconds elapsed
     * k) count - Integer - Number of seconds elapsed
     * l) levelName - String - To store the level selected by the user
     * m) flagLabel - TextView - Label to flagCount
     * n) flagCount - TextView - To display the number of flags in the grid
     * o) mineLabel - TextView - Label for Number of mines
     * p) mineCount - TextView - To display the number of mines placed in the grid
     * q) adjacentSquares - ArrayList - To store the adjacentsqaures to a position
     * r) inputValue - ArrayList - To store the values hidden to the user
     * s) shownValue - ArrayList - To store the values shown to the user
     * t) minePositions - ArrayList - To store the positions of mines
     * u) level - HashMap
     *      * Key - levelName
     *      * Value- Number of Mines
     *
     */

    public GridView gridView;
    Button pauseTimer;
    Button reset;
    Button beginnerButton;
    Button intermediateButton;
    Button expertButton;
    Boolean pause;
    Boolean successFlag = false;
    Boolean firstClick =  true;
    TextView counter;
    int count;
    String levelName;
    TextView flagLabel;
    TextView flagCount;
    TextView mineLabel;
    TextView mineCount;

    public ArrayList<Integer> adjacentSquares = new ArrayList<>();
    public ArrayList<String> inputValue = new ArrayList<>();
    public ArrayList<String> shownValue = new ArrayList<>();
    public ArrayList<Integer> minePositions = new ArrayList<>();
    public HashMap<String, Integer> level = new HashMap<>();

    /**
     * getHash()
     *
     * Method would generate the hashmap with levels as keys(String) and number of mines needed for the position as its values(Integer).
     *
     * @return a hashmap with with levels as keys and number of mines as its values.
     */

    public HashMap<String,Integer> getHash(){
        level.put("beginner",9);
        level.put("intermediate",24);
        level.put("expert", 40);
        return level;
    }

    /**
     * generateUserShownValue():
     *
     * Assigns a space(String) value for 81(Number of elements) elements in the showmValue Array.
     */

    public void generateUserShownValue(){
        Log.d("generateUserShownValue","Start");
        for (int a = 0; a <81; a++){
            shownValue.add(" ");
        }
        Log.d("generateUserShownValue","End");
    }

    /**
     * generateMinePosition
     *
     * Steps Involved:
     *
     * 1) Generate a random number using the Random Class
     * 2) Check if the number already exists in the list
     * 3) If it exists, increase the maxsize if it does not exists add it to the list.
     * 4) Above three steps would run for maxSize number of times.
     *
     * @param maxSize Number of mines to be placed in the grid.
     * @return a list which contains the grid positions where the mines are to be placed. Size of the list is defined by the @param(maxSize).
     */


    public ArrayList<Integer> generateMinePosition(int maxSize){
        Log.d("generateMinePosition", "Start");
        Random random = new Random();
        for (int x =0; x < maxSize; x++){
            int randomPosition = random.nextInt(80);
            if (minePositions.contains(randomPosition)){
                maxSize++;
            }else {
                minePositions.add(randomPosition);
            }
        }
        Log.d("Mine Position",""+minePositions);
        Log.d("Number of mines",""+minePositions.size());
        Log.d("generateMinePosition", "End");
        return minePositions;
    }


    /**
     * generateInputValues
     *
     * Steps Involved:
     *
     * 1) Generate Mine Positions using generateMinePosition method. The method would return a list which contains the minePositions
     * 2) generateMinePosition method require a parameter(maxSize). The parameter values is obtained from getHash Method.
     * 3) Once the minePositions list is obtained, inputValue list is updated.
     * 4) InputValue list is updated in such a way that if a mine is to be places in the position, it is updated with *,
     * else it is updated with the index of it.
     * 5) Text showing number of mines is displayed.
     *
     */


    public void generateInputValues(){
        Log.d("generateInputValues", "Start");
        ArrayList<Integer> tempList =  new ArrayList<>();
        HashMap<String, Integer> hashLevel = new HashMap<>();
        hashLevel = getHash();
        tempList = generateMinePosition(hashLevel.get(levelName));
        Log.d("generateInputValues", "Mine Position Generated");
        for(int i=0; i<81; i++){
            if (tempList.contains(i)){
                inputValue.add("*");
            }
            else{
                inputValue.add(Integer.toString(i));
            }
        }
        mineLabel.setVisibility(View.VISIBLE);
        mineCount.setVisibility(View.VISIBLE);
        mineLabel.setText(R.string.MineLabel);
        mineCount.setText(getString(R.string.mineValue,level.get(levelName)));
        Log.d("generateInputValues", "End");
    }


    /**
     *  findAdjacentSquares
     *
     *  It takes a position as input and find the adjacent squares and add it to a list and returns the list.
     *
     * @param position - Position for which adjacent squares are to be found
     * @return A list which contains the adjacent square positions.
     */


    public ArrayList<Integer> findAdjacentSquares(int position){
        Log.d("findAdjacentSquares", "Start");
        ArrayList<Integer> adjacentSquaresList = new ArrayList<>();
        if (position %9 ==0){
            if (!(position == 72)){
                adjacentSquaresList.add(position + 10);
                adjacentSquaresList.add(position + 9);
            }
            if(!(position == 0)){
                adjacentSquaresList.add(position - 8);
                adjacentSquaresList.add(position - 9);
            }
            adjacentSquaresList.add(position + 1);
        }
        else if(position %9 == 8){
            if (!(position == 8)){
                adjacentSquaresList.add(position - 9);
                adjacentSquaresList.add(position - 10);
            }
            if (!(position == 80)){
                adjacentSquaresList.add(position + 9);
                adjacentSquaresList.add(position + 8);
            }
            adjacentSquaresList.add(position - 1);
        }
        else {
            if (!(position >=1 && position <=8)){
                adjacentSquaresList.add(position - 8);
                adjacentSquaresList.add(position - 9);
                adjacentSquaresList.add(position - 10);
            }
            if (!(position >=73 && position <= 79)){
                adjacentSquaresList.add(position + 10);
                adjacentSquaresList.add(position + 9);
                adjacentSquaresList.add(position + 8);
            }
            adjacentSquaresList.add(position - 1);
            adjacentSquaresList.add(position + 1);
        }
        Log.d("findAdjacentSquares", "End");
        return adjacentSquaresList;
    }


    /**
     * unCoverOperation
     *
     * Functionality:
     *
     * 1) It would determine the number of mines surrounding the position provided to it and return the number of mines.
     *
     * Steps Involved:
     *
     * 1) numberofMines is set to 0
     * 2) Adjacent squares are determined by invoking the findAdjacentSquares method.
     * 3) Once the list which contains the positions is obtained, it is iterated to determine the whether mines are present in those positions.
     * 4) If a mine is found, numberofMines is incremented.
     *
     *
     * @param position - Position of the element where the user clicked.
     * @return Number of mines in the adjacent squares
     */


    public int unCoverOperation(int position){
        Log.d("unCoverOperation", "Start");
        int numberOfMines = 0;
        adjacentSquares = findAdjacentSquares(position);
        Log.d("unCoverOperation", "AdjacentSquaresFound");
        for (int x =0 ; x < adjacentSquares.size(); x++){
            int adjacentPosition = adjacentSquares.get(x);
            Log.d("Trying to check",""+adjacentPosition);
            if (inputValue.get(adjacentPosition).equals("*")){
                numberOfMines++;
            }
        }
        Log.d("unCoverOperation", "End");
        return numberOfMines;
    }

    /**
     * checkWhetherWon
     *
     * It would check whether the game is won by the user or not
     *
     * Steps Involved:
     *
     * 1) Determine the number of positions which is yet to be uncovered (numberOfUncoveredMines)
     * 2) Determine the number of positions which are flagged (numberOfFlags)
     * 3) Add numberOfFlags and numberOfUncoveredMines
     * 4) check if the total matches the number of mines. If it is equal, following additional checks are done.
     *      a) numberOfCoveredMines are set to zero
     *      b) All the positions which is still uncovered and set as flag are determined and added to the list named SuccessCheck
     *      c) SuccessCheckList is iterated and numberofCoveredMines is incremented if mine is found in the position present in the successCheck
     *      d) If the numberofMines is equal to numberofCoveredMines, successFlag is set to true
     *
     * @return <tt>true</tt> if the game is won, else <tt>false</tt>.
     */

    public boolean checkWhetherWon() {
        Log.d("checkWhetherWon", "Start");
        int numberOfUncoveredMines = Collections.frequency(shownValue, " ");
        int numberOfFlags = Collections.frequency(shownValue, "F");
        if (numberOfFlags + numberOfUncoveredMines == level.get(levelName)) {
            Log.d("checkWhetherWon", "NumberofMinesMatched");
            int numberofCoveredMines = 0;
            ArrayList<Integer> successCheck = new ArrayList<>();
            for (int x = 0; x < shownValue.size(); x++) {
                if (shownValue.get(x).equals(" ") || shownValue.get(x).equals("F")) {
                    successCheck.add(x);
                }
            }
            for (int y = 0; y < successCheck.size(); y++) {
                if (inputValue.get(successCheck.get(y)).equals("*")) {
                    numberofCoveredMines++;
                } else {
                    break;
                }
            }
            if (numberofCoveredMines == level.get(levelName)) {
                Log.d("checkWhetherWon", "Game Won!!");
                successFlag = true;
            }
        }
        Log.d("checkWhetherWon", "End");
        return successFlag;
    }

    /**
     * This is used to play the soundEffects. It would take the soundtrack to be played as a parameter
     *
     * Steps :
     *
     * 1) Play the soundtrack.
     * 2) On completion, release the media player resource.
     *
     * Reference: [1]
     *
     * @param resource - Soundtrack to be played. Name of the file in res/raw folder
     */

    public void playSoundEffects(int resource){
        final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this,resource);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
    }

    /**
     * onSuccess
     *
     * This method is invoked when the game is won
     *
     * Steps Involved:
     *
     * 1) Toast message is displayed stating that game is won.
     * 2) Soundeffects are triggered.
     * 3) Reset Image is changed
     * 4) grid and the pause button is disabled.
     * 5) pause is set to false, to stop the counter.
     * 6) Text is shown to intimate that the game is won
     */

    public void onSuccess(){
        Log.d("onSuccess", "Start");
        Toast toast = Toast.makeText(MainActivity.this, "Congratulations!! Game is won", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,100);
        toast.show();
        playSoundEffects(R.raw.crackers);
        reset.setBackgroundResource(R.drawable.footballwon);
        pause = false;
        pauseTimer.setEnabled(false);
        gridView.setEnabled(false);
//        Toast toast1 = Toast.makeText(MainActivity.this, "Congratulations!! Game is won", Toast.LENGTH_SHORT);
//        toast1.setGravity(Gravity.CENTER,0,100);
//        toast1.show();
        mineLabel.setText(R.string.WonLabel);
        mineCount.setVisibility(View.INVISIBLE);
        Log.d("onSuccess", "End");
    }

    /**
     * OnLevelSelection:
     *
     * Takes care of the household activities to be done after level selection.
     *
     * Steps involved:
     *
     * 1) Grid - Enabled and made it visible
     * 2) Reset Button - Enabled, Assigned image and made it visible
     * 3) generateUserShownValue method is invoked.
     * 4) Generated a toast message to guide the user.
     */

    public void onLevelSelection(){
        Log.d("OnLevelSelection", "Start");
        Log.d("OnLevelSelection", levelName);
        gridView.setEnabled(true);
        gridView.setVisibility(View.VISIBLE);
        reset.setEnabled(true);
        reset.setBackgroundResource(R.drawable.footballstart);
        reset.setVisibility(View.VISIBLE);
        generateUserShownValue();
        Toast toast = Toast.makeText(MainActivity.this, "Click anywhere on the grid to start the game", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,100);
        toast.show();
        Log.d("OnLevelSelection", "End");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Initialization :
         *
         * All the necessary Buttons, TexViews are identified and assigned to the variables.
         * This is done using the findViewById method.
         * Identifiers(id,class) assigned to the elements in the XML would act as parameters for this method
         */

        counter = (TextView) findViewById(R.id.counter);
        pauseTimer = (Button) findViewById(R.id.pauseTimer);
        reset = (Button) findViewById(R.id.resetTimer);
        beginnerButton = (Button) findViewById(R.id.beginnerButton);
        intermediateButton = (Button) findViewById(R.id.intermediateButton);
        expertButton = (Button) findViewById(R.id.expertButton);
        gridView = (GridView) this.findViewById(R.id.grid);
        flagCount = (TextView) this.findViewById(R.id.flagCounter);
        flagLabel = (TextView) this.findViewById(R.id.flagLabel);
        mineLabel= (TextView) this.findViewById(R.id.mineLabel);
        mineCount = (TextView) this.findViewById(R.id.mineValue);
        final CustomGridAdapter customGridAdapter = new CustomGridAdapter(MainActivity.this, shownValue);
        gridView.setAdapter(customGridAdapter);
        Log.d("Initialize","All the necessary elements have been assigned to the variables");


        /*
         * Household Activities:
         *
         * Unnecessary button are set to Invisible state and the timer value is being set to zero.
         */

        pauseTimer.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        counter.setText("0");
        mineLabel.setVisibility(View.INVISIBLE);
        mineCount.setVisibility(View.INVISIBLE);


        /*
         * OnclickListeners for level selection button:
         *
         * Steps involved:
         *
         * 1) Set the level name to variable levelName
         * 2) Other levels buttons are set to invisible state
         * 3) OnLevelSelection method is invoked.
         * 4) generateInputValues method is invoked.
         */

        beginnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelName = "beginner";
                beginnerButton.setEnabled(false);
                intermediateButton.setVisibility(View.INVISIBLE);
                expertButton.setVisibility(View.INVISIBLE);
                onLevelSelection();
                generateInputValues();
                customGridAdapter.notifyDataSetChanged();
            }
        });


        intermediateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelName = "intermediate";
                intermediateButton.setEnabled(false);
                expertButton.setVisibility(View.INVISIBLE);
                beginnerButton.setVisibility(View.INVISIBLE);
                onLevelSelection();
                generateInputValues();
                mineCount.setText(getString(R.string.mineValue,level.get(levelName)));
                customGridAdapter.notifyDataSetChanged();
            }
        });


        expertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelName = "expert";
                expertButton.setEnabled(false);
                intermediateButton.setVisibility(View.INVISIBLE);
                beginnerButton.setVisibility(View.INVISIBLE);
                onLevelSelection();
                generateInputValues();
                mineLabel.setVisibility(View.VISIBLE);
                mineCount.setVisibility(View.VISIBLE);
                mineCount.setText(getString(R.string.mineValue,level.get(levelName)));
                customGridAdapter.notifyDataSetChanged();
            }
        });


        /*
         * On Click Listener for Pause Timer Button
         *
         * Functionalities provided:
         *
         * 1) To pause the game
         * 2) To resume the game
         *
         * Steps Involved:
         *
         * 1) Boolean value pause is used to determine the functionality.
         * 2) Once the button is clicked, value of the boolean is checked.
         * 3) If the value is not null and true, it would mean the user is trying to pause the game.
         *     In this scenario, following activities are done
         *      a) Text of the button is changed to Resume
         *      b) Reset Button Image is changed
         *      c) Grid View is disabled
         *      d) Text showing game is paused is displayed
         * 4) If the value is false, it would mean the user is trying to resume the game.
         *      In this scenario, following activities are done
         *      a) Boolean value is changed to true
         *      b) Text of the button is changed to pause
         *      c) Image of the reset button is changed is changed
         *      d) Count Down is started again.
         *      d) Text showing number of mines is displayed
         */

        pauseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause){
                    Log.d("pauseTimer","paused");
                    pause =false;
                    String string = getString(R.string.resumeText);
                    pauseTimer.setText(string);
                    reset.setBackgroundResource(R.drawable.footballpaused);
                    gridView.setEnabled(false);
                    Toast toast = Toast.makeText(MainActivity.this, "Game Paused!! Click on Resume to restart the game", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,100);
                    toast.show();
                    mineLabel.setText(R.string.pausedLabel);
                    mineCount.setVisibility(View.INVISIBLE);
                }
                else {
                    Log.d("pauseTimer","resumed");
                    pause = true;
                    pauseTimer.setText(R.string.pauseText);
                    reset.setBackgroundResource(R.drawable.footballinprogress);
                    gridView.setEnabled(true);
                    Toast toast = Toast.makeText(MainActivity.this, "Game Resumed!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,100);
                    toast.show();
                    mineCount.setVisibility(View.VISIBLE);
                    mineLabel.setText(R.string.MineLabel);
                    mineCount.setText(getString(R.string.mineValue,level.get(levelName)));
                    new CountDownTimer(10000000, 1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (pause && !(checkWhetherWon())){
                                String string = getString(R.string.timerValue,count++);
                                counter.setText(string);
                            }
                            else {
                                if (checkWhetherWon()){
                                    Log.d("pauseTimer","gameWon");
                                    onSuccess();
                                    cancel();
                                }
                                else {
                                    cancel();
                                }
                            }
                        }
                        @Override
                        public void onFinish() {
                            counter.setText(R.string.gameOverText);
                        }
                    }.start();
                }
            }
        });


        /*
         * Reset OnClick Listener
         *
         * Functionality:
         *
         * This allows the user to reset the game and start a new game.
         *
         * Steps Involved:
         *
         * 1) Pause, firstClick, successFlag (Boolean Value) are set to false
         * 2) Counter text, Flag count is set to zero
         * 3) All the level buttons are enabled and made visible
         * 4) Grid is made invisible.
         * 5) Mine Positions, InputValue, ShownValue list is cleared
         * 6) Reset button is made invisible
         * 7) Text displaying number of mines is made invisible
         */


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("ResetButton", "Start");
            pause = false;
            counter.setText("0");
            count =0;
            beginnerButton.setEnabled(true);
            intermediateButton.setEnabled(true);
            expertButton.setEnabled(true);
            pauseTimer.setText(R.string.pauseText);
            pauseTimer.setVisibility(View.INVISIBLE);
            beginnerButton.setVisibility(View.VISIBLE);
            intermediateButton.setVisibility(View.VISIBLE);
            expertButton.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
            inputValue.removeAll(inputValue);
            shownValue.removeAll(shownValue);
            minePositions.removeAll(minePositions);
            successFlag = false;
            firstClick = true;
            flagCount.setText("0");
            mineLabel.setVisibility(View.INVISIBLE);
            mineCount.setVisibility(View.INVISIBLE);
            reset.setVisibility(View.INVISIBLE);
            Log.d("ResetButton", "Stop");
//            reset.setText(R.string.stopText);
            }
        });


        /*
         * gridView OnLongClickListener
         *
         * Functionality:
         *
         * 1) Allows the user to flag a position.
         * 2) Allows the user to undo a flagged position.
         *
         * Steps Involved:
         *
         * 1) If the position already has been flagged, it would be changed back to a closed tile.
         *    else set a flag in the position.
         * 2) If it is the first click, following steps are mentioned
         *      a) firstClick Boolean value is set to false
         *      b) counter is set to zero
         *      c) Pause button is enabled and made visible
         *      d) Sound effects are started
         *      e) Count Down Timer is started
         */


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (shownValue.get(position).equals("F")){
                    shownValue.set(position," ");
                }else if(shownValue.get(position).equals(" ")){
                    shownValue.set(position,"F");
                }
                else {
                    Log.d("Flag Debug",shownValue.get(position));
                    Toast toast = Toast.makeText(MainActivity.this, "Flag cannot be placed on a uncovered element", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,100);
                    toast.show();
                }
                customGridAdapter.notifyDataSetChanged();
                if (firstClick){
                    firstClick = false;
                    count = 0;
                    pause = true;
                    pauseTimer.setVisibility(View.VISIBLE);
                    reset.setBackgroundResource(R.drawable.footballinprogress);
                    playSoundEffects(R.raw.whistle);
                    new CountDownTimer(10000000,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (pause && !(checkWhetherWon())){
                                String string = getString(R.string.timerValue,count++);
                                counter.setText(string);
                                flagCount.setText(String.valueOf(Collections.frequency(shownValue,"F")));
                            }
                            else {
                                if (checkWhetherWon()){
                                    Log.d("OnLongClick","gameWon");
                                    onSuccess();
                                    cancel();
                                }
                                else {
                                    cancel();
                                }
                            }
                        }
                        @Override
                        public void onFinish() {
                            counter.setText(R.string.gameOverText);
                        }
                    }.start();
                }
                return true;
            }
        });


        /*
         * GridView onItemClick Listener
         *
         * Functionality:
         *
         * 1) On the firstclick the game would start.
         * 2) Checks if the user has clicked on mine. If he has clicked on the mine, game is lost.
         * 3) Checks the number of mines in the adjacent position, if there are mines number of mines adjacent to it are shown.
         *    If there are no mines, no number is set and uncover operation is done in the adjacent mines.
         * 4) Steps 2 and 3 checks would happens every time until user loses/wins the game.
         *
         * Steps Involved:
         *
         * 1) If it is the first click, following steps are mentioned
         *      a) firstClick Boolean value is set to false
         *      b) counter is set to zero
         *      c) Pause button is enabled and made visible
         *      d) Sound effects are started
         *      e) Count Down Timer is started
         * 2) If the user has clicked on position which is been flaged. A Toast message describing the mistake is displayed.
         * 3) If the user has clicked on a mine, following steps are done
         *      a) Boolean value pause is set to false to stop the counter.
         *      b) Pause button is disabled.
         *      c) Sound effects are triggered
         *      d) Reset Button image is changed
         *      e) A toast message stating that the game is lost is displayed.
         *      f) All the mine locations are displayed.
         *      g) If the flagged positions are verified whether there is a mine under it. If there is a mine under it, flag is displayed.
         *          else a crossed bomb is displayed(W is used to denote it).
         *      h) GridView is displayed
         *      i) Text Message stating that the game is lost is displayed
         * 4) If the user has clicked on a position which does not fall in the above two categories. Following steps are followed
         *      a) Position is added to the arrayList(positionsTobeUncovered)
         *      b) First, number of mines in the adjacent squares are determined using the unCover operation method
         *      c) If the number of mines is greater than zero, following steps are followed
         *          * number of mines are updated in that position. Both inputValue and shownValue is updated.
         *      d) If the number of mines is equal to zero, following steps are followed
         *          * Value in that position is updated to zero and both shownValue and inputValue is updated.
         *          * adjacentSquaresList is iterated and if the position in it is present in the positionsTobeUncoveredList
         *              or if the position is flagged, it is not added in the positionsTobeUncoveredList. Else it is added to list.
         *      e) Steps b - d are itereated for all elements in the positionsTobeUncovered List.
         */


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (firstClick){
                firstClick = false;
                count = 0;
                pause = true;
                pauseTimer.setVisibility(View.VISIBLE);
                pauseTimer.setEnabled(true);
                reset.setBackgroundResource(R.drawable.footballinprogress);
                playSoundEffects(R.raw.whistle);
                new CountDownTimer(10000000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (pause && !(checkWhetherWon())){
                            String string = getString(R.string.timerValue,count++);
                            counter.setText(string);
                            flagCount.setText(String.valueOf(Collections.frequency(shownValue,"F")));
                        }
                        else {
                            if (checkWhetherWon()){
                                Log.d("onItemLongClickListner","gameWon");
                                onSuccess();
                                cancel();
                            }
                            else {
                                cancel();
                            }
                        }
                    }
                    @Override
                    public void onFinish() {
                        counter.setText(R.string.gameOverText);
                    }
                }.start();
            }
            if (shownValue.get(position).equals("F")){
                Log.d("gridItemOnClick","FlaggedAlready");
                Toast toast = Toast.makeText(MainActivity.this, "It has been flagged. If you want to remove please long press", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,100);
                toast.show();
            }
            else if(inputValue.get(position).equals("*")){
                Log.d("gridItemOnClick","ClickedOnMine");
                pause = false;
                pauseTimer.setEnabled(false);
                playSoundEffects(R.raw.explosion);
                reset.setBackgroundResource(R.drawable.footballlost);
                Toast toast = Toast.makeText(MainActivity.this, "Game Lost!! Click on red card to restart the Game", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,100);
                toast.show();
                for (int x =0; x < inputValue.size(); x++){
                    if (shownValue.get(x).equals("F") && inputValue.get(x).equals("*")){
                        shownValue.set(x,"F");
                    }
                    else if (inputValue.get(x).equals("*")){
                        shownValue.set(x,"*");
                    }
                    else if(shownValue.get(x).equals("F") && !(inputValue.get(x).equals("*"))){
                        shownValue.set(x,"W");
                    }
                }
                customGridAdapter.notifyDataSetChanged();
                mineLabel.setText(R.string.LostLabel);
                mineCount.setVisibility(View.INVISIBLE);
                gridView.setEnabled(false);
            }
            else{
                Log.d("gridItemOnClick","Uncover");
                ArrayList<Integer> positionsToBeUncovered = new ArrayList<>();
                positionsToBeUncovered.add(position);
                for (int x =0; x < positionsToBeUncovered.size(); x++){
                    Log.d("Position",""+positionsToBeUncovered.get(x));
                    int noOfMines = unCoverOperation(positionsToBeUncovered.get(x));
                    if (noOfMines > 0){
                        Log.d("gridItemOnClick","GreaterThanZero");
                        inputValue.set(positionsToBeUncovered.get(x), Integer.toString(noOfMines));
                        shownValue.set(positionsToBeUncovered.get(x), Integer.toString(noOfMines));
                        customGridAdapter.notifyDataSetChanged();
                    }
                    else {
                        Log.d("gridItemOnClick","Zero");
                        inputValue.set(positionsToBeUncovered.get(x), "0");
                        shownValue.set(positionsToBeUncovered.get(x), "0");
                        for (int y = 0; y < adjacentSquares.size();y++){
                            if ((!(positionsToBeUncovered.contains(adjacentSquares.get(y)))) && (!(shownValue.get(adjacentSquares.get(y)).equals("F")))){
                                positionsToBeUncovered.add(adjacentSquares.get(y));
                            }
                        }
                        Log.d("AdjacentSquarePositions", ""+adjacentSquares);
                        Log.d("positionTobeUncovered", ""+positionsToBeUncovered);
                    }
                }
            }
            }
        });
    }
}
