package com.twomoons.factory.partythings;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Joshua on 5/20/2015.
 */
public class GetHookPickPlayerPane implements IMsgHandler{
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghPickPlayerPane;
    private final TextView ghPickPlayerText;
    private final TextView ghpickPlayerChosenResponseText;
    private final ListView ghPickPlayerList;
    private List<String> _Items;
    private MyListAdapter _ListAdapter;

    public GetHookPickPlayerPane(MainActivity ctx, Hub communicationHub){
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghPickPlayerPane = ctx.getViewById(R.id.pickPlayerPane);
        this.ghPickPlayerText = (TextView) ctx.getViewById(R.id.pickPlayerText);
        this.ghpickPlayerChosenResponseText = (TextView) ctx.getViewById(R.id.pickPlayerChosenResponseText);
        this.ghPickPlayerList = (ListView) ctx.getViewById(R.id.pickPlayerList);
        _Items = new ArrayList<String>();
        _ListAdapter = new MyListAdapter(this.ctx, _Items);
        ghPickPlayerList.setAdapter(_ListAdapter);
        setupListener();
        handleOnClick();
    }

    private void setupListener() { hub.RegisterMsgr(this, CommunicatorEvents.PickPlayerEnter); }

    public void hidePane(){ ghPickPlayerPane.setVisibility(View.GONE); }

    public void showPane() { ghPickPlayerPane.setVisibility(View.VISIBLE); }

    private void addElements(String message, List<String> _Items, MyListAdapter _ListAdapter) {
        List<String> responseList = Arrays.asList(message.split(":::"));
        ghpickPlayerChosenResponseText.setText(responseList.get(0));

        for(int i = 1; i < responseList.size(); i++) {
            _Items.add(responseList.get(i));
            _ListAdapter.notifyDataSetChanged();
        }
    }

    private void handleOnClick() {
        ghPickPlayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hidePane();
                String value = (String) ghPickPlayerList.getItemAtPosition(i);
                //Toast.makeText(ctx, value, Toast.LENGTH_LONG).show();
                hub.SendMessage(CommunicatorEvents.PickPlayerExit, value);
            }
        });
    }

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.PickPlayerEnter) {
            addElements(message, _Items, _ListAdapter);
            showPane();
        }
    }
}
