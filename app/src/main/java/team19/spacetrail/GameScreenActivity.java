package team19.spacetrail;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
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
    protected static Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        game = (Game) getIntent().getExtras().getSerializable("Game");
        ArrayList<String> crewNames = getIntent().getStringArrayListExtra("Crew");

        Log.d("GameScreen", "Size of crewNames = " + crewNames.size());

        for(String s : crewNames){
            Log.d("GameScreen", s);
        }
        game.crewNames = crewNames;
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

        /* Setting fields for Food and Fuel */
        TextView fuelView = (TextView) findViewById(R.id.gameScreenFuel);
        TextView foodView = (TextView) findViewById(R.id.gameScreenFood);
        fuelView.setText(Integer.toString(game.getResources().getFuel()));
        foodView.setText(Integer.toString(game.getResources().getFood()));


        if(game.getDistanceRemaining() <= 0 || game.getDestination().name.equals("Temp")) {
            selectPlanet();
        }
        else {
            displayPlanet();
        }

        //Will add an else here when we get loading set up

    //    selectPlanet(); //will be removed when fully integrated
    }

    /* Helper Methods */

    //Process of moving ship across screen
    public void automaticRepair(View v) {
        Log.d("GameScreen", "Pace is " + game.getSpeed());

        if (game.getShip().getEngineStatus() <= 0) {
            if (game.repairEngine()) {
                final AlertDialog.Builder repair_alert = new AlertDialog.Builder(this);
                repair_alert.setTitle(R.string.issue_title);
                repair_alert.setMessage("Your engines were critically damaged and have been replaced with one of your spares.");
                repair_alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        makeMove();
                    }
                });
                Dialog repair_dialog = repair_alert.create();
                repair_dialog.setCancelable(false);
                repair_dialog.show();
            }
        }
        if (game.getShip().getWingStatus() <= 0) {
            if (game.repairWing()) {
                final AlertDialog.Builder repair_alert = new AlertDialog.Builder(this);
                repair_alert.setTitle(R.string.issue_title);
                repair_alert.setMessage("Your wings were critically damaged and have been replaced with one of your spares.");
                repair_alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        makeMove();
                    }
                });
                Dialog repair_dialog = repair_alert.create();
                repair_dialog.setCancelable(false);
                repair_dialog.show();
            }
        }
        if (game.getShip().getLivingBayStatus() <= 0) {
            if (game.repairLivingBay()) {
                final AlertDialog.Builder repair_alert = new AlertDialog.Builder(this);
                repair_alert.setTitle(R.string.issue_title);
                repair_alert.setMessage("Your living bay was critically damaged and has been replaced with one of your spares.");
                repair_alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        makeMove();
                    }
                });
                Dialog repair_dialog = repair_alert.create();
                repair_dialog.setCancelable(false);
                repair_dialog.show();
            }
        }
        makeMove();
    }

    public void makeMove() {
        //Stops ship when bitmap reaches planets outer edge, also shrinks ship when getting closer to planet
        String moveResult = game.makeMove();
        TextView fuelView = (TextView) findViewById(R.id.gameScreenFuel);
        TextView foodView = (TextView) findViewById(R.id.gameScreenFood);
        fuelView.setText(Integer.toString(game.getResources().getFuel()));
        foodView.setText(Integer.toString(game.getResources().getFood()));
        if (!moveResult.equals("Successful Movement!") && (game.getShip().getEngineStatus() > 0 && game.getShip().getWingStatus() > 0 && game.getShip().getLivingBayStatus() > 0)) {
            final GameScreenActivity tempGSA = this;
            final AlertDialog.Builder issue_alert = new AlertDialog.Builder(this);
            issue_alert.setTitle(R.string.issue_title);
            issue_alert.setMessage(moveResult);
            issue_alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                if (game.isLoser()) {
                    dialog.cancel();
                    Intent intent = new Intent(tempGSA, ExitActivity.class);
                    intent.putExtra("activity", "Loser");
                    startActivity(intent);
                    finish();
                } else {
                    dialog.cancel();
                    moveOnScreen();
                }
            }
                          });
            Dialog d = issue_alert.create();
            d.setCancelable(false);
            d.show();
        } else {
            moveOnScreen();
        }
    }



    public void moveOnScreen() {
        if(game.justLoaded){
            spaceship.setX(game.getShip().getXpos());
            game.justLoaded = false;
        }

        double moveScreenPercent = game.getPace() / game.getTotalDistance();
        double amtToMoveOnScreen = moveScreenPercent * (screen_width - planets.get(dest_planet).getX() - planets.get(dest_planet).getWidth() - spaceship.getWidth()) + 10;

        TranslateAnimation anim = new TranslateAnimation(0.0f, (float) (amtToMoveOnScreen * -1), 0.0f, 0.0f);
        if (!game.getArrivedAtPlanet()) {
            spaceship.startAnimation(anim);
            spaceship.setX(spaceship.getX() - (float) amtToMoveOnScreen);
            if (spaceship.getX() < screen_width / 2) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship_crop);
                int width = (int) (spaceship.getWidth() * .90);
                int height = (int) (spaceship.getHeight() * .90);
                Bitmap rbmp = Bitmap.createScaledBitmap(bmp, width, height, true);
                spaceship.setImageBitmap(rbmp);
            }
        }
        //Tells user they arrived at planet, and gives choice of moving to next planet, or stopping on planet to purchase resources
        else {
            if(game.isWinner()){
                Intent intent = new Intent(this, ExitActivity.class);
                intent.putExtra("activity", "Winner");
                startActivity(intent);
                finish();
            }
            else {
                AlertDialog.Builder planet_decider = new AlertDialog.Builder(this);
                planet_decider.setMessage("You've reached " + PLANETS_ARRAY[dest_planet] + "!\nWould you like to stop by the convenience store?");
                planet_decider.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goToPlanetMenu();
                    }
                });
                planet_decider.setNegativeButton(R.string.nextPlanet, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //    recreate();
                        Intent intent = getIntent();
                        game.setArrivedAtPlanet(false);
                        intent.putExtra("Game", game);
                        startActivity(intent);
                        finish();
                    }
                });
                planet_decider.setCancelable(false);
                planet_decider.create().show();
            }
        }
    }

    //Ends this activity and sends planet info to planet activity
    public void goToPlanetMenu() {
        Intent intent = new Intent(this, PlanetActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("Game", game);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    //Displays picture of planet to planet menu for better aesthetic view
    public void selectPlanet(){
        final Dialog planet_selector = new Dialog(GameScreenActivity.this);
        planet_selector.setContentView(R.layout.planet_selector_layout);
        planet_selector.setTitle(R.string.planet_select);
        ArrayList<TextView> choices = new ArrayList<TextView>();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(planet_selector.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        planet_selector.getWindow().setAttributes(lp);

        TextView merc = (TextView) planet_selector.findViewById(R.id.select_mercury);
        merc.setText("Mercury: Distance is " + game.distanceToPlanet(game.getPlanets().get(0)) + ". (Helium, Oxygen)");
        merc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Mercury")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                } else {
                    dest_planet = 0; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(merc);

        TextView venus = (TextView) planet_selector.findViewById(R.id.select_venus);
        venus.setText("Venus: Distance is " + game.distanceToPlanet(game.getPlanets().get(1)) + " - (Nitrogen, Carbon Dioxide)");
        venus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Venus")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
                else {
                    dest_planet = 1; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(venus);

        TextView earth = (TextView) planet_selector.findViewById(R.id.select_earth);
        earth.setText("Earth: Distance is " + game.distanceToPlanet(game.getPlanets().get(2)) + " - (Nitrogen, Oxygen)");
        earth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Earth")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
                else {
                    dest_planet = 2; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(earth);

        TextView mars = (TextView) planet_selector.findViewById(R.id.select_mars);
        mars.setText("Mars: Distance is " + game.distanceToPlanet(game.getPlanets().get(3)) + " - (Nitrogen, Carbon Dioxide)");
        mars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Mars")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
                else {
                    dest_planet = 3; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(mars);

        TextView jupiter = (TextView) planet_selector.findViewById(R.id.select_jupiter);
        jupiter.setText("Jupiter: Distance is " + game.distanceToPlanet(game.getPlanets().get(4)) + " - (Helium, Hydrogen)");
        jupiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Jupiter")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
                else {
                    dest_planet = 4; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(jupiter);

        TextView saturn = (TextView) planet_selector.findViewById(R.id.select_saturn);
        saturn.setText("Saturn: Distance is " + game.distanceToPlanet(game.getPlanets().get(5)) + " - (Helium, Hydrogen)");
        saturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Saturn")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
                else {
                    dest_planet = 5; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(saturn);

        TextView uranus = (TextView) planet_selector.findViewById(R.id.select_uranus);
        uranus.setText("Uranus: Distance is " + game.distanceToPlanet(game.getPlanets().get(6)) + " - (Helium, Methane)");
        uranus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Uranus")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
                else {
                    dest_planet = 6; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(uranus);

        TextView neptune = (TextView) planet_selector.findViewById(R.id.select_neptune);
        neptune.setText("Neptune: Distance is " + game.distanceToPlanet(game.getPlanets().get(7)) + " - (Helium, Methane)");
        neptune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Neptune")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
                else {
                    dest_planet = 7; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(neptune);

        TextView pluto = (TextView) planet_selector.findViewById(R.id.select_pluto);
        pluto.setText("Pluto: Distance is " + game.distanceToPlanet(game.getPlanets().get(8)) + " - (Helium, Methane)");
        pluto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getPrevious().name.equals("Pluto")) {
                    CharSequence popup = "You are already on this planet, explorer!";
                    Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
                else {
                    dest_planet = 8; // This is the numerical representation of the planet
                    planets.get(dest_planet).setVisibility(view.VISIBLE);
                    if (game.getFirstMove()) {
                        game.setFirstDestination(dest_planet);
                    } else {
                        game.setDestination(dest_planet);
                    }
                    planet_selector.dismiss();
                }
            }
        });
        choices.add(pluto);

        for(int i = 0; i < choices.size(); i++) {
            if(game.getPlanets().get(i).visited){
                choices.get(i).setTextColor(Color.RED);
            }
        }
        planet_selector.setCancelable(false);
        planet_selector.show();
    }

    //Displays picture of planet to planet menu for better aesthetic view
    public void displayPlanet(){
        if(game.getDestination().name.equals("Mercury")) {
            planets.get(0).setVisibility(View.VISIBLE);
            dest_planet = 0;
        }
        else if(game.getDestination().name.equals("Venus")) {
            planets.get(1).setVisibility(View.VISIBLE);
            dest_planet = 1;
        }
        else if(game.getDestination().name.equals("Earth")) {
            planets.get(2).setVisibility(View.VISIBLE);
            dest_planet = 2;
        }
        else if(game.getDestination().name.equals("Mars")) {
            planets.get(3).setVisibility(View.VISIBLE);
            dest_planet = 3;
        }
        else if(game.getDestination().name.equals("Jupiter")) {
            planets.get(4).setVisibility(View.VISIBLE);
            dest_planet = 4;
        }
        else if(game.getDestination().name.equals("Saturn")) {
            planets.get(5).setVisibility(View.VISIBLE);
            dest_planet = 5;
        }
        else if(game.getDestination().name.equals("Uranus")) {
            planets.get(6).setVisibility(View.VISIBLE);
            dest_planet = 6;
        }
        else if(game.getDestination().name.equals("Neptune")) {
            planets.get(7).setVisibility(View.VISIBLE);
            dest_planet = 7;
        }
        else {
            planets.get(8).setVisibility(View.VISIBLE);
            dest_planet = 8;
        }
    }

    public void repairHull(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Uh oh! Your Hull needs repairs!");
        builder.setMessage("Use your spare aluminum to repair it!");
    }

    /* Override Methods */

    //Allows for user to save and exit the game
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.promptExit);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                game.getShip().setXpos(spaceship.getX());
                System.out.println("save x: "+game.getShip().getXpos());
                GameFileSaver saver = new GameFileSaver(game);
                File file = new File(getExternalFilesDir(null),"SpaceTrailData.xml");
                saver.saveGameAndroid(file);
                finish();
        /*        File folder = getExternalFilesDir(null);
                File[] listOfFiles = folder.listFiles();

                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        System.out.println("File " + listOfFiles[i].getName());
                    } else if (listOfFiles[i].isDirectory()) {
                        System.out.println("Directory " + listOfFiles[i].getName());
                    }
                }       */
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
        Bundle b = new Bundle();
        b.putSerializable("Game", game);
        intent.putExtras(b);
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
        automaticRepair(v);
        return true;
    }

    //Uses a double tap to propel the ship forward
    @Override
    public boolean onDoubleTap(MotionEvent event) {
        View v = new View(this);
        automaticRepair(v);
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
