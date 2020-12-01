package com.example.bunshop;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bunshop.catalog.Catalog;
import com.example.bunshop.catalog.CatalogAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CatalogFrag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private ListView listView;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public CatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatalogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogFragment newInstance(String param1, String param2) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        if (getView() != null)
            listView = getView().findViewById(R.id.lvCategories);
        getCategoriesFromFirestore();
    }

    private void getCategoriesFromFirestore() {
        ArrayList<Catalog> categories = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories/uk/data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: true");
                            if (task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                                ArrayList<Catalog> categories = new ArrayList<>();
                                for (DocumentSnapshot categoryData : task.getResult().getDocuments()) {
                                    categories.add(new Catalog(
                                            categoryData.getString("name"),
                                            getReferenceIcon(categoryData.getString("icon_name")))
                                    );
                                }
                                setCategoriesFromFirebase(categories);
                            }
                        }
                        else {
                            Log.i(TAG, "onComplete: failure", task.getException());
                        }
                    }
                });
    }

    private void setCategoriesFromFirebase(ArrayList<Catalog> categories) {
        CatalogAdapter adapter = new CatalogAdapter(getContext(), categories);
        listView.setAdapter(adapter);
    }

    private StorageReference getReferenceIcon(String iconName) {
        return storage.getReference().child("category_icon/" + iconName);
    }
}