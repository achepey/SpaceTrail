package team19.spacetrail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import javagame.Game;


public class BarterActivity extends Activity {

    private final int FOOD_COST = 4;
    private final int FUEL_COST = 5;
    private final int ENGINE_COST = 100;
    private final int ALUMINUM_COST = 10;
    private final int WING_COST = 100;
    private final int LIVING_BAY_COST = 100;

    private Game game;
    private Switch buySellSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barter);

        game = (Game) getIntent().getExtras().getSerializable("Game");

        buySellSwitch = (Switch) findViewById(R.id.buySellSwitch);
        buySellSwitch.setTextOff("Buy");
        buySellSwitch.setTextOn("Sell");

        TextView fuelText = (TextView) findViewById(R.id.fuelQuantity);
        TextView foodText = (TextView) findViewById(R.id.foodQuantity);
        TextView engineText = (TextView) findViewById(R.id.engineQuantity);
        TextView aluminumText = (TextView) findViewById(R.id.aluminumQuantity);
        TextView wingsText = (TextView) findViewById(R.id.wingQuantity);
        TextView livingBayText = (TextView) findViewById(R.id.livingBayQuantity);

        TextView moneyText = (TextView) findViewById(R.id.moneyVariable);
        moneyText.setText(Integer.toString(game.getMoney()));

        fuelText.setText("" + game.getResources().getFuel());
        foodText.setText("" + game.getResources().getFood());
        engineText.setText("" + game.getResources().getSpareEngines());
        aluminumText.setText("" + game.getResources().getAluminum());
        wingsText.setText("" + game.getResources().getSpareWings());
        livingBayText.setText("" + game.getResources().getSpareLivingBays());
    }

    public void updateFields(View v){
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

        TextView moneyText = (TextView) findViewById(R.id.moneyVariable);

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

        /* adds default values for resource changes */
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

        //Checked means the user is selling, not checked is buying
        //if(buying)
        if(!buySellSwitch.isChecked()) {

            //checking if player has money to buy all the resources
            if (Integer.parseInt(fuelAmt) * FUEL_COST + Integer.parseInt(foodAmt) * FOOD_COST + Integer.parseInt(engAmt) * ENGINE_COST + Integer.parseInt(almAmt) * ALUMINUM_COST + Integer.parseInt(wingAmt) * WING_COST + Integer.parseInt(lbAmt) * LIVING_BAY_COST - game.getMoney() < 0) {
                fuelText.setText(Integer.toString(Integer.parseInt(fuelText.getText().toString()) + Integer.parseInt(fuelAmt)));
                foodText.setText(Integer.toString(Integer.parseInt(foodText.getText().toString()) + Integer.parseInt(foodAmt)));
                engineText.setText(Integer.toString(Integer.parseInt(engineText.getText().toString()) + Integer.parseInt(engAmt)));
                aluminumText.setText(Integer.toString(Integer.parseInt(aluminumText.getText().toString()) + Integer.parseInt(almAmt)));
                wingsText.setText(Integer.toString(Integer.parseInt(wingsText.getText().toString()) + Integer.parseInt(wingAmt)));
                livingBayText.setText(Integer.toString(Integer.parseInt(livingBayText.getText().toString()) + Integer.parseInt(lbAmt)));

                int money = game.getMoney();
                money -= Integer.parseInt(fuelAmt) * FUEL_COST + Integer.parseInt(foodAmt) * FOOD_COST + Integer.parseInt(engAmt) * ENGINE_COST + Integer.parseInt(almAmt) * ALUMINUM_COST + Integer.parseInt(wingAmt) * WING_COST + Integer.parseInt(lbAmt) * LIVING_BAY_COST;
                game.setMoney(money);

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
            } else {
                //Creates pop-up letting user know the resources were purchased correctly
                Context context = getApplicationContext();
                CharSequence popup = "Not Enough Funds!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.show();
            }
        }
        //Selling
        else{
            if(Integer.parseInt(fuelAmt) <= game.getResources().getFuel() && Integer.parseInt(foodAmt) <= game.getResources().getFood() && Integer.parseInt(almAmt) <= game.getResources().getAluminum() && Integer.parseInt(engAmt) <= game.getResources().getSpareEngines() && Integer.parseInt(wingAmt) <= game.getResources().getSpareWings() && Integer.parseInt(lbAmt) <= game.getResources().getSpareLivingBays()){
                fuelText.setText(Integer.toString(Integer.parseInt(fuelText.getText().toString()) - Integer.parseInt(fuelAmt)));
                foodText.setText(Integer.toString(Integer.parseInt(foodText.getText().toString()) - Integer.parseInt(foodAmt)));
                engineText.setText(Integer.toString(Integer.parseInt(engineText.getText().toString()) - Integer.parseInt(engAmt)));
                aluminumText.setText(Integer.toString(Integer.parseInt(aluminumText.getText().toString()) - Integer.parseInt(almAmt)));
                wingsText.setText(Integer.toString(Integer.parseInt(wingsText.getText().toString()) - Integer.parseInt(wingAmt)));
                livingBayText.setText(Integer.toString(Integer.parseInt(livingBayText.getText().toString()) - Integer.parseInt(lbAmt)));

                int money = game.getMoney();
                money += Integer.parseInt(fuelAmt) * FUEL_COST + Integer.parseInt(foodAmt) * FOOD_COST + Integer.parseInt(engAmt) * ENGINE_COST + Integer.parseInt(almAmt) * ALUMINUM_COST + Integer.parseInt(wingAmt) * WING_COST + Integer.parseInt(lbAmt) * LIVING_BAY_COST;
                game.setMoney(money);

                //Creates pop-up letting user know the resources were purchased correctly
                Context context = getApplicationContext();
                CharSequence popup = "Money Received!";
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
                CharSequence popup = "Cannot Sell That Much!";
                Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.show();
            }
        }
        moneyText.setText(Integer.toString(game.getMoney()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_barter, menu);
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
