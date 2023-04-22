package com.bid.app.ui.base;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.NotificationListInfo;
import com.bid.app.model.response.TransactionsHistory;
import com.bid.app.model.view.About;
import com.bid.app.model.view.Address;
import com.bid.app.model.view.BIDHome;
import com.bid.app.model.view.Country;
import com.bid.app.model.view.CovidResult;
import com.bid.app.model.response.IdDocumentsData;
import com.bid.app.model.view.FamilyData;
import com.bid.app.model.view.PersonalDetail;
import com.bid.app.model.view.Restaurant;
import com.bid.app.model.view.SelectIdDocument;
import com.bid.app.model.view.Settings;
import com.bid.app.model.view.Symptoms;
import com.bid.app.model.view.Ticket;
import com.bid.app.model.view.TransactionHistory;
import com.bid.app.model.view.Trip;
import com.bid.app.ui.fragment.discover.transit.ScheduleFragment;

import org.json.JSONObject;

public class BaseFragment extends Fragment implements View.OnClickListener, IRecyclerViewClickListener {

    @Override
    public void onClick(View view) {

    }

    @Override
    public void clickOnBIDHome(int position, BIDHome home) {

    }

    @Override
    public void clickOnSchedule(int position, Ticket object) {

    }

    @Override
    public void clickOnSettings(int position, Settings setting) {

    }

    @Override
    public void clickOnNotificationsSettings(int position, NotificationListInfo setting) {

    }
    @Override
    public void clickApprove(int position,Boolean decision) {

    }

    @Override
    public void clickOnAbout(int position, About about) {

    }

    @Override
    public void clickOnPersonalDetail(int position, PersonalDetail personalDetail) {

    }

    @Override
    public void clickOnAddress(int position, Address address, boolean isDelete) {

    }

    @Override
    public void clickOnResult(int position, CovidResult covidResult) {

    }

    @Override
    public void clickOnSlot(int position) {

    }

    @Override
    public void clickOnRemove(int position) {

    }

    @Override
    public void clickOnSymptoms(int position, Symptoms symptoms) {

    }

    @Override
    public void clickOnAddCard(CardListInfo paymentGateway) {

    }

    @Override
    public void clickOnCountry(int position, Country country) {

    }

    @Override
    public void clickIdDocuments(int position, IdDocumentsData idDocument) {

    }

    @Override
    public void clickFamilyData(int position, FamilyData familyData) {

    }
    @Override
    public void clickTickets(int position, Ticket idDocument, int type) {

    }

    @Override
    public void clickTrip(int position, Trip idDocument, int type) {

    }

    @Override
    public void clickSeeBus(int position, Ticket idDocument) {

    }
    @Override
    public void clickTransactionHistory(int position, TransactionsHistory transactionHistory) {

    }
    @Override
    public void clickRestaurant(int position, Restaurant restaurant) {

    }

    @Override
    public void clickAddIDCardType(int position, SelectIdDocument selectIdDocument) {

    }

    @Override
    public void clickOnDailyTest(int position , Symptoms symptoms) {

    }

    @Override
    public void clickOnCardListInfo(int position, CardListInfo cardListInfo) {

    }

}
