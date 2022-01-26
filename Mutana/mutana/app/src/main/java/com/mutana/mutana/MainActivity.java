package com.mutana.mutana;

import android.Manifest;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /* UI Variables */
    TextView resultBoard;
    EditText dashBoard;
    Button recButton, padButton;
    ImageView voiceButton;

    /* User variable */
    boolean isListening, isVoiceOn;
    String data,filtered_result,lastResults;
    String ID_YOUTUBE = "channel/UCkdmBXlWO1o_ZJa2HalMeCw";
    private static final String EMAIL = "mutanahelp@gmail.com";

    /* System Variable */
    Animation animation;
    private Intent intent;
    TextToSpeech mTextToSpeech;
    SpeechRecognizer mSpeechRecognizer;

    /* Ads Variables */
    private final String APP_ID = "appd16c6db47adc43b08c";
    private final String FULLSCREEN = "vz6f8102f05f9d4f5692";
    private final String BANNER = "vzb8117e41dc1340309a" ;
    private AdColonyInterstitial mAdColonyIntertitial;
    private AdColonyAdOptions mAdColonyAdOptions;
    String consent = "1";
    int countAds;
    RelativeLayout ad_container;
    AdColonyAdView adView;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Ads Initialization */
        AdColonyAppOptions options = new AdColonyAppOptions().setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true).setPrivacyConsentString(AdColonyAppOptions.GDPR,consent);
        AdColony.configure(this,options,APP_ID, BANNER);
        AdColonyAdViewListener adColonyAdViewListener = new AdColonyAdViewListener() {
            @Override
            public void onRequestFilled(AdColonyAdView adColonyAdView) {
                ad_container = findViewById(R.id.ad_container);
                ad_container.addView(adColonyAdView);
                adView = adColonyAdView;
            }
        };
        AdColony.requestAdView(BANNER, adColonyAdViewListener, AdColonyAdSize.BANNER, mAdColonyAdOptions);


        /* Connect my UI */
        dashBoard = findViewById(R.id.dashboard_voice);
        recButton = findViewById(R.id.mic_btn);
        resultBoard = findViewById(R.id.dashResult);
        padButton = findViewById(R.id.pad_btn);


        /* Get Application Permission */
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.RECORD_AUDIO}, getPackageManager().PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.VIBRATE}, getPackageManager().PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                Manifest.permission.ACCESS_NETWORK_STATE},getPackageManager().PERMISSION_GRANTED);

        /* Definition of my SpeechRecognizer & Text-to-speech*/
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)
                {
                    int result = mTextToSpeech.setLanguage(Locale.FRENCH);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(getApplicationContext(), "Synthèse vocale non disponible", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        /* Animation Manager */
        dashBoard.setShowSoftInputOnFocus(false);
        resultBoard.setShowSoftInputOnFocus(false);
        overridePendingTransition(0,0);
        AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rec_animation);

        /* Button Rec/Pad Listener */
        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countAds++;
                if(countAds==7)
                {
                    AdColony.configure(MainActivity.this, options,APP_ID, FULLSCREEN);
                    AdColonyInterstitialListener adColonyInterstitialListener = new AdColonyInterstitialListener() {
                        @Override
                        public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                            mAdColonyIntertitial = adColonyInterstitial;
                            mAdColonyIntertitial.show();
                        }
                    };
                    AdColony.requestInterstitial(FULLSCREEN,adColonyInterstitialListener,mAdColonyAdOptions);
                    countAds = 0;
                }

                if ( !isConnected(MainActivity.this) ) {
                    showCustomDialog();

                } else {
                    try {
                        boolean isSpeech2Running;
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
                        if(isSpeech2Running = true)
                        {
                            mSpeechRecognizer.startListening(intent);
                            isSpeech2Running = false;
                        }
                        else
                        {
                            mSpeechRecognizer.startListening(intent);
                            isSpeech2Running = true;
                        }

                        /* Button Pressed CustomEvent */
                        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rec_animation);
                        recButton.startAnimation(animation);
                        recButton.setBackgroundResource(R.drawable.rec_load);
                        mTextToSpeech.stop();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Réessayer", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        recButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                recButton.setBackgroundResource(R.drawable.mic);
                recButton.clearAnimation();
                return true;
            }
        });

        padButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openPadIntent = new Intent(MainActivity.this, Pad.class);
                startActivity(openPadIntent);
            }
        });

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                recButton.setBackgroundResource(R.drawable.rec_load);
            }

            @Override
            public void onBeginningOfSpeech() {
                recButton.setBackgroundResource(R.drawable.red_mic_load);
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            /* Need To Destroy this Object */
            }

            @Override
            public void onError(int error) {

                recButton.setBackgroundResource(R.drawable.mic);
                recButton.clearAnimation();

                /* Mutator Destructor : Multi Touch ReInitializer : !Need */
                mSpeechRecognizer.cancel();
                mSpeechRecognizer.destroy();
                mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                mSpeechRecognizer.setRecognitionListener(this);
                mTextToSpeech.stop();


            }

            @Override
            public void onResults(Bundle results) {
                /* Data : collection by getStringArrayList */
                recButton.setBackgroundResource(R.drawable.mic);
                recButton.clearAnimation();
                ArrayList<String> matches = results.getStringArrayList(mSpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {
                    data = matches.get(0);
                    filtered_result = Analyzor(data);

                    /* Scripting Algorithm Data */
                    try{
                        Context rhino = Context.enter();
                        rhino.setOptimizationLevel(-1);
                        Scriptable scriptable = rhino.initStandardObjects();
                        lastResults = rhino.evaluateString(scriptable, filtered_result, "Javascript", 1, null).toString();
                        dashBoard.setText(filtered_result);

                        resultBoard.setText( " = "+ new DecimalFormat("0.#####").format(Double.valueOf(lastResults)));
                        /* Mutator Destructor */
                        mSpeechRecognizer.cancel();
                        mSpeechRecognizer.destroy();
                        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                        mSpeechRecognizer.setRecognitionListener(this);
                        AddTextToSpeech( filtered_result,new DecimalFormat("0.#####").format(Double.valueOf(lastResults)));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        /* ! Need Custom Exception */
                    }


                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });
    }

    /*-------------------------------------------------------------------------------------------*/
    /*----------------------------       CUSTOM METHODS       -----------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    /* My Filter : CLEAN DATA FROM COLLECTED DATA */
    private String Analyzor(String data) {
        data = data.replaceAll("x", "*");
        data = data.replaceAll("[a-zA-Z]", "");
        data = data.replaceAll(",", ".");
        return data;
    }

    /* Post Filter : CLEAN DATA BEFORE SENDING TO MY TTS */
    private void AddTextToSpeech(String other,String operation) {
        if(operation != null )
        {
            other = other.replaceAll("[*]","par");
            other = other.replaceAll("[-]","moins");
            other = other.replaceAll("[/]","diviser par");
            mTextToSpeech.speak(other+" donne "+operation,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    /* Connection Method : Phone State about Network Verification */
    private boolean isConnected(MainActivity ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        if (wifiConn != null && wifiConn.isConnected() || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    /* Dialog Method : !Need : Incomplete */
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Pas de connexion d'internet").setCancelable(false).setPositiveButton("Paramètre", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));}
        }).setNegativeButton("D'accord", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        isConnected(this);
    }

    /*-------------------------------------------------------------------------------------------*/
    /*----------------------------        Core Method         -----------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    /* Create Menu */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItem;
        menuItem = item.getItemId();
        if(menuItem == R.id.help)
        {
            Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://"+ID_YOUTUBE));
            startActivity(youtube);
        }
        if(menuItem == R.id.feedback)
        {
            Intent intentEmail = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+EMAIL));
            try {
                startActivity(Intent.createChooser(intentEmail, "Send Feedback"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
            if(menuItem == R.id.privacy)
        {
            Intent Site =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://izimat.netlify.app/privacy.html"));
            startActivity(Site);
        }
            return true;
    }

    /* KEY METHOD : !Need */

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause( ) {
        super.onPause();
        mSpeechRecognizer.stopListening();

    }
}