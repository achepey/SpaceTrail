package team19.spacetrail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javagame.*;


public class MainActivity extends Activity {

    private final double FOOD_COST = 1;
    private final double FUEL_COST = 5;
    private final int ENGINE_COST = 15;
    private final int ALUMINUM_COST = 10;
    private final int WING_COST = 15;
    private final int LIVING_BAY_COST = 15;

    private Game game;
    private int startingMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        game = new Game();
        try {
            AssetManager assetManager = getAssets();
            InputStream planetInput = assetManager.open("planetData.xml");

            game.setPlanetInputStream(planetInput);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        startingMoney = game.getMoney();
    }

    /* Helper Methods */
    //Sets Layout to the new game menu
    public void newGameMenu(View view) {
        setContentView(R.layout.new_game_menu);
    }

    //Sets Layout to the buying starting resources menu
    public void buyResourceMenu(View view) {
        EditText captain = (EditText) findViewById(R.id.editCapField);
        EditText crew1 = (EditText) findViewById(R.id.firstOtherNameField);
        EditText crew2 = (EditText) findViewById(R.id.secOtherNameField);
        EditText crew3 = (EditText) findViewById(R.id.thirdOtherNameField);
        EditText crew4 = (EditText) findViewById(R.id.fourOtherNameField);

        String capName = captain.getText().toString();
        String crew1Name = crew1.getText().toString();
        String crew2Name = crew2.getText().toString();
        String crew3Name = crew3.getText().toString();
        String crew4Name = crew4.getText().toString();

        if(capName.length()!=0 && crew1Name.length()!=0 && crew2Name.length()!=0 && crew3Name.length()!=0 && crew4Name.length()!=0) {
            game.addCrew(capName, true);
            game.addCrew(crew1Name, false);
            game.addCrew(crew2Name, false);
            game.addCrew(crew3Name, false);
            game.addCrew(crew4Name, false);

            final AlertDialog.Builder issue_alert = new AlertDialog.Builder(this);
            issue_alert.setTitle(R.string.race_info_title);
            issue_alert.setMessage("Your crew race is the " + game.getRace().getName() + ". The compound you need to survive is " + game.getRace().getStrength() + " and the compound that is toxic to you is " + game.getRace().getWeakness() + ".");
            issue_alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            Dialog d = issue_alert.create();
            d.setCancelable(false);
            d.show();

            setContentView(R.layout.resource_menu);
            TextView moneyText = (TextView) findViewById(R.id.moneyVariable);
            moneyText.setText(Integer.toString(startingMoney));
        }
        else {
            //Pop-up alert telling user to fill in all names
            Context context = getApplicationContext();
            CharSequence popup = "Need to input all names!";
            Toast toast = Toast.makeText(context, popup, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
        }
    }

    //Sets Layout to the load game menu
    public void loadGame(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        startActivity(intent);
        //setContentView(R.layout.activity_load_game);
    }

    //Sets Layout to the Instructions menu
    public void loadInstructions(View view) {
        Game testGame = new Game();
        testGame.setMoney(1000000);
        testGame.getResources().setCompound(10000);
        testGame.getResources().setAluminum(10000);
        testGame.getResources().setFood(10000);
        testGame.getResources().setFuel(10000);
        testGame.getResources().addSpare(new Part("engine",100));
        testGame.getResources().addSpare(new Part("wing",100));
        testGame.getResources().addSpare(new Part("livingBay",100));
        testGame.addCrew("cap",true);
        testGame.addCrew("1",false);
        testGame.addCrew("2",false);
        testGame.addCrew("3",false);
        testGame.addCrew("4",false);

        try {
            AssetManager assetManager = getAssets();
            InputStream planetInput = assetManager.open("planetData.xml");

            testGame.setPlanetInputStream(planetInput);

        }
        catch(Exception e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(this, GameScreenActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("Game", testGame);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    //    setContentView(R.layout.instruction_screen);
    }

    //Does the function of buying the original resources.
    public void buyStartingResources(View view) {
        //This is where we will change the xml file to reflect the changes in resources.
        Button buy = (Button) findViewById(R.id.buyButton);
        EditText fuel = (EditText) findViewById(R.id.fuelTextField);
        EditText food = (EditText) findViewById(R.id.foodTextField);
        EditText engine = (EditText) findViewById(R.id.engineTextField);
        EditText aluminum = (EditText) findViewById(R.id.aluminumTextField);
        EditText wings = (EditText) findViewById(R.id.wingsTextField);
        EditText livingBays = (EditText) findViewById(R.id.livingBayTextField);

        TextView fuelText = (TextView) findViewById(R.id.fuelQuantity);
        TextView foodText = (TextView) findViewById(R.id.foodQuantity);
        TextView engineText = (TextView) findViewById(R.id.engineQuantity);
        TextView aluminumText = (TextView) findViewById(R.id.aluminumQuantity);
        TextView wingsText = (TextView) findViewById(R.id.wingQuantity);
        TextView livingBayText = (TextView) findViewById(R.id.livingBayQuantity);

        String fuelAmt = fuel.getText().toString();
        String foodAmt = food.getText().toString();
        String almAmt = aluminum.getText().toString();
        String engAmt = engine.getText().toString();
        String wingAmt = wings.getText().toString();
        String lbAmt = livingBays.getText().toString();

        /* If no resources are input, does not allow user to buy them*/
        if (fuelAmt.matches("") && foodAmt.matches("") && almAmt.matches("") && engAmt.matches("") && wingAmt.matches("") && lbAmt.matches("")) {
            CharSequence popup = "You did not enter any valid numbers!";
            Toast toast = Toast.makeText(getApplicationContext(), popup, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return;
        }

        /* adds default values for resource acquisitions */
        if(fuelAmt.equals("")) {
            fuelAmt = "0";
        }
        if(foodAmt.equals("")) {
            foodAmt = "0";
        }
        if(almAmt.equals("")) {
            almAmt = "0";
        }
        if(engAmt.equals("")) {
            engAmt = "0";
        }
        if(wingAmt.equals("")) {
            wingAmt = "0";
        }
        if(lbAmt.equals("")) {
            lbAmt = "0";
        }

        if(Integer.parseInt(fuelAmt) * FUEL_COST + Integer.parseInt(foodAmt)*FOOD_COST + Integer.parseInt(engAmt)*ENGINE_COST+Integer.parseInt(almAmt)*ALUMINUM_COST+Integer.parseInt(wingAmt)*WING_COST+Integer.parseInt(lbAmt) * LIVING_BAY_COST - startingMoney < 0) {
            fuelText.setText(Integer.toString(Integer.parseInt(fuelText.getText().toString()) + Integer.parseInt(fuelAmt)));
            foodText.setText(Integer.toString(Integer.parseInt(foodText.getText().toString()) + Integer.parseInt(foodAmt)));
            engineText.setText(Integer.toString(Integer.parseInt(engineText.getText().toString()) + Integer.parseInt(engAmt)));
            aluminumText.setText(Integer.toString(Integer.parseInt(aluminumText.getText().toString()) + Integer.parseInt(almAmt)));
            wingsText.setText(Integer.toString(Integer.parseInt(wingsText.getText().toString()) + Integer.parseInt(wingAmt)));
            livingBayText.setText(Integer.toString(Integer.parseInt(livingBayText.getText().toString()) + Integer.parseInt(lbAmt)));

            startingMoney -= Integer.parseInt(fuelAmt) * FUEL_COST + Integer.parseInt(foodAmt)*FOOD_COST + Integer.parseInt(engAmt)*ENGINE_COST+Integer.parseInt(almAmt)*ALUMINUM_COST+Integer.parseInt(wingAmt)*WING_COST + Integer.parseInt(lbAmt) * LIVING_BAY_COST;

            //Creates pop-up letting user know the resources were purchased correctly
            Context context = getApplicationContext();
            CharSequence popup = "Resources Acquired!";
            Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
        }
        else {
            //Creates pop-up letting user know the resources were purchased correctly
            Context context = getApplicationContext();
            CharSequence popup = "Not Enough Funds!";
            Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
        }


        //Resets the fields to be blank values
        fuel.setText("");
        food.setText("");
        engine.setText("");
        aluminum.setText("");
        wings.setText("");
        livingBays.setText("");
        TextView moneyText = (TextView) findViewById(R.id.moneyVariable);
        moneyText.setText(Integer.toString(startingMoney));
    }
    // Called when the user hits the send to space button, stores the starting resources
    public void headToSpace(View view) {
        TextView fuel = (TextView) findViewById(R.id.fuelQuantity);
        int fuelint = Integer.parseInt(fuel.getText().toString());
        game.sellFuel(fuelint);

        TextView food = (TextView) findViewById(R.id.foodQuantity);
        int foodint = Integer.parseInt(food.getText().toString());
        game.sellFood(foodint);

        TextView engine = (TextView) findViewById(R.id.engineQuantity);
        int engineint = Integer.parseInt(engine.getText().toString());
        game.sellParts(engineint, "engine");

        TextView alum = (TextView) findViewById(R.id.aluminumQuantity);
        int alumint = Integer.parseInt(alum.getText().toString());
        game.sellAluminum(alumint);

        TextView wings = (TextView) findViewById(R.id.wingQuantity);
        int wingsint = Integer.parseInt(wings.getText().toString());
        game.sellParts(wingsint, "wing");

        //Ends MainActivity and starts the GameScreenActivity
        Intent intent = new Intent(this, GameScreenActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("Game", game);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    //Used for a button to start the GameScreenActivity
    public void gameScreen(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        intent.putExtra("Game", game);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.promptBack);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setContentView(R.layout.activity_main);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
