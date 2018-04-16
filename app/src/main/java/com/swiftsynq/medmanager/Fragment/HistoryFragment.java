package com.swiftsynq.medmanager.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swiftsynq.medmanager.MedManagerTbOperations;
import com.swiftsynq.medmanager.Model.History;
import com.swiftsynq.medmanager.Model.Medication;
import com.swiftsynq.medmanager.R;
import com.swiftsynq.medmanager.data.MedmanagerDbHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by popoolaadebimpe on 31/03/2018.
 */

public class HistoryFragment extends Fragment implements SearchView.OnQueryTextListener {
    private SectionedRecyclerViewAdapter sectionAdapter;
    private MedmanagerDbHelper mDbHelper;
    List<History>historyList;
    List<String> dates;
    @BindView(R.id.imgEmptylistHis)
    ImageView imgEmptylistHis;
    @BindView(R.id.rvhistory)
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        ButterKnife.bind(this,rootView); // bind butterknife after
        Init();
        return rootView;

    }
    private void Init()
    {
        mDbHelper= new MedmanagerDbHelper(getContext());
        historyList = MedManagerTbOperations.getHistory(mDbHelper);
        sectionAdapter = new SectionedRecyclerViewAdapter();

        if(historyList.size()>0)
        {
            imgEmptylistHis.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            dates=new ArrayList<>();
            for(History history : historyList)
            {
                if(!dates.contains(history.getDateString()))
                    dates.add(history.getDateString());
            }
            List<History> originalList=null;
            for (String date : dates) {

                originalList = getMedicationWithDate(date);
                if (originalList.size() > 0) {
                    sectionAdapter.addSection(new HistorySection(date, originalList));
                }
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(sectionAdapter);
        }

        else
            imgEmptylistHis.setVisibility(View.VISIBLE);
    }
    public static HistoryFragment newInstance() {
    HistoryFragment fragment=new HistoryFragment();
    return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("History");
            }
        }
    }


    private List<History> getMedicationWithDate(String date) {
        List<History> histories = new ArrayList<>();
        for (History history : historyList) {
            if (date.contains(history.getDateString())) {
                histories.add(history);
            }
        }

        return histories;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionable_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }
    @Override
    public boolean onQueryTextChange(String query) {

        for (Section section : sectionAdapter.getCopyOfSectionsMap().values()) {
            if (section instanceof HistorySection) {
                ((HistorySection) section).filter(query);
            }
        }
        sectionAdapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private class HistorySection extends StatelessSection implements FilterableSection {

        String title;
        List<History> list;
        List<History> filteredList;
        HistorySection(String title, List<History> list) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.custom_history)
                    .headerResourceId(R.layout.custom_section_header)
                    .build());

            this.title = title;
            this.list = list;
            this.filteredList = new ArrayList<>(list);
        }

        @Override
        public int getContentItemsTotal() {
            return filteredList.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            String drugName = filteredList.get(position).getPillName();
            String datetaken = filteredList.get(position).getDateString();
            String hour = String.valueOf(filteredList.get(position).getHourTaken());
            String min = String.valueOf(filteredList.get(position).getMinuteTaken());
            itemHolder.tvMedication.setText(drugName);
            itemHolder.tvDate.setText(datetaken);
            itemHolder.tvTime.setText(hour+":"+min);
            itemHolder.imgItem.setImageResource(R.drawable.pill_icon);
            //itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.pill_icon : R.drawable.ic_tag_faces_black_48dp);


        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);
        }
        @Override
        public void filter(String query) {
            if (TextUtils.isEmpty(query)) {
                filteredList = new ArrayList<>(list);
                this.setVisible(true);
            } else {
                filteredList.clear();
                for (History value : list) {
                    if (value.getPillName().contains(query.toLowerCase())) {
                        filteredList.add(value);
                    }
                }

                this.setVisible(!filteredList.isEmpty());
            }
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView imgItem;
        private final TextView tvMedication;
        private final TextView tvDate;
        private final TextView tvTime;
        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
            tvMedication = (TextView) view.findViewById(R.id.tvMedication);
            tvDate=(TextView)view.findViewById(R.id.tvDate);
            tvTime=(TextView)view.findViewById(R.id.tvTime);
        }
    }
    interface FilterableSection {
        void filter(String query);
    }
    @Override
    public void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

}
