package com.example.soumendutta.mappsepnavigation.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.soumendutta.mappsepnavigation.BuildConfig;
import com.example.soumendutta.mappsepnavigation.R;
import com.example.soumendutta.mappsepnavigation.util.IabHelper;
import com.example.soumendutta.mappsepnavigation.util.IabResult;
import com.example.soumendutta.mappsepnavigation.util.Inventory;
import com.example.soumendutta.mappsepnavigation.util.Purchase;
import com.example.soumendutta.mappsepnavigation.util.Security;

import java.security.PublicKey;

import static com.example.soumendutta.mappsepnavigation.R.id.buy1;

public class BuyActivity extends AppCompatActivity {

    Toolbar toolbar;
    private static final String TAG = "com.app.remindd.inappbilling";
    IabHelper mHelper;
    static final String ITEM_SKU = "com.app.remindd";

    private Button clickButton;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Buy Product");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buyButton = (Button)findViewById(buy1);

        String base64EncodedPublicKey =
                "<your license key here>";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.e("hu", ""+result);
                                           } else {
                                               Log.e("ha", "ok");
                                           }
                                       }
                                   });

    buyButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mHelper.launchPurchaseFlow(BuyActivity.this, ITEM_SKU, 10001,
                        mPurchaseFinishedListener, "mypurchasetoken");
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    });


    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
                buyButton.setEnabled(false);
            }

        }
    };
    public void consumeItem() {
        try {
            mHelper.queryInventoryAsync(mReceivedInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                try {
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                            mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        clickButton.setEnabled(true);
                    } else {
                        // handle error
                    }
                }
            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static boolean verifyPurchase(String base64PublicKey,
                                         String signedData, String signature) {
        if (TextUtils.isEmpty(signedData) ||
                TextUtils.isEmpty(base64PublicKey) ||
                TextUtils.isEmpty(signature)) {
            Log.e("h", "Purchasailed: missing data.");
            if (BuildConfig.DEBUG) {
                return true;
            }
            return false;
        }

        PublicKey key = Security.generatePublicKey(base64PublicKey);
        return Security.verify(key, signedData, signature);
    }
}
