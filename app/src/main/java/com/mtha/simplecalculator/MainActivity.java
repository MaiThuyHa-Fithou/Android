package com.mtha.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    // Mang chua cac ID của cac nut so
    private int[] numericButtons = {R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine};
    // Mang chua ID cac nut phep toan
    private int[] operatorButtons = {R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide};
    // Textview hien thi ket qua
    private TextView txtScreen;
    // Kiem tra xem nut cuoi cung co phai la nut so khong
    private boolean lastNumeric;
    // kiem tra trang thai hien tai co loi hay khong
    private boolean stateError;
    // Neu dung thi khong cho phep them Dau Cham thap phan
    private boolean lastDot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Gan dieu khien txtScreen voi doi tuong UI TextView
        this.txtScreen = (TextView) findViewById(R.id.txtScreen);
        // Tim va dat su kien OnClickListener cho cac nut so
        setNumericOnClickListener();
        // Tim va dat su kien OnClickListener cho cac nut toan tu, nut dau bang va nut dau cham thap phan
        setOperatorOnClickListener();
    }
    /**
     * Tim va dat su kien OnClickListener cho cac nut so
     */
    private void setNumericOnClickListener() {
        // Tao mot xu ly su kien OnClickListener chung
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Noi va thiet lap nhan cua cac nut da click
                Button button = (Button) v;
                if (stateError) {
                    // Neu trang thai la loi, hien thi thong bao loi
                    txtScreen.setText(button.getText());
                    stateError = false;
                } else {
                    // Neu khong loi, thi them gia tri text cua nut do vao bieu thuc
                    txtScreen.append(button.getText());
                }
                // Dat co kiem tra xem la nut so cuoi cung chua?
                lastNumeric = true;
            }
        };
        // Set su kien OnClickListener cho tat ca cac nut so
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * Tim va dat su kien OnClickListener cho cac nut toan tu, nut dau bang va nut dau cham thap phan
     */
    private void setOperatorOnClickListener() {
        // Tao mot xu ly su kien OnClickListener chung cho cac toan tu
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Neu trang thai hien tai la Loi, khong noi toan tu
                // Neu dau vao cuoi cung chi la so, hay them toan tu
                if (lastNumeric && !stateError) {
                    Button button = (Button) v;
                    txtScreen.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;    //Dat lai co dau cham thap phan
                }
            }
        };
        // Set xu ly su kien OnClickListener cho tat ca cac nut toan tu
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        // Dau cham thap phan
        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    txtScreen.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });
        // Nut xoa
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText("");  // Xoa du lieu tren man hinh
                // Dat lai tat ca cac trang thai va co
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });
        // Nut dau bang
        findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }

    /**
     * Giai phap cho tinh toan don gian Calculator
     */
    private void onEqual() {
        // Neu trang thai hien tai la loi, thi khong lam gi
        // Neu dau vao cuoi cung chi la mot so, co the tim thay giai phap.
        if (lastNumeric && !stateError) {
            // Doc day bieu thuc
            String txt = txtScreen.getText().toString();
            // Tao mot bieu thuc su dung lop Expression cua thu vien exp4j
            // tham khao tai: https://www.objecthunter.net/exp4j/
            //thu vien ho tro tinh toan tren bieu thuc
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // Tinh toan va hien thi ket qua
                double result = expression.evaluate();
                txtScreen.setText(Double.toString(result));
                lastDot = true; // Ket qua chua 1 dau cham thap phan
            } catch (ArithmeticException ex) {
                // Hien thi thong bao loi
                txtScreen.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}