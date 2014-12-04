package team19.spacetrail;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import javagame.*;


public class GameScreenActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    /* Constants used for access outside of this activity */
    public final static String PLANET_NAME = "team19.spacetrail.PLANET_NAME";
    public final static String[] PLANETS_ARRAY = {"Mercury","Venus","Earth","Mars","Jupiter","Saturn","Uranus","Neptune","Pluto"};

    /* Instance Fields */
    private ImageView spaceship;
    private ArrayList<ImageView> planets; // Holds image views for all planets
    private int dest_planet = 0; // Integer representing the user's desired planet destination
    private GestureDetector detector;
    private int screen_width;
    private Game game;

    /*Helper Fields while Game integration is in progress*/
    private int food = 1000;
    private int fuel = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Sets listener to the screen for tap and long tap
        detector = new GestureDetector(this, this);
        detector.setOnDoubleTapListener(this);

        setContentView(R.layout.activity_game_screen);
        spaceship = (ImageView) findViewById(R.id.spaceship);

        planets = new ArrayList<ImageView>();

        //adding planets to arraylist
        planets.add((ImageView) findViewById(R.id.mercury));
        planets.add((ImageView) findViewById(R.id.venus));
        planets.add((ImageView) findViewById(R.id.earth));
        planets.add((ImageView) findViewById(R.id.mars));
        planets.add((ImageView) findViewById(R.id.jupiter));
        planets.add((ImageView) findViewById(R.id.saturn));
        planets.add((ImageView) findViewById(R.id.uranus));
        planets.add((ImageView) findViewById(R.id.neptune));
        planets.add((ImageView) findViewById(R.id.pluto));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;

        /* Setting fields for Food and Fuel, using temp variables */
        TextView fuelView = (TextView) findViewById(R.id.gameScreenFuel);
        TextView foodView = (TextView) findViewById(R.id.gameScreenFood);
        fuelView.setText(Integer.toString(fuel));
        foodView.setText(Integer.toString(food));

        //game = (Game) getIntent().getSerializableExtra("Game");
       /* if(game.getDestination().getDistanceRemaining() == -1){
            selectPlanet();
        }*/
        //Will add an else here when we get loading set up

        selectPlanet(); //will be removed when fully integrated
    }

    /* Helper Methods */

    //Process of moving ship across screen
    public void moveShip(View v) {
        //Random chance to encounter issue
        double rand = Math.random();
        if(rand < .001) {
            final Intent intent = new Intent(this, ExitActivity.class);
            AlertDialog.Builder issue_alert = new AlertDialog.Builder(this);
            issue_alert.setTitle(R.string.issue_title);
            issue_alert.setMessage("Your spaceship has been destroyed!");
            issue_alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                    intent.putExtra("activity", "GameScreen");
                    startActivity(intent);
                }
            });
            Dialog d = issue_alert.create();
            d.setCancelable(false);
            d.show();
        }

        //Stops ship when bitmap reaches planets outer edge, also shrinks ship when getting closer to planet
        TranslateAnimation anim = new TranslateAnimation(0.0f, -30.0f, 0.0f, 0.0f);
        if(spaceship.getX() >= planets.get(dest_planet).getX() + planets.get(dest_planet).getWidth()) {
            spaceship.startAnimation(anim);
            spaceship.setX(spaceship.getX()- 30.0f);
            if(spaceship.getX() < screen_width/2) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship_crop);
                int width = (int) (spaceship.getWidth() * .95);
                int height = (int) (spaceship.getHeight() * .95);
                Bitmap rbmp = Bitmap.createScaledBitmap(bmp, width, height, true);
                spaceship.setImageBitmap(rbmp);
            }
            TextView fuelView = (TextView) findViewById(R.id.gameScreenFuel);
            TextView foodView = (TextView) findViewById(R.id.gameScreenFood);
            fuelView.setText(Integer.toString(--fuel)); //USE THIS TO SUBTRACT ACTUAL FUEL
            foodView.setText(Integer.toString(--food)); //USE THIS TO SUBTRACT ACTUAL FOOD
        }
        //Tells user they arrived at planet, and gives choice of moving to next planet, or stopping on planet to purchase resources
        else{
            AlertDialog.Builder planet_decider = new AlertDialog.Builder(this);
            planet_decider.setMessage("You've reached " + PLANETS_ARRAY[dest_planet] + "!\nWould you like to stop by the convenience store?");
            planet_decider.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    goToPlanetMenu();
                }
            });
            planet_decider.setNegativeButton(R.string.nextPlanet, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    recreate();
                }
            });
            planet_decider.setCancelable(false);
            planet_decider.create().show();
        }

    }

    //Ends this activity and sends planet info to planet activity
    public void goToPlanetMenu() {
        Intent intent = new Intent(this, PlanetActivity.class);
        String p_name = Integer.toString(dest_planet);
        intent.putExtra(PLANET_NAME, p_name);
        startActivity(intent);
        finish();
    }

    //Displays picture of planet to planet menu for better aesthetic view
    public void selectPlanet(){
        AlertDialog.Builder planet_selector= new AlertDialog.Builder(this);
        planet_selector.setTitle(R.string.planet_select);

        planet_selector.setItems(PLANETS_ARRAY, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dest_planet = which;
                dialog.dismiss();
                planets.get(dest_planet).setVisibility(View.VISIBLE);
                //game.setDestination(dest_planet);
            }
        });
        planet_selector.setCancelable(false);
        AlertDialog dialog = planet_selector.create();
        dialog.show();
    }

    /* Override Methods */

    //Allows for user to save and exit the game
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.promptExit);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_screen, menu);
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

    //Opens up the Game info layout, and allows user to hit back button to return to game.
    @Override
    public void onLongPress(MotionEvent event) {
        Intent intent = new Intent(this, GameInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    //Uses a single tap to propel the ship forward
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        View v = new View(this);
        moveShip(v);
        return true;
    }

    //Uses a double tap to propel the ship forward
    @Override
    public boolean onDoubleTap(MotionEvent event) {
        View v = new View(this);
        moveShip(v);
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
