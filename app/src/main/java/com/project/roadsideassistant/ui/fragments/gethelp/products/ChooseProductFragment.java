package com.project.roadsideassistant.ui.fragments.gethelp.products;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;

public class ChooseProductFragment extends Fragment {
    private static final String TAG = "ChooseProductFragment";

    private ChooseProductAdapter chooseProductAdapter;
    private RecyclerView productsRecyclerView;
    private Message message;

    public ChooseProductFragment() {
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
        assert getContext() != null;
        productsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        productsRecyclerView.setHasFixedSize(true);

        assert getArguments() != null;
        //The the message argument first
        message = ChooseProductFragmentArgs.fromBundle(getArguments()).getMessage();
        assert message != null;
        Log.d(TAG, "onViewCreated: services length : " + message.getServicesList().size());
        //Register the button and it's click listener
        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {
            assert chooseProductAdapter != null;
            message.addProducts(chooseProductAdapter.getCheckedProducts());
            ChooseProductFragmentDirections.ActionChooseProductFragmentToLocationFragment action = ChooseProductFragmentDirections.actionChooseProductFragmentToLocationFragment();
            action.setMessage(message);
            Navigation.findNavController(v).navigate(action);

            Log.d(TAG, "onViewCreated: products count: " + chooseProductAdapter.getCheckedProducts().size());
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ChooseProductViewModel mViewModel = new ViewModelProvider(this).get(ChooseProductViewModel.class);

        mViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {

            chooseProductAdapter = new ChooseProductAdapter(products);
            productsRecyclerView.setAdapter(chooseProductAdapter);

        });
    }

}
