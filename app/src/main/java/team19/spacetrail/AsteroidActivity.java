package team19.spacetrail;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;


public class AsteroidActivity extends Activity {
    private ImageView ship;
    private ImageView ship_hit;
    private ImageView ast1;
    private ImageView ast2;
    private ImageView ast3;
    private ImageView ast4;
    private boolean destroyed = false;
    private int numAsteroidsDestroyed = 0;
    private int enginePercent = 100; // THIS IS DEFAULT, USE THIS TO ACTUALLY UPDATE ENGINE PERCENT FROM GAME

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_asteroid);
        ship = (ImageView) findViewById(R.id.spaceshipMiddle);
        ship_hit = (ImageView) findViewById(R.id.spaceshipHit);
        ast1 = (ImageView) findViewById(R.id.asteroid1);
        ast2 = (ImageView) findViewById(R.id.asteroid2);
        ast3 = (ImageView) findViewById(R.id.asteroid3);
        ast4 = (ImageView) findViewById(R.id.asteroid4);
        TextView engine = (TextView) findViewById(R.id.enginePercent);
        engine.setText(Integer.toString(enginePercent)); // CHANGE THIS TO REPRESENT ACTUAL ENGINE PERCENTAGE

        Point size = new Point();
        Random rand = new Random(); // used to generate a random number for the start of the asteroid position
        int yPos = rand.nextInt(1400) - 200; // range is from -200 to 1200, randomly assigned this.
        getWindowManager().getDefaultDisplay().getSize(size); // Stores the height and width of the phone screen size

        /* Setting starting positions of the asteroids */
        ast1.setX(size.x + 200);
        ast1.setY(yPos);
        ast2.setX(-250);
        yPos = rand.nextInt(1400) - 200; // generates a new random number
        ast2.setY(yPos);
        ast3.setX(-250);
        yPos = rand.nextInt(1400) - 200; // generates a new random number
        ast3.setY(yPos);
        ast4.setX(size.x + 200);
        yPos = rand.nextInt(1400) - 200; // generates a new random number
        ast4.setY(yPos);
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        int[] ship_coordinates = new int[2];
        ship.getLocationOnScreen(ship_coordinates);
        //moveAsteroid(ship_coordinates);
        moveAsteroidObjects(ship_coordinates);
    }

    public int getAmountOfFuel() {
        //Figure out way to get random number of fuel to add. Multiply by number of asteroids destroyed
        Random rand = new Random();
        int amountPerAsteroid = rand.nextInt(50);
        int fuel = amountPerAsteroid * numAsteroidsDestroyed;
        return fuel; // arbitrary number right now
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

        final AnimationSet moveAsteroid = new AnimationSet(true);
        final AnimationSet move2Asteroid = new AnimationSet(true);

        Animation mveAst = AnimationUtils.loadAnimation(this, R.anim.asteroid_anim);
        Animation mveAst1 = AnimationUtils.loadAnimation(this, R.anim.asteroid_anim);

        TranslateAnimation translate = new TranslateAnimation(200, -coordinates[0]+100, -200, coordinates[1]-100);
        translate.setDuration(1500);
        TranslateAnimation translate2 = new TranslateAnimation(-200, coordinates[0], 200, -coordinates[1]+100);
        translate2.setDuration(1500);

        moveAsteroid.addAnimation(mveAst);
        moveAsteroid.addAnimation(translate);
        moveAsteroid.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ast2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ast1.setVisibility(View.GONE);
                ast1.setVisibility(View.GONE);
                ship.setVisibility(View.INVISIBLE);
                ship_hit.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        ship.setVisibility(View.VISIBLE);
                        ship_hit.setVisibility(View.INVISIBLE);
                    }
                }, 500);
                ast2.startAnimation(move2Asteroid);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        move2Asteroid.addAnimation(mveAst1);
        move2Asteroid.addAnimation(translate2);
        move2Asteroid.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ast1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ast1.setVisibility(View.GONE);
                ship.setVisibility(View.INVISIBLE);
                ship_hit.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        ship.setVisibility(View.VISIBLE);
                        ship_hit.setVisibility(View.INVISIBLE);
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ast1.startAnimation(moveAsteroid);
    }

    public void moveAsteroidObjects(int[] coordinates) {
        Random rand = new Random();
        int delay = rand.nextInt(1200) + 400; //random number for delay between 400 and 1600 milliseconds

        /* Asteroid 1 animation */
        ast1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Asteroid", "Registered Click for Asteroid 1");
                destroyed = true;
                ast1.animate().cancel();
            }
        });
        ast1.animate().setDuration(2500);
        ast1.animate().setStartDelay(delay);
        ast1.animate().x(coordinates[0] + 200).y(coordinates[1]);
        ast1.animate().setInterpolator(new LinearInterpolator());
        ast1.animate().alphaBy(1);
        ast1.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!destroyed) {
                    ast1.setVisibility(View.GONE);
                    shipHitAction();
                }
                destroyed = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                numAsteroidsDestroyed++;
                ast1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        /* Asteroid 2 Animation */
        ast2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Asteroid", "Registered Click for Asteroid 2");
                destroyed = true;
                ast2.animate().cancel();
            }
        });
        ast2.animate().setDuration(2500);
        ast2.animate().setStartDelay(delay + 350);
        ast2.animate().x(coordinates[0] + 200).y(coordinates[1]);
        ast2.animate().setInterpolator(new LinearInterpolator());
        ast2.animate().alphaBy(1);
        ast2.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!destroyed) {
                    ast2.setVisibility(View.GONE);
                    shipHitAction();
                }
                destroyed = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                numAsteroidsDestroyed++;
                ast2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        /* Asteroid 3 Animation */
        ast3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Asteroid", "Registered Click for Asteroid 3");
                destroyed = true;
                ast3.animate().cancel();
            }
        });
        ast3.animate().setDuration(2500);
        ast3.animate().setStartDelay(delay + 1000);
        ast3.animate().x(coordinates[0] + 200).y(coordinates[1]);
        ast3.animate().setInterpolator(new LinearInterpolator());
        ast3.animate().alphaBy(1);
        ast3.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!destroyed) {
                    ast3.setVisibility(View.GONE);
                    shipHitAction();
                }
                destroyed = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                numAsteroidsDestroyed++;
                ast3.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        /* Asteroid 4 Animation */
        ast4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Asteroid", "Registered Click for Asteroid 4");
                destroyed = true;
                ast4.animate().cancel();
            }
        });
        ast4.animate().setDuration(2500);
        ast4.animate().setStartDelay(delay + 2500);
        ast4.animate().x(coordinates[0] + 200).y(coordinates[1]);
        ast4.animate().setInterpolator(new LinearInterpolator());
        ast4.animate().alphaBy(1);
        ast4.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!destroyed) {
                    ast4.setVisibility(View.GONE);
                    shipHitAction();
                }
                destroyed = false;
                miningResults();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                numAsteroidsDestroyed++;
                ast4.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void shipHitAction(){
        TextView engineView = (TextView) findViewById(R.id.enginePercent);
        enginePercent -= 10;
        engineView.setText(Integer.toString(enginePercent)); //USE THIS TO SUBTRACT ACTUAL FUEL
        ship.setVisibility(View.INVISIBLE);
        ship_hit.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ship.setVisibility(View.VISIBLE);
                ship_hit.setVisibility(View.INVISIBLE);
            }
        }, 500);
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
