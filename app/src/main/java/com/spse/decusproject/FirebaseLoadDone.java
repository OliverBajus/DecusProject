package com.spse.decusproject;

import java.util.List;

public interface FirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Product> productList);
    void onFirebaseLoadSuccessAllergens(List<Allergen> allergenList);
    void onFirebaseLoadFailed(String message);
}
