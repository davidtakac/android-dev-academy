package ada.osc.bmicalculator;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BmiCalcHome extends AppCompatActivity {

    @BindView(R.id.edittext_weight)
    EditText weightEditText;
    @BindView(R.id.edittext_height)
    EditText heightEditText;
    @BindView(R.id.button_calculate)
    Button calcButton;

    @BindView(R.id.imageview_silhouette)
    ImageView silhouetteImageView;
    @BindView(R.id.textview_numericResult)
    TextView numericResTextView;
    @BindView(R.id.textview_titleResult)
    TextView titleResTextView;
    @BindView(R.id.textview_descriptionResult)
    TextView descriptionResTextView;

    Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calc_home);

        ButterKnife.bind(this);

        res = getResources();
    }

    @OnClick(R.id.button_calculate)
    public void calculateBmi() {
        if (!isHeightValid() || !isWeightValid()) {
            return;
        }

        double height = Double.parseDouble(heightEditText.getText().toString());
        double weight = Double.parseDouble(weightEditText.getText().toString());

        double numericRes = BmiCalculator.getBmi(weight, height);
        numericResTextView.setText(String.format("BMI: %.2f", numericRes));

        setBmiTitleAccordingTo(numericRes);
        setBmiDescAccordingTo(numericRes);
        setBmiPictureAccordingTo(numericRes);
    }

    private boolean isHeightValid() {
        double height;
        try {
            height = Double.parseDouble(heightEditText.getText().toString());
        } catch (NumberFormatException e) {
            heightEditText.setError(res.getString(R.string.string_error_emptyinput));
            return false;
        }

        if (height < 1.3 || height > 2.5) {
            heightEditText.setError(res.getString(R.string.string_error_illegalheight));
            return false;
        }

        return true;
    }

    private boolean isWeightValid() {
        double weight;
        try {
            weight = Double.parseDouble(weightEditText.getText().toString());
        } catch (NumberFormatException e) {
            weightEditText.setError(res.getString(R.string.string_error_emptyinput));
            return false;
        }

        if (weight < 40.0 || weight > 350.0) {
            weightEditText.setError(res.getString(R.string.string_error_illegalweight));
            return false;
        }

        return true;
    }

    private void setBmiTitleAccordingTo(double bmi){
        String title = "";
        if(bmi < 18.5){
            title = res.getString(R.string.string_bmiTitle_under18_5);
        } else if(bmi < 24.9){
            title = res.getString(R.string.string_bmiTitle_under24_9);
        } else if(bmi < 29.9){
            title = res.getString(R.string.string_bmiTitle_under29_9);
        } else {
            title = res.getString(R.string.string_bmiTitle_under34_9);
        }

        titleResTextView.setText(title);
    }

    private void setBmiDescAccordingTo(double bmi){
        String desc = "";
        if(bmi < 18.5){
            desc = res.getString(R.string.string_bmiDesc_under18_5);
        } else if(bmi < 24.9){
            desc = res.getString(R.string.string_bmiDesc_under24_9);
        } else if(bmi < 29.9){
            desc = res.getString(R.string.string_bmiDesc_under29_9);
        } else {
            desc = res.getString(R.string.string_bmiDesc_under34_9);
        }

        descriptionResTextView.setText(desc);
    }

    private void setBmiPictureAccordingTo(double bmi) {
        Drawable img;
        if(bmi < 18.5){
            img = res.getDrawable(R.drawable.underweight);
        } else if(bmi < 24.9){
            img = res.getDrawable(R.drawable.normalweight);
        } else if(bmi < 29.9){
            img = res.getDrawable(R.drawable.overweight);
        } else {
            img = res.getDrawable(R.drawable.overweight2);
        }

        silhouetteImageView.setImageDrawable(img);
    }
}
