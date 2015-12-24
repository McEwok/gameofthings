package com.twomoons.factory.partythings;

import android.content.Context;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.sample.castcompanionlibrary.cast.DataCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.DataCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.provider.Settings.Secure;

/**
 * Created by JnA-PC on 4/30/2015.
 */
public class CastService extends MediaRouter.Callback implements ICommunicator, IMsgHandler {

    private DataCastManager mCaster;
    private String mNamespace = "urn:x-cast:com.partythings";
    private String cGameName = ".gamename";
    private String cPlayerName = ".playername";
    private String cStandBy = ".standby";
    private String cReady = ".ready";
    private String cPrompt = ".prompt";
    private String cThing = ".thing";
    private String cGuess = ".guess";
    private String cQuit = ".quit";
    private String cConnected = ".connected";
    private IHub _messageHub;
    private String lastPlayers = null;
    private String selectedResponse = null;
    private String deviceId = null;

    @Override
    public void Initialize(Context ctx, IHub messageHub) {
        mCaster = DataCastManager.initialize(ctx, "285A9A14",   mNamespace+cGameName,
                                                                mNamespace+cPlayerName,
                                                                mNamespace+cStandBy,
                                                                mNamespace+cReady,
                                                                mNamespace+cPrompt,
                                                                mNamespace+cThing,
                                                                mNamespace+cGuess,
                                                                mNamespace+cQuit,
                                                                mNamespace+cConnected);

        //mCaster.enableFeatures(DataCastManager.FEATURE_WIFI_RECONNECT);
        mCaster.addDataCastConsumer(new CastMessageConsumer());
        _messageHub = messageHub;

        _messageHub.RegisterMsgr(this, buildEventList());

        deviceId = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
    }

    private ArrayList<CommunicatorEvents> buildEventList(){
        ArrayList<CommunicatorEvents> list =  new ArrayList<CommunicatorEvents>();
        list.add(CommunicatorEvents.EnterGameNameExit);
        list.add(CommunicatorEvents.EnterPlayerNameExit);
        list.add(CommunicatorEvents.EnterResponseExit);
        list.add(CommunicatorEvents.PickPlayerExit);
        list.add(CommunicatorEvents.PickResponseExit);
        list.add(CommunicatorEvents.PromptSelectionExit);
        list.add(CommunicatorEvents.ReadyExit);
        list.add(CommunicatorEvents.Handshake);
        return list;
    }

    @Override
    public void InitializeMenu(Menu menu, int media_route_menu_item) {
        mCaster.addMediaRouterButton(menu, media_route_menu_item);
    }

    @Override
    public void OnResume() {
        mCaster.incrementUiCounter();
        if(mSelectedDevice != null){
            _messageHub.SendMessage(CommunicatorEvents.NotConnectedExit, "");
            _messageHub.SendMessage(CommunicatorEvents.WaitingEnter, "");
            _messageHub.SendMessage(CommunicatorEvents.Handshake, "");
        }
    }

    @Override
    public void OnPause() {
        mCaster.decrementUiCounter();
    }

    private CastDevice mSelectedDevice;

    @Override
    public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
        mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
//        String routeId = info.getId();
    }

    @Override
    public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
        //teardown();
        mSelectedDevice = null;
    }

