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
import android.view.animation.LinearInterpolator;
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
    private ImageView ast5;
    private ImageView ast6;
    private boolean destroyed = false;
    private int numAsteroidsDestroyed = 0;
    private int hullPercent = 50; // THIS IS DEFAULT, USE THIS TO ACTUALLY UPDATE ENGINE PERCENT FROM GAME

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_asteroid);

        if(getIntent().getIntExtra("HullHealth", 0) != 0) {
            hullPercent = getIntent().getExtras().getInt("HullHealth");
        }
        ship = (ImageView) findViewById(R.id.spaceshipMiddle);
        ship_hit = (ImageView) findViewById(R.id.spaceshipHit);
        ast1 = (ImageView) findViewById(R.id.asteroid1);
        ast2 = (ImageView) findViewById(R.id.asteroid2);
        ast3 = (ImageView) findViewById(R.id.asteroid3);
        ast4 = (ImageView) findViewById(R.id.asteroid4);
        ast5 = (ImageView) findViewById(R.id.asteroid5);
        ast6 = (ImageView) findViewById(R.id.asteroid6);
        TextView hull = (TextView) findViewById(R.id.hullHealth);
        hull.setText(Integer.toString(hullPercent)); // CHANGE THIS TO REPRESENT ACTUAL ENGINE PERCENTAGE

        Point size = new Point();
        Random rand = new Random(); // used to generate a random number for the start of the asteroid position
        int yPos = rand.nextInt(1400) - 200; // range is from -200 to 1200, randomly assigned this.
        int xPos = rand.nextInt(2500) - 200;
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
        ast5.setX(xPos);
        ast5.setY(size.y+200);
        xPos = rand.nextInt(2500) - 200;
        ast6.setX(xPos);
        ast6.setY(-250);
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        if (hasFocus) {
            int[] ship_coordinates = new int[2];
            ship.getLocationOnScreen(ship_coordinates);
            moveAsteroidObjects(ship_coordinates);
        }
    }

    public int getAmountOfFuel() {
        //Figure out way to get random number of fuel to add. Multiply by number of asteroids destroyed
        Random rand = new Random();
        int amountPerAsteroid = rand.nextInt(5) + 1;
        int fuel = amountPerAsteroid * numAsteroidsDestroyed;
        return fuel; // arbitrary number right now
    }

    public void miningResults() {
        int fuelAmount = getAmountOfFuel();
        Log.d("Asteroid", "Asteroids Clicked =" + numAsteroidsDestroyed + ", amount of fuel " + fuelAmount);
        AlertDialog.Builder planet_decider = new AlertDialog.Builder(this);
        planet_decider.setMessage("You have mined " + fuelAmount + " units of fuel.\nIt has been added to your inventory.");
        planet_decider.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        planet_decider.create().show();
    }

    public void moveAsteroidObjects(int[] coordinates) {
        Random rand = new Random();
        int delay = rand.nextInt(800) + 400; //random number for delay between 400 and 1200 milliseconds
        int numAsteroidsFaced = rand.nextInt(5) + 1;
        Log.d("Asteroid", "asteroids to be faced = " + numAsteroidsFaced);
        /* Handler to quit the activity once all asteroids have passed */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                miningResults();
            }
        }, 7000);

        /* This switch statement allows for a different number of asteroids to be faced every time */
        switch(numAsteroidsFaced) {

        case 1:
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
                    if (!destroyed) {
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

        case 2:
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
                    if (!destroyed) {
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

        case 3:
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
                    if (!destroyed) {
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

        case 4:
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
            ast4.animate().setStartDelay(delay + 1800);
            ast4.animate().x(coordinates[0] + 200).y(coordinates[1]);
            ast4.animate().setInterpolator(new LinearInterpolator());
            ast4.animate().alphaBy(1);
            ast4.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!destroyed) {
                        ast4.setVisibility(View.GONE);
                        shipHitAction();
                    }
                    destroyed = false;
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

        case 5:
        /* Asteroid 5 Animation */
            ast5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Asteroid", "Registered Click for Asteroid 5");
                    destroyed = true;
                    ast5.animate().cancel();
                }
            });
            ast5.animate().setDuration(2500);
            ast5.animate().setStartDelay(delay + 2200);
            ast5.animate().x(coordinates[0] + 200).y(coordinates[1]);
            ast5.animate().setInterpolator(new LinearInterpolator());
            ast5.animate().alphaBy(1);
            ast5.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!destroyed) {
                        ast5.setVisibility(View.GONE);
                        shipHitAction();
                    }
                    destroyed = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    numAsteroidsDestroyed++;
                    ast5.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        case 6:
        /* Asteroid 6 Animation */
            ast6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Asteroid", "Registered Click for Asteroid 6");
                    destroyed = true;
                    ast6.animate().cancel();
                }
            });
            ast6.animate().setDuration(2500);
            ast6.animate().setStartDelay(delay + 2700);
            ast6.animate().x(coordinates[0] + 200).y(coordinates[1]);
            ast6.animate().setInterpolator(new LinearInterpolator());
            ast6.animate().alphaBy(1);
            ast6.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!destroyed) {
                        ast6.setVisibility(View.GONE);
                        shipHitAction();
                    }
                    destroyed = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    numAsteroidsDestroyed++;
                    ast6.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        default: break;
        }
    }

    public void shipHitAction(){
        TextView engineView = (TextView) findViewById(R.id.hullPercent);
        hullPercent -= 5;
        engineView.setText(Integer.toString(hullPercent)); //USE THIS TO SUBTRACT ACTUAL FUEL
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
        ; // Disallow Exiting of game
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
