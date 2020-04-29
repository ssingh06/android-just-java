package com.example.android.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.justjava.util.Constants;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method executes the logic to submit the current order for coffees.
     * @param view view that invoked this method
     */
    public void submitOrder(View view) {
        /* invoke email app and include order summary in the mail body */
        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.NEW_ORDER_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, getOrderSummary());
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    /**
     * This method increments the number of coffees in the order.
     * @param view view that invoked this method
     */
    public void incrementOrder(View view) {
        final int numberOfCoffees = getNumberOfCoffees();
        if (numberOfCoffees == Constants.MAX_NUM_COFFEES_IN_ORDER) {
            Toast.makeText(getApplicationContext(),
                    "Maximum number of coffees in one order reached",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        displayQuantity(numberOfCoffees+1);
    }

    /**
     * This method decrements the number of coffees in the order.
     * @param view view that invoked this method
     */
    public void decrementOrder(View view) {
        final int numberOfCoffees = getNumberOfCoffees();
        if (numberOfCoffees == Constants.MIN_NUM_COFFEES_IN_ORDER) {
            Toast.makeText(getApplicationContext(),
                    "Minimum number of coffees in one order reached",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        displayQuantity(numberOfCoffees-1);
    }

    /**
     * This method displays the number of coffees in the current order
     * @param quantity quantity to display
     */
    private void displayQuantity(int quantity) {
        final TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(quantity));
    }

    /**
     * This method retrieves the number of coffees in the current order from quantity_text_view
     * @return int number of coffees
     */
    private int getNumberOfCoffees() {
        return Integer.parseInt(((TextView)findViewById(R.id.quantity_text_view)).getText().toString());
    }

    /**
     * This method returns the order summary to be displayed to the user.
     * @return String order summary
     */
    private String getOrderSummary() {
        final int numberOfCoffees = getNumberOfCoffees();
        if (numberOfCoffees == 0) {
            return Constants.FREE_MESSAGE;
        }

        final String name = ((EditText) findViewById(R.id.name_edit_text))
                .getText().toString();

        return Constants.NAME + ": " +
                (name.isEmpty() ? Constants.DEFAULT_NAME : name) + "\n" +
                getToppingsSummary() + "\n" +
                Constants.QUANTITY + ": " + numberOfCoffees + "\n" +
                Constants.TOTAL + ": " +
                NumberFormat.getCurrencyInstance().format(getTotalPrice()) + "\n" +
                Constants.BYE_GREETING;
    }

    /**
     * This method returns the details about the user selected toppings in the
     * order
     * @return String toppings summary
     */
    private String getToppingsSummary() {
        return Constants.WHIPPED_CREAM_PREFIX + " " +
                ((CheckBox) findViewById(R.id.whipped_cream_checkbox))
                        .isChecked() + "\n" +
                Constants.CHOCOLATE_PREFIX + " " +
                ((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked();
    }

    /**
     * This method returns the total order price.
     * @return int total order price.
     */
    private int getTotalPrice() {
        int unitPrice = 0;
        if (((CheckBox) findViewById(R.id.whipped_cream_checkbox)).isChecked()) {
            unitPrice += Constants.PRICE_TOPPING_WHIPPED_CREAM;
        }

        if (((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked()) {
            unitPrice += Constants.PRICE_TOPPING_CHOCOLATE;
        }

        unitPrice += Constants.PRICE_PER_COFFEE;
        return getNumberOfCoffees() * unitPrice;
    }
}
