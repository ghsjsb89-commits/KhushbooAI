package com.khushboo.ai;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.*;
import android.graphics.Color;
import android.view.Gravity;
import java.util.Locale;
import java.util.ArrayList;

public class MainActivity extends Activity {

    TextToSpeech voice;
    TextView output;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(25,25,25,25);
        layout.setBackgroundColor(Color.rgb(3,8,22));

        TextView title = new TextView(this);
        title.setText("KHUSHBOO AI");
        title.setTextColor(Color.CYAN);
        title.setTextSize(32);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("JARVIS VIP SYSTEM");
        subtitle.setTextColor(Color.YELLOW);
        subtitle.setTextSize(17);
        subtitle.setGravity(Gravity.CENTER);

        output = new TextView(this);
        output.setText("Assalamualaikum! Main Khushboo hoon.");
        output.setTextColor(Color.WHITE);
        output.setTextSize(18);
        output.setGravity(Gravity.CENTER);

        Button talk = new Button(this);
        talk.setText("MIC - TALK TO KHUSHBOO");

        Button youtube = new Button(this);
        youtube.setText("OPEN YOUTUBE");

        Button whatsapp = new Button(this);
        whatsapp.setText("OPEN WHATSAPP");

        Button phone = new Button(this);
        phone.setText("OPEN PHONE");

        layout.addView(title);
        layout.addView(subtitle);
        layout.addView(output);
        layout.addView(talk);
        layout.addView(youtube);
        layout.addView(whatsapp);
        layout.addView(phone);

        setContentView(layout);

        voice = new TextToSpeech(this, status -> {
            if(status == TextToSpeech.SUCCESS) {
                voice.setLanguage(new Locale("ur","PK"));
            }
        });

        talk.setOnClickListener(v -> listen());

        youtube.setOnClickListener(v ->
            openApp("com.google.android.youtube"));

        whatsapp.setOnClickListener(v ->
            openApp("com.whatsapp"));

        phone.setOnClickListener(v ->
            startActivity(new Intent(Intent.ACTION_DIAL)));
    }

    void listen() {
        Intent intent = new Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "ur-PK");

        try {
            startActivityForResult(intent,100);
            output.setText("Khushboo is listening...");
        } catch(Exception e) {
            output.setText("Voice service unavailable.");
        }
    }

    @Override
    protected void onActivityResult(
        int request, int result, Intent data) {

        super.onActivityResult(request,result,data);

        if(request == 100 && result == RESULT_OK
            && data != null) {

            ArrayList<String> words =
                data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            if(words != null && !words.isEmpty()) {
                command(words.get(0));
            }
        }
    }

    void command(String text) {
        String s = text.toLowerCase(Locale.ROOT);

        if(s.contains("youtube") || s.contains("یوٹیوب")) {
            openApp("com.google.android.youtube");
            speak("جی، یوٹیوب کھول رہی ہوں۔");
        }
        else if(s.contains("whatsapp") || s.contains("واٹس ایپ")) {
            openApp("com.whatsapp");
            speak("جی، واٹس ایپ کھول رہی ہوں۔");
        }
        else if(s.contains("call") || s.contains("کال")) {
            speak("جی، فون کھول رہی ہوں۔");
            startActivity(new Intent(Intent.ACTION_DIAL));
        }
        else {
            speak("جی، میں سن رہی ہوں۔");
        }
    }

    void openApp(String name) {
        Intent i = getPackageManager()
            .getLaunchIntentForPackage(name);

        if(i != null) {
            startActivity(i);
        } else {
            speak("یہ ایپ انسٹال نہیں ہے۔");
        }
    }

    void speak(String message) {
        output.setText(message);

        if(voice != null) {
            voice.speak(message,
                TextToSpeech.QUEUE_FLUSH,
                null,"KHUSHBOO");
        }
    }

    @Override
    protected void onDestroy() {
        if(voice != null) {
            voice.shutdown();
        }
        super.onDestroy();
    }
}
