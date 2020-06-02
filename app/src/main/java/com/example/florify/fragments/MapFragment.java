package com.example.florify.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.florify.R;
import com.example.florify.db.DBInstance;
import com.example.florify.dialogs.FiltersDialog;
import com.example.florify.dialogs.OnFiltersSubmitted;
import com.example.florify.helpers.DateTimeHelper;
import com.example.florify.helpers.ListsHelper;
import com.example.florify.helpers.MapResolver;
import com.example.florify.helpers.NetworkHelper;
import com.example.florify.models.DateRangeItems;
import com.example.florify.models.Post;
import com.example.florify.models.PostFilters;
import com.example.florify.models.PostType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

public class MapFragment extends Fragment  implements OnMapReadyCallback, OnFiltersSubmitted {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private MapResolver mapResolver;
    private NetworkHelper networkHelper;
    private String[] plant_names;
    private ImageButton magnifier;
    private FloatingActionButton floatingActionButton;
    private FiltersDialog filtersDialog;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);

        networkHelper = new NetworkHelper();
        networkHelper.checkLocation(view.getContext());

        mapResolver = new MapResolver(getContext());
        floatingActionButton = view.findViewById(R.id.fab_filter);

        initMap();
        //Context context = this;
        final OnFiltersSubmitted mapContext = MapFragment.this;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersDialog = new FiltersDialog(getContext(), mapContext);
                filtersDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(networkHelper.checkLocation(getContext()) && mapResolver.getLastKnownLocation() != null) {

            moveCamera(new LatLng(mapResolver.getLastKnownLocation().getLatitude(), mapResolver.getLastKnownLocation().getLongitude()), 1f);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            getAllPosts();
        }
        else {

            Toast.makeText(getContext(), "GPS not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_explore);

        mapFragment.getMapAsync(MapFragment.this);
    }

    private void moveCamera(LatLng loc, float zoom) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;

                            return;
                        }
                    }

                    mLocationPermissionsGranted = true;

                    initMap();
                }
            }
        }
    }

    @Override
    public void OnFiltersSubmitCompleted(final boolean plantNameEnabled, boolean radiusEnabled,
                                         final boolean dateTimeRangeEnabled, final PostFilters postFilters) {

        final ArrayList<Post> postsByPostType = new ArrayList<>();

        //getPostsWithAllFilters(plantNameEnabled, dateTimeRangeEnabled, postFilters);

        if (postFilters.getPostTypes().size() > 0 && postFilters.getPostTypes().size() < 4) {
            getPostsByType(postFilters.getPostTypes()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot snapshot : task.getResult()) {
                            Post post = snapshot.toObject(Post.class);
                            postsByPostType.add(post);
                        }
                        getPostsWithAllFilters(plantNameEnabled, dateTimeRangeEnabled, postFilters,
                                postsByPostType);
                    }
                    else {
                        Toast.makeText(getContext(), "couldn't fetch", Toast.LENGTH_SHORT).show();
                        filtersDialog.dismiss();
                    }

                }

            });
        }
        else {
            DBInstance.getCollection("posts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    postsByPostType.add(snapshot.toObject(Post.class));
                                }
                                getPostsWithAllFilters(plantNameEnabled, dateTimeRangeEnabled, postFilters,
                                        postsByPostType);
                            }
                            else {
                                Toast.makeText(getContext(), "couldn't fetch", Toast.LENGTH_SHORT).show();
                                filtersDialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void getAllPosts() {

        final ArrayList<Post> posts = new ArrayList<>();
        DBInstance.getCollection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot snapshot : task.getResult()){
                                posts.add(snapshot.toObject(Post.class));
                            }
                            addMarkersOnMap(posts);
                        }
                        else {
                            Toast.makeText(getContext(), "couldn't fetch", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Task<QuerySnapshot> getPlantNameSnapshot(String plantName) {
        Task<QuerySnapshot> plantNameSnapshot = DBInstance.getCollection("posts")
                .whereEqualTo("plantName", plantName)
                .get();
        return plantNameSnapshot;
    }

    private Task<QuerySnapshot> getDateTimeRange(DateRangeItems item) {
        long miliseconds = DateTimeHelper.getMilisecondsByDateRangeItem(item);
        Task<QuerySnapshot> dateRangeSnapshot = DBInstance.getCollection("posts")
                .whereGreaterThan("date", miliseconds)
                .get();
        return dateRangeSnapshot;
    }

    private Task<QuerySnapshot> getPostsByType(ArrayList<PostType> types) {

        Task<QuerySnapshot> postTypeSnapshot = DBInstance.getCollection("posts")
                .whereIn("postType", types).get();
        return postTypeSnapshot;
    }

    private void getPostsWithAllFilters(boolean plantNameEnabled, boolean dateTimeRangeEnabled,
                                        final PostFilters filters, final ArrayList<Post> postsByType) {

        final ArrayList<Post> postsByPlantName = new ArrayList<>();
        final ArrayList<Post> postsByDateRange = new ArrayList<>();

        if(plantNameEnabled && dateTimeRangeEnabled) {
            getPlantNameSnapshot(filters.getPlantName()).addOnCompleteListener(
                    new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Post post = documentSnapshot.toObject(Post.class);
                            postsByPlantName.add(post);
                        }

                        getDateTimeRange(filters.getDateRange()).addOnCompleteListener(
                                new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        Post post = documentSnapshot.toObject(Post.class);
                                        postsByDateRange.add(post);
                                    }

                                    ArrayList<Post> intersectedFirst =
                                            new ListsHelper<Post>().
                                                    intersectTwoLists(postsByPlantName, postsByDateRange);
                                    ArrayList<Post> intersectedFinal =
                                            new ListsHelper<Post>().
                                                    intersectTwoLists(intersectedFirst, postsByType);

                                    getPostsWithinRadiusAndUpdateUI(intersectedFinal, filters.getRadius());

                                }
                                else {
                                    Toast.makeText(getContext(), "Couldnt fetch",
                                            Toast.LENGTH_SHORT).show();
                                    filtersDialog.dismiss();
                                }
                            }
                        });
                    }
                    else
                        Toast.makeText(getContext(), "Couldnt fetch",
                                Toast.LENGTH_SHORT).show();
                    filtersDialog.dismiss();
                }
            });
        }
        if(plantNameEnabled && !dateTimeRangeEnabled) {
            getPlantNameSnapshot(filters.getPlantName()).addOnCompleteListener(
                    new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Post post = documentSnapshot.toObject(Post.class);
                            postsByPlantName.add(post);
                        }

                        ArrayList<Post> intersectedFinal =
                                new ListsHelper<Post>().intersectTwoLists(postsByPlantName, postsByType);
                        getPostsWithinRadiusAndUpdateUI(intersectedFinal, filters.getRadius());
                    }
                    else {
                        Toast.makeText(getContext(), "Couldnt fetch",
                                Toast.LENGTH_SHORT).show();
                        filtersDialog.dismiss();
                    }
                }
            });
        }
        if(!plantNameEnabled && dateTimeRangeEnabled) {
            getDateTimeRange(filters.getDateRange())
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Post post = documentSnapshot.toObject(Post.class);
                                    postsByDateRange.add(post);
                                }
                                ArrayList<Post> intersectedFinal =
                                        new ListsHelper<Post>().intersectTwoLists(postsByDateRange, postsByType);
                                getPostsWithinRadiusAndUpdateUI(intersectedFinal, filters.getRadius());
                            }
                            else {
                                Toast.makeText(getContext(), "Couldnt fetch",
                                        Toast.LENGTH_SHORT).show();
                                filtersDialog.dismiss();
                            }
                        }
                    });
        }
        if(!plantNameEnabled && !dateTimeRangeEnabled) {
            getPostsWithinRadiusAndUpdateUI(postsByType, filters.getRadius());
        }
    }

    private void getPostsWithinRadiusAndUpdateUI(ArrayList<Post> intersectedFinal, int radius) {

        ArrayList<Post> allFiltered = new ArrayList<>();

        if(radius > 0) {
            Location myLocation = mapResolver.getLastKnownLocation();
            if(myLocation != null) {
                for(Post post : intersectedFinal) {
                    double distanceInM = SphericalUtil.computeDistanceBetween(
                            new LatLng(myLocation.getLatitude(), myLocation.getLongitude()),
                            new LatLng(post.getL().getLatitude(), post.getL().getLongitude()));

                    if ((distanceInM/1000) < radius)
                        allFiltered.add(post);

                }
            }
            addMarkersOnMap(allFiltered);
        }
        else {
            addMarkersOnMap(intersectedFinal);
        }
        filtersDialog.dismiss();
    }

    private void addMarkersOnMap(ArrayList<Post> posts) {
        mMap.clear();
        for(Post post : posts) {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(post.getL().getLatitude(),
                    post.getL().getLongitude())));
            mMap.addMarker(configureCustomMarker(post));
        }
    }

    private MarkerOptions configureCustomMarker(Post post) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(post.getL().getLatitude(), post.getL().getLongitude()));
        markerOptions.title(post.getL().getLatitude() + " : " + post.getL().getLatitude());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(
                getDrawableByType(post.getPostType()), 120, 120)));
        return markerOptions;
    }

    private int getDrawableByType(PostType postType) {
        switch(postType) {
            case LEAF: return R.drawable.list__marker;
            case SCAPE: return R.drawable.stablo_marker;
            case FLOWER: return R.drawable.cvet_marker;
            case WHOLEPLANT: return R.drawable.biljka_marker;
        }
        return 0;
    }

    public Bitmap resizeMapIcons(int res, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), res );
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
