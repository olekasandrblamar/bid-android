package com.bid.app.ui.fragment.tripHistory;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.BuildConfig;
import com.bid.app.R;
import com.bid.app.adaper.AttachedFilesHorizontalAdapter;
import com.bid.app.adaper.CardsHorizontalAdapter;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.model.request.PostSOSRequest;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.retrofit.APIClient;
import com.bid.app.service.GPSTracker;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.bid.app.util.FileUtils;
import com.bid.app.util.Logger;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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


public class SOSFragment extends BaseFragment {
    private static final String TAG = SOSFragment.class.getSimpleName();

    View root;

    EditText contents;
    Button sendSOS;
    SessionManager sessionManager;
    APIInterface apiInterface;
    String busNo;
    RecyclerView fileAttachedStatus;
    private Dialog cameraGalleryDialog = null;
    private Boolean isPermissionGranted = false;
    Button fileAdd;
    private Uri photoURI;
    private File mPhotoFile;
    AttachedFilesHorizontalAdapter attachedFilesHorizontalAdapter;
    ArrayList<Uri> uriArrayList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_sos, container, false);
        getBundle();
        initIds(root, savedInstanceState);
        initFragment(root);
        run();
        return root;
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
//            ticketData = (Ticket) bundle.getSerializable(IBundle.BUNDLE_TICKET);
            busNo = bundle.getString(IBundle.BUNDLE_BUS_ID);
        }
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.send_sos:
                postSOS();
                break;
            case R.id.file_add:
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


    private void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
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

    private void initFragment(View view) {


    }
    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
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
                takePictureIntent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, photoURI);
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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("*/*");
        photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
        startActivityForResult(photoPickerIntent, Constants.GALLERY_REQUEST_CODE);
//
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        String[] mimeTypes = {"image/jpeg", "image/png"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            uriArrayList.add(photoURI);
            attachedFilesHorizontalAdapter.notifyDataSetChanged();
//                fileAttachedStatus.setText("fileAttached");
//            CropImage.activity(photoURI)
//                    .setCropShape(CropImageView.CropShape.OVAL)
//                    .setAspectRatio(5, 5)
//                    .start(DashboardActivity.context, SOSFragment.this);
        } else if (requestCode == Constants.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
//                fileAttachedStatus.setText("fileAttached");
            Uri selectedImage = data.getData();

            uriArrayList.add(selectedImage);
            attachedFilesHorizontalAdapter.notifyDataSetChanged();

//            CropImage.activity(selectedImage)
//                    .setCropShape(CropImageView.CropShape.OVAL)
//                    .setAspectRatio(5, 5)
//                    .start(DashboardActivity.context, SOSFragment.this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                fileAttachedStatus.setText("fileAttached");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = Objects.requireNonNull(result).getUri();
                uriArrayList.add(resultUri);
                attachedFilesHorizontalAdapter.notifyDataSetChanged();
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(requireActivity()).getContentResolver(), resultUri);
//                    if (bitmap != null) {
//                        FileUtils fileUtils = new FileUtils(requireActivity());
//                        File imageFile = fileUtils.saveIMAImage(bitmap);
////                        Glide.with(requireActivity()).load(bitmap).into(ivProfile);
//                        callUploadService(imageFile);
//                    } else {
//                        Logger.e(TAG, "bitmap is null");
////                        Glide.with(requireActivity()).load(R.drawable.ic_user).into(ivProfile);
//                    }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Logger.e(TAG, "error " + error.toString());
            }
        } else {
            Logger.e(TAG, "Camera, Gallery and Crop request cancelled");
        }
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public void postSOS() {

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
//
//        PostSOSRequest postSOSRequest = new PostSOSRequest();
//        postSOSRequest.setContent(contents.getText().toString());
//        postSOSRequest.setLocation("");
//        postSOSRequest.setTicketId("0");//ticketData.getBusNo()
        GPSTracker gps;
        gps = new GPSTracker(getContext());
        double latitude;
        double longitude;
        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        } else {
            gps.showSettingsAlert();
            latitude = 0.0;
            longitude = 0.0;
        }
        String latStr = (new Double(latitude)).toString();
        String lonStr = (new Double(longitude)).toString();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("ticketId", busNo)//busNo
                .addFormDataPart("content", contents.getText().toString());
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        for (Uri uri : uriArrayList) {
            if (uri != null && uri.getPath() != null) {
                File file = new File(getPath(uri));
                if (file.exists()) {
                    builder.addFormDataPart("files[]", file.getName(), RequestBody.create(MultipartBody.FORM, file));
                }
            }
        }

        MultipartBody requestBody = builder.build();

        final Call<StatusResponse> call = apiInterface.postSOS(sessionManager.getAccessToken(),latStr, lonStr, requestBody);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                final StatusResponse statusResponse = response.body();
                if(statusResponse == null) {
                    AlertUtils.showAlerterWarning(requireActivity(), "connection failed");
                }
                else if(statusResponse.getError().getCode().equals("0")) {
                    AlertUtils.showAlerterSuccess(requireActivity(), statusResponse.getError().getMessage());
                    getFragmentManager().popBackStack();

                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(), statusResponse.getError().getMessage());
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();

            }
        });
    }

    private void callUploadService(File file) {
//
//        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
//        Logger.e(TAG, "photoRequestBody : " + body.toString());
//        Call<ImageUploadResponse> call = apiInterface.updateUserImage(sessionManager.getAccessToken(), body);
//        call.enqueue(new Callback<ImageUploadResponse>() {
//            @Override
//            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
//
//                CustomProgressDialog.getInstance().dismissDialog();
//
//                if (response.body() != null) {
//                    final ImageUploadResponse body = response.body();
//                    final ErrorStatus status = body.getErrorStatus();
//
//                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
//                        Logger.e(TAG, "Attachment : " + body.getAttachment());
//                        //callProfileUpdate(body.getAttachment());
//                    } else {
////                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
//                    }
//                } else {
//                    Logger.e(TAG, "response.body() == null");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
//                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
//            }
//        });
    }

    private void initIds(View view, Bundle savedInstanceState) {
        contents = view.findViewById(R.id.contents);
        sendSOS = view.findViewById(R.id.send_sos);
        fileAttachedStatus = view.findViewById(R.id.file_attached_status);
        fileAdd = view.findViewById(R.id.file_add);
        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sendSOS.setOnClickListener(this);
        fileAdd.setOnClickListener(this);
    }

    private void run() {
        uriArrayList = new ArrayList<>();
        attachedFilesHorizontalAdapter = new AttachedFilesHorizontalAdapter(getContext(), uriArrayList, this );
        fileAttachedStatus.setAdapter(attachedFilesHorizontalAdapter);
        attachedFilesHorizontalAdapter.notifyDataSetChanged();
        fileAttachedStatus.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void clickOnRemove(int position) {
        super.clickOnRemove(position);

        uriArrayList.remove(position);
        attachedFilesHorizontalAdapter.setList(uriArrayList);
        attachedFilesHorizontalAdapter.notifyDataSetChanged();

    }
}
