package team19.spacetrail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


public class AsteroidActivity extends Activity {
    private ImageView ship;
    private ImageView ship_hit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_asteroid);
        ship = (ImageView) findViewById(R.id.spaceshipMiddle);
        ship_hit = (ImageView) findViewById(R.id.spaceshipHit);
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        int[] ship_coordinates = new int[2];
        ship.getLocationOnScreen(ship_coordinates);
        moveAsteroid(ship_coordinates);
    }

    public int getAmountOfFuel() {
        //Figure out way to get random number of fuel to add. Multiply by number of asteroids destroyed
        return 10; // arbitrary number right now
    }

    public void miningResults() {
        AlertDialog.Builder planet_decider = new AlertDialog.Builder(this);
        planet_decider.setMessage("You have mined " + getAmountOfFuel() + " units of fuel.\nIt has been added to your inventory.");
        planet_decider.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        planet_decider.create().show();
    }

    public void moveAsteroid(int[] coordinates) {
        final ImageView ast1 = (ImageView) findViewById(R.id.asteroid1);
        final ImageView ast2 = (ImageView) findViewById(R.id.asteroid2);

        final AnimationSet moveAsteroid = new AnimationSet(true);
        Animation mveAst = AnimationUtils.loadAnimation(this, R.anim.asteroid_anim);
        TranslateAnimation translate = new TranslateAnimation(200, -coordinates[0]+100, -200, coordinates[1]-100);
        translate.setDuration(1500);

        moveAsteroid.addAnimation(mveAst);
        moveAsteroid.addAnimation(translate);
        ast1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ast1.clearAnimation();
            }
        });
        ast2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ast2.clearAnimation();
            }
        });
        moveAsteroid.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ast1.setVisibility(View.GONE);
                ship.setVisibility(View.INVISIBLE);
                ship_hit.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ast1.startAnimation(moveAsteroid);

        TranslateAnimation translate1 = new TranslateAnimation(200, -coordinates[0]+100, -200, -coordinates[1]+100);
        translate1.setDuration(1500);
        final AnimationSet move2Asteroid = new AnimationSet(true);
        move2Asteroid.addAnimation(mveAst);
        move2Asteroid.addAnimation(translate1);
        move2Asteroid.setStartTime(4000);
        move2Asteroid.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ship.setVisibility(View.VISIBLE);
                ship_hit.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ast1.setVisibility(View.GONE);
                ship.setVisibility(View.INVISIBLE);
                ship_hit.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ast2.startAnimation(move2Asteroid);

        //ship.setColorFilter(original);
    }

    //Temporarily used for checking the end result
    @Override
    public void onBackPressed() {
        miningResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asteroid, menu);
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
}
