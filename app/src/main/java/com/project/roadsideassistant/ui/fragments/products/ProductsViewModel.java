package com.project.roadsideassistant.ui.fragments.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.roadsideassistant.data.models.Product;
import com.project.roadsideassistant.data.repositories.ProductsRepository;

import java.util.List;

public class ProductsViewModel extends ViewModel implements ProductsRepository.ProductTaskListener {

    private ProductsRepository productsRepository;

    private MutableLiveData<List<Product>> _products = new MutableLiveData<>();

    public ProductsViewModel() {

        productsRepository = new ProductsRepository(this);
        productsRepository.getProducts();
    }

    public LiveData<List<Product>> getProducts(){
        return _products;
    }

    @Override
    public void showProducts(List<Product> products) {
        _products.setValue(products);
    }

    @Override
    public void showError(String message) {
        //Show error
    }
}
