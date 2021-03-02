package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button _resetBtn, _scoreBtn, _commaBtn, _bracketBtn, _plusBtn, _minusBtn, _multiplyBtn, _devideBtn, _zeroBtn, _oneBtn, _twoBtn, _threeBtn, _fourBtn, _fiveBtn, _sixBtn, _sevenBtn,
            _eightBtn, _nineBtn;
    private ImageButton _deleteBtn;
    private EditText scoreEditTxt;
    private TextView scoreTxt;
    private ArrayList<String> listnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            inicialize();
            listnumber = new ArrayList<>();
            scoreEditTxt.setShowSoftInputOnFocus(false);

            _resetBtn.setOnClickListener(this);
            _scoreBtn.setOnClickListener(this);
            _commaBtn.setOnClickListener(this);
            _bracketBtn.setOnClickListener(this);
            _plusBtn.setOnClickListener(this);
            _minusBtn.setOnClickListener(this);
            _multiplyBtn.setOnClickListener(this);
            _devideBtn.setOnClickListener(this);
            _zeroBtn.setOnClickListener(this);
            _oneBtn.setOnClickListener(this);
            _twoBtn.setOnClickListener(this);
            _threeBtn.setOnClickListener(this);
            _fourBtn.setOnClickListener(this);
            _fiveBtn.setOnClickListener(this);
            _sixBtn.setOnClickListener(this);
            _sevenBtn.setOnClickListener(this);
            _eightBtn.setOnClickListener(this);
            _nineBtn.setOnClickListener(this);
            _deleteBtn.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicialize() {
        _resetBtn = findViewById(R.id.ResetBtn);
        _scoreBtn = findViewById(R.id.ScoreBtn);
        _commaBtn = findViewById(R.id.CommaBtn);
        _bracketBtn = findViewById(R.id.BracketBtn);
        _plusBtn = findViewById(R.id.PlusBtn);
        _minusBtn = findViewById(R.id.MinusBtn);
        _multiplyBtn = findViewById(R.id.MultiplyBtn);
        _devideBtn = findViewById(R.id.DevideBtn);
        _zeroBtn = findViewById(R.id.ZeroBtn);
        _oneBtn = findViewById(R.id.OneBtn);
        _twoBtn = findViewById(R.id.TwoBtn);
        _threeBtn = findViewById(R.id.ThreeBtn);
        _fourBtn = findViewById(R.id.FourBtn);
        _fiveBtn = findViewById(R.id.FiveBtn);
        _sixBtn = findViewById(R.id.SixBtn);
        _sevenBtn = findViewById(R.id.SevenBtn);
        _eightBtn = findViewById(R.id.EightBtn);
        _nineBtn = findViewById(R.id.NineBtn);
        _deleteBtn = findViewById(R.id.DeleteBtn);
        scoreEditTxt = findViewById(R.id.ScoreEditView);
        scoreTxt = findViewById(R.id.OpertationTextView);

    }

    @Override
    public void onClick(View view) {
        String scoretext;
        int start = scoreEditTxt.getSelectionStart();
        String[] charsoperation = {"+", "-", "÷", "x"};
        switch (view.getId()) {
            case R.id.ResetBtn:
                scoreEditTxt.setText("");
                scoreEditTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dpToPx(40, MainActivity.this));
                scoreTxt.setText("");
                break;
            case R.id.ScoreBtn:
                updatelist();
                calculate(listnumber);
                break;
            case R.id.CommaBtn:
                scoreEditTxt.getText().insert(start, ".");
                break;
            case R.id.BracketBtn:
                scoretext = scoreEditTxt.getText().toString();
                if (scoreEditTxt.getText().length() < 25) {
                    if (scoreEditTxt.getText().length() > 0) {
                        if (!checkchar(scoretext.substring(start - 1, start), charsoperation)) {
                            scoreEditTxt.getText().insert(start, "x");
                            scoreEditTxt.getText().insert(start + 1, "()");
                        } else
                            scoreEditTxt.getText().insert(start, "()");
                    } else
                        scoreEditTxt.getText().insert(start, "()");
                } else
                    Toast.makeText(MainActivity.this, "Too many characters", Toast.LENGTH_SHORT).show();
                break;
            case R.id.PlusBtn:
                setoperation("+", start);
                break;
            case R.id.MinusBtn:
                setoperation("-", start);
                break;
            case R.id.MultiplyBtn:
                setoperation("x", start);
                break;
            case R.id.DevideBtn:
                setoperation("÷", start);
                break;
            case R.id.DeleteBtn:
                StringBuilder sb = new StringBuilder(scoreEditTxt.getText().toString());
                if (scoreEditTxt.getText().length() > 0 & start > 0) {
                    sb.deleteCharAt(start - 1);
                    scoreEditTxt.setText(sb.toString());
                    scoreEditTxt.setSelection(start - 1);
                }
                setSize(3);
                break;
            case R.id.ZeroBtn:
                setText("0", start);
                break;
            case R.id.OneBtn:
                setText("1", start);
                break;
            case R.id.TwoBtn:
                setText("2", start);
                break;
            case R.id.ThreeBtn:
                setText("3", start);
                break;
            case R.id.FourBtn:
                setText("4", start);
                break;
            case R.id.FiveBtn:
                setText("5", start);
                break;
            case R.id.SixBtn:
                setText("6", start);
                break;
            case R.id.SevenBtn:
                setText("7", start);
                break;
            case R.id.EightBtn:
                setText("8", start);
                break;
            case R.id.NineBtn:
                setText("9", start);
                break;
        }
    }

    private void updatelist() {
        listnumber.clear();
        String text = scoreEditTxt.getText().toString();
        String[] chars = {"+", "-", "÷", "x", "(", ")"};
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            if (i == 0) {
                if (checklist(text.substring(i, i + 1), chars, listnumber, "")) {
                    listnumber.add(text.substring(j, i));
                    listnumber.add(text.substring(i, i + 1));
                    j = i + 1;
                }
            } else if (i == text.length() - 1 && text.length() > 2 && !checklist(text.substring(text.length() - 1, text.length()), chars, listnumber, text.substring(text.length() - 2, text.length() - 1))) {
                int p = 0;
                for (int k = text.length(); k > 1; k--) {
                    if (!checklist(text.substring(k - 1, k), chars, listnumber, text.substring(k - 2, k - 1)))
                        p = p + 1;
                    else
                        break;
                }
                if (!checklist(text.substring(text.length() - 1, text.length()), chars, listnumber, text.substring(text.length() - 2, text.length() - 1))) {
                    listnumber.add(text.substring(text.length() - p, text.length()));
                }
            } else {
                if (checklist(text.substring(i, i + 1), chars, listnumber, text.substring(i - 1, i))) {
                    listnumber.add(text.substring(j, i));
                    listnumber.add(text.substring(i, i + 1));
                    j = i + 1;
                }
            }
        }
    }

    private void calculate(ArrayList<String> list) {
        try {
            double result = 0;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals(""))
                    list.remove(j);
            }
            int i = list.size() - 1;
            int ik = i;
            int j = 0;
            while (ik != 0 || list.get(0).equals("(")) {
                if (list.get(ik).equals("(")) {
                    if (!list.get(ik + 2).equals(")")) {
                        for (j = ik; j < list.size() - 1; j++) {
                            if (list.get(j).equals(")")) {
                                break;
                            }
                        }
                        int l = checkoperator(ik + 1, j - 1, list);
                        if (checkoperator(ik + 1, j - 1, list) != 0) {
                            result = scoreoper(Double.parseDouble(list.get(l - 1)), Double.parseDouble(list.get(l + 1)), operator(list.get(l)));
                            for (int k = 0; k <= 2; k++)
                                list.remove(l - 1);
                            list.add(l - 1, "" + result);
                        } else {
                            result = scoreoper(Double.parseDouble(list.get(ik + 1)), Double.parseDouble(list.get(ik + 3)), operator(list.get(ik + 2)));
                            for (int k = 0; k <= 2; k++)
                                list.remove(ik + 1);
                            list.add(ik + 1, "" + result);
                        }
                    } else {
                        list.remove(ik);
                        list.remove(ik + 1);
                    }
                } else
                    ik--;
            }
            int lk = checkoperator(0, list.size() - 1, list);
            int l = lk;
            while (l != 0) {
                if (l != 0) {
                    result = scoreoper(Double.parseDouble(list.get(l - 1)), Double.parseDouble(list.get(l + 1)), operator(list.get(l)));
                    for (int k = 0; k <= 2; k++)
                        list.remove(l - 1);
                    list.add(l - 1, "" + result);
                    l = checkoperator(0, list.size() - 1, list);
                }
            }
            int z = list.size() - 1;
            int zk = z;
            while (list.size() - 1 != 0) {
                result = scoreoper(Double.parseDouble(list.get(0)), Double.parseDouble(list.get(2)), operator(list.get(1)));
                for (int k = 0; k <= 2; k++)
                    list.remove(0);
                list.add(0, "" + result);
            }
            double result1 = result;
            if ((result1 % 1) == 0)
                scoreTxt.setText("" + (int) result1);
            else
                scoreTxt.setText("" + result1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setoperation(String operation, int start) {
        if (scoreEditTxt.getText().length() < 35) {
            String[] charsoperation = {"+", "-", "÷", "x"};
            String scoretext = scoreEditTxt.getText().toString();
            if (!scoretext.equals(""))
                if (checkchar(scoretext.substring(start - 1, start), charsoperation)) {
                    scoreEditTxt.setText(scoretext.replace(scoretext.substring(start - 1, start), operation));
                    scoreEditTxt.setSelection(start);
                } else
                    scoreEditTxt.getText().insert(start, operation);
            setSize(-3);
        } else
            Toast.makeText(MainActivity.this, "Too many characters", Toast.LENGTH_SHORT).show();
    }

    private void setText(String text, int start) {
        if (scoreEditTxt.getText().length() < 35) {
            String scoretext = scoreEditTxt.getText().toString();
            if (scoretext.length() > 1) {
                if (start == 0)
                    scoreEditTxt.getText().insert(start, text);
                else if (!scoretext.substring(start - 1, start).equals(")"))
                    scoreEditTxt.getText().insert(start, text);
                else
                    scoreEditTxt.getText().insert(start, "x" + text);
            } else
                scoreEditTxt.getText().insert(start, text);
            updatelist();
            setSize(-3);
        } else
            Toast.makeText(MainActivity.this, "Too many characters", Toast.LENGTH_SHORT).show();
    }

    private boolean checklist(String a, String[] chars, ArrayList<String> list, String lasttext) {
        for (int j = chars.length - 1; j >= 0; j--) {
            if (a.equals(chars[j])) {
                if (list.size() > 0) {
                    if (lasttext.equals("(")) {
                        if (chars[j].equals("-"))
                            return false;
                        else
                            return true;
                    } else
                        return true;
                } else
                    return true;
            }
        }
        return false;
    }

    private static float pixelsToSp(Context context, Float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    private static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private void setSize(int diff) {
        float k;
        if (scoreEditTxt.getText().length() > 10) {
            k = scoreEditTxt.getTextSize();
            scoreEditTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, pixelsToSp(MainActivity.this, k + diff));
        }
    }

    private boolean checkchar(String a, String[] chars) {
        for (int j = chars.length - 1; j >= 0; j--) {
            if (a.equals(chars[j]))
                return true;
        }
        return false;
    }

    private double scoreoper(double d1, double d2, int oper) {
        if (oper == 1)
            return d1 + d2;
        if (oper == 2)
            return d1 - d2;
        if (oper == 3)
            return d1 / d2;
        if (oper == 4)
            return d1 * d2;
        return 0;
    }

    private int operator(String operator) {
        if (operator.equals("+"))
            return 1;
        if (operator.equals("-"))
            return 2;
        if (operator.equals("÷"))
            return 3;
        if (operator.equals("x"))
            return 4;
        return 0;
    }

    private int checkoperator(int start, int stop, ArrayList<String> list) {
        for (int i = start; i < stop; i++) {
            if (list.get(i).equals("x"))
                return i;
            if (list.get(i).equals("÷"))
                return i;
        }
        return 0;
    }
}

