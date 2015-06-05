package com.demo.joel.mindwavedemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;


public class MainActivity extends Activity {

    TGDevice mTGDevice;
    BluetoothAdapter mBtAdapter;
    TextView mTV_poorSignal;
    TextView mTV_attention_value;
    TextView mTV_meditation_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBtAdapter !=null)
        {
            mTGDevice = new TGDevice(mBtAdapter,handler);
            mTGDevice.connect(true);
            mTV_poorSignal =  (TextView) findViewById(R.id.poor_signal);
            mTV_meditation_value = (TextView)findViewById(R.id.meditation_value);
            mTV_attention_value = (TextView)findViewById(R.id.attention_value);
        }
    }



    private final Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case TGDevice.MSG_STATE_CHANGE:
                    switch (msg.arg1)
                    {
                        case TGDevice.STATE_IDLE:
                            break;

                        case TGDevice.STATE_CONNECTING:
                            break;
                        case TGDevice.STATE_CONNECTED:
                            mTGDevice.start();

                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            mTGDevice.close();
                            break;
                        case TGDevice.STATE_NOT_FOUND:
                        case TGDevice.STATE_NOT_PAIRED://test paired
                        default:
                            break;
                    }
                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                    Log.d("Hello EEG ", "Poor Signal: " + msg.arg1);
                    mTV_poorSignal.setText("This is the poor Signal Value " + msg.arg1);
                case TGDevice.MSG_ATTENTION:
                    Log.d("Hello EEG ", "Attention: " + msg.arg1);
                    mTV_attention_value.setText("This is the Attention value " + msg.arg1);
                    break;
                case TGDevice.MSG_RAW_DATA:
                    int rawValue = msg.arg1;
                    Log.v("Hello EEG ", "Raw Values " + rawValue );
                    break;
                case TGDevice.MSG_MEDITATION:
                    Log.d("Hello EEG ", "Meditation: " + msg.arg1 );
                    mTV_meditation_value.setText("This is the Meditation value "+ msg.arg1);
                    break;
                case TGDevice.MSG_EEG_POWER:
                    TGEegPower ep = (TGEegPower) msg.obj;
                    Log.d("Hello EEG ", "Delta " + ep.delta);
                    Log.d("Hello EEG ", "Theta " + ep.theta);
                    Log.d("Hello EEG ", "Low Alpha " + ep.lowAlpha);
                    Log.d("Hello EEG ", "High Alpha " + ep.highAlpha);
                    Log.d("Hello EEG ", "Low Beta " + ep.lowBeta);
                    Log.d("Hello EEG ", "High Beta " + ep.highBeta);
                    Log.d("Hello EEG ", "Low Gamma " + ep.lowGamma);
                    Log.d("Hello EEG ", "Mid Gamma " + ep.midGamma);
                default:
                    break;
            }

        }


    };


}
