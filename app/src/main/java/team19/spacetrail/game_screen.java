package team19.spacetrail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;


public class game_screen extends Activity {

    private ImageView spaceship;
    private ArrayList<ImageView> planets;
    private int dest_planet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        AlertDialog.Builder planet_selector= new AlertDialog.Builder(this);
        planet_selector.setTitle(R.string.planet_select);

    }

    public void moveShip(View v) {
        TranslateAnimation anim = new TranslateAnimation(0.0f, -30.0f, 0.0f, 0.0f);
        if(spaceship.getX() >= 30) {
            spaceship.startAnimation(anim);
            spaceship.setX(spaceship.getX()-30.0f);
        }

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
}
