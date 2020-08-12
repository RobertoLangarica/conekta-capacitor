package com.villavanilla.conekta.capacitor;

import android.app.Activity;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

@NativePlugin
public class ConektaPlugin extends Plugin {

    @PluginMethod
    public  void setPublicKey(PluginCall call){
        String key = call.getString("key","");
        try{
            Conekta.setPublicKey(key);
            call.success();
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

    @PluginMethod
    public  void getPublicKey(PluginCall call){
        try{
            String key = Conekta.getPublicKey();
            JSObject res = new JSObject();
            res.put("key",key);
            call.success(res);
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

    @PluginMethod
    public  void setLanguage(PluginCall call){
        String language = call.getString("language","en");
        try{
            Conekta.setLanguage(language);
            call.success();
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

    @PluginMethod
    public  void getLanguage(PluginCall call){
        try{
            String language = Conekta.getLanguage();
            JSObject res = new JSObject();
            res.put("language",language);
            call.success(res);
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

    @PluginMethod
    public  void createToken(PluginCall call){
        JSObject remote_card = call.getObject("card",new JSObject());

        // Checking minimum values
        if(!remote_card.has("number")){
            call.reject("Missing car:number param.");
        }

        if(!remote_card.has("name")){
            call.reject("Missing car:name param.");
        }

        if(!remote_card.has("cvc")){
            call.reject("Missing car:cvc param.");
        }

        if(!remote_card.has("exp_month")){
            call.reject("Missing car:exp_month param.");
        }

        if(!remote_card.has("exp_year")){
            call.reject("Missing car:exp_month param.");
        }
        String c_name, c_number, c_cvc, c_expMonth, c_expYear;

        c_name = remote_card.getString("name");
        c_number = remote_card.getString("number");
        c_cvc = remote_card.getString("cvc");
        c_expMonth = remote_card.getString("exp_month");
        c_expYear = remote_card.getString("exp_year");

        try{
            final Card card = new Card(c_name,c_number,c_cvc,c_expMonth,c_expYear);
            final Activity activity = getActivity();
            final Token token = new Token(activity);

            saveCall(call);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Conekta.collectDevice(activity);
                    token.onCreateTokenListener( new Token.CreateToken(){
                        @Override
                        public void onCreateTokenReady(JSONObject data){
                           PluginCall savedCall = getSavedCall();
                           JSObject res = new JSObject();
                           Iterator<String> it = data.keys();
                           String key;
                           try{
                               while(it.hasNext()){
                                   key = it.next();
                                   res.put(key, data.get(it.toString()));
                               }
                           }catch (JSONException e){
                               savedCall.reject(e.getMessage(),e);
                           }finally {
                               savedCall.success(res);
                           }
                        }
                    });
                    token.create(card);
                }
            });
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

    @PluginMethod
    public void setApiVersion(PluginCall call){
        try{
            String version =  call.getString("api_version","1.0.0");
            Conekta.setApiVersion(version);
            call.success();
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

    @PluginMethod
    public  void getApiVersion(PluginCall call){
        try{
            String version = Conekta.getApiVersion();
            JSObject res = new JSObject();
            res.put("api_version",version);
            call.success(res);
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

    @PluginMethod
    public void deviceFingerPrint(PluginCall call){
        try{
            String fingerprint = Conekta.deviceFingerPrint(getActivity());
            JSObject res = new JSObject();
            res.put("fingerprint",fingerprint);
            call.success(res);
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

    @PluginMethod
    public void getBaseUri(PluginCall call){
        try{
            String uri = Conekta.getBaseUri();
            JSObject res = new JSObject();
            res.put("uri",uri);
            call.success(res);
        }catch(Exception err){
            call.reject(err.toString());
        }
    }

}

