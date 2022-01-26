 package com.mutana.mutana;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;

public class Pad extends AppCompatActivity {

    /* User Variable */
    public String result ;
    EditText dashBoard;
    Button delete;
    boolean afterEqual;

    /* ADS Variable */
    private final String APP_ID = "appd16c6db47adc43b08c";
    private final String BANNER = "vzb8117e41dc1340309a" ;

    private AdColonyAdOptions mAdColonyAdOptions;
    String consent = "1";
    public static int VARS = 0;
    RelativeLayout ad_container;
    AdColonyAdView adView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        /* Ads Initialization */
        AdColonyAppOptions options = new AdColonyAppOptions().setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true).setPrivacyConsentString(AdColonyAppOptions.GDPR,consent);
        AdColony.configure(this,options,APP_ID,BANNER);
        AdColonyAdViewListener adColonyAdViewListener = new AdColonyAdViewListener() {
            @Override
            public void onRequestFilled(AdColonyAdView adColonyAdView) {
                ad_container = findViewById(R.id.ad_container);
                ad_container.addView(adColonyAdView);
                adView = adColonyAdView;
            }
        };
        AdColony.requestAdView(BANNER, adColonyAdViewListener, AdColonyAdSize.BANNER, mAdColonyAdOptions);
        /* Action Bar */
        ActionBar menu = getSupportActionBar();
        menu.setDisplayHomeAsUpEnabled(true);
        menu.setTitle("Calculatrice");

        /* Connect UI */
        dashBoard = findViewById(R.id.dashBoad);
        delete = findViewById(R.id.delete_btn);
        dashBoard.setShowSoftInputOnFocus(false);

        /* Eraser Method > delete button */
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dashBoard.setText("");
                return false;
            }
        });
    }

    /* Algorithm to manage the cursor : CPYCODE */
    public void UpdateDash(String strToAdd){

        dashBoard.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.color_light));
        String oldStr = dashBoard.getText().toString();
        int cursorPos = dashBoard.getSelectionStart();
        String leftStr = oldStr.substring(0,cursorPos);
        String rightStr = oldStr.substring(cursorPos);
        dashBoard.setText(String.format("%s%s%s", leftStr,strToAdd,rightStr));
        dashBoard.setSelection(cursorPos+1);


    }

    /* Math Pad : !need > Steve For Algorithm */
    public void zero_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("0");
            afterEqual = false;
        }
    }

    public void seven_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("7");
            afterEqual = false;
        }
    }

    public void eight_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("8");
            afterEqual = false;
        }
    }

    public void nine_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("9");
            afterEqual = false;
        }
    }

    public void four_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("4");
            afterEqual = false;
        }
    }

    public void five_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("5");
            afterEqual = false;
        }
    }

    public void six_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("6");
            afterEqual = false;
        }
    }

    public void one_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("1");
            afterEqual = false;
        }
    }

    public void two_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("2");
            afterEqual = false;
        }
    }

    public void tree_btn(View view) {
        if(afterEqual == true)
        {
            dashBoard.setText("");
        }
        VARS++;
        if(VARS <=12)
        {
            UpdateDash("3");
            afterEqual = false;
        }
    }

    /* Math Operator */
    public void minus_btn(View view) {
        UpdateDash("-");
        afterEqual = false;
        VARS =0;
    }

    public void multiply_btn(View view) {
        UpdateDash("×");
        afterEqual = false;
        VARS =0;
    }

    public void divide_btn(View view) {
        UpdateDash("÷");
        afterEqual = false;
        VARS =0;
    }
    public void plus_btn(View view) {
        UpdateDash("+");
        afterEqual = false;
        VARS =0;
    }

    public void del_btn(View view) {
        int cursorPos = dashBoard.getSelectionStart();
        int textLen = dashBoard.getText().length();
        dashBoard.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.color_light));

        /* Delete > delete buttom: Original Algorithm  */
        if(cursorPos !=0 && textLen !=0)
        {
            SpannableStringBuilder Captor = (SpannableStringBuilder) dashBoard.getText();
            Captor.replace(cursorPos-1,cursorPos,"");
            dashBoard.setText(Captor);
            dashBoard.setSelection(cursorPos-1);
        }
        VARS =0;
    }
    public void dot_btn(View view) {
        UpdateDash(".");
        VARS =0;
    }
    public void equal_btn(View view) {
        Equal();
        dashBoard.setSelection(dashBoard.length());
        VARS =0;
    }

    public void Equal() {
        /* PareseTask Model - My Script Data */
        try {
            String data = dashBoard.getText().toString();

            /* Pre Filter */
            data = data.replaceAll("×","*");
            data = data.replaceAll("÷","/");

            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1);
            Scriptable scriptable = rhino.initStandardObjects();
            if(data != null && !data.isEmpty())
            {
                result = rhino.evaluateString(scriptable, data, "Javascript", 1, null).toString();
            }
            else
            {
                dashBoard.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.error_color));
                dashBoard.setText(dashBoard.getText().toString());
            }

            /* Filter & output */
            if(result !=null && !result.isEmpty())
            {
                dashBoard.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.color_light));
                dashBoard.setText(new DecimalFormat("0.####").format(Double.valueOf(result)));
                afterEqual = true;
            }

        }catch (Exception e)
        {
            dashBoard.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.error_color));
            dashBoard.setText(dashBoard.getText().toString());
        }
    }

    /* KEY Methods */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}