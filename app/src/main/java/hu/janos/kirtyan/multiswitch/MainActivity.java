package hu.janos.kirtyan.multiswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvHW = (TextView) findViewById(R.id.tvHW);
        final MultiSwitch msTextColor = (MultiSwitch) findViewById(R.id.msTextColor);

        msTextColor.setOnSwitchChangeListener(new MultiSwitch.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(int selectedPosition) {
                int colorRes;
                switch (selectedPosition) {
                    default:
                    case 0: colorRes = R.color.color_1; break;
                    case 1: colorRes = R.color.color_2; break;
                    case 2: colorRes = R.color.color_3; break;
                }

                tvHW.setTextColor(getResources().getColor(colorRes));
            }
        });

        tvHW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msTextColor.setEnabled(!msTextColor.isEnabled());
            }
        });
    }
}
