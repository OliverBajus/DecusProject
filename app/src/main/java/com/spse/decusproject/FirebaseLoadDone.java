package com.spse.decusproject;

import java.util.List;

public interface FirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Product> productList);
    void onFirebaseLoadFailed(String message);
}
