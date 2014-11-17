package team19.spacetrail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;


public class GameScreenActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    public final static String PLANET_NAME = "team19.spacetrail.PLANET_NAME";
    public final static String[] PLANETS_ARRAY = {"Mercury","Venus","Earth","Mars","Jupiter","Saturn","Uranus","Neptune"};

    private ImageView spaceship;
    private ArrayList<ImageView> planets;
    private int dest_planet = 0;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        selectPlanet();
    }
    /* Helper Methods */
    public void moveShip(View v) {
        //Random chance to encounter issue
        double rand = Math.random();
        if(rand < .001) {
            AlertDialog.Builder issue_alert = new AlertDialog.Builder(this);
            issue_alert.setTitle(R.string.issue_title);
            issue_alert.setMessage("Your spaceship has been destroyed!");
            issue_alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            Dialog d = issue_alert.create();
            d.setCanceledOnTouchOutside(false);
            d.show();
        }

        TranslateAnimation anim = new TranslateAnimation(0.0f, -30.0f, 0.0f, 0.0f);
        if(spaceship.getX() >= planets.get(dest_planet).getX() + planets.get(dest_planet).getWidth()) {
            spaceship.startAnimation(anim);
            spaceship.setX(spaceship.getX()- 30.0f);
        /*    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams)spaceship.getLayoutParams();
            params.width = (int)(params.width * .50);
            params.height = (int)(params.height * .50);
            spaceship.setLayoutParams(params);  */
        }
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
            planet_decider.create().show();
        }

    }

    public void goToPlanetMenu() {
        Intent intent = new Intent(this, PlanetActivity.class);
        String p_name = Integer.toString(dest_planet);
        intent.putExtra(PLANET_NAME, p_name);
        startActivity(intent);
        finish();
    }

    public void selectPlanet(){
        AlertDialog.Builder planet_selector= new AlertDialog.Builder(this);
        planet_selector.setTitle(R.string.planet_select);

        planet_selector.setItems(PLANETS_ARRAY, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dest_planet = which;
                dialog.dismiss();
                planets.get(dest_planet).setVisibility(View.VISIBLE);
            }
        });
        AlertDialog dialog = planet_selector.create();
        dialog.show();
    }

    /* Override Methods */
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

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        // Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
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
