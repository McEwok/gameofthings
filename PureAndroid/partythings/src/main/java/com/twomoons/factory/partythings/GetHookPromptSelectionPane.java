package com.twomoons.factory.partythings;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Joshua on 5/20/2015.
 */
public class GetHookPromptSelectionPane implements IMsgHandler {
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghPromptSelectionPane;
    private final TextView ghVoteForPrompt;
    private final Button ghPrompt_Button_1;
    private final Button ghPrompt_Button_2;
    private final Button ghPrompt_Button_3;
    private String sSelection;
    private String[] sSplit;

    public GetHookPromptSelectionPane(MainActivity ctx, Hub communicationHub){
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghPromptSelectionPane = ctx.getViewById(R.id.promptSelectionPane);
        this.ghVoteForPrompt = (TextView) ctx.getViewById(R.id.readyPromptText);
        this.ghPrompt_Button_1 = (Button) ctx.getViewById(R.id.prompt_button_1);
        this.ghPrompt_Button_2 = (Button) ctx.getViewById(R.id.prompt_button_2);
        this.ghPrompt_Button_3 = (Button) ctx.getViewById(R.id.prompt_button_3);
        setupListener();
    }

    private void setupListener() { hub.RegisterMsgr(this, CommunicatorEvents.PromptSelectionEnter); }

    public void hidePane(){ ghPromptSelectionPane.setVisibility(View.GONE); }

    public void showPane() { ghPromptSelectionPane.setVisibility(View.VISIBLE); }

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.PromptSelectionEnter){
            setupButtons(message);
            showPane();
            setupButtonListeners();
        }
    }

    private void setupButtons(String message){
        sSplit = message.split(":::");
        ghPrompt_Button_1.setText(sSplit[0]);
        if(sSplit.length > 1) {
            ghPrompt_Button_2.setText(sSplit[1]);
            if(sSplit.length > 2) {
                ghPrompt_Button_3.setText(sSplit[2]);
            }
        }
    }

    private void setupButtonListeners(){
        this.ghPrompt_Button_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sSelection = sSplit[0];
                hub.SendMessage(CommunicatorEvents.PromptSelectionExit, "sSelection");
                hidePane();
            }
        });
        if(sSplit.length > 1) {
            this.ghPrompt_Button_2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sSelection = sSplit[1];
                    hub.SendMessage(CommunicatorEvents.PromptSelectionExit, "sSelection");
                    hidePane();
                }
            });
            if(sSplit.length >2) {
                this.ghPrompt_Button_3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        sSelection = sSplit[2];
                        hub.SendMessage(CommunicatorEvents.PromptSelectionExit, "sSelection");
                        hidePane();
                    }
                });
            }
        }
    }
}
