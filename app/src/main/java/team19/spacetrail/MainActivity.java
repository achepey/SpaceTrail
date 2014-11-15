package team19.spacetrail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    public void newGameMenu(View view) {
        setContentView(R.layout.new_game_menu);
    }

    public void buyResourceMenu(View view) {
        setContentView(R.layout.resource_menu);
    }

    public void loadGame(View view) {
        setContentView(R.layout.activity_load_game);
    }

    public void buyResources() {
        //This is where we will change the xml file to reflect the changes in resources.
        Button buy = (Button) findViewById(R.id.buyButton);
        EditText fuel = (EditText) findViewById(R.id.fuelTextField);
        EditText food = (EditText) findViewById(R.id.foodTextField);
        EditText engine = (EditText) findViewById(R.id.enginePartTextField);
        EditText aluminum = (EditText) findViewById(R.id.aluminumPartsTextField);
        EditText wings = (EditText) findViewById(R.id.wingTextField);
    }
    /* Called when the user hits the send to space button */
    public void headToSpace(View view) {
        Intent intent = new Intent(this, SpaceActivity.class);
        TextView fuel = (TextView) findViewById(R.id.fuelQuantity);
        int fuelint = Integer.parseInt(fuel.getText().toString());

        TextView food = (TextView) findViewById(R.id.foodQuantity);
        int foodint = Integer.parseInt(fuel.getText().toString());

        TextView engine = (TextView) findViewById(R.id.engineQuantity);
        int engineint = Integer.parseInt(fuel.getText().toString());

        TextView alum = (TextView) findViewById(R.id.alumQuantity);
        int alumint = Integer.parseInt(fuel.getText().toString());

        TextView wings = (TextView) findViewById(R.id.wingQuantity);
        int wingsint = Integer.parseInt(fuel.getText().toString());

        startActivity(intent);
    }

    public void gameScreen(View view) { setContentView(R.layout.activity_game_screen); }

    public void gameInfoScreen(View view) { setContentView(R.layout.activity_game_info); }

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
