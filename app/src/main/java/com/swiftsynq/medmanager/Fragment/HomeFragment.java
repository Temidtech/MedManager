package com.swiftsynq.medmanager.Fragment;

import android.net.Uri;
import android.os.Binder;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.swiftsynq.medmanager.MedManagerTbOperations;
import com.swiftsynq.medmanager.Model.Medication;
import com.swiftsynq.medmanager.R;
import com.swiftsynq.medmanager.Utils.CircleTransform;
import com.swiftsynq.medmanager.data.MedManagerPreferences;
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

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener{
    private SectionedRecyclerViewAdapter sectionAdapter;
    List<Medication>mMedications;
    List<String> dates;
    @BindView(R.id.imgEmptylist)
    ImageView imgEmptylist;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.lytProfile)
    LinearLayout lytProfile;
    @BindView(R.id.tvDisplayName)
    TextView tvDisplayName;
    @BindView(R.id.profile_image)
    ImageView profile_image;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this,rootView); // bind butterknife after
        Init();
        return rootView;

    }
    private void Init()
    {
        new MedManagerTbOperations(getContext());
        mMedications = MedManagerTbOperations.Retrieve();
        sectionAdapter = new SectionedRecyclerViewAdapter();

        if(mMedications.size()>0)
        {
            imgEmptylist.setVisibility(View.GONE);
            lytProfile.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            dates=new ArrayList<>();
            for(Medication medication : mMedications)
            {
                if(!dates.contains(medication.getStartdate()))
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
            tvDisplayName.setText(MedManagerPreferences.getUserDetails(getContext()).getDisplayName());
        Glide
                .with(getContext())
                .load(Uri.parse(MedManagerPreferences.getUserDetails(getContext()).getPhotoUrl()))
                .transform(new CircleTransform(getContext()))
                .placeholder(R.drawable.pill_icon)
                .into(profile_image);
           // profile_image.setImageURI(Uri.parse(MedManagerPreferences.getUserDetails(getContext()).getPhotoUrl()));
        lytProfile.setVisibility(View.VISIBLE);
        imgEmptylist.setVisibility(View.VISIBLE);
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


    private List<Medication> getMedicationWithDate(String date) {
        List<Medication> medications = new ArrayList<>();
        for (Medication medication : mMedications) {
            if (date.contains(medication.getStartdate())) {
                medications.add(medication);
            }
        }

        return medications;
    }
    @Override
    public boolean onQueryTextChange(String query) {

        for (Section section : sectionAdapter.getCopyOfSectionsMap().values()) {
            if (section instanceof FilterableSection) {
                ((FilterableSection) section).filter(query);
            }
        }
        sectionAdapter.notifyDataSetChanged();

        return true;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionable_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private class ContactsSection extends StatelessSection implements FilterableSection {

        String title;
        List<Medication> list;
        List<Medication> filteredList;
        ContactsSection(String title, List<Medication> list) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.custom_medication)
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

            String drugName = filteredList.get(position).getDrugName();
            String decsription = filteredList.get(position).getDecsription();
            String enddate = filteredList.get(position).getEnddate();
            String interval = filteredList.get(position).getInterval();
            String startdate = filteredList.get(position).getStartdate();
            itemHolder.tvItem5.setText(drugName);
            itemHolder.tvInterval.setText(interval+"time(s)");
            itemHolder.tvDescription.setText(decsription);
            itemHolder.tvStartDate.setText(startdate);
            itemHolder.tvEndDate.setText(enddate);
            itemHolder.imgItem.setImageResource(R.drawable.pill_icon);
            //itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.pill_icon : R.drawable.ic_tag_faces_black_48dp);

            /*itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),
                            String.format("Clicked on position #%s of Section %s",
                                    sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()),
                                    title),
                            Toast.LENGTH_SHORT).show();
                }
            });*/
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
                for (Medication value : list) {
                    if (value.getDrugName().toLowerCase().contains(query.toLowerCase())) {
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
    @Override
    public void onDestroy() {
        MedManagerTbOperations.close();
        super.onDestroy();
    }
    interface FilterableSection {
        void filter(String query);
    }
}
