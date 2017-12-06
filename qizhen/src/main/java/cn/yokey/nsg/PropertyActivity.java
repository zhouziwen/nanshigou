package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PropertyActivity extends AppCompatActivity {

    public static Activity mActivity;

    private void createControl(){

    }

    private void createEvent(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propert);
        createControl();
        createEvent();
    }

}
