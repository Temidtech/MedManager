package com.swiftsynq.medmanager.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swiftsynq.medmanager.MedManagerTbOperations;
import com.swiftsynq.medmanager.Model.Medication;
import com.swiftsynq.medmanager.R;
import com.swiftsynq.medmanager.data.MedmanagerDbHelper;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by popoolaadebimpe on 31/03/2018.
 */

public class HomeFragment extends Fragment {
    private SectionedRecyclerViewAdapter sectionAdapter;
    private MedmanagerDbHelper mDbHelper;
    List<Medication>mMedications;
    List<String> dates;
    ImageView imgEmptylist;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        imgEmptylist=(ImageView)rootView.findViewById(R.id.imgEmptylist);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mDbHelper= new MedmanagerDbHelper(getContext());
        mMedications = MedManagerTbOperations.Retrieve(mDbHelper);
        sectionAdapter = new SectionedRecyclerViewAdapter();
        
        if(mMedications.size()>0)
        {
            imgEmptylist.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            dates=new ArrayList<>();
            for(Medication medication : mMedications)
            {
                dates.add(medication.getStartdate());
            }
            List<Medication> originalList=null;
            for (String date : dates) {

                originalList = getMedicationWithDate(date);
                if (originalList.size() > 0) {
                    sectionAdapter.addSection(new ContactsSection(date, originalList));
                }
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(sectionAdapter);
        }

       else
           imgEmptylist.setVisibility(View.VISIBLE);
        return rootView;

    }
    public static HomeFragment newInstance() {
    HomeFragment fragment=new HomeFragment();
    return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Home");
            }
        }
    }

    private List<String> getContactsWithLetter(char letter) {
        List<String> contacts = new ArrayList<>();

        for (String contact : getResources().getStringArray(R.array.names)) {
            if (contact.charAt(0) == letter) {
                contacts.add(contact);
            }
        }

        return contacts;
    }
    private List<Medication> getMedicationWithDate(String date) {
        List<Medication> medications = new ArrayList<>();
        for (Medication medication : mMedications) {
            if (date.equals(medication.getStartdate())) {
                medications.add(medication);
            }
        }

        return medications;
    }
    private class ContactsSection extends StatelessSection {

        String title;
        List<Medication> list;

        ContactsSection(String title, List<Medication> list) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.custom_medication)
                    .headerResourceId(R.layout.custom_section_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            String drugName = list.get(position).getDrugName();
            String decsription = list.get(position).getDecsription();
            String enddate = list.get(position).getEnddate();
            String interval = list.get(position).getInterval();
            String startdate = list.get(position).getStartdate();
            itemHolder.tvItem5.setText(drugName);
            itemHolder.tvInterval.setText(interval);
            itemHolder.tvDescription.setText(decsription);
            itemHolder.tvStartDate.setText(startdate);
            itemHolder.tvEndDate.setText(enddate);
            itemHolder.imgItem.setImageResource(R.drawable.pill_icon);
            //itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.pill_icon : R.drawable.ic_tag_faces_black_48dp);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),
                            String.format("Clicked on position #%s of Section %s",
                                    sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()),
                                    title),
                            Toast.LENGTH_SHORT).show();
                }
            });
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
        private final TextView tvItem5;
        private final TextView tvDescription;
        private final TextView tvInterval;
        private final TextView tvStartDate;
        private final TextView tvEndDate;
        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
            tvItem5 = (TextView) view.findViewById(R.id.tvItem5);
            tvDescription=(TextView)view.findViewById(R.id.tvDescription);
            tvInterval=(TextView)view.findViewById(R.id.tvInterval);
            tvStartDate=(TextView)view.findViewById(R.id.tvStartDate);
            tvEndDate=(TextView)view.findViewById(R.id.tvEndDate);
        }
    }
}
