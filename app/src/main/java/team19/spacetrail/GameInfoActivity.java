package team19.spacetrail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import javagame.Game;


public class GameInfoActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private GestureDetector detector;
    private String pace;
    private Game game;
    private TextView hullHealth;

    //Displays user resources, ship health, and explorer's health in orderly fashion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_info);

        detector = new GestureDetector(this, this);
        detector.setOnDoubleTapListener(this);

        game = (Game) getIntent().getExtras().getSerializable("Game");

        /* Setting fields for ship status */
        TextView engineHealth = (TextView) findViewById(R.id.engineHealth);
        TextView livingBayHealth = (TextView) findViewById(R.id.livingBayHealth);
        TextView wingsHealth = (TextView) findViewById(R.id.wingHealth);
        hullHealth = (TextView) findViewById(R.id.hullHealth);

        Spinner spinner = (Spinner) findViewById(R.id.changePaceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pace_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

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
        Log.d("GameInfo", pace);
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

        money.setText(Integer.toString(game.getMoney()));
        fuel.setText(Integer.toString(game.getResources().getFuel()));
        food.setText(Integer.toString(game.getResources().getFood()));
        aluminum.setText(Integer.toString(game.getResources().getAluminum()));
        sprEngines.setText(Integer.toString(game.getResources().getSpareEngines()));
        sprWings.setText(Integer.toString(game.getResources().getSpareWings()));
        sprLivBay.setText(Integer.toString(game.getResources().getSpareLivingBays()));

        /* Setting fields for names and their info */
        TextView capt = (TextView) findViewById(R.id.captainName);
        TextView person1 = (TextView) findViewById(R.id.peopleName1);
        TextView person2 = (TextView) findViewById(R.id.peopleName2);
        TextView person3 = (TextView) findViewById(R.id.peopleName3);
        TextView person4 = (TextView) findViewById(R.id.peopleName4);
        TextView captInfo = (TextView) findViewById(R.id.captainInfo);
        TextView person1Info = (TextView) findViewById(R.id.peopleNameInfo1);
        TextView person2Info = (TextView) findViewById(R.id.peopleNameInfo2);
        TextView person3Info = (TextView) findViewById(R.id.peopleNameInfo3);
        TextView person4Info = (TextView) findViewById(R.id.peopleNameInfo4);

        capt.setText(game.getPeople().get(0).getName());
        person1.setText(game.getPeople().get(1).getName());
        person2.setText(game.getPeople().get(2).getName());
        person3.setText(game.getPeople().get(3).getName());
        person4.setText(game.getPeople().get(4).getName());

        captInfo.setText("" + game.getPeople().get(0).getCondition() +"%");
        person1Info.setText("" + game.getPeople().get(1).getCondition()+"%");
        person2Info.setText("" + game.getPeople().get(2).getCondition()+"%");
        person3Info.setText("" + game.getPeople().get(3).getCondition()+"%");
        person4Info.setText("" + game.getPeople().get(4).getCondition()+"%");
    }

    public void mineAsteroids(View v) {
        Intent intent = new Intent(this, AsteroidActivity.class);
        Bundle b = new Bundle();
        String hullHlth = hullHealth.getText().toString();
        Log.d("GameInfo", "Hull Health = " + hullHlth);
        int hullHlthInt = Integer.parseInt(hullHlth.substring(0, hullHlth.length()-1));
        Log.d("GameInfo", "Hull Health = " + hullHlthInt);
        b.putInt("HullHealth", hullHlthInt);
        startActivity(intent);
    }

    //this is where the pace of the game is changed
    public void changePace(View v) {

        Spinner spin = (Spinner) findViewById(R.id.changePaceSpinner);
        pace = spin.getSelectedItem().toString();
        if(pace.equals("Strenuous")){
            game.setSpeed(3);
        }
        else if(pace.equals("Normal")){
            game.setSpeed(2);
        }
        else if(pace.equals("Leisurely")){
            game.setSpeed(1);
        }
        else{
            Log.d("GameInfo", "ERROR: BAD PACE FROM SPINNER!");
        }
        //Creates pop-up letting user know the resources were purchased correctly
        Context context = getApplicationContext();
        CharSequence popup = "Pace has Changed!";
        Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.show();
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
