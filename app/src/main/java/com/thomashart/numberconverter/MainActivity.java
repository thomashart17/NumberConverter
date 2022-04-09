package com.thomashart.numberconverter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigInteger;
import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BINARY, DECIMAL, HEXADECIMAL, OCTAL})
    private @interface NumberSystem {}
    private static final int BINARY = 0;
    private static final int DECIMAL = 1;
    private static final int HEXADECIMAL = 2;
    private static final int OCTAL = 3;

    private @NumberSystem int inputSystem;
    private @NumberSystem int outputSystem;

    private HashMap<Integer, Integer> digitsMap = new HashMap<>();

    private Spinner inputSpinner, outputSpinner;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private String inputText = "";
    private String answer = "";
    private TextView inputTextView;
    private TextView answerTextView;

    private Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTextView = findViewById(R.id.input_text);
        answerTextView = findViewById(R.id.answer_view);

        inputSystem = BINARY;
        outputSystem = DECIMAL;

        initAnswerClipboard();
        initButtons();
        initHashMap();
        initSpinners();
    }

    @Override
    public void onClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
        switch (view.getId()) {
            case R.id.clear_button:
                clear();
                break;
            case R.id.delete_button:
                if (!inputText.equals("")) {
                    inputText = inputText.substring(0, inputText.length() - 1);
                    inputTextView.setText(inputText);
                }
                break;
            case R.id.equals_button:
                if (!inputText.equals("")) calculate();
                break;
            case R.id.switch_button:
                @NumberSystem int temp = inputSystem;
                inputSystem = outputSystem;
                outputSystem = temp;
                inputSpinner.setSelection(inputSystem);
                outputSpinner.setSelection(outputSystem);
                break;
            default:
                Button pressedButton = findViewById(view.getId());
                String text = pressedButton.getText().toString();
                inputText = inputText + text;
                inputTextView.setText(inputText);
        }
    }

    private void initSpinners() {
        inputSpinner = findViewById(R.id.input_spinner);
        outputSpinner = findViewById(R.id.outputSpinner);

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);

        inputSpinner.setAdapter(arrayAdapter);
        outputSpinner.setAdapter(arrayAdapter);

        inputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, @NumberSystem int position, long id) {
                inputSystem = position;
                setMode(position);
                clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        outputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, @NumberSystem int position, long id) {
                outputSystem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initButtons() {
        buttons = new Button[] {
                findViewById(R.id.zero_button),
                findViewById(R.id.one_button),
                findViewById(R.id.two_button),
                findViewById(R.id.three_button),
                findViewById(R.id.four_button),
                findViewById(R.id.five_button),
                findViewById(R.id.six_button),
                findViewById(R.id.seven_button),
                findViewById(R.id.eight_button),
                findViewById(R.id.nine_button),
                findViewById(R.id.a_button),
                findViewById(R.id.b_button),
                findViewById(R.id.c_button),
                findViewById(R.id.d_button),
                findViewById(R.id.e_button),
                findViewById(R.id.f_button)
        };
    }

    private void initHashMap() {
        digitsMap.put(0, 2);
        digitsMap.put(1, 10);
        digitsMap.put(2, 16);
        digitsMap.put(3, 8);
    }

    private void initAnswerClipboard() {
        answerTextView.setOnClickListener(view -> {
            if (!answer.equals("")) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Answer", answer);
                clipboardManager.setPrimaryClip(clipData);
            }
        });
    }

    private void setMode(@NumberSystem int mode) {
        int digits = digitsMap.get(mode);
        for (int i = 0; i < digits; i++) {
            buttons[i].setEnabled(true);
            buttons[i].setTextColor(getColor(R.color.white));
        }
        for (int i = digits; i < 16; i++) {
            buttons[i].setEnabled(false);
            buttons[i].setTextColor(getColor(R.color.divider));
        }
    }

    private void clear() {
        inputText = "";
        answer = "";
        inputTextView.setText("");
        answerTextView.setText("");
    }

    private void calculate() {
        BigInteger answerInt = new BigInteger(inputText, digitsMap.get(inputSystem));
        answer = answerInt.toString(digitsMap.get(outputSystem)).toUpperCase();
        answerTextView.setText(answer);
    }
}