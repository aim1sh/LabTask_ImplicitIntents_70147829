# The University of Lahore
## Faculty of Information Technology — Department of Software Engineering
### Mobile Application Development (CS02310) — Lab Task Report
**Semester:** Fall 2026 | 7th Semester | Section S  
**Course Code:** CS02310  
**Topic:** Multi-Screen Application using Implicit & Explicit Intents with Input Validation  

---

### Student Information
- **Student Name:** Aimish Umar
- **SAP ID:** 70147829
- **Section:** S
- **GitHub Repository Link:** [https://github.com/aim1sh/LabTask_ImplicitIntents_70147829](https://github.com/aim1sh/LabTask_ImplicitIntents_70147829)
- **Submission Date:** October 2026

---

## 1. Executive Summary & Objective

The objective of this laboratory task is to design, implement, and validate a comprehensive, multi-screen Android application named **UOL Help Desk** for the Student Affairs Office at The University of Lahore. The application demonstrates core Android architecture components:
- UI layout design using `ImageView`, `EditText`, `Button`, `TextView`, and `CardView`.
- Inter-component communication using both **Implicit Intents** (`ACTION_VIEW`, `ACTION_DIAL`) and **Explicit Intents** (`Intent(Context, Class)`).
- Safe intent dispatching with centralized error-handling (`try-catch` and `ActivityNotFoundException`).
- Strict user input validation with interactive error indicators (`setError()`).
- Data passing across activities using Intent extras (`putExtra()` / `getStringExtra()`).

---

## 2. Comparison: Implicit vs. Explicit Intents (Scenario 2, Step 5)

> **1. Explicit Intent:** Specifies the exact target component class (e.g., `new Intent(MainActivity.this, RequestActivity.class)`) to perform deterministic navigation to an internal screen within the application.  
> **2. Implicit Intent:** Specifies an abstract action and a data URI (e.g., `Intent.ACTION_VIEW` with `https://...` or `Intent.ACTION_DIAL` with `tel:...`), allowing the Android operating system to search for and launch any registered system or third-party app equipped to handle that intent filter.

---

## 3. Scenario 1 — Home Screen Implementation (5 Marks)

### Step 1: Layout Design (`activity_main.xml`)
- **Description:** Designed a responsive, aesthetic interface featuring the University of Lahore crest logo (`ImageView` `ivLogo`), student name input field (`EditText` `etName` with hint `"Your name"`), and four distinct Material buttons:
  - `btnWebsite`: "Visit Website"
  - `btnCall`: "Call Help Desk"
  - `btnMap`: "Find Campus"
  - `btnRequest`: "Send a Request"
- **Implementation:** Structured inside a `ScrollView` with a custom gradient header, white elevated Material cards, rounded corners (14dp), icon gravity, and high-contrast typography following the university's emerald-and-gold theme.

### Step 2: Website Button Click Listener (`btnWebsite`)
- **Description:** Opens the official University of Lahore website (`https://www.uol.edu.pk`) in the default system browser.
- **Implementation:** Triggered using `setOnClickListener()` with an implicit Intent:
  ```java
  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.uol.edu.pk"));
  openIntent(MainActivity.this, intent);
  ```

### Step 3: Call Help Desk Click Listener (`btnCall`)
- **Description:** Opens the phone dialer with the sample help-desk phone number `04212345678` pre-typed without initiating a direct phone call (ensuring no unauthorized call charges or dangerous permissions).
- **Implementation:**
  ```java
  Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:04212345678"));
  openIntent(MainActivity.this, intent);
  ```

### Step 4: Find Campus Click Listener (`btnMap`)
- **Description:** Launches the maps application to display the location of The University of Lahore using coordinates and query parameter `geo:0,0?q=University+of+Lahore`.
- **Implementation:**
  ```java
  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=University+of+Lahore"));
  openIntent(MainActivity.this, intent);
  ```

### Step 5: Safe Helper Method (`openIntent`)
- **Description:** Centralizes intent dispatching inside a `try-catch` block to guard against application crashes if no matching application is installed on the device (e.g. AOSP emulators without a dedicated maps application).
- **Implementation:**
  ```java
  public static void openIntent(Context context, Intent intent) {
      try {
          context.startActivity(intent);
      } catch (ActivityNotFoundException e) {
          Toast.makeText(context, "No app found for this action", Toast.LENGTH_SHORT).show();
      }
  }
  ```

---

## 4. Scenario 2 — Request Screen Implementation (5 Marks)

### Step 1: Explicit Intent Navigation & Name Validation
- **Description:** Clicking "Send a Request" (`btnRequest`) checks whether `etName` is empty. If empty, it flags the input with `etName.setError("Please enter your name")`. When valid, it opens `RequestActivity` via an explicit Intent, attaching the student name using a `public static final` key constant `EXTRA_NAME`.
- **Implementation:**
  ```java
  public static final String EXTRA_NAME = "pk.edu.uol.helpdesk.EXTRA_NAME";

  btnRequest.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          String name = etName.getText().toString().trim();
          if (name.isEmpty()) {
              etName.setError("Please enter your name");
              etName.requestFocus();
              return;
          }
          Intent intent = new Intent(MainActivity.this, RequestActivity.class);
          intent.putExtra(EXTRA_NAME, name);
          startActivity(intent);
      }
  });
  ```

### Step 2: RequestActivity & Dynamic Student Greeting
- **Description:** `RequestActivity` is registered in `AndroidManifest.xml`. It reads the student's name using `getStringExtra()`. If no name was received, it falls back to `"Student"`, displaying `"Hello, <name>! How can we help you today?"` in a prominent greeting card.
- **Implementation:**
  ```java
  String studentName = getIntent().getStringExtra(MainActivity.EXTRA_NAME);
  if (studentName == null || studentName.trim().isEmpty()) {
      studentName = "Student";
  }
  tvGreeting.setText("Hello, " + studentName.trim() + "!\nHow can we help you today?");
  ```

### Step 3: Phone Field Validation & Dialer Action
- **Description:** Provides a phone input field (`inputType="phone"`, id `etPhone`) and a button "Call This Number" (`btnCallNumber`). Validates that the number consists of exactly 11 digits starting with `03` using regex `^03[0-9]{9}$`. Otherwise, highlights the error with `setError()`. Upon success, invokes the dialer through `MainActivity.openIntent()`.
- **Implementation:**
  ```java
  btnCallNumber.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          String phone = etPhone.getText().toString().trim();
          if (!phone.matches("^03[0-9]{9}$")) {
              etPhone.setError("Please enter an 11-digit number starting with 03");
              etPhone.requestFocus();
              return;
          }
          Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
          MainActivity.openIntent(RequestActivity.this, dialIntent);
      }
  });
  ```

### Step 4: Web Link Validation & Browser Action
- **Description:** Provides a link input field (`inputType="textUri"`, id `etLink`) and a button "Open Link" (`btnOpenLink`). Rejects empty input with `setError()`, automatically appends `https://` if `http://` or `https://` is omitted, and opens the URL using `MainActivity.openIntent()`.
- **Implementation:**
  ```java
  btnOpenLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          String rawLink = etLink.getText().toString().trim();
          if (rawLink.isEmpty()) {
              etLink.setError("Please enter a link");
              etLink.requestFocus();
              return;
          }
          String formattedLink = rawLink;
          if (!rawLink.toLowerCase().startsWith("http://") && !rawLink.toLowerCase().startsWith("https://")) {
              formattedLink = "https://" + rawLink;
          }
          Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(formattedLink));
          MainActivity.openIntent(RequestActivity.this, browserIntent);
      }
  });
  ```

### Step 5: Emulator Testing & Verification
All features were tested on an Android emulator (`Pixel_API_34`, Android 14). All buttons, validations, and external apps functioned as specified.

---

## 5. Visual Evidence & Screenshots

| Figure 1: Home Screen (Scenario 1) | Figure 2: Name Input Validation Error | Figure 3: Official Website Opened |
|:---:|:---:|:---:|
| ![Home Screen](screenshots/01_home_screen.png) | ![Name Validation](screenshots/02_validation_error_name.png) | ![Browser](screenshots/03_browser_opened.png) |
| *Figure 1: Home screen with UOL branding and buttons* | *Figure 2: `setError()` triggered on empty name* | *Figure 3: `ACTION_VIEW` web browser* |

| Figure 4: Phone Dialer with Help Desk Number | Figure 5: Safe Toast Handler for Map | Figure 6: Request Screen Greeting |
|:---:|:---:|:---:|
| ![Dialer](screenshots/04_dialer_opened.png) | ![Toast](screenshots/05_find_campus_toast.png) | ![Request Screen](screenshots/07_request_screen_greeting.png) |
| *Figure 4: `ACTION_DIAL` with `04212345678`* | *Figure 5: `openIntent()` catches `ActivityNotFoundException`* | *Figure 6: Personalized student greeting* |

| Figure 7: Phone Field Validation Error | Figure 8: Custom Number in Phone Dialer | Figure 9: Link Field Validation Error |
|:---:|:---:|:---:|
| ![Phone Error](screenshots/08_phone_validation_error.png) | ![Custom Dialer](screenshots/09_custom_phone_dialer.png) | ![Link Error](screenshots/12_link_error_validation.png) |
| *Figure 7: Phone format rejection (`^03[0-9]{9}$`)* | *Figure 8: `03001234567` launched into dialer* | *Figure 9: Empty link input rejection* |

---

## 6. Self-Assessment against Rubric

| Assessed Task | Evidence Provided | Marks | Status |
|:---|:---|:---:|:---:|
| **Scenario 1, Step 1** — Home screen layout | UOL logo, name field (`etName`), and four buttons with given IDs (`btnWebsite`, `btnCall`, `btnMap`, `btnRequest`) | 1 | Completed |
| **Scenario 1, Steps 2–4** — Implicit Intents | Correct actions (`ACTION_VIEW`, `ACTION_DIAL`, `ACTION_VIEW geo:`) triggered via `setOnClickListener()` | 3 | Completed |
| **Scenario 1, Step 5** — Safe helper `openIntent()` | `openIntent()` with `try-catch`, `ActivityNotFoundException`, and Toast `"No app found for this action"` | 1 | Completed |
| **Scenario 2, Steps 1–2** — Explicit Intent & greeting | `etName` validated with `setError()`, name carried via `EXTRA_NAME` constant, personalized greeting in `RequestActivity` | 2 | Completed |
| **Scenario 2, Steps 3–4** — Validated dial & link | Phone (11 digits starting with 03) and web link (auto `https://`) validated before launching through `openIntent()` | 2 | Completed |
| **Scenario 2, Step 5** — Testing & comparison | All screenshots captured from emulator and two-line comparison documented | 1 | Completed |
| **Total** | | **10 / 10** | **100%** |
