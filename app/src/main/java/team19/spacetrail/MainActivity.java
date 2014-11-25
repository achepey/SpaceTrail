package team19.spacetrail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import javagame.*;


public class MainActivity extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        game = new Game();
    }

    /* Helper Methods */
    //Sets Layout to the new game menu
    public void newGameMenu(View view) {
        setContentView(R.layout.new_game_menu);
    }

    //Sets Layout to the buying starting resources menu
    public void buyResourceMenu(View view) {
        setContentView(R.layout.resource_menu);
    }

    //Sets Layout to the load game menu
    public void loadGame(View view) {
        setContentView(R.layout.activity_load_game);
    }

    //Sets Layout to the Instructions menu
    public void loadInstructions(View view) {
        setContentView(R.layout.instruction_screen);
    }

    //Does the function of buying the original resources.
    public void buyStartingResources(View view) {
        //This is where we will change the xml file to reflect the changes in resources.
        Button buy = (Button) findViewById(R.id.buyButton);
        EditText fuel = (EditText) findViewById(R.id.fuelTextField);
        EditText food = (EditText) findViewById(R.id.foodTextField);
        EditText engine = (EditText) findViewById(R.id.enginePartTextField);
        EditText aluminum = (EditText) findViewById(R.id.aluminumPartsTextField);
        EditText wings = (EditText) findViewById(R.id.wingTextField);

        //Creates pop-up letting user know the resources were purchased correctly
        Context context = getApplicationContext();
        CharSequence popup = "Resources Acquired!";
        Toast toast = Toast.makeText(context, popup, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.show();

        //Resets the fields to be blank values
        fuel.setText("");
        food.setText("");
        engine.setText("");
        aluminum.setText("");
        wings.setText("");
    }
    // Called when the user hits the send to space button, stores the starting resources in data file
    public void headToSpace(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        TextView fuel = (TextView) findViewById(R.id.fuelQuantity);
        int fuelint = Integer.parseInt(fuel.getText().toString());
        game.getResources().setFuel(fuelint);

        TextView food = (TextView) findViewById(R.id.foodQuantity);
        int foodint = Integer.parseInt(fuel.getText().toString());
        game.getResources().setFood(foodint);

        TextView engine = (TextView) findViewById(R.id.engineQuantity);
        int engineint = Integer.parseInt(fuel.getText().toString());
        for(int i = 0; i < engineint; ++i) {
            game.getResources().addSpare(new Part("Engine", 100));
        }

        TextView alum = (TextView) findViewById(R.id.alumQuantity);
        int alumint = Integer.parseInt(fuel.getText().toString());
        game.getResources().setAluminum(alumint);

        TextView wings = (TextView) findViewById(R.id.wingQuantity);
        int wingsint = Integer.parseInt(fuel.getText().toString());
        for(int i = 0; i < wingsint; ++i) {
            game.getResources().addSpare(new Part("Wing", 100));
        }

        //Ends MainActivity and starts the GameScreenActivity
        startActivity(intent);
        finish();
    }

    //Used for a button to start the GameScreenActivity
    public void gameScreen(View view) {
        Intent intent = new Intent(this, GameScreenActivity.class);
        startActivity(intent);
        finish();
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
