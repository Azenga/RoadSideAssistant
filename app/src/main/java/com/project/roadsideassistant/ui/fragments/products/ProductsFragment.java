package com.project.roadsideassistant.ui.fragments.products;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roadsideassistant.R;

public class ProductsFragment extends Fragment {

    private RecyclerView productsRecyclerView;

    private ProductsAdapter productsAdapter;
    private ProductsViewModel productsViewModel;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Register and setup the recycler view
        productsRecyclerView = view.findViewById(R.id.products_recycler_view);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productsRecyclerView.setHasFixedSize(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        productsViewModel = new ViewModelProvider(getActivity()).get(ProductsViewModel.class);

        productsViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {

            productsAdapter = new ProductsAdapter(products);

            productsRecyclerView.setAdapter(productsAdapter);

        });

    }
}
