package com.bid.app.ui.fragment.profile;

import static com.bid.app.util.Constants.DOC_UPLOAD_API_KEY;
import static com.bid.app.util.Constants.DOC_UPLOAD_URL;
import static com.bid.app.util.Constants.FACE_UPLOAD_URL;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.bid.app.R;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.FamilyMembersRequest;
import com.bid.app.model.request.FindSomeOneWithMobileRequest;
import com.bid.app.model.response.FindSomeoneWithMobileResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.FamilyData;
import com.bid.app.model.view.Profile;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.IdDocuments.DocumentUploadResponseModel;
import com.bid.app.ui.fragment.IdDocuments.ScannedIDRModel;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Logger;
import com.google.gson.Gson;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditFamilyMemberFragment extends BaseFragment {

    private static final String TAG = AddEditFamilyMemberFragment.class.getSimpleName();
    private static final int SCAN_ID_DOC = 0;
    private static final int SCAN_FACE = 1;
    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private AppCompatButton btnSave, btnDelete, btnScanIdDocuments, findBtn;
    private AppCompatEditText relationshipValue, firstNameValue, lastNameValue, mobileValue;
    private ImageView faceBtn;
    private TextView searchResult;
    private View view;

    private FamilyData selectedFamilyMember;
    private int scanMode;
    private Boolean isExistingUser;
    ScannedIDRModel scannedIDRModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile_addeditfamilymember, container, false);
        sessionManager = new SessionManager(Objects.requireNonNull(requireActivity()));
        apiInterface = APIClient.getClient().create(APIInterface.class);

        getBundle();

        ((DashboardActivity) requireActivity()).setTitleAndImage("Family members", R.drawable.ic_back_arrow, false);

        initController(view);



        run();
        return view;
    }

    private void run() {
        mobileValue.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            //we need to know if the user is erasing or inputing some new character
            private boolean backspacingFlag = false;
            //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
            private boolean editedFlag = false;
            //we need to mark the cursor position and restore it after the edition
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //we store the cursor local relative to the end of the string in the EditText before the edition
                cursorComplement = s.length()-mobileValue.getSelectionStart();
                //we check if the user ir inputing or erasing a character
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here =D
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
                String phone = string.replaceAll("[^\\d]", "");

                //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
                //if the flag is false, this is a original user-typed entry. so we go on and do some magic
                if (!editedFlag) {

                    //we start verifying the worst case, many characters mask need to be added
                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 7 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans = "+" + phone.substring(0, 1) +"(" + phone.substring(1, 4) + ") " + phone.substring(4,7) + "-" + phone.substring(7,Math.min(11, phone.length()));
                        mobileValue.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        mobileValue.setSelection(mobileValue.getText().length()-cursorComplement);

                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
                    } else if (phone.length() >= 4 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "+" + phone.substring(0, 1) + "(" +phone.substring(1, 4) + ") " + phone.substring(4);
                        mobileValue.setText(ans);
                        mobileValue.setSelection(mobileValue.getText().length()-cursorComplement);
                    }
                    // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }
            }
        });

    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {

            selectedFamilyMember = (FamilyData) bundle.getSerializable(IBundle.BUNDLE_FAMILY_MEMBER);


        }
    }

    private void initController(View view) {
        scannedIDRModel = null;
        isExistingUser = false;
        btnSave = view.findViewById(R.id.btn_save);
        btnDelete = view.findViewById(R.id.btn_delete);
        findBtn = view.findViewById(R.id.btn_find);
        relationshipValue = view.findViewById(R.id.relationship_value);
        firstNameValue = view.findViewById(R.id.first_name_value);
        lastNameValue = view.findViewById(R.id.last_name_value);
//        idValue = view.findViewById(R.id.id_value);
        mobileValue = view.findViewById(R.id.mobile_value);
        btnScanIdDocuments = view.findViewById(R.id.btn_scan_id_documents);
        searchResult = view.findViewById(R.id.search_result);
        faceBtn = view.findViewById(R.id.iv_scan_face);
        if(selectedFamilyMember !=null) {
            relationshipValue.setText(selectedFamilyMember.getRelationship());
            firstNameValue.setText(selectedFamilyMember.getFirstName());
            lastNameValue.setText(selectedFamilyMember.getLastName());
//            idValue.setText(selectedFamilyMember.getNationalId());
            mobileValue.setText(selectedFamilyMember.getMobile());
            btnSave.setVisibility(View.GONE);
            btnScanIdDocuments.setVisibility(View.GONE);
            faceBtn.setVisibility(View.GONE);
            findBtn.setVisibility(View.GONE);
        }
        else{
            btnDelete.setVisibility(View.GONE);
        }
        findBtn.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnScanIdDocuments.setOnClickListener(this);
        faceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_save:
                if(confirmValidation()) {
                    if(selectedFamilyMember == null) {
                        createData();
                    }
                    else {
                        //updateData(selectedFamilyMember.getId());
                    }
                }
                break;
            case R.id.btn_delete:
                if(selectedFamilyMember != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("Delete Confirm");
                    builder.setMessage("Really delete?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteData(selectedFamilyMember.getId());
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }

                break;
            case R.id.btn_find:
                findUser();
                break;
            case R.id.btn_scan_id_documents:
                scanMode = SCAN_ID_DOC;
                IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                break;
            case R.id.iv_scan_face:
                scanMode = SCAN_FACE;
                IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) {
                    scanDocument(new File(path));
                }
            }
        }
    }

    private void scanDocument(File file) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBody);
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        String scanUrl = "";
        if(scanMode == SCAN_FACE) scanUrl = FACE_UPLOAD_URL;
        else scanUrl = DOC_UPLOAD_URL;
        final Call<DocumentUploadResponseModel> call = apiInterface.scanDocument(scanUrl, DOC_UPLOAD_API_KEY, body);
        call.enqueue(new Callback<DocumentUploadResponseModel>() {
            @Override
            public void onResponse(Call<DocumentUploadResponseModel> call, Response<DocumentUploadResponseModel> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                final DocumentUploadResponseModel documentUploadResponseModel = response.body();

                if (documentUploadResponseModel != null) {
                    final String status = documentUploadResponseModel.getStatus();
                    final String message = documentUploadResponseModel.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        scannedIDRModel  = documentUploadResponseModel.getData();

                        if(scanMode == SCAN_ID_DOC) {
                            firstNameValue.setText(documentUploadResponseModel.getData().getNamef());
                            lastNameValue.setText(documentUploadResponseModel.getData().getNamel());
                        }

                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), message);
                    }
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred");
                }
            }

            @Override
            public void onFailure(Call<DocumentUploadResponseModel> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
                CustomProgressDialog.getInstance().dismissDialog();

            }
        });
    }

    private Boolean confirmValidation() {
        String mobileNumber = mobileValue.getText().toString();
        mobileNumber = mobileNumber.replaceAll("\\D+","");

        if(relationshipValue.getText().toString().length() ==0 ){
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter RelationShip");
            return false;
        } else if(firstNameValue.getText().toString().length() == 0 && !isExistingUser) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter First Name");
            return false;
        } else if(lastNameValue.getText().toString().length() == 0 && !isExistingUser) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter Last Name");
            return false;
        } else if(scannedIDRModel == null && !isExistingUser) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please select face");
            return false;
        }

        return true;
    }
    public void createData() {
        String relationship = relationshipValue.getText().toString();
        String mobile = mobileValue.getText().toString();
        mobile = mobile.replaceAll("\\D+","");
        FamilyMembersRequest familyMembersRequest = new FamilyMembersRequest();
        familyMembersRequest.setRelationShip(relationship);
        familyMembersRequest.setMobile(mobile);
        familyMembersRequest.setFirstName(firstNameValue.getText().toString());
        familyMembersRequest.setLastName(lastNameValue.getText().toString());
        if(!isExistingUser) {
            familyMembersRequest.setUserData(scannedIDRModel);
        }
        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        final Call<StatusResponse> call = apiInterface.createFamilyMembers(sessionManager.getAccessToken(),familyMembersRequest);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final StatusResponse familyMembersResponse = response.body();
                    final ErrorStatus status = familyMembersResponse.getError();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AppActivityManager.popStack((AppCompatActivity) requireActivity(),1);
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new FamilyMembersFragment(), R.id.frame_layout, false, false, false, null);
                    } else {
                        AlertUtils.showAlerterWarning(requireActivity(), status.getMessage());
                    }

                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    public void updateData(String SelID) {
        String name = firstNameValue.getText().toString() + " " + lastNameValue.getText().toString();
        String relationship = relationshipValue.getText().toString();
        String mobile = mobileValue.getText().toString();
        FamilyMembersRequest familyMembersRequest = new FamilyMembersRequest();
        familyMembersRequest.setRelationShip(relationship);
        familyMembersRequest.setMobile(mobile);

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
        final Call<StatusResponse> call = apiInterface.updateFamilyMembers(SelID, sessionManager.getAccessToken(),familyMembersRequest);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final StatusResponse familyMembersResponse = response.body();
                    final ErrorStatus status = familyMembersResponse.getError();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterWarning(requireActivity(), status.getMessage());
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        AppActivityManager.popStack((AppCompatActivity) requireActivity(),1);
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new FamilyMembersFragment(), R.id.frame_layout, false, false, false, null);
                    } else {
                        AlertUtils.showAlerterWarning(requireActivity(), status.getMessage());
                    }

                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    public void deleteData(String selId) {

        final Call<StatusResponse> call = apiInterface.deleteFamilyMembers(selId, sessionManager.getAccessToken());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final StatusResponse body = response.body();
                    final ErrorStatus status = body.getError();

                    Logger.e(TAG, "callProfileUpdate Response: " + new Gson().toJson(body));

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());
                        AppActivityManager.popStack((AppCompatActivity) requireActivity(),1);
                        AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new FamilyMembersFragment(), R.id.frame_layout, false, false, false, null);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    public void findUser() {
        String mobileNumber = mobileValue.getText().toString();
        mobileNumber = mobileNumber.replaceAll("\\D+","");
        if(mobileNumber.length() == 0) {
            AlertUtils.showAlerterWarning(requireActivity(), "Please enter phone number");
        }
        FindSomeOneWithMobileRequest request = new FindSomeOneWithMobileRequest();
        request.setMobile(mobileNumber);
        final Call<FindSomeoneWithMobileResponse> call = apiInterface.findSomeoneWithMobile(request);
        call.enqueue(new Callback<FindSomeoneWithMobileResponse>() {
            @Override
            public void onResponse(Call<FindSomeoneWithMobileResponse> call, Response<FindSomeoneWithMobileResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final FindSomeoneWithMobileResponse body = response.body();
                    final ErrorStatus status = body.getErrorStatus();
                    Profile profile = body.getData();
                    Logger.e(TAG, "callProfileUpdate Response: " + new Gson().toJson(body));

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        if(profile == null) {
                            searchResult.setText("No member found");
                            isExistingUser = false;
                            firstNameValue.setFocusableInTouchMode(true);
                            firstNameValue.setFocusable(true);
                            firstNameValue.setClickable(true);
                            lastNameValue.setFocusableInTouchMode(true);
                            lastNameValue.setFocusable(true);
                            lastNameValue.setClickable(true);
                            btnScanIdDocuments.setVisibility(View.VISIBLE);
                            faceBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            isExistingUser = true;
                            searchResult.setText("");
                            firstNameValue.setText(profile.getFirstName());
                            lastNameValue.setText(profile.getLastName());
                            firstNameValue.setFocusableInTouchMode(false);
                            firstNameValue.setFocusable(false);
                            firstNameValue.setClickable(false);
                            lastNameValue.setFocusableInTouchMode(false);
                            lastNameValue.setFocusable(false);
                            lastNameValue.setClickable(false);
                            btnScanIdDocuments.setVisibility(View.GONE);
                            faceBtn.setVisibility(View.GONE);
                            AlertUtils.showAlerterSuccess(requireActivity(),status.getMessage() );

                        }
                    } else {
                        AlertUtils.showAlerterWarning(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<FindSomeoneWithMobileResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }

}