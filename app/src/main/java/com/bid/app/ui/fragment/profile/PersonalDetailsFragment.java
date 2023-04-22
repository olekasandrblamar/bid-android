package com.bid.app.ui.fragment.profile;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.BuildConfig;
import com.bid.app.R;
import com.bid.app.adaper.PersonalDetailsAdapter;
import com.bid.app.constants.Global;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.AddressRequestEdit;
import com.bid.app.model.request.ProfileRequest;
import com.bid.app.model.response.ImageUploadResponse;
import com.bid.app.model.response.ProfileResponse;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.PersonalDetail;
import com.bid.app.model.view.Profile;
import com.bid.app.model.view.Settings;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.account.AccountSettingsFragment;
import com.bid.app.ui.fragment.address.CurrentAddressFragment;
import com.bid.app.ui.fragment.home.IdDocumentsFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.FileUtils;
import com.bid.app.util.Logger;
import com.bid.app.util.SessionManagerUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetailsFragment extends BaseFragment {

    private static final String TAG = PersonalDetailsFragment.class.getSimpleName();


    private SessionManager sessionManager;
    private APIInterface apiInterface;

    private RecyclerView recyclerView;

    private ImageView ivProfile;
    private ConstraintLayout constraintLayoutProfile;

    private Dialog cameraGalleryDialog = null;
    private Uri photoURI;
    private File mPhotoFile;

    private Boolean isPermissionGranted = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);

        sessionManager = new SessionManager(Objects.requireNonNull(requireActivity()));
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_personal_details), R.drawable.ic_back_arrow, false);

        initController(view);

        return view;
    }


    @Override
    public void onPause() {
        Logger.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Logger.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Logger.e(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onStop() {
        Logger.e(TAG, "onStop");
        super.onStop();
        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_my_bid), R.drawable.ic_menu, true);
    }

    private void initController(View view) {

        ivProfile = view.findViewById(R.id.iv_personal_profile);
        constraintLayoutProfile = view.findViewById(R.id.constraint_layout);

        recyclerView = view.findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.context);
        recyclerView.setLayoutManager(layoutManager);

        constraintLayoutProfile.setOnClickListener(this);

        Logger.e(TAG, "getUserImage : " + sessionManager.getUserImage());

        Glide.with(Objects.requireNonNull(requireActivity()))
                .load(sessionManager.getUserImage())
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(false)
//                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(ivProfile);
    }

    @Override
    public void onResume() {
        super.onResume();

        getSettingsList();


        /*final Attachment attachment = sessionManager.get();
        String imageUrl = "";
        if (attachment != null) {
            final String hashKey = Utils.getMD5Hash(attachment);
            imageUrl = WebServices.API_IMAGE_URL(attachment, hashKey, "jpg");
        } else {
            imageUrl = WebServices.NO_IMAGE_URL;
        }

        Logger.e(TAG, imageUrl);*/
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.constraint_layout:
                /**
                 *  Get permission
                 */
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    getPermission();
                } else {
                    Logger.e(TAG, "LOLLIPOP_MR1 version");
                    showCameraGalleryDialog();
                }
                break;

            case R.id.linear_layout_camera_dialog:
                //captureFromCamera();
                dispatchTakePictureIntent();
                if (cameraGalleryDialog != null) {
                    cameraGalleryDialog.dismiss();
                }
                break;

            case R.id.linear_layout_gallery_dialog:
                pickFromGallery();
                if (cameraGalleryDialog != null) {
                    cameraGalleryDialog.dismiss();
                }
                break;
        }
    }

    private void initAdapter(final List<PersonalDetail> list) {
        final PersonalDetailsAdapter personalDetailAdapter = new PersonalDetailsAdapter(requireActivity(), list, PersonalDetailsFragment.this);
        recyclerView.setAdapter(personalDetailAdapter);
        personalDetailAdapter.notifyDataSetChanged();
    }

    private void getSettingsList() {
        List<PersonalDetail> list = new ArrayList<>();

        final String firstName = !StringUtils.isEmpty(sessionManager.getFirstName()) ? sessionManager.getFirstName() : "";
        final String lastName = !StringUtils.isEmpty(sessionManager.getLastName()) ? sessionManager.getLastName() : "";
        final String dob = !StringUtils.isEmpty(sessionManager.getDob()) ? sessionManager.getDob() : "";
        final String mobile = !StringUtils.isEmpty(sessionManager.getMobileNumber()) ? sessionManager.getMobileNumber() : "";
        final String email = !StringUtils.isEmpty(sessionManager.getEmail()) ? sessionManager.getEmail() : "";

        final String addressOne = !StringUtils.isEmpty(sessionManager.getAddressOne()) ? sessionManager.getAddressOne() : "";
//        final String addressTwo = !StringUtils.isEmpty(sessionManager.getAddressTwo()) ? sessionManager.getAddressTwo() : "";
        final String address = addressOne  + ", " + sessionManager.getCity() + ", " + sessionManager.getCountry() + ", " + sessionManager.getZipCode();

        final String gender = !StringUtils.isEmpty(sessionManager.getGender()) ? sessionManager.getGender() : "";
//        final String nationality = !StringUtils.isEmpty(sessionManager.getNationality()) ? sessionManager.getNationality() : "";
        final String nameOfEmployer = !StringUtils.isEmpty(sessionManager.getDescription()) ? sessionManager.getDescription() : "";
        final String statusOfEmployment = !StringUtils.isEmpty(sessionManager.getEmploymentStatus()) ? sessionManager.getEmploymentStatus() : "";

        final PersonalDetail fullNamePersonal = new PersonalDetail("Full Name", firstName + " " + lastName, "Full Name", R.drawable.ic_fullname, R.drawable.ic_edit, false);
        final PersonalDetail dobPersonal = new PersonalDetail("Date Of Birth", dob, "Date Of Birth", R.drawable.ic_calendar, R.drawable.ic_edit, false);
        final PersonalDetail numberPersonal = new PersonalDetail("Mobile Number", mobile, "Mobile Number", R.drawable.ic_phone, 0, false);
        final PersonalDetail emailPersonal = new PersonalDetail("email", email, "Email", R.drawable.ic_mails, R.drawable.ic_edit, false);
        final PersonalDetail addressPersonal = new PersonalDetail("Address", address, "Address", R.drawable.ic_address, R.drawable.ic_edit, false);
        final PersonalDetail genderPersonal = new PersonalDetail("Gender", gender, "Gender", R.drawable.ic_gender, R.drawable.ic_edit, false);
        final PersonalDetail statusOfEmploymentPersonal = new PersonalDetail("Employment Status", statusOfEmployment, "Status Of Employment", R.drawable.ic_download, R.drawable.ic_edit, false);
        final PersonalDetail nameOfEmployerPersonal = new PersonalDetail("Name Of Employer", nameOfEmployer, "Name Of Employer", R.drawable.ic_download, R.drawable.ic_edit, false);
        final PersonalDetail idDocumentsPersonal = new PersonalDetail("Id Document", "Id Document", "Id Document", R.drawable.ic_inbox, R.drawable.ic_edit, false);
        final PersonalDetail family = new PersonalDetail("Family", "Family", "Family", R.drawable.family, R.drawable.ic_edit, false);

//        final PersonalDetail nationalityPersonal = new PersonalDetail("Nationality", nationality, "Nationality", R.drawable.ic_flag, 0, false);

        fullNamePersonal.getName().trim();

        list.add(fullNamePersonal);
        list.add(dobPersonal);
        list.add(numberPersonal);
        list.add(emailPersonal);
        list.add(addressPersonal);
        list.add(genderPersonal);
        list.add(statusOfEmploymentPersonal);
        list.add(nameOfEmployerPersonal);
        list.add(idDocumentsPersonal);
        list.add(family);

        initAdapter(list);
    }

    @Override
    public void clickOnSettings(int position, Settings setting) {
        super.clickOnSettings(position, setting);

        if (getResources().getString(R.string.hint_notifications).equalsIgnoreCase(setting.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new AccountSettingsFragment(), R.id.frame_layout, true, false, false, null);
        } else {

        }
    }

    @Override
    public void clickOnPersonalDetail(int position, PersonalDetail personalDetail) {
        super.clickOnPersonalDetail(position, personalDetail);
        if ("Address".equalsIgnoreCase(personalDetail.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new CurrentAddressFragment(), R.id.frame_layout, true, false, false, null);
        } else if ("Id Document".equalsIgnoreCase(personalDetail.getName())) {
            Global.isHealthCheckUp = false;
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new IdDocumentsFragment(), R.id.frame_layout, true, false, false, null);
        } else if("Family".equalsIgnoreCase(personalDetail.getName())) {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new FamilyMembersFragment(), R.id.frame_layout, true, false, false, null);
        }else {
            AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new ProfileUpdateFragment(), R.id.frame_layout, true, false, false, null);
        }
    }

    private void getPermission() {
        Dexter.withActivity(requireActivity())
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Logger.e(TAG, "areAllPermissionsGranted");
                            isPermissionGranted = true;
                            showCameraGalleryDialog();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Logger.e(TAG, "onPermissionRationaleShouldBeShown");
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Logger.e(TAG, "error" + error.toString());
            }
        })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showCameraGalleryDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View optionView = inflater.inflate(R.layout.dialog_camera_gallery, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(optionView);
        final LinearLayout cameraLayout = optionView.findViewById(R.id.linear_layout_camera_dialog);
        final LinearLayout galleryLayout = optionView.findViewById(R.id.linear_layout_gallery_dialog);

        cameraLayout.setOnClickListener(this);
        galleryLayout.setOnClickListener(this);

        cameraGalleryDialog = builder.create();
        cameraGalleryDialog.show();
    }


    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        } else {
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        }

        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Constants.CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() {
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
        return image;
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                CropImage.activity(photoURI)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(5, 5)
                        .start(DashboardActivity.context, PersonalDetailsFragment.this);
            } else if (requestCode == Constants.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(5, 5)
                        .start(DashboardActivity.context, PersonalDetailsFragment.this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = Objects.requireNonNull(result).getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(requireActivity()).getContentResolver(), resultUri);
                    if (bitmap != null) {
                        FileUtils fileUtils = new FileUtils(requireActivity());
                        File imageFile = fileUtils.saveIMAImage(bitmap);
                        Glide.with(requireActivity()).load(bitmap).into(ivProfile);
                        callUploadService(imageFile);
                    } else {
                        Logger.e(TAG, "bitmap is null");
                        Glide.with(requireActivity()).load(R.drawable.ic_user).into(ivProfile);
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Logger.e(TAG, "error " + error.toString());
                }
            } else {
                Logger.e(TAG, "Camera, Gallery and Crop request cancelled");
            }
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
    }

    private void callUploadService(File file) {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Logger.e(TAG, "photoRequestBody : " + body.toString());
        Call<ImageUploadResponse> call = apiInterface.updateUserImage(sessionManager.getAccessToken(), body);
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();

                if (response.body() != null) {
                    final ImageUploadResponse body = response.body();
                    final ErrorStatus status = body.getErrorStatus();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        Logger.e(TAG, "Attachment : " + body.getAttachment());
                        callProfileUpdate(body.getAttachment());
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    private void callProfileUpdate(final String image) {
        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        final String fName = sessionManager.getFirstName();
        final String lName = sessionManager.getLastName();
        final String userName = sessionManager.getUserName();
        final String addressOne = sessionManager.getAddressOne();
        final String addressTwo = sessionManager.getAddressTwo();
        final String zipCode = sessionManager.getZipCode();
        final String city = sessionManager.getCity();
        final String state = sessionManager.getState();
        final String country = sessionManager.getCountry();
        final String mobile = sessionManager.getMobileNumber();
        final String email = sessionManager.getEmail();
        final String gender = sessionManager.getGender();
        final String employmentStatus = sessionManager.getEmploymentStatus();
        final String description = sessionManager.getDescription();


        final ProfileRequest request = new ProfileRequest();
        request.setFirstName(fName);
        request.setLastName(lName);
        request.setUsername(userName);
        request.setMobile(mobile);
        request.setImage(image);
        request.setEmail(email);
        request.setGender(gender);
        request.setEmployee_status(employmentStatus);
        request.setDescription(description);

        AddressRequestEdit address = new AddressRequestEdit();
        address.setAddressLine1(addressOne);
        address.setAddressLine2(addressTwo);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setZipCode(zipCode);

        request.setAddress(address);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            final String json = objectMapper.writeValueAsString(request);
            Logger.e(TAG, "callProfileUpdate Request: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Call<ProfileResponse> call = apiInterface.updateUser(sessionManager.getAccessToken(), request);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                CustomProgressDialog.getInstance().dismissDialog();
                if (response.body() != null) {
                    final ProfileResponse body = response.body();
                    final ErrorStatus status = body.getErrorStatus();

                    Logger.e(TAG, "callProfileUpdate Response: " + new Gson().toJson(body));

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        AlertUtils.showAlerterSuccess(requireActivity(), status.getMessage());

                        Profile profile = body.getProfile();
//                        profile.setDescription(request.getDescription());
//                        profile.setGender(request.getGender());
//                        profile.setEmployee_status(request.getEmployee_status());
//                        profile.setAttachment(body.getProfile().getAttachment());

                        SessionManagerUtils.setUserDetailsInSession(sessionManager, profile);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                } else {
                    Logger.e(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }
}
