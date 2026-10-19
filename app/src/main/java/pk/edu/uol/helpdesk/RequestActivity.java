package pk.edu.uol.helpdesk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Mobile Application Development (CS02310) — Lab Task — Implicit Intents — Section S
 * The University of Lahore | Department of Software Engineering
 *
 * Scenario 2 — The Request Screen:
 * Receives student name via explicit Intent, provides personalized greeting,
 * validates phone input (11 digits starting with 03) and web links (prepending https://),
 * and reuses MainActivity.openIntent() to trigger implicit Intents for dialer and browser.
 */
public class RequestActivity extends AppCompatActivity {

    private TextView tvGreeting;
    private EditText etPhone;
    private Button btnCallNumber;
    private EditText etLink;
    private Button btnOpenLink;
    private ImageButton btnBack;
    private Button btnReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // Initialize UI Elements
        tvGreeting = findViewById(R.id.tvGreeting);
        etPhone = findViewById(R.id.etPhone);
        btnCallNumber = findViewById(R.id.btnCallNumber);
        etLink = findViewById(R.id.etLink);
        btnOpenLink = findViewById(R.id.btnOpenLink);
        btnBack = findViewById(R.id.btnBack);
        btnReturnHome = findViewById(R.id.btnReturnHome);

        // Scenario 2, Step 2: Read student name from explicit Intent
        String studentName = getIntent().getStringExtra(MainActivity.EXTRA_NAME);
        if (studentName == null || studentName.trim().isEmpty()) {
            studentName = "Student";
        }

        // Display personalized greeting
        String greetingText = "Hello, " + studentName.trim() + "!\nHow can we help you today?";
        tvGreeting.setText(greetingText);

        // Scenario 2, Step 3: Phone field validation and ACTION_DIAL
        // Accept only 11 digits starting with 03 (e.g. 03001234567)
        btnCallNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString().trim();

                // Validate phone format: exactly 11 digits starting with '03'
                if (!phone.matches("^03[0-9]{9}$")) {
                    etPhone.setError("Please enter an 11-digit number starting with 03");
                    etPhone.requestFocus();
                    return;
                }

                // Launch dialer via reused helper method
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                MainActivity.openIntent(RequestActivity.this, dialIntent);
            }
        });

        // Scenario 2, Step 4: Link field validation and ACTION_VIEW
        // Reject empty input, ensure https:// prefix if http:// or https:// is missing
        btnOpenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rawLink = etLink.getText().toString().trim();

                // Reject empty field
                if (rawLink.isEmpty()) {
                    etLink.setError("Please enter a link");
                    etLink.requestFocus();
                    return;
                }

                // Add "https://" when link does not already start with http:// or https://
                String lower = rawLink.toLowerCase();
                String formattedLink = rawLink;
                if (!lower.startsWith("http://") && !lower.startsWith("https://")) {
                    formattedLink = "https://" + rawLink;
                }

                // Open link in browser via reused helper method
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(formattedLink));
                MainActivity.openIntent(RequestActivity.this, browserIntent);
            }
        });

        // Back navigation handlers
        View.OnClickListener finishListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        if (btnBack != null) {
            btnBack.setOnClickListener(finishListener);
        }
        if (btnReturnHome != null) {
            btnReturnHome.setOnClickListener(finishListener);
        }
    }
}
