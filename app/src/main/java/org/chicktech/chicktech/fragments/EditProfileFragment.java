package org.chicktech.chicktech.fragments;



import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.Address;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.BitmapUtils;
import org.chicktech.chicktech.utils.CameraLaunchingActivity;
import org.chicktech.chicktech.utils.CameraLaunchingListener;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EditProfileFragment extends Fragment implements Validator.ValidationListener, CameraLaunchingListener {
    // 8dp view size * xxhdpi scale factor
    private static final int PROFILE_PHOTO_DIMENSION = 80 * 3;

    private Person user;

    private OnSaveListener listener;
    private Validator validator;

    @Required(order = 1)
    EditText etName;
    EditText etDetails;
    Button btnChangeProfile;
    ImageView imgPhoto;

    @Email(order = 2, message = "This doesn't look like an e-mail address")
    EditText etEmail;
    @Required(order = 3)
    //TODO: Make a validation rule for phone number
    EditText etPhoneNumber;
    EditText etAddress1;
    EditText etAddress2;
    EditText etCity;
    @TextRule(order = 4, minLength = 2, maxLength = 2, message = "Must be 2 letter state name")
    EditText etState;
    //TODO: Make a validation rule for zipcode
    EditText etZip;

    EditText etWhy;
    EditText etWhat;
    private Button btnSave;

    public interface OnSaveListener {
        public void onProfileSave(Person u);
    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        //TODO: Add args here
        fragment.setArguments(args);
        return fragment;
    }
    public EditProfileFragment() {
        // Required empty public constructor
    }

    public OnSaveListener getListener() {
        return listener;
    }

    public void setListener(OnSaveListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //TODO: Retrieve args here
        }
        user = (Person) ParseUser.getCurrentUser();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        etName = (EditText) v.findViewById(R.id.etName);
        etDetails = (EditText) v.findViewById(R.id.etDetails);
        imgPhoto = (ImageView) v.findViewById(R.id.imgPhoto);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etPhoneNumber = (EditText) v.findViewById(R.id.etPhoneNumber);
        etAddress1 = (EditText) v.findViewById(R.id.etAddress1);
        etAddress2 = (EditText) v.findViewById(R.id.etAddress2);
        etCity = (EditText) v.findViewById(R.id.etCity);
        etState = (EditText) v.findViewById(R.id.etState);
        etZip = (EditText) v.findViewById(R.id.etZip);
        etWhy = (EditText) v.findViewById(R.id.etWhy);
        etWhat = (EditText) v.findViewById(R.id.etWhat);

        btnChangeProfile = (Button) v.findViewById(R.id.btnChangeProfile);
        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePhoto();
            }
        });
        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });

        populateProfile();
        return v;
    }

    private void populateProfile() {
        //TODO: Populate photo
        etName.setText(user.getPersonName());
        String description = user.getTagline();
        if (description == null || description.isEmpty()) {
            switch(user.getRole()) {
                case MENTOR:
                    etDetails.setHint("Profession");
                    break;
                case STUDENT:
                default:
                    etDetails.setHint("School & Year");
            }
        }
        etDetails.setText(description);
        etEmail.setText(user.getEmail());
        etPhoneNumber.setText(user.getPhoneNumber());
        populateAddress();
        etWhy.setText(user.getInterestReason());
        etWhat.setText(user.getInterests());
    }

    private void populateAddress() {
        Address addr = user.getAddress();
        if (addr == null) {
            return;
        }
        etAddress1.setText(addr.getAddress1());
        etAddress2.setText(addr.getAddress2());
        etCity.setText(addr.getCity());
        etState.setText(addr.getState());
        etZip.setText(addr.getZipcode());
    }

    private void saveAddress() {
//        String addr1, addr2, city, state, zip;
//        addr1 = etAddress1.getText().toString();
//        addr2 = etAddress2.getText().toString();
//        city = etCity.getText().toString();
//        state = etState.getText().toString();
//        zip = etZip.getText().toString();
//
//        if (addr1.isEmpty() && addr2.isEmpty() && city.isEmpty() && state.isEmpty() & zip.isEmpty() ) {
//            user.setAddress(null);
//            return;
//        }
//
//        Address addr = user.getAddress();
//        if (addr == null) {
//            addr = new Address();
//            user.setAddress(addr);
//        }
//        addr.setAddress1(addr1);
//        addr.setAddress2(addr2);
//        addr.setCity(city);
//        addr.setState(state);
//        addr.setZipcode(zip);
    }

    private void saveUser() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        //TODO: Set photo
        user.setPersonName(etName.getText().toString());
        user.setTagline(etDetails.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setPhoneNumber(etPhoneNumber.getText().toString());
        saveAddress();
        user.setInterestReason(etWhy.getText().toString());
        user.setInterests(etWhat.getText().toString());

        user.saveInBackground();

        if (listener != null) {
            listener.onProfileSave(user);
        }

        // Hide the keyboard
        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseProfilePhoto() {
        FragmentActivity activity = getActivity();
        if (activity instanceof CameraLaunchingActivity) {
            ((CameraLaunchingActivity) activity).launchCamera(this);
        } else {
            Log.d("profile", "Cannot take photo b/c activity isn't instance of CameraLaunchingActivity");
        }
    }

    @Override
    public void onCameraSuccess(Bitmap image) {
        Bitmap scaledImage = BitmapUtils.scaleToCoverSide(image, PROFILE_PHOTO_DIMENSION);
        imgPhoto.setImageBitmap(scaledImage);
    }

    @Override
    public void onCameraFailure(int resultCode) {
        Toast.makeText(getActivity(), "Oops, something went wrong and your picture isn't saved.", Toast.LENGTH_SHORT).show();
        Log.d("profile", "Error: Camera launching result code " + resultCode );
    }
}
