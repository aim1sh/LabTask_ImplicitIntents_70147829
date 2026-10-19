package pk.edu.uol.helpdesk;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Mobile Application Development (CS02310) — Lab Task — Implicit Intents — Section S
 * The University of Lahore | Department of Software Engineering
 *
 * Scenario 1 — The Home Screen:
 * Demonstrates implicit Intents for Web, Dialer, and Map actions with a safe helper method,
 * and launches Scenario 2 with an explicit Intent passing student name data.
 */
public class MainActivity extends AppCompatActivity {

    // Public static final key constant for passing student name via explicit Intent
    public static final String EXTRA_NAME = "pk.edu.uol.helpdesk.EXTRA_NAME";

    private ImageView ivLogo;
    private EditText etName;
    private Button btnWebsite;
    private Button btnCall;
    private Button btnMap;
    private Button btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1: Initialize Views from activity_main.xml
        ivLogo = findViewById(R.id.ivLogo);
        etName = findViewById(R.id.etName);
        btnWebsite = findViewById(R.id.btnWebsite);
        btnCall = findViewById(R.id.btnCall);
        btnMap = findViewById(R.id.btnMap);
        btnRequest = findViewById(R.id.btnRequest);

        // Step 2: Implicit Intent to open UOL official website
        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.uol.edu.pk"));
                openIntent(MainActivity.this, intent);
            }
        });

        // Step 3: Implicit Intent to open phone dialer with help-desk number
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:04212345678"));
                openIntent(MainActivity.this, intent);
            }
        });

        // Step 4: Implicit Intent to view University of Lahore in Maps
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=University+of+Lahore"));
                openIntent(MainActivity.this, intent);
            }
        });

        // Scenario 2, Step 1: Explicit Intent to open RequestActivity with name validation
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();

                // Validate that name is not empty
                if (name.isEmpty()) {
                    etName.setError("Please enter your name");
                    etName.requestFocus();
                    return;
                }

                // Explicit Intent carrying student name
                Intent intent = new Intent(MainActivity.this, RequestActivity.class);
                intent.putExtra(EXTRA_NAME, name);
                startActivity(intent);
            }
        });
    }

    /**
     * Scenario 1, Step 5: Formulate one helper method that calls startActivity()
     * inside try–catch and shows the Toast "No app found for this action" on ActivityNotFoundException.
     * Reused across MainActivity and RequestActivity.
     *
     * @param context Application/Activity context
     * @param intent  The intent to be launched
     */
    public static void openIntent(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No app found for this action", Toast.LENGTH_SHORT).show();
        }
    }
}
