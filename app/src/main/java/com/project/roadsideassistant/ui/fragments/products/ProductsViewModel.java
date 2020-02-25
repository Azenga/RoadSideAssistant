package com.project.roadsideassistant.ui.fragments.products;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.roadsideassistant.data.models.Product;
import com.project.roadsideassistant.data.repositories.ProductsRepository;

import java.util.List;

public class ProductsViewModel extends ViewModel implements ProductsRepository.ProductTaskListener {

    private static final String TAG = "ProductsViewModel";

    private ProductsRepository productsRepository;

    private MutableLiveData<List<Product>> _products = new MutableLiveData<>();

    public ProductsViewModel() {

        productsRepository = new ProductsRepository(this);
        productsRepository.getProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return _products;
    }

    @Override
    public void showProducts(List<Product> products) {
        Log.d(TAG, "showProducts: products size" + products.size());
        _products.setValue(products);
    }

    @Override
    public void showError(String message) {
        Log.d(TAG, "showError: viewmodel" + message);
        //Show error
    }
}
