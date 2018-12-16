package ada.osc.myfirstweatherapp.ui.addlocation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.contracts.AddLocationContract;
import ada.osc.myfirstweatherapp.presentation.addlocation.AddLocationPresenter;
import ada.osc.myfirstweatherapp.util.Constants;
import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddLocationFragment extends Fragment implements AddLocationContract.View {
    @BindView(R.id.fragment_add_location_enter_city_edit_text)
    EditText etEnterLocationName;
    @BindView(R.id.fragment_add_location_button)
    Button btnAddLocation;

    private AddLocationContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = new AddLocationPresenter(App.getCityDaoInteractor());
        presenter.setView(this);
    }

    @Override
    public void onSuccess() {
        ToastUtil.showToastWithString(getContext(), getString(R.string.location_added_success_toast_message));
        getActivity().finish();
    }

    @Override
    public void onLocationAlreadyExistsError() {
        etEnterLocationName.setError(getActivity().getString(R.string.location_already_exists_error_message));
    }

    @Override
    public void onEmptyStringRequestError() {
        etEnterLocationName.setError(getActivity().getString(R.string.empty_location_string_error_message));
    }

    @OnClick(R.id.fragment_add_location_button)
    public void onAddLocationClick(){
        presenter.saveCityToDatabase(etEnterLocationName.getText().toString());
    }
}