//    private void SendMessage() {
//        try{
//            mCaster.sendDataMessage("Marco", mNamespace);
//        } catch(IOException ex){
//            Log.e("MESSAGE_FAILED_IO", "Failed to send message: " + ex);
//        } catch(TransientNetworkDisconnectionException ex) {
//            Log.e("MESSAGE_FAILED_NET", "Failed to send message: " + ex);
//        } catch(NoConnectionException ex) {
//            Log.e("MESSAGE_FAILED_CONN", "Failed to send message: " + ex);
//        }
//    }


    @Override
    public void HandleMessage(CommunicatorEvents eventType, String message) {
        if(eventType == CommunicatorEvents.EnterGameNameExit){
            String[] split = message.split(":::");
            JSONObject json = new JSONObject();

            try {
                json.put("gameName", split[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                json.put("playerName", split[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendMessage(json.toString(), mNamespace + cGameName);
        } else if (eventType == CommunicatorEvents.EnterPlayerNameExit){
            JSONObject json = new JSONObject();
            try {
                json.put("playerName", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendMessage(json.toString(), mNamespace + cGameName);
        } else if(eventType == CommunicatorEvents.ReadyExit){
            sendMessage("{}", mNamespace + cReady);
        } else if(eventType == CommunicatorEvents.PromptSelectionExit){
            JSONObject json = new JSONObject();
            try {
                json.put("prompt", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendMessage(json.toString(), mNamespace + cPrompt);
        } else if(eventType == CommunicatorEvents.EnterResponseExit){
            JSONObject json = new JSONObject();
            try {
                json.put("thing", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendMessage(json.toString(), mNamespace + cThing);
        } else if(eventType == CommunicatorEvents.PickResponseExit){
            List<String> responseListObjects = Arrays.asList(message.split(":::"));
            selectedResponse = responseListObjects.get(0);
            _messageHub.SendMessage(CommunicatorEvents.PickPlayerEnter, responseListObjects.get(1) + ":::" + lastPlayers);
        } else if(eventType == CommunicatorEvents.PickPlayerExit){
            JSONObject json = new JSONObject();
            try {
                json.put("playerId", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                json.put("responseId", selectedResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendMessage(json.toString(), mNamespace + cGuess);
            lastPlayers = null;
            selectedResponse = null;
        } else if(eventType == CommunicatorEvents.Handshake) {
            JSONObject json = new JSONObject();

            try{
                json.put("playerId", deviceId);
            }  catch (JSONException e) {
                e.printStackTrace();
            }

            sendMessage(json.toString(), mNamespace + cConnected);
        }
    }

    private void sendMessage(String message, String namespace){
        try {
            System.out.println("Sending Message: " + " ::: " + namespace + " ::: " + message);
            mCaster.sendDataMessage(message, namespace);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransientNetworkDisconnectionException e) {
            e.printStackTrace();
        } catch (NoConnectionException e) {
            e.printStackTrace();
        }
    }

    private class CastMessageConsumer extends DataCastConsumerImpl {
        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace, String data) {

            System.out.println("Cast message received: ");
            System.out.println(namespace + " ::: " + data);
            JSONObject json;
            try {
                json = new JSONObject(data);
            } catch (JSONException e) {
                System.out.println("Error parsing json:");
                System.out.println(data);
                e.printStackTrace();
                return;
            }

            if (namespace.equals(mNamespace + cPlayerName)) {
                String message = GetMessage(json);
                _messageHub.SendMessage(CommunicatorEvents.EnterPlayerNameEnter, message);
            } else if (namespace.equals(mNamespace + cGameName)) {
                String message = GetMessage(json);
                _messageHub.SendMessage(CommunicatorEvents.EnterGameNameEnter, message);
            } else if (namespace.equals(mNamespace + cReady)) {
                String message = GetMessage(json);
                _messageHub.SendMessage(CommunicatorEvents.ReadyEnter, message);
            } else if (namespace.equals(mNamespace + cPrompt)) {
                String[] prompts = GetPrompts(json);
                String promptsStr = prompts[0] + ":::" + prompts[1] + ":::" + prompts[2];
                _messageHub.SendMessage(CommunicatorEvents.PromptSelectionEnter, promptsStr);
            } else if (namespace.equals(mNamespace + cThing)) {
                String message = GetMessage(json);
                _messageHub.SendMessage(CommunicatorEvents.EnterResponseEnter, message);
            } else if (namespace.equals(mNamespace + cGuess)) {
                String things = GetNestedObject(json, "things", "response", "responseId");
                lastPlayers = GetNestedObject(json, "elegiblePlayers", "playerName", "playerId");

                _messageHub.SendMessage(CommunicatorEvents.PickResponseEnter, things);
            } else if (namespace.equals(mNamespace + cConnected)) {
                String message = GetMessage(json);
                _messageHub.SendMessage(CommunicatorEvents.Handshake, "");
            }

        }

        private String GetNestedObject(JSONObject json, String outerArrayName, String prop1, String prop2){
            JSONObject messageObj = GetMessageObj(json);

            JSONArray thingsJSON = null;
            if(null != messageObj){
                try{
                    thingsJSON = messageObj.getJSONArray(outerArrayName);
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }

            String things = "";
            if(null != thingsJSON){

                    for (int i = 0; i < thingsJSON.length(); i++) {
                        if (i > 0) {
                            things += ":::";
                        }
                        try {
                            JSONObject thing = thingsJSON.getJSONObject(i);
                            things += thing.getString(prop1) + ";;;" + thing.getString(prop2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }

            return things;
        }

        private String[] GetPrompts(JSONObject json){
            JSONObject messageObj = GetMessageObj(json);

            JSONArray promptsJSON = null;
            if(null != messageObj){
                try{
                    promptsJSON = messageObj.getJSONArray("prompts");
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }

            String[] prompts = new String[0];
            if(null != promptsJSON){
                prompts = new String[promptsJSON.length()];

                try {
                    for (int i = 0; i < promptsJSON.length(); i++) {
                        prompts[i] = promptsJSON.getString(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return prompts;
        }

        private JSONObject GetMessageObj(JSONObject json){
            JSONObject messageObj = null;
            try {
                messageObj = json.getJSONObject("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return messageObj;
        }

        private String GetMessage(JSONObject json){
            String message = "No Message Found";
            try {
                message = json.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return message;
        }
    }
}


