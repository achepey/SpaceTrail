package team19.spacetrail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import javagame.Game;
import javagame.Person;


public class ExitActivity extends Activity {

    private MediaPlayer mp;
    private boolean won = false;
    private Game game;
    private int gameScore = 0;
    //Allows for clean exit of the game or has user decide to play again or not.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_exit);
        //Determines which activity called this activity, in order to respond appropriately
        String caller = this.getIntent().getExtras().getString("activity");

        /* Grabs copy of game for scoring */
        game = (Game) getIntent().getExtras().getSerializable("Game");
        TextView exitMessage = (TextView) findViewById(R.id.exitMessage);
        if(caller.equals("Loser")){
            exitMessage.setText("Sorry, You Lose!");
        }
        else {
            exitMessage.setText("Congratulations! You have visited all the planets!");
            //getting audio file from raw folder
            mp = MediaPlayer.create(getApplicationContext(), R.raw.you_won);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.seekTo(2600); //2.6 seconds in
                    try {
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                }
            });
            mp.setLooping(true);
            mp.start();
            won = true;
        }

        //sets the score on screen to the player's score
        TextView score = (TextView) findViewById(R.id.score);
        score.setText("" + generateScore());
    }

    /* Helper Methods */
    //Cleanly exists game
    public void exitGame(View view) {
        finish();
        System.exit(0);
    }

    //If user chooses to restart, takes them back to main activity
    public void startOver(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        if(won){
            mp.stop();
        }
        startActivity(intent);
    }

    public int generateScore() {
        //Generate score based upon current amounts of resources left.

        //Adds percent of remaining living crew members
        for(Person p : game.getPeople()){
            gameScore+=p.getCondition();
        }

        //Rest of score TBD

        return gameScore;
    }

    @Override
    public void onBackPressed(){
        finish();
        System.exit(0);
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
