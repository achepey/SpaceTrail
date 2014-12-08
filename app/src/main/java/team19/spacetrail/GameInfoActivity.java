package team19.spacetrail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javagame.Game;


public class GameInfoActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private GestureDetector detector; // Allows user interactivity with the screen
    private String pace; // holds the string representation for the pace of the game
    public Game game;

    //Displays user resources, ship health, and explorer's health in orderly fashion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_info);

        detector = new GestureDetector(this, this);
        detector.setOnDoubleTapListener(this);

        game = GameScreenActivity.game;

        /* Setting fields for ship status */
        TextView engineHealth = (TextView) findViewById(R.id.engineHealth);
        TextView livingBayHealth = (TextView) findViewById(R.id.livingBayHealth);
        TextView wingsHealth = (TextView) findViewById(R.id.wingHealth);
        TextView hullHealth = (TextView) findViewById(R.id.hullHealth);

        engineHealth.setText(Integer.toString(game.getShip().getEngineStatus())+"%");
        livingBayHealth.setText(Integer.toString(game.getShip().getLivingBayStatus())+"%");
        wingsHealth.setText(Integer.toString(game.getShip().getWingStatus())+"%");
        hullHealth.setText(Integer.toString(game.getShip().getHullStatus())+"%");

        /* Setting up pace spinner */
        Spinner spinner = (Spinner) findViewById(R.id.changePaceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pace_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setMinimumWidth(400);

        /* Make sure pace is correct in the menu */
        int gameSpeed = game.getSpeed(); //fast = 1, medium = 2, slow = 3
        if(gameSpeed == 1){
            pace = "Strenuous";
        }
        else if(gameSpeed == 2){
            pace = "Normal";
        }
        else if(gameSpeed == 3){
            pace = "Leisurely";
        }
        else{
            Log.d("GameInfo", "ERROR: BAD GAME SPEED FROM OBJECT");
        }
        Log.d("GameInfo", "pace on create is " + pace + " " + gameSpeed);
        int paceInt = adapter.getPosition(pace);
        spinner.setSelection(paceInt);

        /* Setting fields for resources */
        TextView money = (TextView) findViewById(R.id.moneyVariable);
        TextView fuel = (TextView) findViewById(R.id.fuelVariable);
        TextView food = (TextView) findViewById(R.id.foodVariable);
        TextView aluminum = (TextView) findViewById(R.id.aluminumVariable);
        TextView sprEngines = (TextView) findViewById(R.id.engineVariable);
        TextView sprWings = (TextView) findViewById(R.id.wingVariable);
        TextView sprLivBay = (TextView) findViewById(R.id.livingBayVariable);
        TextView compoundName = (TextView) findViewById(R.id.resourceCompound);
        TextView compound = (TextView) findViewById(R.id.compoundVariable);
        TextView weakness = (TextView) findViewById(R.id.weaknessVariable);

        money.setText(Integer.toString(game.getMoney()));
        fuel.setText(Integer.toString(game.getResources().getFuel()));
        food.setText(Integer.toString(game.getResources().getFood()));
        aluminum.setText(Integer.toString(game.getResources().getAluminum()));
        sprEngines.setText(Integer.toString(game.getResources().getSpareEngines()));
        sprWings.setText(Integer.toString(game.getResources().getSpareWings()));
        sprLivBay.setText(Integer.toString(game.getResources().getSpareLivingBays()));
        compoundName.setText(game.getRace().getStrength());
        compound.setText((Integer.toString(game.getResources().getCompound())));
        weakness.setText(game.getRace().getWeakness());

        /* Setting fields for names and their info */
        ArrayList<TextView> names = new ArrayList<TextView>();
        ArrayList<TextView> nameInfo = new ArrayList<TextView>();
        TextView capt = (TextView) findViewById(R.id.captainName);
        names.add(capt);
        TextView person1 = (TextView) findViewById(R.id.peopleName1);
        names.add(person1);
        TextView person2 = (TextView) findViewById(R.id.peopleName2);
        names.add(person2);
        TextView person3 = (TextView) findViewById(R.id.peopleName3);
        names.add(person3);
        TextView person4 = (TextView) findViewById(R.id.peopleName4);
        names.add(person4);
        TextView captInfo = (TextView) findViewById(R.id.captainInfo);
        nameInfo.add(captInfo);
        TextView person1Info = (TextView) findViewById(R.id.peopleNameInfo1);
        nameInfo.add(person1Info);
        TextView person2Info = (TextView) findViewById(R.id.peopleNameInfo2);
        nameInfo.add(person2Info);
        TextView person3Info = (TextView) findViewById(R.id.peopleNameInfo3);
        nameInfo.add(person3Info);
        TextView person4Info = (TextView) findViewById(R.id.peopleNameInfo4);
        nameInfo.add(person4Info);

        Log.d("Gameinfo", "Size of games people = " + game.getPeople().size());
        int i = 0; // declared outside to figure out how many people have already died
        for(; i < game.getPeople().size(); i++){
            names.get(i).setText(game.getPeople().get(i).getName());
            nameInfo.get(i).setText(game.getPeople().get(i).getAge() + ", " + game.getPeople().get(i).getCondition()+"%");
            Log.d("Gameinfo", game.getPeople().get(i).getName() + " added to list");
        }

        System.out.println("crew names info page:");
        for(String s: game.crewNames) {
            System.out.println("1:"+s + "\n");
        }

        /* deadNames will contain all the names of dead crew members */
        ArrayList<String> deadNames = new ArrayList<String>(game.crewNames);
        Log.d("gi", "i is " + i);
        for(int j = 0; j < game.getPeople().size(); j++){
            Log.d("gi", "j = " + j);
            for(String s : deadNames){
                if(game.getPeople().get(j).getName().equals(s)){
                    deadNames.remove(s);
                    Log.d("Gameinfo", s + " is alive!");
                    break;
                }
            }
        }
        Log.d("GameInfo", "Size of deadNames = " + deadNames.size());
        // i holds the current position in names without a crew member name
        for(int j = 0; j < deadNames.size(); j++){
            names.get(i+j).setText(deadNames.get(j));
            nameInfo.get(i+j).setText("Dead");
        }
    }

    /* Starts the asteroid mining animation activity */
    public void mineAsteroids(View v) {
        Intent intent = new Intent(this, AsteroidActivity.class);
        Bundle b = new Bundle();
        TextView hullHealth = (TextView) findViewById(R.id.hullHealth);
        String hullHlth = hullHealth.getText().toString();
        Log.d("GameInfo", "Hull Health = " + hullHlth);
        int hullHlthInt = Integer.parseInt(hullHlth.substring(0, hullHlth.length() - 1));
        Log.d("GameInfo", "Hull Health = " + hullHlthInt);
        b.putInt("HullHealth", hullHlthInt);
        intent.putExtras(b);
        finish();
        startActivity(intent);
    }

    //this is where the pace of the game is changed
    public void changePace(View v) {

        /* Spinner object */
        Spinner spin = (Spinner) findViewById(R.id.changePaceSpinner);
        pace = spin.getSelectedItem().toString();
        if(pace.equals("Strenuous")){
            game.setSpeed(1);
        }
        else if(pace.equals("Normal")){
            game.setSpeed(2);
        }
        else if(pace.equals("Leisurely")){
            game.setSpeed(3);
        }
        else{
            Log.d("GameInfo", "ERROR: BAD PACE FROM SPINNER!");
        }
        //Creates pop-up letting user know that the pace has changed
        Context context = getApplicationContext();
        CharSequence popup = "Pace has Changed!";
        Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.show();

        Log.d("GameInfo", "Pace is " + game.getSpeed());
    }

    /* Allows user to repair hull with spare aluminum */
    public void repairHull(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Repair Hull");
        builder.setMessage("How much aluminum would like to spend on repairs?\nCurrent aluminum levels: " + game.getResources().getAluminum());
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("Repair!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = input.getText().toString();
                if(value.equals("")){
                    Context context = getApplicationContext();
                    CharSequence popup = "Input a value!";
                    Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 200);
                    toast.show();
                }
                else{
                    game.repairHull(Integer.parseInt(value));
                    updateFields();
                }
            }
        }).setNegativeButton("No, we'll take our chances!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ;
            }
        });
        builder.setCancelable(false).create().show();

    }

    /* Updates the fields for the info screen after asteroid activity */
    public void updateFields(){
        TextView hull = (TextView) findViewById(R.id.hullHealth);
        hull.setText(Integer.toString(game.getShip().getHullStatus())+"%");

        TextView alum = (TextView) findViewById(R.id.aluminumVariable);
        alum.setText(Integer.toString(game.getResources().getAluminum()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Methods used with Implementation */

    //Tells activity touch event occurred
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    //Closes the menu activity and continues to the game activity
    @Override
    public void onLongPress(MotionEvent event) {
        finish();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        ;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
       return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }
}
