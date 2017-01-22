package com.skuhleesi.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.name;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    boolean addWhippedCream = false;
    boolean addChocolate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the + button is clicked
     */

    public void increment(View view){

        quantity = quantity + 1;

        //make sure we don't display over 100 coffees
        if(quantity >= 101){
            Context context = getApplicationContext();
            CharSequence text = "Can't have more than 100 coffees!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            quantity = 100;
            displayQuantity(quantity);
        }

        displayQuantity(quantity);
    }

    /**
     * This method is called when the + button is clicked
     */

    public void decrement(View view){
        quantity -= 1;

        //make sure we don't display less than one coffee
        if(quantity <= 0){
            Context context = getApplicationContext();
            CharSequence text = "Can't have less than 1 coffee!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            quantity = 1;
            displayQuantity(quantity);
        }
        displayQuantity(quantity);
    }

    /**
     * This method will tell if whipped cream is checked or not
     */
    public void isChecked(View view){
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        addWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        addChocolate = chocolateCheckBox.isChecked();
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.name);
        String names = nameEditText.getText().toString();

        String priceMessage = createOrderSummary(names) + "\nTotal: $" +calculatePrice() +"\n" + getString(R.string.thank_you);


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(intent.EXTRA_SUBJECT, "JustJava order for " + names);
        intent.putExtra(intent.EXTRA_TEXT, priceMessage);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
        displayMessage(priceMessage);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method will calculate the cost of coffee
     */
    public int calculatePrice(){
        //Price one cup of coffee
        int basePrice = 5;

        //User wants whipped cream add 1 dollar
        if(addWhippedCream){
            basePrice += 1;
        }

        //User wants chocolate add 2 dollars
        if(addChocolate){
            basePrice += 2;
        }
        //Calculate total order cost
        int price = quantity * basePrice;

        return price;
    }

    /**
     * This method makes an order summary
     */

    public String createOrderSummary(String name){
        String customerName = getString(R.string.order_summary_name, name);
        String toppingWhipped = "\nAdd whipped cream? " + addWhippedCream;
        String toppingChocolate = "\nAdd chocolate? " +addChocolate;
        String amount = "\nQuantity: " + quantity;
        String orderSummary = customerName + toppingWhipped + toppingChocolate + amount;
        return orderSummary;
    }
}