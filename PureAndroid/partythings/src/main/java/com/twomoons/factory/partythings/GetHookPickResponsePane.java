package com.twomoons.factory.partythings;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joshua on 5/20/2015.
 */
public class GetHookPickResponsePane extends ListActivity implements IMsgHandler {
    private final Hub hub;
    private final MainActivity ctx;
    private final View ghPickResponsePane;
    private final TextView ghPickResponseText;
    private final ListView ghPickResponseList;
    private List<String> _Items;
    private MyListAdapter _ListAdapter;

    public GetHookPickResponsePane(MainActivity ctx, Hub communicationHub){
        this.ctx = ctx;
        this.hub = communicationHub;
        this.ghPickResponsePane = ctx.getViewById(R.id.pickResponsePane);
        this.ghPickResponseText = (TextView) ctx.getViewById(R.id.pickResponseText);
        this.ghPickResponseList = (ListView) ctx.getViewById(R.id.pickResponseList);
        _Items = new ArrayList<String>();
        _ListAdapter = new MyListAdapter(this.ctx, _Items);
        ghPickResponseList.setAdapter(_ListAdapter);
        setupListener();
        handleOnClick();
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
            addElements(message, _Items, _ListAdapter);
            showPane();
        }
    }

    private void addElements(String message, List<String> _Items, MyListAdapter _ListAdapter) {
        List<String> responseList = Arrays.asList(message.split(":::"));

        _Items.addAll(responseList);
        _ListAdapter.notifyDataSetChanged();
    }

    private void handleOnClick(){
        ghPickResponseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hidePane();
                String value = (String) ghPickResponseList.getItemAtPosition(i);
                hub.SendMessage(CommunicatorEvents.PickResponseExit, value);
            }
        });
    }
}
