package team19.spacetrail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javagame.Game;


public class PlanetActivity extends Activity {
    /* Instance Fields */
    private Game game;
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
        planets.add((ImageView) findViewById(R.id.pluto));

        Spinner spinner = (Spinner) findViewById(R.id.ResourceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.resources_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //Allows for changing price with spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            private TextView priceText = (TextView) findViewById(R.id.priceVariable);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch((int)id){
                    case 0:
                        priceText.setText(Double.toString(game.getDestination().fuelCost));
                        break;
                    case 1:
                        priceText.setText(Double.toString(game.getDestination().foodCost));
                        break;
                    case 2:
                        priceText.setText(Double.toString(game.getDestination().aluminumCost));
                        break;
                    default:
                        priceText.setText(Double.toString(game.getDestination().partCost));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                priceText.setText("");
            }
        });

        game = (Game) getIntent().getExtras().getSerializable("Game");

        //Setting fields for resources
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
        setPlanetInfo();

    }

    /* Helper Methods */

    //Gets info from previous activity, and sets the planet info screen appropriately
    public void setPlanetInfo() {
        int planet_name_as_int = game.getDestinationIndex();
        planet_name = GameScreenActivity.PLANETS_ARRAY[planet_name_as_int];
        TextView p_name = (TextView) findViewById(R.id.planetName);
        p_name.setText(planet_name);
        planets.get(planet_name_as_int).setVisibility(View.VISIBLE);
    }

    //Does the process of buying resources and updating data file to reflect them
    public void buyResources(View view) {
        //This is where we will change the xml file to reflect the changes in resources.
        Button buy = (Button) findViewById(R.id.buyButtonPlanet);
        EditText rscText = (EditText) findViewById(R.id.buyAmountField);
        Spinner spin = (Spinner) findViewById(R.id.ResourceSpinner);

        //Might want to check to see if it is a digit as well
        String stringRscText = rscText.getText().toString();
        if (stringRscText.matches("")) {
            CharSequence popup = "You did not enter a valid number!";
            Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 350);
            toast.show();
            return;
        }

        //Checking which resource is selected and then buying it from game
        String prompt = spin.getSelectedItem().toString();
        int num = Integer.parseInt(stringRscText);
        if(prompt.equals("Fuel")) {
            if(game.sellFuel(num)) {
                Context context = getApplicationContext();
                CharSequence popup = "Resources Acquired!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
                TextView fuel = (TextView) findViewById(R.id.fuelVariable);
                fuel.setText(Integer.toString(game.getResources().getFuel()));
            }
            else{
                Context context = getApplicationContext();
                CharSequence popup = "Not Enough Funds!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
            }
        }
        else if(prompt.equals("Food")){
            if(game.sellFood(num)) {
                Context context = getApplicationContext();
                CharSequence popup = "Resources Acquired!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
                TextView food = (TextView) findViewById(R.id.foodVariable);
                food.setText(Integer.toString(game.getResources().getFood()));
            }
            else{
                Context context = getApplicationContext();
                CharSequence popup = "Not Enough Funds!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
            }
        }
        else if(prompt.equals("Aluminum")){
            if(game.sellAluminum(num)) {
                Context context = getApplicationContext();
                CharSequence popup = "Resources Acquired!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
                TextView aluminum = (TextView) findViewById(R.id.aluminumVariable);
                aluminum.setText(Integer.toString(game.getResources().getAluminum()));
            }
            else{
                Context context = getApplicationContext();
                CharSequence popup = "Not Enough Funds!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
            }
        }
        else if(prompt.equals("Spare Engines")){
            if(game.sellParts(num, "engine")) {
                Context context = getApplicationContext();
                CharSequence popup = "Resources Acquired!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
                TextView engines = (TextView) findViewById(R.id.engineVariable);
                engines.setText(Integer.toString(game.getResources().getSpareEngines()));
            }
            else{
                Context context = getApplicationContext();
                CharSequence popup = "Not Enough Funds!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
            }
        }
        else if(prompt.equals("Spare Wings")){
            if(game.sellParts(num, "wing")) {
                Context context = getApplicationContext();
                CharSequence popup = "Resources Acquired!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
                TextView wing = (TextView) findViewById(R.id.wingVariable);
                wing.setText(Integer.toString(game.getResources().getSpareWings()));
            }
            else{
                Context context = getApplicationContext();
                CharSequence popup = "Not Enough Funds!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
            }
        }
        else{
            if(game.sellParts(num,"livingBay")) {
                Context context = getApplicationContext();
                CharSequence popup = "Resources Acquired!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
                TextView livingBay = (TextView) findViewById(R.id.livingBayVariable);
                livingBay.setText(Integer.toString(game.getResources().getSpareLivingBays()));
            }
            else{
                Context context = getApplicationContext();
                CharSequence popup = "Not Enough Funds!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 350);
                toast.show();
                rscText.setText("");
            }
        }
        TextView money = (TextView) findViewById(R.id.moneyVariable);
        money.setText(Integer.toString(game.getMoney()));


       if(planet_name.equals("Venus") && prompt.equals("Spare Wings") && num == 7) {
            Intent intent = new Intent(this, ExitActivity.class);
            finish();
            intent.putExtra("activity", "Planet");
            startActivity(intent);
        }

    }

    //called when user wants to travel to next planet, ends this activity and starts another Game Screen Activity
    public void nextPlanet(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        Bundle b = new Bundle();
        //Sending game object to GameScreenActivity
        game.setArrivedAtPlanet(false);
        b.putSerializable("Game", game);
        b.putStringArrayList("Crew", game.crewNames);
        intent.putExtras(b);
        finish();
        startActivity(intent);
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
