package com.twomoons.factory.partythings;

import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Joshua on 5/20/2015.
 */
public class GetHookGameNamePane implements IMsgHandler{
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghGameNamePane;
    private final TextView ghEnterGameNameText;
    private final EditText ghGameEditText;
    private final Button ghEnterGameNameButton;
    private final EditText ghPlayerEditText;
    private final View ghEnterGamePane;
    private Boolean isPlayerNameOnly;

    public GetHookGameNamePane(MainActivity ctx, Hub communicationHub){
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghGameNamePane = ctx.getViewById(R.id.gameNamePane);
        this.ghEnterGamePane = ctx.getViewById(R.id.enterGamePane);
        this.ghEnterGameNameText = (TextView) ctx.getViewById(R.id.enterGameNameText);
        this.ghGameEditText = (EditText) ctx.getViewById(R.id.editText);
        this.ghPlayerEditText = (EditText) ctx.getViewById(R.id.editText2);
        this.ghEnterGameNameButton = (Button) ctx.getViewById(R.id.createGameButton);
        setupListener();
        setupSubmitButton();
    }

    private void setupSubmitButton() {
        this.ghEnterGameNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hidePane();
                String playerName = ghPlayerEditText.getText().toString();
                if(isPlayerNameOnly) {
                    hub.SendMessage(CommunicatorEvents.EnterPlayerNameExit, playerName);
                } else {
                    String gameName = ghGameEditText.getText().toString();
                    hub.SendMessage(CommunicatorEvents.EnterGameNameExit, gameName + ":::" + playerName);
                }
            }
        });
    }

    private void setupListener() {
        //This is a comment
        //This is another comment
        hub.RegisterMsgr(this, CommunicatorEvents.EnterGameNameEnter);
        hub.RegisterMsgr(this, CommunicatorEvents.EnterPlayerNameEnter);
        String str = "str";
    }

    public void hidePane(){ ghEnterGamePane.setVisibility(View.GONE); }

    public void showPane(){ ghEnterGamePane.setVisibility(View.VISIBLE);}

    public void showGameNamePane(){ ghGameNamePane.setVisibility(View.VISIBLE);}

    public void hideGameNamePane(){ ghGameNamePane.setVisibility(View.GONE);}

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.EnterGameNameEnter) {
            showPane();
            showGameNamePane();
            isPlayerNameOnly = false;
        } else if (eventType == CommunicatorEvents.EnterPlayerNameEnter) {
            showPane();
            showGameNamePane();
            hideGameNamePane();
            isPlayerNameOnly = true;
        }
    }
}
