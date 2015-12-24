package com.twomoons.factory.partythings;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twomoons.factory.partythings.Mocks.CastServiceMock;


public class MainActivity extends ActionBarActivity implements IView, IMsgHandler {

    private TextView mText;
    private ICommunicator mCommunicator;
    private Hub communicationHub;

    public MainActivity() {
        mCommunicator = new CastService();
    }

    //Used by Unit Test only
    public MainActivity(ICommunicator communicator) {
        if(communicator == null) {
            mCommunicator = new CastService();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final Button button = (Button) findViewById(R.id.click_me_button);
//        final RelativeLayout lay1 = (RelativeLayout) findViewById(R.id.text1Layout);
//        final RelativeLayout lay2 = (RelativeLayout) findViewById(R.id.text2Layout);
//
//        mText = (TextView) findViewById(R.id.displayText);
//        button.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                int v1 = lay1.getVisibility();
//                int v2 = lay2.getVisibility();
//                lay1.setVisibility(v2);
//                lay2.setVisibility(v1);
//            }
//        });

         communicationHub = new Hub();

        mCommunicator.Initialize(this, communicationHub);

        communicationHub.RegisterMsgr(this, CommunicatorEvents.EnterGameNameEnter);

        setupPaneHooks();
    }

    public View getViewById(int id){return findViewById(id);}

    private void setupPaneHooks() {
        GetHookEnterResponsePane enterResponsePane = new GetHookEnterResponsePane(this,communicationHub);
        GetHookEnterGamePane enterGamePane = new GetHookEnterGamePane(this,communicationHub);
        GetHookNotConnectedPane notConnectedPane = new GetHookNotConnectedPane(this,communicationHub);
        GetHookPickResponsePane pickResponsePane = new GetHookPickResponsePane(this,communicationHub);
        GetHookPickPlayerPane pickPlayerPane = new GetHookPickPlayerPane(this,communicationHub);
        GetHookPromptSelectionPane promptSelectionPane = new GetHookPromptSelectionPane(this,communicationHub);
        GetHookGameNamePane gameNamePane = new GetHookGameNamePane(this,communicationHub);
        GetHookWaitingPane waitingPane = new GetHookWaitingPane(this,communicationHub);
        GetHookReadyPane readyPane = new GetHookReadyPane(this,communicationHub);
        GetHookResultsPane resultsPane = new GetHookResultsPane(this,communicationHub);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mCommunicator.InitializeMenu(menu, R.id.media_route_menu_item);

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

    @Override
    public void onResume(){

        super.onResume();
        mCommunicator.OnResume();
    }

    @Override
    public void onPause(){

        super.onPause();
    }

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {

    }
}
