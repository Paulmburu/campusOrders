/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 19/ 10/ 2018.
 * MIT License
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package campusorders.com.quebasetech.joe.campusorders.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import campusorders.com.quebasetech.joe.campusorders.R;
import campusorders.com.quebasetech.joe.campusorders.model.Gig;

public class EditGigDialog extends DialogFragment {
    private EditText mName;
    private EditText mUnitMeasure;
    private EditText mPrice;
    private Button mEditButton, mCancelButton;
    private DatabaseReference gigsDatabase;
    private ProgressDialog progressDialog;
    private Context mContext;

    public void setContext(Context context) {
        this.mContext = context;
    }

    public static EditGigDialog newInstance() {
        return new EditGigDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View editDialog = inflater.inflate(R.layout.edit_item_dialog, null);

        // Init view
        mName = (EditText) editDialog.findViewById(R.id.item_name);
        mUnitMeasure = (EditText) editDialog.findViewById(R.id.item_unit);
        mPrice = (EditText) editDialog.findViewById(R.id.item_price);
        gigsDatabase = FirebaseDatabase.getInstance().getReference("gigs");
        /*mCancelButton = (Button) editDialog.findViewById(R.id.edit_item_cancel_btn);
        mEditButton = (Button) editDialog.findViewById(R.id.edit_item_btn);
        progressDialog = new ProgressDialog(getContext());

        // Handle button clicks
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:: Save item to database
                progressDialog.setMessage("Saving item");
                progressDialog.show();
                Gig gig = editGig(new Gig());
                if(gig != null) {
                    gigsDatabase.child(gig.getId())
                            .setValue(gig)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Item edited", Toast.LENGTH_SHORT).show();
                                    dismiss();
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
        });
*/
        builder.setView(editDialog)
                .setMessage("Edit item");

        return builder.create();
    }

    private Gig editGig(Gig gig){
        String name = mName.getText().toString().trim();
        String unit = mUnitMeasure.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();

        if(name.isEmpty()){
            mName.setError("Name is required");
            return null;
        } else {
            gig.setName(name);
        }

        if(unit.isEmpty()){
            mUnitMeasure.setError("Unit is required");
            return null;
        } else {
            gig.setUnit(unit);
        }

        if(priceString.isEmpty()){
            mPrice.setError("Price is required");
            return null;
        } else {
            int price = Integer.parseInt(priceString);
            gig.setPrice(price);
        }
        return gig;
    }
}

