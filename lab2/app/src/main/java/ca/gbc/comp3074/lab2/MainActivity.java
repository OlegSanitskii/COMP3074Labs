package ca.gbc.comp3074.lab2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    int cnt = 0;
    int step = 1;
    final int def_step = 1;

    TextView label = null;

    void update(int val) {
        cnt = val;
        label.setText(getResources().getString(R.string.counter, cnt));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        label = findViewById(R.id.label);
        update(0);

        Button btn_step = findViewById(R.id.btn_step);
        btn_step.setText(getResources().getString(R.string.step, step));


        ((Button) findViewById(R.id.btn_plus)).setOnClickListener((View v) -> {
            update(cnt + step);
        });


        ((Button) findViewById(R.id.btn_minus)).setOnClickListener(v -> {
            update(cnt - step);
        });


        ((Button) findViewById(R.id.btn_reset)).setOnClickListener((View v) -> {
            update(0);
            step = def_step;
            btn_step.setText(getResources().getString(R.string.step, step));
        });


        btn_step.setOnClickListener((View v) -> {
            step = 2;
            ((Button) v).setText(getResources().getString(R.string.step, step));
        });
    }
}
