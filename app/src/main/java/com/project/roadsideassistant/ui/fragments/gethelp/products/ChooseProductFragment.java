package com.project.roadsideassistant.ui.fragments.gethelp.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roadsideassistant.R;

public class ChooseProductFragment extends Fragment {

    private ChooseProductViewModel mViewModel;
    private ChooseProductAdapter chooseProductAdapter;
    private RecyclerView productsRecyclerView;

    public static ChooseProductFragment newInstance() {
        return new ChooseProductFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_product_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productsRecyclerView = view.findViewById(R.id.products_recycler_view);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productsRecyclerView.setHasFixedSize(true);

        //Register the button and it's click listener
        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_chooseProductFragment_to_locationFragment);
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChooseProductViewModel.class);

        mViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {

            chooseProductAdapter = new ChooseProductAdapter(products);
            productsRecyclerView.setAdapter(chooseProductAdapter);

        });
    }

}
