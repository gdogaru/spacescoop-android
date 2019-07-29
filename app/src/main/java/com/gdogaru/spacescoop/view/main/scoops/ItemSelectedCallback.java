package com.gdogaru.spacescoop.view.main.scoops;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.gdogaru.spacescoop.db.model.HasId;

public interface ItemSelectedCallback {
    void onItemSelected(HasId hasId, @Nullable ImageView image);
}
