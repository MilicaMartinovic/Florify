package com.example.florify.helpers;

import java.util.ArrayList;
import java.util.Locale;

public class ListOfCountries {

    private ArrayList<String> counties;

    public ListOfCountries() {
        counties = new ArrayList<>();

        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            counties.add(obj.getDisplayCountry());
        }
    }

    public ArrayList<String> getCounties() {
        return this.counties;
    }
}
