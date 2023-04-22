package com.bid.app.adaper;

import static com.bid.app.ui.fragment.home.SymptomsBottomSheetDialog.NONE_OF_THE_ABOVE;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.model.view.Symptoms;

import java.util.ArrayList;
import java.util.List;

public class BookATestSymptomsAdapter extends RecyclerView.Adapter<BookATestSymptomsAdapter.ViewHolder> {

    private static final String TAG = BookATestSymptomsAdapter.class.getSimpleName();
    private IReasonClickListenerListener iReasonClickListenerListener;
    private ArrayList<String> selectedSymptomsPositions = new ArrayList<>();
    private List<Symptoms> symptomsList;

    public interface IReasonClickListenerListener {
        void clickOnASymptom(int position);
    }

    public ArrayList<String> getSelectedSymptomsPositions() {
        return selectedSymptomsPositions;
    }

    public void setSelectedSymptomsPositions(ArrayList<String> selectedSymptomsPositions) {
        this.selectedSymptomsPositions = selectedSymptomsPositions;
    }

    public BookATestSymptomsAdapter(List<Symptoms> reasons, IReasonClickListenerListener listener) {
        this.iReasonClickListenerListener = listener;
        this.symptomsList = reasons;
    }

    @NonNull
    @Override
    public BookATestSymptomsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptom_item, parent, false);
        return new BookATestSymptomsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookATestSymptomsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.reason_name.setText(symptomsList.get(position).getName());

        holder.options_cb.setChecked(selectedSymptomsPositions.contains(String.valueOf(position)));

        holder.constraint_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Symptoms symptoms = symptomsList.get(holder.getAdapterPosition());
                if (selectedSymptomsPositions.contains(String.valueOf(holder.getAdapterPosition()))) {
                    selectedSymptomsPositions.remove(String.valueOf(holder.getAdapterPosition()));
                } else {
                    if (symptoms.getName().equalsIgnoreCase(NONE_OF_THE_ABOVE)) {
                        selectedSymptomsPositions.clear();
                        selectedSymptomsPositions.add(String.valueOf(holder.getAdapterPosition()));
                    } else if (!selectedSymptomsPositions.contains(String.valueOf(symptomsList.size() - 1))) {
                        selectedSymptomsPositions.add(String.valueOf(holder.getAdapterPosition()));
                    }
                }

                iReasonClickListenerListener.clickOnASymptom(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return symptomsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatCheckBox options_cb;
        private AppCompatTextView reason_name;
        private ConstraintLayout constraint_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reason_name = itemView.findViewById(R.id.reason_name);
            options_cb = itemView.findViewById(R.id.options_cb);
            constraint_layout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}