package team19.spacetrail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class PlanetActivity extends Activity {

    private String planet_name;
    private ArrayList<ImageView> planets = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_planet);

        //adding planets to arraylist
        planets.add((ImageView) findViewById(R.id.mercury));
        planets.add((ImageView) findViewById(R.id.venus));
        planets.add((ImageView) findViewById(R.id.earth));
        planets.add((ImageView) findViewById(R.id.mars));
        planets.add((ImageView) findViewById(R.id.jupiter));
        planets.add((ImageView) findViewById(R.id.saturn));
        planets.add((ImageView) findViewById(R.id.uranus));
        planets.add((ImageView) findViewById(R.id.neptune));

        Spinner spinner = (Spinner) findViewById(R.id.ResourceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.resources_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        setPlanetInfo();

    }

    public void setPlanetInfo() {
        Intent intent = getIntent();
        planet_name = intent.getStringExtra(GameScreenActivity.PLANET_NAME);
        int planet_name_as_int = Integer.parseInt(planet_name);
        planet_name = GameScreenActivity.PLANETS_ARRAY[planet_name_as_int];
        TextView p_name = (TextView) findViewById(R.id.planetName);
        p_name.setText(planet_name);
        planets.get(planet_name_as_int).setVisibility(View.VISIBLE);
    }

    public void buyResources(View view) {
        //This is where we will change the xml file to reflect the changes in resources.
        Button buy = (Button) findViewById(R.id.buyButtonPlanet);
        EditText rscText = (EditText) findViewById(R.id.spinnerTextField);
    }

    public void nextPlanet(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        finish();
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_planet, menu);
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
