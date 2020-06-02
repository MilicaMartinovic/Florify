package com.example.florify.dialogs;

import com.example.florify.models.PostFilters;

public interface OnFiltersSubmitted {
    void OnFiltersSubmitCompleted(boolean plantNameEnabled, boolean radiusEnabled,
                                  boolean dateTimeRangeEnabled, PostFilters postFilters);
}
