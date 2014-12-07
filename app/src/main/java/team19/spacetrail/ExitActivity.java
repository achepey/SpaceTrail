package team19.spacetrail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class ExitActivity extends Activity {

    //Allows for clean exit of the game or has user decide to play again or not.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_exit);
        //Determines which activity called this activity, in order to respond appropriately
        String caller = this.getIntent().getExtras().getString("activity");
        TextView exitMessage = (TextView) findViewById(R.id.exitMessage);
        if(caller.equals("Loser")){
            exitMessage.setText("Sorry, You Lose!");
        }
        if(caller.equals("winner")) {
            exitMessage.setText("Congratulations! You have visited all the planets!");
        }
    }

    /* Helper Methods */
    //Cleanly exists game
    public void exitGame(View view) {
        finish();
    }

    //If user chooses to restart, takes them back to main activity
    public void startOver(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    /* Override Methods */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exit, menu);
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
