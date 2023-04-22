package com.bid.app.ui.fragment.discover.transit.driver;

import static com.bid.app.ui.activity.DashboardActivity.context;
import static com.bid.app.util.Constants.DOC_UPLOAD_API_KEY;
import static com.bid.app.util.Constants.DOC_UPLOAD_URL;
import static com.bid.app.util.Constants.FACE_CHECK_URL;
import static com.bid.app.util.Constants.FACE_UPLOAD_URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.APIInterface;
import com.bid.app.interfaces.IBundle;
import com.bid.app.manager.AppActivityManager;
import com.bid.app.model.request.PurchaseTicketByDriverRequest;
import com.bid.app.model.request.PurchaseTicketRequest;
import com.bid.app.model.response.ATripResponse;
import com.bid.app.model.response.CheckFaceResponse;
import com.bid.app.model.response.GetWalletBalanceResponse;
import com.bid.app.model.response.StatusResponse;
import com.bid.app.model.view.CheckFaceResponseData;
import com.bid.app.model.view.PassengerData;
import com.bid.app.model.view.PurchaseTicketRequestData;
import com.bid.app.model.view.RoutePath;
import com.bid.app.model.view.Ticket;
import com.bid.app.model.view.Waypoint;
import com.bid.app.retrofit.APIClient;
import com.bid.app.session.SessionManager;
import com.bid.app.ui.activity.DashboardActivity;
import com.bid.app.ui.base.BaseFragment;
import com.bid.app.ui.fragment.IdDocuments.DocumentUploadResponseModel;
import com.bid.app.ui.fragment.discover.transit.InstructionFragment;
import com.bid.app.ui.fragment.wallet.TopUpFragment;
import com.bid.app.ui.popup.CustomProgressDialog;
import com.bid.app.util.AlertUtils;
import com.bid.app.util.Utils;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyTicketWalletFragment extends BaseFragment {
    ImageView qrCode;
    AppCompatButton buyTicket, cancelTicket;

    TextView cash, fareCost;
    RecyclerView summary;
    SessionManager sessionManager;

    PassengerData passengerData;
    ArrayList<String> list1, list2;
    ArrayList<String> posList1, posList2;

    Spinner fromSpinner, toSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transport_buy_ticket_wallet, container, false);

        getBundle();
        init(view);
        run();
        return view;
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            passengerData = (PassengerData) bundle.getSerializable(IBundle.BUNDLE_PASSENGER);
        }
    }
    public void init(View view) {
        sessionManager = new SessionManager(requireActivity());
        ((DashboardActivity) requireActivity()).setTitleAndImage("Ticket", R.drawable.ic_back_arrow, false);
        cash = view.findViewById(R.id.cash);
        fareCost = view.findViewById(R.id.fare_cost);
        summary = view.findViewById(R.id.summary);
        summary.setLayoutManager(new LinearLayoutManager(getContext()));
        fromSpinner = view.findViewById(R.id.from_spinner);
        toSpinner = view.findViewById(R.id.to_spinner);
        buyTicket = view.findViewById(R.id.buy_ticket);
        cancelTicket = view.findViewById(R.id.cancel_ticket);
        buyTicket.setOnClickListener(this);
        cancelTicket.setOnClickListener(this);

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        posList1 = new ArrayList<>();
        posList2 = new ArrayList<>();
        for(Waypoint waypoint: passengerData.getGetRouteByBusResponse().getData().getWaypoints()) {
            list1.add(waypoint.getName());
            posList1.add("(" + waypoint.getPosition().getLat() + "," + waypoint.getPosition().getLng() + ")");
            list2.add(waypoint.getName());
            posList2.add("(" + waypoint.getPosition().getLat() + "," + waypoint.getPosition().getLng() + ")");
        }

        ArrayAdapter adapter1 = new ArrayAdapter<String>( getContext(), R.layout.support_simple_spinner_dropdown_item, list1);
        fromSpinner.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter<String>( getContext(), R.layout.support_simple_spinner_dropdown_item, list2);
        toSpinner.setAdapter(adapter2);


    }
    public void run() {
        //showQrCode();

        loadWalletInfo();

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.buy_ticket:
                IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                break;
            case R.id.cancel_ticket:
                AppActivityManager.popStack((AppCompatActivity) requireActivity(),1);
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
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBody);
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        String scanUrl = FACE_CHECK_URL;
        final Call<CheckFaceResponse> call = apiInterface.checkFace(scanUrl, DOC_UPLOAD_API_KEY,passengerData.getBid(), body);
        call.enqueue(new Callback<CheckFaceResponse>() {
            @Override
            public void onResponse(Call<CheckFaceResponse> call, Response<CheckFaceResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();

                final CheckFaceResponse checkFaceResponse = response.body();

                if (checkFaceResponse != null) {
                    final String status = checkFaceResponse.getStatus();
                    final String message = checkFaceResponse.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        CheckFaceResponseData checkFaceResponseData  = checkFaceResponse.getData();
                        if(checkFaceResponseData.getCheckResult()) {
                            purchaseTicket();
                        }
                        else {
                            AlertUtils.showAlerterFailure(requireActivity(), "Not Correct User!");
                        }

                    } else {
                        AlertUtils.showAlerterFailure(requireActivity(), message);
                    }
                } else {
                    AlertUtils.showAlerterFailure(requireActivity(), "Some Error Occurred");
                }
            }

            @Override
            public void onFailure(Call<CheckFaceResponse> call, Throwable t) {
                t.printStackTrace();
//                CustomProgressDialog.getInstance().dismissDialog();
                CustomProgressDialog.getInstance().dismissDialog();

            }
        });
    }

    public void purchaseTicket() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        PurchaseTicketByDriverRequest purchaseTicketRequests = new PurchaseTicketByDriverRequest();
        purchaseTicketRequests.setFirstName(passengerData.getFirstName());
        purchaseTicketRequests.setLastName(passengerData.getLastName());
        purchaseTicketRequests.setMobile(passengerData.getMobile());
        purchaseTicketRequests.setSeatNo(passengerData.getSeatNumber());
        purchaseTicketRequests.setPaymentMethod("");
        purchaseTicketRequests.setCardNumber("");
        purchaseTicketRequests.setCardName("");
        purchaseTicketRequests.setExpiryDate("");
        purchaseTicketRequests.setCvv("");
        int fromPos = fromSpinner.getSelectedItemPosition();
        int toPos = toSpinner.getSelectedItemPosition();
        int i = 0;
        String routePaths = "";
        Double distance = 0.0;
        for(RoutePath routePath: passengerData.getGetRouteByBusResponse().getData().getPaths()) {
            i++;
            if(fromPos<i && i < toPos) {
                for(String path:routePath.getPath()) {
                    routePaths += " " + path;
                }
                distance += Utils.getDistance(routePath);
            }
        }

        purchaseTicketRequests.setFrom(list1.get(fromPos));
        purchaseTicketRequests.setTo(list2.get(toPos));
        purchaseTicketRequests.setFromLocation(posList1.get(fromPos));
        purchaseTicketRequests.setToLocation(posList2.get(toPos));
        purchaseTicketRequests.setDistance(distance.toString());
        purchaseTicketRequests.setPolyline(routePaths);
        purchaseTicketRequests.setFrom(passengerData.getGetRouteByBusResponse().getData().getWaypoints().get(fromPos).getId());
        purchaseTicketRequests.setTo(passengerData.getGetRouteByBusResponse().getData().getWaypoints().get(toPos).getId());

        purchaseTicketRequests.setFromLocation(posList1.get(fromPos));
        purchaseTicketRequests.setToLocation(posList2.get(toPos));
        purchaseTicketRequests.setDistance(distance.toString());
        purchaseTicketRequests.setPolyline(routePaths);
        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        Call<StatusResponse> listResponseCall = apiInterface.purchaseTicketByDriver(sessionManager.getAccessToken(), purchaseTicketRequests);
        listResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                final StatusResponse purchaseTicketResponse = response.body();
                if(purchaseTicketResponse == null){
                    AlertUtils.showAlerterWarning(requireActivity(), "Failed");
//                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpFragment(), R.id.frame_layout, true, false, false, null);
                }
                if(purchaseTicketResponse.getError().getCode().equals("0")){
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.purchase_success, null);
                    new AlertDialog.Builder(context)
                            .setView(dialogView)
                            .setTitle("Success!")
                            .setMessage("Buy ticket has succeed!")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                    AppActivityManager.popStack((AppCompatActivity) requireActivity(),2);
                                }
                            }).show();
                }
                else{
                    AlertUtils.showAlerterWarning(requireActivity(), purchaseTicketResponse.getError().getMessage());
//                    AppActivityManager.redirectTo((AppCompatActivity) requireActivity(), new TopUpFragment(), R.id.frame_layout, true, false, false, null);
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                AlertUtils.showAlerterWarning(requireActivity(), "Net work Connection Problem!");
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });
    }

    public void loadWalletInfo() {
//        CustomProgressDialog.getInstance().showDialog(getContext(), true);
        sessionManager = new SessionManager(getContext());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<GetWalletBalanceResponse> listResponseCall = apiInterface.getWalletBalance(sessionManager.getAccessToken());
        listResponseCall.enqueue(new Callback<GetWalletBalanceResponse>() {
            @Override
            public void onResponse(Call<GetWalletBalanceResponse> call, Response<GetWalletBalanceResponse> response) {
                CustomProgressDialog.getInstance().dismissDialog();
                GetWalletBalanceResponse walletBalanceResponse = response.body();
                DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
                cash.setText(moneyFormat.format(new Float(walletBalanceResponse.getBalance())));
            }

            @Override
            public void onFailure(Call<GetWalletBalanceResponse> call, Throwable t) {
                t.printStackTrace();
                CustomProgressDialog.getInstance().dismissDialog();
            }
        });

    }
}
