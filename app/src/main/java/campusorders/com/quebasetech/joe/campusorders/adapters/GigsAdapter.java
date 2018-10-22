package campusorders.com.quebasetech.joe.campusorders.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.dialogs.EditGigDialog;
import campusorders.com.quebasetech.joe.campusorders.fragments.SellerItems;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;

public class GigsAdapter extends ArrayAdapter<Gig> {
    private  final Context mContext;
    private List<Gig> gigsList;
    private DatabaseReference gigsDataBaseRef;
    ProgressDialog progressDialog = null;
    private EditText mName;
    private EditText mUnitMeasure;
    private EditText mPrice;

    public GigsAdapter(@NonNull Context context, List<Gig> gigs) {
        super(context, R.layout.items_list, gigs);
        this.mContext = context;
        this.gigsList = gigs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gigs_listing, parent, false);
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_unit);
        TextView id = (TextView) view.findViewById(R.id.gig_id);
        Button editBtn = (Button) view.findViewById(R.id.edit_gig_btn);
        Button deleteBtn = (Button) view.findViewById(R.id.delete_gig_btn);
        Switch sellingStatusSwitch = (Switch) view.findViewById(R.id.selling_status_switch);
        gigsDataBaseRef = FirebaseDatabase.getInstance().getReference("gigs");

        final Gig gig = gigsList.get(position);
        itemName.setText(gig.getName());
        itemPrice.setText(""+gig.getPrice());
        sellingStatusSwitch.setChecked(gig.isSelling());
        id.setText(gig.getId());
        id.setVisibility(View.INVISIBLE);
        final String gigId = gig.getId();

        sellingStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gigsDataBaseRef.child(gigId).child("selling").setValue(isChecked);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGig(gig);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGig(gig);
            }
        });
        return view;
    }

    public void deleteGig(final Gig gig) {
        DialogInterface.OnClickListener dialogClickListener = new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                        gigsDataBaseRef.child(gig.getId()).removeValue();
                        // TODO :: Add Completion listener
                        break;
                    case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void editGig(final Gig gig) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View editDialog = inflater.inflate(R.layout.edit_item_dialog, null);
        // Init view
        mName = (EditText) editDialog.findViewById(R.id.item_name);
        mUnitMeasure = (EditText) editDialog.findViewById(R.id.item_unit);
        mPrice = (EditText) editDialog.findViewById(R.id.item_price);
        final DatabaseReference gigsDatabase = FirebaseDatabase.getInstance().getReference("gigs");
        progressDialog = new ProgressDialog(getContext());
        //Populate fields
        mName.setText(gig.getName());
        mPrice.setText(""+gig.getPrice());
        mUnitMeasure.setText(gig.getUnit());
        //Handle click events
        DialogInterface.OnClickListener dialogClickListener = new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                        saveGig(gig);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                        break;
                }
            }
        };
        builder.setView(editDialog)
                .setPositiveButton("Edit", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setMessage("Edit item").show();

    }

    private void saveGig(Gig gig) { // Save to backend
        String name = mName.getText().toString().trim();
        String unit = mUnitMeasure.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();

        if(name.isEmpty()){
            mName.setError("Name is required");
            return;
        } else {
            gig.setName(name);
        }

        if(unit.isEmpty()){
            mUnitMeasure.setError("Unit is required");
            return;
        } else {
            gig.setUnit(unit);
        }

        if(priceString.isEmpty()){
            mPrice.setError("Price is required");
            return;
        } else {
            int price = Integer.parseInt(priceString);
            gig.setPrice(price);
        }
        progressDialog.setMessage("Saving item");
        progressDialog.show();
        if(gig != null) {
            gigsDataBaseRef.child(gig.getId())
                    .setValue(gig)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Item edited", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed editing item: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
