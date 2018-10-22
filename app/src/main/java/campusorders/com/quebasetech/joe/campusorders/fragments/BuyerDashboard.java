package campusorders.com.quebasetech.joe.campusorders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import campusorders.com.quebasetech.joe.campusorders.adapters.Items_Adapter;
import campusorders.com.quebasetech.joe.campusorders.R;

public class BuyerDashboard extends Fragment {
    private ListView itemsList;
    private String[] items = {
            "Smokies",
            "Smokies & eggs",
            "Eggs",
            "Eggs",
            "Apples",
            "Apples",
            "Groceries",
            "Milk",
            "Milk",
    };
    private Double[] prices = {
            22.0,
            40.0,
            40.0,
            20.0,
            25.0,
            25.0,
            75.0,
            35.0,
            35.0,
    };
    private String[] sellers = {
            "Jane Doe",
            "Awesome Dude",
            "Awesome Dude",
            "John Doe",
            "Joe Nyugoh",
            "Joe Nyugoh",
            "Fisher Maxwell",
            "Milly Bett",
            "Milly Bett",
    };


    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buyer_dashboard, container, false);
        itemsList = (ListView) view.findViewById(R.id.items_list_view);
        Items_Adapter adapter = new Items_Adapter(context, items, prices, sellers);
        itemsList.setAdapter(adapter);

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO:: Launch dialog for each
                Toast.makeText(context, "View selected: " + view.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
