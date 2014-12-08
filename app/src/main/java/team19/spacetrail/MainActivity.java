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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javagame.*;


public class MainActivity extends Activity {

    //Starting costs for resources
    private final int FOOD_COST = 4;
    private final int FUEL_COST = 5;
    private final int ENGINE_COST = 100;
    private final int ALUMINUM_COST = 10;
    private final int WING_COST = 100;
    private final int LIVING_BAY_COST = 100;

    private Game game;
    private int startingMoney;
    protected String currentView;
    private ArrayList<String> crewNames;

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        //tag was needed for pressing back button to go back to splash screen
        currentView = (String)view.getTag();
        super.setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        crewNames = new ArrayList<String>();
        game = new Game();
        try {
            AssetManager assetManager = getAssets();
            InputStream planetInput = assetManager.open("planetData.xml");
            InputStream schema = assetManager.open("mySchema.xsd");
            //input stream needed to load planet info
            game.setPlanetInputStream(planetInput);
            //needed for validation
            game.setSchema(schema);
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
        game.crewNames = new ArrayList<String>(crewNames);
        //checking if 5 names have been entered
        if(capName.length()!=0 && crew1Name.length()!=0 && crew2Name.length()!=0 && crew3Name.length()!=0 && crew4Name.length()!=0) {
            game.addCrew(capName, true);
            crewNames.add(capName);
            game.addCrew(crew1Name, false);
            crewNames.add(crew1Name);
            game.addCrew(crew2Name, false);
            crewNames.add(crew2Name);
            game.addCrew(crew3Name, false);
            crewNames.add(crew3Name);
            game.addCrew(crew4Name, false);
            crewNames.add(crew4Name);

            //displaying race
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
        File file = new File(getExternalFilesDir(null),"SpaceTrailData.xml");
        GameFileLoader loader = new GameFileLoader(file, game.getSchema());
        game = loader.loadGame();
        game.justLoaded = true;

        try {
            AssetManager assetManager = getAssets();
            InputStream planetInput = assetManager.open("planetData.xml");
            game.setPlanetInputStream(planetInput);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //at this point the game is loaded and playable

        setContentView(R.layout.activity_load_game);

        //displaying game information on loading page
        TextView engineHealth = (TextView) findViewById(R.id.engineHealth);
        TextView livingBayHealth = (TextView) findViewById(R.id.livingBayHealth);
        TextView wingsHealth = (TextView) findViewById(R.id.wingHealth);
        TextView hullHealth = (TextView) findViewById(R.id.hullHealth);
        TextView pace = (TextView) findViewById(R.id.paceValue);

        engineHealth.setText(Integer.toString(game.getShip().getEngineStatus())+"%");
        livingBayHealth.setText(Integer.toString(game.getShip().getLivingBayStatus())+"%");
        wingsHealth.setText(Integer.toString(game.getShip().getWingStatus())+"%");
        hullHealth.setText(Integer.toString(game.getShip().getHullStatus())+"%");
        if(game.getSpeed() == 1) {
            pace.setText("Fast");
        }
        else if(game.getSpeed() == 2) {
            pace.setText("Medium");
        }
        else {
            pace.setText("Slow");
        }

        /* Setting fields for resources */
        TextView money = (TextView) findViewById(R.id.moneyVariable);
        TextView fuel = (TextView) findViewById(R.id.fuelVariable);
        TextView food = (TextView) findViewById(R.id.foodVariable);
        TextView aluminum = (TextView) findViewById(R.id.aluminumVariable);
        TextView sprEngines = (TextView) findViewById(R.id.engineVariable);
        TextView sprWings = (TextView) findViewById(R.id.wingVariable);
        TextView sprLivBay = (TextView) findViewById(R.id.livingBayVariable);
        TextView compoundName = (TextView) findViewById(R.id.resourceCompound);
        TextView compound = (TextView) findViewById(R.id.compoundVariable);
        TextView weakness = (TextView) findViewById(R.id.weaknessVariable);

        money.setText(Integer.toString(game.getMoney()));
        fuel.setText(Integer.toString(game.getResources().getFuel()));
        food.setText(Integer.toString(game.getResources().getFood()));
        aluminum.setText(Integer.toString(game.getResources().getAluminum()));
        sprEngines.setText(Integer.toString(game.getResources().getSpareEngines()));
        sprWings.setText(Integer.toString(game.getResources().getSpareWings()));
        sprLivBay.setText(Integer.toString(game.getResources().getSpareLivingBays()));
        compoundName.setText(game.getRace().getStrength());
        compound.setText((Integer.toString(game.getResources().getCompound())));
        weakness.setText(game.getRace().getWeakness());

        /* Setting fields for names and their info */
        ArrayList<TextView> names = new ArrayList<TextView>();
        ArrayList<TextView> nameInfo = new ArrayList<TextView>();
        TextView capt = (TextView) findViewById(R.id.captainName);
        names.add(capt);
        TextView person1 = (TextView) findViewById(R.id.peopleName1);
        names.add(person1);
        TextView person2 = (TextView) findViewById(R.id.peopleName2);
        names.add(person2);
        TextView person3 = (TextView) findViewById(R.id.peopleName3);
        names.add(person3);
        TextView person4 = (TextView) findViewById(R.id.peopleName4);
        names.add(person4);
        TextView captInfo = (TextView) findViewById(R.id.captainInfo);
        nameInfo.add(captInfo);
        TextView person1Info = (TextView) findViewById(R.id.peopleNameInfo1);
        nameInfo.add(person1Info);
        TextView person2Info = (TextView) findViewById(R.id.peopleNameInfo2);
        nameInfo.add(person2Info);
        TextView person3Info = (TextView) findViewById(R.id.peopleNameInfo3);
        nameInfo.add(person3Info);
        TextView person4Info = (TextView) findViewById(R.id.peopleNameInfo4);
        nameInfo.add(person4Info);

        Log.d("Gameinfo", "Size of games people = " + game.getPeople().size());
        int i = 0;
        for(; i < game.getPeople().size(); i++){
            names.get(i).setText(game.getPeople().get(i).getName());
            nameInfo.get(i).setText(game.getPeople().get(i).getAge() + ", " + game.getPeople().get(i).getCondition()+"%");
            Log.d("Gameinfo", game.getPeople().get(i).getName() + " added to list");
        }

        System.out.println("crew names: "+game.crewNames.size());
        for(String s: game.crewNames) {
            System.out.println(s + "\n");
        }

        /* Will only work if names are unique, I think... maybe, depends on remove function */
        /* deadNames will contain all the names of dead crew members */
        ArrayList<String> deadNames = new ArrayList<String>(game.crewNames);
        Log.d("gi", "i is " + i);
        for(int j = 0; j < game.getPeople().size(); j++){
            Log.d("gi", "j = " + j);
            for(String s : deadNames){
                if(game.getPeople().get(j).getName().equals(s)){
                    deadNames.remove(s);
                    Log.d("Gameinfo", s + " is alive!");
                    break;
                }
            }
        }
        Log.d("GameInfo", "Size of deadNames = " + deadNames.size());
        // i holds the current position in names without a crew member name
        System.out.println("Dead Names");
        for(String s: deadNames) {
            System.out.println(s + "\n");
        }
        for(int j = 0; j < deadNames.size(); j++){
            names.get(i+j).setText(deadNames.get(j));
            nameInfo.get(i+j).setText("Dead");
        }
    }

    //Sets Layout to the Instructions menu
    public void loadInstructions(View view) {
        /*setContentView(R.layout.instruction_screen);
        Game testGame = new Game();
        testGame.setMoney(1000000);
        testGame.getResources().setCompound(10000);
        testGame.getResources().setAluminum(10000);
        testGame.getResources().setFood(10000);
        testGame.getResources().setFuel(10000);
        testGame.getResources().addSpare(new Part("engine",100));
        testGame.getResources().addSpare(new Part("wing",100));
        testGame.getResources().addSpare(new Part("livingBay", 100));
        testGame.addCrew("cap",true);
        crewNames.add("cap");
        testGame.addCrew("1",false);
        crewNames.add("1");
        testGame.addCrew("2",false);
        crewNames.add("2");
        testGame.addCrew("3",false);
        crewNames.add("3");
        testGame.addCrew("4",false);
        crewNames.add("4");
        game.crewNames = new ArrayList<String>(crewNames);


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
        b.putStringArrayList("Crew", crewNames);
        intent.putExtras(b);
        startActivity(intent);
        finish();*/
      setContentView(R.layout.instruction_screen);
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

        //checking if player has money to buy all the resources
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

            fuel.setText("");
            food.setText("");
            engine.setText("");
            aluminum.setText("");
            wings.setText("");
            livingBays.setText("");
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
 /*       fuel.setText("");
        food.setText("");
        engine.setText("");
        aluminum.setText("");
        wings.setText("");
        livingBays.setText("");*/
        TextView moneyText = (TextView) findViewById(R.id.moneyVariable);
        moneyText.setText(Integer.toString(startingMoney));
    }
    // Called when the user hits the send to space button, stores the starting resources
    public void headToSpace(View view) {
        //the game object will be updated with resources bought
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

        TextView bay = (TextView) findViewById(R.id.livingBayQuantity);
        int bayint = Integer.parseInt(bay.getText().toString());
        game.sellParts(bayint, "livingBay");

        //Ends MainActivity and starts the GameScreenActivity
        //Passes game object to new activity
        Intent intent = new Intent(this, GameScreenActivity.class);
        Bundle b = new Bundle();
        game.setSchemaNull();
        b.putSerializable("Game", game);
        b.putStringArrayList("Crew", crewNames);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    //Used in autogenerating the names for the player's crew
    public void autoGenerate(View v){
        ArrayList<String> alienNames = new ArrayList<String>();
        alienNames.add("Glemb");
        alienNames.add("Welldoss");
        alienNames.add("Entayta");
        alienNames.add("Brownfox");
        alienNames.add("Tarkal");
        alienNames.add("Mustafav");
        alienNames.add("Sarah Palin");
        alienNames.add("Obama");
        alienNames.add("Nekav");
        alienNames.add("J'Satun");
        alienNames.add("Nihan");
        alienNames.add("Koocrah");
        alienNames.add("Javaris");
        alienNames.add("Xwing@Aliciousness");
        alienNames.add("Beezer");
        alienNames.add("Eeeeeeeee");
        alienNames.add("J'Pluto");
        alienNames.add("Torque");
        alienNames.add("Probincrux");
        alienNames.add("Jackmerius");
        alienNames.add("Tyroil");
        alienNames.add("Jimmy");
        alienNames.add("Jimmy Jr.");
        alienNames.add("Jimmy Sr.");
        alienNames.add("Jimmy III");
        alienNames.add("Jimmy IV");
        alienNames.add("Hinglecringle");
        alienNames.add("Waxon");
        alienNames.add("SmoochieWallis");
        alienNames.add("Peele");
        alienNames.add("Quatro");
        alienNames.add("Ozamataz");
        alienNames.add("Washingbeard");
        alienNames.add("T.G.I.F");
        alienNames.add("Lewith");
        alienNames.add("Mousecop");
        alienNames.add("Dan Smith");
        alienNames.add("Mergatroid");
        alienNames.add("Quackadilly");
        alienNames.add("Trisket");
        alienNames.add("Fartrell");
        alienNames.add("Jammie");
        alienNames.add("Fresca");
        alienNames.add("Yeast");
        alienNames.add("Toyata");
        alienNames.add("Dahistorius");
        alienNames.add("Lamystorius");
        alienNames.add("AyeAyeRon");
        alienNames.add("JayQuelIn");

        ArrayList<EditText> fields = new ArrayList<EditText>();

        EditText capt = (EditText) findViewById(R.id.editCapField);
        fields.add(capt);
        EditText person1 = (EditText) findViewById(R.id.firstOtherNameField);
        fields.add(person1);
        EditText person2 = (EditText) findViewById(R.id.secOtherNameField);
        fields.add(person2);
        EditText person3 = (EditText) findViewById(R.id.thirdOtherNameField);
        fields.add(person3);
        EditText person4 = (EditText) findViewById(R.id.fourOtherNameField);
        fields.add(person4);

        Random rand = new Random();
        int number = 0;
        String name = "";
        number = rand.nextInt(alienNames.size());

        Log.d("Main", "Number = " + number);

        for(int i = 0; i < 5; i++){
            name = alienNames.get(number);
            alienNames.remove(number);
            fields.get(i).setText(name, TextView.BufferType.EDITABLE);
            number = rand.nextInt(alienNames.size());
        }
    }

    //Used for a button to start the GameScreenActivity from the Load Game
    public void gameScreen(View view) {
        //Passing game object to new instance
        Intent intent = new Intent(this, GameScreenActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("Game", game);
        b.putStringArrayList("Crew", game.crewNames);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //Checks if the current screen is not the activity_main.xml layout
        if (!currentView.equals("main")) {
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
            AlertDialog dialog = builder.show();
            TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            dialog.show();
        }
        else{
            super.onBackPressed();
        }
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
