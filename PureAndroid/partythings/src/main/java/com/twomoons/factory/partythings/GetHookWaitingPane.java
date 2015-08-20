package com.twomoons.factory.partythings;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Joshua on 5/20/2015.
 */
public class GetHookWaitingPane implements IMsgHandler {
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghWaitingPane;
    private final TextView ghWaitingText;

    public GetHookWaitingPane(MainActivity ctx,Hub communicationHub){
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghWaitingPane = ctx.getViewById(R.id.waitingPane);
        this.ghWaitingText = (TextView) ctx.getViewById(R.id.waitingText);
        setupListener();
    }

    private void closeWaiting(String message) {
        hidePane();
        hub.SendMessage(CommunicatorEvents.WaitingExit, message);
    }

    private void setupListener() {
        hub.RegisterMsgr(this,CommunicatorEvents.WaitingEnter);
    }

    public void hidePane(){
        ghWaitingPane.setVisibility(View.GONE);
    }

    public void showPane() {
        ghWaitingPane.setVisibility(View.VISIBLE);
    }

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.WaitingEnter){
            showPane();
            closeWaiting(message);
        }
    }
}
