package com.example.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private static final int coffeePrice = 5;
    private static final int whippedCreamPrice = 1;
    private static final int chocolatePrice = 2;
    private static final int minimumNumberOfCoffees = 0;
    private static final int maximumNumberOfCoffees = 100;
    int numberOfCoffees = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayOrderSummaryPrice(getOrderSummaryPrice());
    }

    //-----------//
    //-- VIEWS --//
    //-----------//

    public void submitOrder(View view) {
        boolean isValidate = validateUserName(hasUserName(getUserName()));

        if (!isValidate) {
            return;
        }

        if (numberOfCoffees == 0) {
            displayOrderSummaryPrice(getOrderSummaryPrice());
        } else {
            displayOrderSummary(getOrderSummary());
        }
    }

    public void decrement(View view) {
        decreaseQuantity();

        displayQuantity(numberOfCoffees);
    }

    public void increment(View view) {
        increaseQuantity();

        displayQuantity(numberOfCoffees);
    }

    //-----------//
    //-- FUNCTIONS --//
    //-----------//

    private String getOrderSummaryPrice() {
        int price = getTotalAmount(numberOfCoffees, getCoffeePrice(coffeePrice, hasWhippedCream(), hasChocolate()));

        return getFormattedPrice(price);
    }

    private void displayOrderSummaryPrice(String orderSummary) {
        TextView priceTextView = findViewById(R.id.price_text_view);

        priceTextView.setText(getString(R.string.order_summary_price, getOrderSummaryPrice()));
    }

    private void displayOrderSummary(String orderSummary) {
        TextView priceTextView = findViewById(R.id.price_text_view);

        priceTextView.setText(orderSummary);
    }

    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);

        quantityTextView.setText("" + number);
    }

    public void decreaseQuantity() {
        if (numberOfCoffees > minimumNumberOfCoffees) {
            numberOfCoffees--;
        }
    }

    public void increaseQuantity() {
        if (numberOfCoffees < maximumNumberOfCoffees) {
            numberOfCoffees++;
        }
    }

    private int getTotalAmount(int numberOfCoffees, int coffeePrice) {
        return numberOfCoffees * coffeePrice;
    }

    private int getCoffeePrice(int coffeePrice, boolean hasWhippedCream, boolean hasChocolate) {
        int totalCoffeePrice = coffeePrice;

        if (hasWhippedCream)
            totalCoffeePrice += whippedCreamPrice;
        if (hasChocolate)
            totalCoffeePrice += chocolatePrice;

        return totalCoffeePrice;
    }

    private boolean hasWhippedCream() {
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream);

        return whippedCreamCheckBox.isChecked();
    }

    private boolean hasChocolate() {
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate);

        return chocolateCheckBox.isChecked();
    }

    private String getOrderSummary() {
        String orderSummary = getString(R.string.order_summary_name, getUserName());
        orderSummary += "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream());
        orderSummary += "\n" + getString(R.string.order_summary_chocolate, hasChocolate());
        orderSummary += "\n" + getString(R.string.order_summary_quantity, numberOfCoffees);
        orderSummary += "\n" + getString(R.string.order_summary_price, getOrderSummaryPrice());
        orderSummary += "\n" + getString(R.string.thank_you);

        return orderSummary;
    }

    private String getFormattedPrice(int price) {
        return NumberFormat.getCurrencyInstance().format(price);
    }

    private String getUserName() {
        EditText userNameText = findViewById(R.id.user_name);

        return userNameText.getText().toString();
    }

    private boolean hasUserName(String userName) {
        return !TextUtils.isEmpty(userName);
    }

    public void sendOrderEmail(View view) {
        boolean isValidate = validateUserName(hasUserName(getUserName()));

        if (!isValidate) {
            return;
        }

        if (numberOfCoffees > 0) {
            sendOrderSummaryToEmailApp(getOrderSummary(), getOrderSummaryEmailSubject());
        }
    }

    private void sendOrderSummaryToEmailApp(String orderSummary, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private String getOrderSummaryEmailSubject() {
        return getString(R.string.order_summary_email_subject, getUserName());
    }

    private boolean validateUserName(boolean hasUserName) {
        if (hasUserName) return true;

        EditText editText = findViewById(R.id.user_name);
        editText.setError(getString(R.string.empty_name_error));

        return false;
    }
}