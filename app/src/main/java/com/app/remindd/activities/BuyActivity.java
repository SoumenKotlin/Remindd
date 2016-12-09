package com.app.remindd.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.remindd.BuildConfig;
import com.app.remindd.R;
import com.app.remindd.util.IabHelper;
import com.app.remindd.util.IabResult;
import com.app.remindd.util.Inventory;
import com.app.remindd.util.Purchase;
import com.app.remindd.util.Security;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static com.app.remindd.R.id.buy1;
import static com.app.remindd.R.id.buy2;
import static com.app.remindd.R.id.buy3;

public class BuyActivity extends AppCompatActivity {

    Toolbar toolbar;
    private static final String TAG = "com.app.remindd.inappbilling";
    IabHelper mHelper;
    //static final String ITEM_SKU = "com.app.remindd";

      String CHECK_ITEMS="";
    static  final String[] ITEMS = {"com.app.remindd", "com.app.remindd2", "com.app.remindd3"};
  //  static final String ITEMS_SKU = "android.test.purchased";
    private Button clickButton;
    private Button buyButton1, buyButton2, buyButton3;
    TextView price1, price2, price3;
    List<String> additionalSkuList;

   // List<String> checkPurchased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Buy Product");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

       /* checkPurchased = new ArrayList<>();
        checkPurchased.add(ITEMS_SKU);*/

        additionalSkuList = new ArrayList<>();

        price1 = (TextView)findViewById(R.id.price1);
        price2 = (TextView)findViewById(R.id.price2);
        price3 = (TextView)findViewById(R.id.price3);

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

        buyButton1 = (Button)findViewById(buy1);
        buyButton2 = (Button)findViewById(buy2);
        buyButton3 = (Button)findViewById(buy3);

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApBA3MPUXLXn7WiR+WkI/0N+cs5u210gZgpbC5/tjgz4gjubK4TcSbEvNigpMAtofe2yrHIe9AUT+ZI+RXP8sgl4DmRR+V/g2rQEZxMQaP0rmTK7mSWcJOdpd0VQcjxRXO3C4usBV54Q1veJPPbfsCqDE46/n2MwLAId5OyrWtxIXEaUgQDWCDd+XtvcbFAOrvS8IXpzoR2aV0I9e22fZebOO611XAZp3OmhlSlMkME57BAOteH8rl/+whn55A+kA6rcvVdCvsXl6W53OjD+mSi/hlhah6/7SAkMoiCpNWNkumeW7G5KuuoC+ALahVENHYFX4KpM2qRGrc85xwHG33wIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {

                                           if (!result.isSuccess()) {
                                               Log.e("hu", ""+result);
                                           } else {
                                               Log.e("ha", "ok");
                                               new GetPrice().execute();
                                           }
                                       }
                                   });



        buyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CHECK_ITEMS = ITEMS[0];
                    mHelper.launchPurchaseFlow(BuyActivity.this, ITEMS[0], 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        });

        buyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CHECK_ITEMS = ITEMS[1];
                    mHelper.launchPurchaseFlow(BuyActivity.this, ITEMS[1], 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        });

        buyButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CHECK_ITEMS = ITEMS[2];
                    mHelper.launchPurchaseFlow(BuyActivity.this, ITEMS[2], 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d("PAY", "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null)
                return;

            Purchase purchase1 = inventory.getPurchase(ITEMS[0]);
            Purchase purchase2 = inventory.getPurchase(ITEMS[1]);
            Purchase purchase3 = inventory.getPurchase(ITEMS[2]);


            //purchased
            if (purchase1 != null) {
                buyButton1.setText("Done");
            }
            if (purchase2 != null) {
                buyButton2.setText("Done");
            }

            if (purchase3 != null) {
                buyButton3.setText("Done");
            }
            /*else {
                Toast.makeText(BuyActivity.this, "Not purchased", Toast.LENGTH_SHORT).show();
            }*/
        }
    };

    class GetPrice extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            additionalSkuList.add(ITEMS[0]);
            additionalSkuList.add(ITEMS[1]);
            additionalSkuList.add(ITEMS[2]);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                mHelper.queryInventoryAsync(true, additionalSkuList, null,
                        mReceivedInventoryListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
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
            else if (purchase.getSku().equals(ITEMS[0]) || purchase.getSku().equals(ITEMS[1]) || purchase.getSku().equals(ITEMS[2])) {
                consumeItem();
                //  buyButton1.setEnabled(false);
            }

        }
    };

    public void consumeItem() {
        try {
            mHelper.queryInventoryAsync(true, additionalSkuList, null,
                    mReceivedInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // handle error
                Log.e("Error", "Items error");
                return;
            }

            String str_price1 = inventory.getSkuDetails(ITEMS[0]).getPrice();
            String str_price2 = inventory.getSkuDetails(ITEMS[1]).getPrice();
            String str_price3 = inventory.getSkuDetails(ITEMS[2]).getPrice();


            price1.setText(str_price1);
            price2.setText(str_price2);
            price3.setText(str_price3);


            try {
                mHelper.queryInventoryAsync(mGotInventoryListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }


            // Toast.makeText(BuyActivity.this, "Reminders: "+price1+ " :"+ price2+ " :"+price3, Toast.LENGTH_SHORT).show();


            /* if (result.isFailure()) {
                // Handle failure
            }



            else {
                try {


                    mHelper.consumeAsync(inventory.getPurchase(ITEMS[1]),
                            mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }*/
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
