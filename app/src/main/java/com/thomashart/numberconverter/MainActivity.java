package com.thomashart.numberconverter;

import android.app.Activity;
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

    private Spinner inputSpinner, outputSpinner;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private String inputText = "";
    private TextView inputTextView;
    private TextView answerTextView;
    private boolean decimalStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTextView = findViewById(R.id.input_text);
        answerTextView = findViewById(R.id.answer_view);

        inputSystem = BINARY;
        outputSystem = DECIMAL;

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
                    if (inputText.charAt(inputText.length() - 1) == '.') {
                        decimalStatus = false;
                    }
                    inputText = inputText.substring(0, inputText.length() - 1);
                    inputTextView.setText(inputText);
                }
                break;
            case R.id.decimal_button:
                if (!decimalStatus) {
                    inputText = inputText + ".";
                    inputTextView.setText(inputText);
                    decimalStatus = true;
                }
                break;
            case R.id.equals_button:
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

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputSpinner.setAdapter(arrayAdapter);
        outputSpinner.setAdapter(arrayAdapter);

        inputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, @NumberSystem int position, long id) {
                inputSystem = position;
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

    private void clear() {
        inputText = "";
        inputTextView.setText("");
        answerTextView.setText("");
        decimalStatus = false;
    }
}