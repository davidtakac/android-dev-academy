package ada.osc.resume7;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResumeHome extends AppCompatActivity {

    @BindView(R.id.button_bio)
    Button bioButton;
    @BindView(R.id.button_work)
    Button workButton;
    @BindView(R.id.button_education)
    Button educationButton;
    @BindView(R.id.button_projects)
    Button projectsButton;
    @BindView(R.id.button_contact)
    Button contactButton;

    @BindView(R.id.textview_content)
    TextView contentTextView;

    //list of buttons allows for cleaner color setting
    List<Button> buttons = new ArrayList<>();
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_home);

        ButterKnife.bind(this);

        //enables clicking on links
        contentTextView.setMovementMethod(LinkMovementMethod.getInstance());

        buttons.add(bioButton);
        buttons.add(workButton);
        buttons.add(educationButton);
        buttons.add(projectsButton);
        buttons.add(contactButton);

        resources = getResources();

        showBio();
    }

    @OnClick(R.id.button_bio)
    public void showBio() {
        resetButtonColors();
        setButtonColorToClicked(bioButton);
        contentTextView.setText(resources.getText(R.string.text_bio));
    }

    @OnClick(R.id.button_work)
    public void showWork(){
        resetButtonColors();
        setButtonColorToClicked(workButton);
        contentTextView.setText(resources.getText(R.string.text_work));
    }

    @OnClick(R.id.button_education)
    public void showEducation(){
        resetButtonColors();
        setButtonColorToClicked(educationButton);
        contentTextView.setText(resources.getText(R.string.text_education));
    }

    @OnClick(R.id.button_projects)
    public void showProjects(){
        resetButtonColors();
        setButtonColorToClicked(projectsButton);
        contentTextView.setText(resources.getText(R.string.link_githubProjects));
    }

    @OnClick(R.id.button_contact)
    public void showContact(){
        resetButtonColors();
        setButtonColorToClicked(contactButton);
        contentTextView.setText(resources.getText(R.string.text_contact));
    }

    private void resetButtonColors() {
        for (Button btn : buttons) {
            btn.setBackgroundColor(resources.getColor(R.color.color_buttonNotClicked));
        }
    }

    private void setButtonColorToClicked(Button button){
        button.setBackgroundColor(resources.getColor(R.color.color_buttonClicked));
    }
}
