package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.adapters.GigsAdapter;
import campusorders.com.quebasetech.joe.campusorders.dialogs.AddItemDialog;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class SellerItems extends Fragment {
    private FloatingActionButton floatingAddItemButton;
    private List<Gig> mGigList;
    private DatabaseReference gigsRef;
    private View sellerGigsView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sellerGigsView = inflater.inflate(R.layout.seller_items, container, false);

        mGigList = new ArrayList<>();
        gigsRef = FirebaseDatabase.getInstance().getReference("gigs");

        //Add add Floating button
        floatingAddItemButton = (FloatingActionButton) sellerGigsView.findViewById(R.id.floatingAddItemButton);
        floatingAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemDialog dialog = new AddItemDialog();
                dialog.show(getFragmentManager(), "add item");
            }
        });
        return sellerGigsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        final String currentUser = utils.getCurrentUserEmail();
        // TODO:: Add query filter here to only fetch current user items
        Query gigs = gigsRef.orderByChild("sellerId").equalTo(currentUser);
        gigs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGigList.clear();
                for(DataSnapshot gigSnapShot: dataSnapshot.getChildren()){
                    Gig gig = gigSnapShot.getValue(Gig.class);
//                    if(currentUser.equals(gig.getSellerId()))
                    mGigList.add(gig);
                }
                //Display items
                ListView gigsList = (ListView) sellerGigsView.findViewById(R.id.items_for_sale);
                GigsAdapter gigsAdapter = new GigsAdapter(getContext(), mGigList);
                gigsList.setAdapter(gigsAdapter);
                // TODO:: Fix this code smell (- ^ -)
                /*gigsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Gig gig = mGigList.get(position);
                        Toast.makeText(getContext(), gig.toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
