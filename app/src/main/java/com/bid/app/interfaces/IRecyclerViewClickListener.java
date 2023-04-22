package com.bid.app.interfaces;

import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.response.IdDocumentsData;
import com.bid.app.model.response.NotificationListInfo;
import com.bid.app.model.response.TransactionsHistory;
import com.bid.app.model.view.About;
import com.bid.app.model.view.Address;
import com.bid.app.model.view.BIDHome;
import com.bid.app.model.view.Country;
import com.bid.app.model.view.CovidResult;
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

public interface IRecyclerViewClickListener {

    void clickOnBIDHome(int position, BIDHome home);

    void clickOnSettings(int position, Settings setting);

    void clickOnNotificationsSettings(int position, NotificationListInfo setting);

    void clickApprove(int position,Boolean decision);

    void clickOnAbout(int position, About about);

    void clickOnSchedule(int position, Ticket schedule);

    void clickOnPersonalDetail(int position, PersonalDetail personalDetail);

    void clickOnAddress(int position, Address address, final boolean isDelete);

    void clickOnResult(int position, CovidResult covidResult);

    void clickOnSlot(int position);

    void clickOnRemove(int position);

    void clickOnSymptoms(final int position, final Symptoms symptoms);

    void clickOnAddCard(CardListInfo paymentGateway);

    void clickOnCountry(int position, Country country);

    void clickIdDocuments(int position, IdDocumentsData idDocument);

    void clickFamilyData(int position, FamilyData familyData);

    void clickTickets(int position, Ticket ticket, int Type);

    void clickTrip(int position, Trip trip, int Type);

    void clickSeeBus(int position, Ticket ticket);

    void clickTransactionHistory(int position, TransactionsHistory transactionHistory);

    void clickRestaurant(int position, Restaurant restaurant);

    void clickAddIDCardType(int position, SelectIdDocument selectIdDocument);

    void clickOnDailyTest(int position , Symptoms symptoms);

    void clickOnCardListInfo(int position, CardListInfo cardListInfo);

}
