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

import bbt.com.prompter.ContactsListingActivity;
import bbt.com.prompter.R;
import bbt.com.prompter.adapters.TemplateAdapter;

/**
 * Created by anish on 08-01-2018.
 */

public class TemplateFragment extends Fragment {
    TemplateAdapter templateAdapter;
    private RecyclerView rvTemplates;
    private FloatingActionButton fabAddContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_templates, null, false);
        this.fabAddContact = (FloatingActionButton) view.findViewById(R.id.fabAddContact);
        this.rvTemplates = (RecyclerView) view.findViewById(R.id.rvTemplates);
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
    }
}
