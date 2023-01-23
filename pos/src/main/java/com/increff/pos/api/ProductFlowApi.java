//package com.increff.pos.api;
//
//import com.increff.pos.helper.ProductHelper;
//import com.increff.pos.model.data.ProductData;
//import com.increff.pos.model.form.ProductForm;
//import com.increff.pos.pojo.ProductPojo;
//import com.increff.pos.util.ApiException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//@Component
//@Service
//public class ProductFlowApi {
//
//    @Autowired
//    private ProductApi api;
//
//    @Autowired
//    private BrandApi brandApi;
//
//    public void add(ProductPojo p, String brand, String category) throws ApiException {
//        p.setBrandCategory(brandApi.getBrandCategoryId(brand, category));
//        api.add(p);
//    }
//
//    public ProductData get(int id) throws ApiException {
//        ProductPojo p = api.get(id);
//        return ProductHelper.convert(p);
//    }
//}
