package com.twomoons.factory.partythings;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Joshua on 5/21/2015.
 */
public class GetHookEnterResponsePane implements IMsgHandler{
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghEnterResponsePane;
    private final TextView ghEnterResponseText;
    private final TextView ghEnterResponsePromptText;
    private final EditText ghEnterResponseEdit;
    private final Button ghResponse_Button;


    public GetHookEnterResponsePane(MainActivity ctx, Hub communicationHub) {
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghEnterResponsePane = ctx.getViewById(R.id.enterResponsePane);
        this.ghEnterResponsePromptText = (TextView) ctx.getViewById(R.id.enterResponsePromptText);
        this.ghEnterResponseText = (TextView) ctx.getViewById(R.id.enterResponseText);
        this.ghEnterResponseEdit = (EditText) ctx.getViewById(R.id.enterResponseEdit);
        this.ghResponse_Button = (Button) ctx.getViewById(R.id.response_button);
        setupListener();
        setupResponseButton();
    }

    private void setupResponseButton() {
        this.ghResponse_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hidePane();
                String response = ghEnterResponseEdit.getText().toString();
                hub.SendMessage(CommunicatorEvents.EnterResponseExit, response);
            }
        });
    }

    private void setupListener() {
        hub.RegisterMsgr(this,CommunicatorEvents.EnterResponseEnter);
    }

    public void hidePane(){
        ghEnterResponsePane.setVisibility(View.GONE);
    }

    public void showPane() {
        ghEnterResponsePane.setVisibility(View.VISIBLE);
    }

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.EnterResponseEnter) {
            ghEnterResponsePromptText.setText(message);
            showPane();
        }
    }
}
