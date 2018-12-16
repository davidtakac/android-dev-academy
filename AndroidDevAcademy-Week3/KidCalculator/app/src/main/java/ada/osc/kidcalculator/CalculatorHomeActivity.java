package ada.osc.kidcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculatorHomeActivity extends AppCompatActivity {

    private String emptyFieldError;
    private String dividingByZeroError;

    @BindView(R.id.edittext_firstnum)
    EditText firstNumEditText;
    @BindView(R.id.edittext_secondnum)
    EditText secondNumEditText;
    @BindView(R.id.textview_result)
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_home);

        ButterKnife.bind(this);
        emptyFieldError = getString(R.string.edittext_emptyerror);
        dividingByZeroError = getString(R.string.edittext_dividebyzeroerror);

        this.addWatchersToEditTexts();
    }

    private void addWatchersToEditTexts() {
        firstNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (firstNumEditText.getText().toString().isEmpty()) {
                    firstNumEditText.setError(emptyFieldError);
                }
            }

        });

        secondNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = secondNumEditText.getText().toString();
                if (text.isEmpty()) {
                    secondNumEditText.setError(emptyFieldError);
                }
            }
        });
    }

    @OnClick(R.id.button_sum)
    public void sum() {
        double first, second;
        try {
            first = Double.parseDouble(firstNumEditText.getText().toString());
            second = Double.parseDouble(secondNumEditText.getText().toString());
        } catch (NumberFormatException e) {
            showShortToastWithString(emptyFieldError);
            return;
        }

        clearInputFields();
        clearErrors();
        String res = String.format("%.2f + ( %.2f ) = %.2f", first, second, first + second);

        resultTextView.setText(res);
    }

    @OnClick(R.id.button_subtract)
    public void subtract() {
        double first, second;
        try {
            first = Double.parseDouble(firstNumEditText.getText().toString());
            second = Double.parseDouble(secondNumEditText.getText().toString());
        } catch (NumberFormatException e) {
            showShortToastWithString(emptyFieldError);
            return;
        }

        clearInputFields();
        clearErrors();
        String res = String.format("%.2f - ( %.2f ) = %.2f", first, second, first - second);

        resultTextView.setText(res);
    }

    @OnClick(R.id.button_divide)
    public void divide() {
        double first, second;

        /*
         * Since the EditText input type is set to numberDecimal|numberSigned,
         * we only need to handle empty inputs and division by zero. Number
         * parsing will always work.
         */
        try {
            first = Double.parseDouble(firstNumEditText.getText().toString());
            second = Double.parseDouble(secondNumEditText.getText().toString());
            if (second == 0) {
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException e) {
            showShortToastWithString(emptyFieldError);
            return;
        } catch (IllegalArgumentException e) {
            showShortToastWithString(dividingByZeroError);
            secondNumEditText.setError(dividingByZeroError);
            return;
        }

        clearInputFields();
        clearErrors();
        String res = String.format("%.2f / ( %.2f ) = %.2f", first, second, first / second);

        resultTextView.setText(res);
    }

    @OnClick(R.id.button_multiply)
    public void multiply() {
        double first, second;

        try {
            first = Double.parseDouble(firstNumEditText.getText().toString());
            second = Double.parseDouble(secondNumEditText.getText().toString());
        } catch (NumberFormatException e) {
            showShortToastWithString(emptyFieldError);
            return;
        }

        clearInputFields();
        clearErrors();
        String res = String.format("%.2f x ( %.2f ) = %.2f", first, second, first * second);

        resultTextView.setText(res);
    }

    public void clearErrors() {
        firstNumEditText.setError(null);
        secondNumEditText.setError(null);
    }

    private void clearInputFields() {
        firstNumEditText.setText(null);
        secondNumEditText.setText(null);
    }

    private void showShortToastWithString(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
