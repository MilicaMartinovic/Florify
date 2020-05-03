package com.example.florify.Dialogs;

import com.example.florify.models.DateRangeItems;
import com.example.florify.models.PostType;

public interface OnFiltersSubmitted {
    void OnFiltersSubmitCompleted(String plantName, PostType postType, int radius, DateRangeItems dateRange);
}
