package com.twomoons.factory.partythings;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joshua on 5/20/2015.
 */
public class GetHookPickResponsePane extends ListActivity implements IMsgHandler {
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghPickResponsePane;
    private final TextView ghPickResponseText;
    private final ListView ghPickResponseList;
    private String[] ghSplit;
    private ArrayAdapter<String> ghListAdapter;
    private ArrayList<String> ghList = new ArrayList<String>();

    public GetHookPickResponsePane(MainActivity ctx, Hub communicationHub){
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghPickResponsePane = ctx.getViewById(R.id.pickResponsePane);
        this.ghPickResponseText = (TextView) ctx.getViewById(R.id.pickResponseText);
        this.ghPickResponseList = (ListView) ctx.getViewById(R.id.pickResponseList);
        setupListener();
    }

    private void setupListener() {
        hub.RegisterMsgr(this, CommunicatorEvents.PickResponseEnter);
    }

    public void hidePane(){ ghPickResponsePane.setVisibility(View.GONE); }

    public void showPane() {
        ghPickResponsePane.setVisibility(View.VISIBLE);
    }

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.PickResponseEnter){
            addElements(message);
            showPane();
        }
    }

    private void addElements(String message) {
        ghSplit = message.split(":::");
        ItemSelectionAdapter adapter = new ItemSelectionAdapter(this, R.layout.item_selection_button_item); //This is where I am having problems
        //SelectionItem i1 = new SelectionItem();
        //adapter.add(i1);
    }
}
