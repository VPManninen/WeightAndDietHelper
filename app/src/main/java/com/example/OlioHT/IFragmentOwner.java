package com.example.OlioHT;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface IFragmentOwner {
    // Interface to use "changeFragment" from other classes
    void changeFragment(@NonNull Fragment newFragment, boolean addToBackStack, boolean lockDrawer);
}
