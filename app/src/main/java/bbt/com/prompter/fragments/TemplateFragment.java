package bbt.com.prompter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bbt.com.prompter.ContactsListingActivity;
import bbt.com.prompter.R;
import bbt.com.prompter.adapters.TemplateAdapter;
import bbt.com.prompter.model.ContactTable;

/**
 * Created by anish on 08-01-2018.
 */

public class TemplateFragment extends Fragment {
    TemplateAdapter templateAdapter;
    private RecyclerView rvTemplates;
    private FloatingActionButton fabAddContact;
    private android.widget.RelativeLayout cLayout;
    private View emptyLayout;
    private boolean firstVisit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_templates, null, false);
        this.emptyLayout = view.findViewById(R.id.emptyLayout);
        this.fabAddContact = (FloatingActionButton) view.findViewById(R.id.fabAddContact);
        this.rvTemplates = (RecyclerView) view.findViewById(R.id.rvTemplates);

        firstVisit = true;
        initAdapter();
        init();
        return view;
    }

    private void init() {
        initListener();

    }

    private void initListener() {
        //-------rv scroll listener---------------
        rvTemplates.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabAddContact.isShown())
                    fabAddContact.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fabAddContact.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ContactsListingActivity.class));
            }
        });
    }

    private void initAdapter() {
        templateAdapter = new TemplateAdapter(getActivity());
        rvTemplates.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTemplates.setAdapter(templateAdapter);
        if (firstVisit) {
            fetchRecords();
        }

    }

    private void fetchRecords() {
        List<ContactTable> contactTables = new ArrayList<>();
        contactTables = ContactTable.getAllContacts(getActivity());
        if (!contactTables.isEmpty()) {
            templateAdapter.setItems(contactTables);
        } else {
            rvTemplates.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstVisit) {
            firstVisit = false;
        } else {
            fetchRecords();
        }
    }
}
