package com.bid.app.ui.fragment.IdDocuments;

import static com.bid.app.util.Constants.DOC_UPLOAD_API_KEY;
import static com.bid.app.util.Constants.DOC_UPLOAD_URL;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bid.app.R;
import com.bid.app.adaper.SelectIdDocumentsAdapter;
import com.bid.app.enums.ERESTServiceStatus;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.model.request.AddDocumentRequest;
import com.bid.app.model.request.BIDRequestModel;
import com.bid.app.model.response.BIDResponse;
import com.bid.app.model.response.ImageUploadResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.AddIDDocumentRequestDataModel;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.SelectIdDocument;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Constants;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentSelectIdDocument extends BaseFragment {
    private static final String TAG = FragmentSelectIdDocument.class.getSimpleName();
    private static final int MY_REQUEST_CODE = 1;
    private AppCompatImageView ivLogoBahamas;
    private AppCompatTextView tvIdDocumentsDesc;
    private RecyclerView recyclerView;
    private String front_pic_path;
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private LinearLayout upload_progress_ll;
    private ConstraintLayout select_id_screen_cl;
    private LinearLayout photo_in_review_ll;
    private AppCompatButton btn_ok;
    private ArrayList<SelectIdDocument> selectIdDocuments;
    private String selectedIdCardType = "";
    private TextView we_are_reviewing_tv;
    private ImageView we_are_reviewing_iv;
    private ImageView uploading_iv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_id_document, container, false);

        sessionManager = new SessionManager(requireActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ((DashboardActivity) requireActivity()).setTitleAndImage(getResources().getString(R.string.title_select_your_id_documents), R.drawable.ic_back_arrow, false);
        initController(view);

        return view;
    }

    private void initController(View view) {

        ivLogoBahamas = view.findViewById(R.id.iv_logo_bahamas);
        tvIdDocumentsDesc = view.findViewById(R.id.tv_id_documents_desc);
        recyclerView = view.findViewById(R.id.recycler_view);
        upload_progress_ll = view.findViewById(R.id.upload_progress_ll);
        select_id_screen_cl = view.findViewById(R.id.select_id_screen_cl);
        photo_in_review_ll = view.findViewById(R.id.photo_in_review_ll);
        we_are_reviewing_tv = view.findViewById(R.id.we_are_reviewing_tv);
        we_are_reviewing_iv = view.findViewById(R.id.we_are_reviewing_iv);
        uploading_iv = view.findViewById(R.id.uploading_iv);
        btn_ok = view.findViewById(R.id.btn_ok);

        setLayoutManager();
        initAdapter(getSelectId());

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setLayoutManager() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    private void initAdapter(final List<SelectIdDocument> list) {
        final SelectIdDocumentsAdapter selectIdDocumentsAdapter = new SelectIdDocumentsAdapter(requireActivity(), list, FragmentSelectIdDocument.this);
        recyclerView.setAdapter(selectIdDocumentsAdapter);
        selectIdDocumentsAdapter.notifyDataSetChanged();
    }

    private List<SelectIdDocument> getSelectId() {
        selectIdDocuments = new ArrayList<>();

        final SelectIdDocument id1 = new SelectIdDocument(Constants.NIB_CARD, R.drawable.booking_idcard_png);
        final SelectIdDocument id2 = new SelectIdDocument(Constants.LICENSE, R.drawable.booking_license);
        final SelectIdDocument id3 = new SelectIdDocument(Constants.PASSPORT, R.drawable.booking_passport);

        selectIdDocuments.add(id1);
        selectIdDocuments.add(id2);
        selectIdDocuments.add(id3);

        return selectIdDocuments;
    }

    @Override
    public void clickAddIDCardType(int position, SelectIdDocument selectIdDocument) {
        selectedIdCardType = selectIdDocuments.get(position).getName();
        IDCardCamera.create(FragmentSelectIdDocument.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
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
        showUploadingDocumentView();

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBody);

        final Call<DocumentUploadResponseModel> call = apiInterface.scanDocument(DOC_UPLOAD_URL, DOC_UPLOAD_API_KEY, body);
        call.enqueue(new Callback<DocumentUploadResponseModel>() {
            @Override
            public void onResponse(Call<DocumentUploadResponseModel> call, Response<DocumentUploadResponseModel> response) {
                final DocumentUploadResponseModel documentUploadResponseModel = response.body();

                if (documentUploadResponseModel != null) {
                    final String status = documentUploadResponseModel.getStatus();
                    final String message = documentUploadResponseModel.getMessage();
                    if (status.equalsIgnoreCase("success")) {

                        if (userHasBID()) {
                            uploadDocument(file, documentUploadResponseModel.getData());
                        } else {
                            getUserBid(file, documentUploadResponseModel.getData());
                        }

                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), message);
                        upload_progress_ll.setVisibility(View.GONE);
                    }
                } else {
                    upload_progress_ll.setVisibility(View.GONE);
                    AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred");
                }
            }

            @Override
            public void onFailure(Call<DocumentUploadResponseModel> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
                upload_progress_ll.setVisibility(View.GONE);

            }
        });
    }

    private void uploadDocument(File file, ScannedIDRModel data) {

        Log.d(TAG, "Uploading " + selectedIdCardType + "..");

        RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
//                .addFormDataPart("b_id", sessionManager.getBid())
                .addFormDataPart("class", "Document")
                .addFormDataPart("file", file.getName(), requestBodyFile)
                .build();


        final Call<ImageUploadResponse> call = apiInterface.uploadDocument(sessionManager.getAccessToken(), requestBody);
        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                upload_progress_ll.setVisibility(View.GONE);
                final ImageUploadResponse documentUploadResponseModel = response.body();

                if (documentUploadResponseModel != null) {
                    final String status = documentUploadResponseModel.getErrorStatus().getCode();
                    final String message = documentUploadResponseModel.getErrorStatus().getMessage();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status)) {
                        final String attachmentName = documentUploadResponseModel.getAttachment();

                        addDocument(attachmentName, data);

//                        AlertUtils.showAlerterSuccess(requireActivity(), message);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), message);
                    }
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred");
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
                upload_progress_ll.setVisibility(View.GONE);

            }
        });

    }

    private String getDocumentID() {
        if(selectedIdCardType.equals(Constants.PASSPORT)) return "1";
        else if(selectedIdCardType.equals(Constants.LICENSE)) return "2";
        else if(selectedIdCardType.equals(Constants.NIB_CARD)) return "3";
        return "0";
    }

    private void addDocument(String attachmentName, ScannedIDRModel data) {
        AddDocumentRequest addDocumentRequest = new AddDocumentRequest();
        addDocumentRequest.setImage(attachmentName);
        addDocumentRequest.setDocumentId(getDocumentID());
        addDocumentRequest.setUserData(data);
        addDocumentRequest.setLocation("");
        addDocumentRequest.setCaption("");
        AddIDDocumentRequestDataModel addIDDocumentRequestDataModel = new AddIDDocumentRequestDataModel();
        addIDDocumentRequestDataModel.setSurnames(data.getNamel());
        addIDDocumentRequestDataModel.setGivenNames(data.getNamef());
        addIDDocumentRequestDataModel.setCountryCode("");
        addIDDocumentRequestDataModel.setDocumentNumber("");
        addIDDocumentRequestDataModel.setNationality("");
        addIDDocumentRequestDataModel.setBirthDate("");
        addIDDocumentRequestDataModel.setSex("");

        addDocumentRequest.setData(addIDDocumentRequestDataModel);
        final Call<StatusResponse> call = apiInterface.addDocument(sessionManager.getAccessToken(), addDocumentRequest);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                upload_progress_ll.setVisibility(View.GONE);
                final StatusResponse statusResponse = response.body();

                if (statusResponse != null) {
                    final String status = statusResponse.getError().getCode();
                    final String message = statusResponse.getError().getMessage();

                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status)) {
                        showPhotoInReviewView();

                        AlertUtils.showAlerterSuccess(requireActivity(), message);
                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), message);
                    }
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred");
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
                upload_progress_ll.setVisibility(View.GONE);

            }
        });
    }

    private void getUserBid(File file, ScannedIDRModel data) {

        Log.d(TAG, "getting BID..");

        CustomProgressDialog.getInstance().showDialog(requireActivity(), true);

        BIDRequestModel bidRequestModel = new BIDRequestModel();
        bidRequestModel.setFirst_name(sessionManager.getFirstName());
        bidRequestModel.setLast_name(sessionManager.getLastName());
        bidRequestModel.setUser_id(sessionManager.getUserId());
        bidRequestModel.setUser_data(data);

        final Call<BIDResponse> call = apiInterface.getUserBID(sessionManager.getAccessToken(), bidRequestModel);
        call.enqueue(new Callback<BIDResponse>() {
            @Override
            public void onResponse(Call<BIDResponse> call, Response<BIDResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                final BIDResponse bidResponse = response.body();

                if (bidResponse != null) {
                    final ErrorStatus status = bidResponse.getError();
                    if (ERESTServiceStatus.REST_OK_CODE.getRestApiStatus().equalsIgnoreCase(status.getCode())) {
                        sessionManager.setBid(bidResponse.getB_id());
                        Log.d(TAG, "setBid: " + sessionManager.getBid());

                        uploadDocument(file, data);

                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), status.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BIDResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();

            }
        });
    }


    private boolean userHasBID() {
        return !sessionManager.getBid().isEmpty() && !sessionManager.getBid().equalsIgnoreCase("ID-default");
    }

    private void showUploadingDocumentView() {

        if (selectedIdCardType.equalsIgnoreCase(Constants.NIB_CARD)) {
            uploading_iv.setImageResource(R.drawable.booking_idcard_png);
        } else if (selectedIdCardType.equalsIgnoreCase(Constants.LICENSE)) {
            uploading_iv.setImageResource(R.drawable.booking_license);
        } else if (selectedIdCardType.equalsIgnoreCase(Constants.PASSPORT)) {
            uploading_iv.setImageResource(R.drawable.booking_passport);
        }

        upload_progress_ll.setVisibility(View.VISIBLE);
    }

    private void showPhotoInReviewView() {

        if (selectedIdCardType.equalsIgnoreCase(Constants.NIB_CARD)) {
            we_are_reviewing_iv.setImageResource(R.drawable.booking_idcard_png);
        } else if (selectedIdCardType.equalsIgnoreCase(Constants.LICENSE)) {
            we_are_reviewing_iv.setImageResource(R.drawable.booking_license);
        } else if (selectedIdCardType.equalsIgnoreCase(Constants.PASSPORT)) {
            we_are_reviewing_iv.setImageResource(R.drawable.booking_passport);
        }

        we_are_reviewing_tv.setText(requireContext().getResources().getString(R.string.we_are_reviewing_your) + " " + selectedIdCardType);
        photo_in_review_ll.setVisibility(View.VISIBLE);
    }

//    private File bitmapToFile(Bitmap fullDocumentImage) {
//        //create a file to write bitmap data
//        File front_image_file = new File(DashboardActivity.context.getCacheDir(), "temp_bid_file");
//        try {
//            front_image_file.createNewFile();
//            //Convert bitmap to byte array
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            fullDocumentImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//            byte[] bitmapdata = bos.toByteArray();
//
//            //write the bytes in file
//            FileOutputStream fos = new FileOutputStream(front_image_file);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return front_image_file;
//    }

}
