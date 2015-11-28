package com.twomoons.factory.partythings;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Joshua on 5/20/2015.
 */
public class GetHookResultsPane implements IMsgHandler{
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghResultsPane;
    private final TextView ghChosenPlayerIntroText;
    private final TextView ghChosenPlayerText;
    private final TextView ghChosenResponseIntroText;
    private final TextView ghChosenResponseText;
    private final TextView ghSuccessRateIntroText;
    private final TextView ghSuccessText;
    private final TextView ghScoreIntroText;
    private final TextView ghPointsText;
    private final TextView ghPointsOutroText;

    public GetHookResultsPane(MainActivity ctx, Hub communicationHub){
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghResultsPane = ctx.getViewById(R.id.resultsPane);
        this.ghChosenPlayerIntroText = (TextView) ctx.getViewById(R.id.chosenPlayerIntroText);
        this.ghChosenPlayerText = (TextView) ctx.getViewById(R.id.chosenPlayerText);
        this.ghChosenResponseIntroText = (TextView) ctx.getViewById(R.id.chosenResponseIntroText);
        this.ghChosenResponseText = (TextView) ctx.getViewById(R.id.chosenResponseText);
        this.ghSuccessRateIntroText = (TextView) ctx.getViewById(R.id.successRateIntroText);
        this.ghSuccessText = (TextView) ctx.getViewById(R.id.successText);
        this.ghScoreIntroText = (TextView) ctx.getViewById(R.id.scoreIntroText);
        this.ghPointsText = (TextView) ctx.getViewById(R.id.pointsText);
        this.ghPointsOutroText = (TextView) ctx.getViewById(R.id.pointsOutroText);
        setupListener();
    }

    private void setupListener() {
        hub.RegisterMsgr(this, CommunicatorEvents.ResultsEnter);
    }

    public void hidePane(){
        ghResultsPane.setVisibility(View.GONE);
    }

    public void showPane() {
        ghResultsPane.setVisibility(View.VISIBLE);
    }

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.ResultsEnter){
            populateResults(message);
            showPane();
        }
    }

    private void populateResults(String message) {
        String[] messageSplit = message.split(":::");
        ghChosenPlayerText.setText(messageSplit[0]);
        ghChosenResponseText.setText(messageSplit[1]);
        ghSuccessText.setText(messageSplit[2]);
        ghPointsText.setText(messageSplit[3]);
    }
}