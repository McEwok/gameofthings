package com.twomoons.factory.partythings.Mocks;

import android.content.Context;
import android.view.Menu;

import com.twomoons.factory.partythings.CommunicatorEvents;
import com.twomoons.factory.partythings.ICommunicator;
import com.twomoons.factory.partythings.IHub;
import com.twomoons.factory.partythings.IMsgHandler;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Joshua on 6/7/2015.
 */
public class CastServiceMock implements ICommunicator, IMsgHandler {
    private int waitingLoop = 0;
    private IHub _messageHub;

    @Override
    public void Initialize(Context ctx, IHub messageHub) {
        _messageHub = messageHub;
        setupListeners();
    }

    private void setupListeners() {
        ArrayList<CommunicatorEvents> events = new ArrayList<CommunicatorEvents>();
        events.add(CommunicatorEvents.NotConnectedEnter);
        events.add(CommunicatorEvents.NotConnectedExit);
        events.add(CommunicatorEvents.EnterGameEnter);
        events.add(CommunicatorEvents.EnterGameExit);
        events.add(CommunicatorEvents.EnterGameNameEnter);
        events.add(CommunicatorEvents.EnterGameNameExit);
        events.add(CommunicatorEvents.PromptSelectionEnter);
        events.add(CommunicatorEvents.PromptSelectionExit);
        events.add(CommunicatorEvents.WaitingEnter);
        events.add(CommunicatorEvents.WaitingExit);
        events.add(CommunicatorEvents.EnterResponseEnter);
        events.add(CommunicatorEvents.EnterResponseExit);
        events.add(CommunicatorEvents.PickResponseEnter);
        events.add(CommunicatorEvents.PickResponseExit);
        _messageHub.RegisterMsgr(this, events);
    }
    Random rdm = new Random();

    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.NotConnectedExit) {
            _messageHub.SendMessage(CommunicatorEvents.EnterGameNameEnter, rdm.nextBoolean() ? "gameExists" : "");
        } else if(eventType == CommunicatorEvents.EnterGameNameExit) {
            System.out.println("Game/Player name entered: " + message);
            _messageHub.SendMessage(CommunicatorEvents.PromptSelectionEnter, "A:::B:::C");
        } else if(eventType == CommunicatorEvents.PromptSelectionExit){
            System.out.println("PromptSelected: " + message);
            _messageHub.SendMessage(CommunicatorEvents.WaitingEnter, "EnterResponse");
        } else if(eventType == CommunicatorEvents.WaitingExit) {
            System.out.println("Waiting Exit");
            if (message == "EnterResponse") {
                _messageHub.SendMessage(CommunicatorEvents.EnterResponseEnter, "Donuts");
            } else if (message == "PickResponse"){
                _messageHub.SendMessage(CommunicatorEvents.PickResponseEnter, "A:::B:::C");
            }
        } else if(eventType == CommunicatorEvents.EnterResponseExit) {
            System.out.println(message);
            _messageHub.SendMessage(CommunicatorEvents.WaitingEnter,"PickResponse");
        }
        /*
            GOOGLE: String Parsing in JAVA (Split) "Response A ::::: Response B ::::: Response C"
            NotConnected
            EnterGameName
            EnterGame
            PromptSelection
            Waiting
            EnterResponse
            Waiting
            PickResponse
            PickPlayer
            Waiting
            Results
            REPEAT back to Pick Response
            OR REPEAT back to PromptSelection
            OR ENDGAME
         */
    }

    @Override
    public void InitializeMenu(Menu menu, int media_route_menu_item) {

    }

    @Override
    public void OnResume() {

    }

    @Override
    public void OnPause() {

    }

}
