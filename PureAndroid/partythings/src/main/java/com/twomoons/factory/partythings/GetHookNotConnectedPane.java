package com.twomoons.factory.partythings;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Joshua on 5/20/2015.
 */
public class GetHookNotConnectedPane implements IMsgHandler{
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghNotConnectedPane;
    private final TextView ghNotConnectedText;
    private final Button ghNotConnectedButton;

    public GetHookNotConnectedPane(MainActivity ctx, Hub communicationHub){
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghNotConnectedPane = ctx.getViewById(R.id.notConnectedPane);
        this.ghNotConnectedText = (TextView) ctx.getViewById(R.id.notConnectedText);
        this.ghNotConnectedButton = (Button) ctx.getViewById(R.id.connectedButton);
        setupListener();
        handleConnected();
    }

    private void handleConnected() {
        this.ghNotConnectedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hidePane();
                hub.SendMessage(CommunicatorEvents.NotConnectedExit, "");
            }
        });
    }

    private void setupListener() {
        hub.RegisterMsgr(this,CommunicatorEvents.NotConnectedEnter);
        hub.RegisterMsgr(this, CommunicatorEvents.EnterPlayerNameEnter);
        hub.RegisterMsgr(this, CommunicatorEvents.EnterGameNameEnter);
    }

    public void hidePane(){
        ghNotConnectedPane.setVisibility(View.GONE);
    }

    public void showPane() {
        ghNotConnectedPane.setVisibility(View.VISIBLE);
    }


    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.NotConnectedEnter){
            showPane();
        } else if(eventType == CommunicatorEvents.EnterPlayerNameEnter || eventType == CommunicatorEvents.EnterGameNameEnter){
            hidePane();
        }
    }

}
