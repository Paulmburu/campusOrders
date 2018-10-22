package campusorders.com.quebasetech.joe.campusorders.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import campusorders.com.quebasetech.joe.campusorders.R;

public class Items_Adapter extends ArrayAdapter<String> {

    private  final Context context;
    private final String[] items;
    private final Double[] prices;
    private final String[] sellers;

    public Items_Adapter(@NonNull Context context, String[] items, Double[] prices, String[] sellers) {
        super(context, R.layout.items_list, items);
        this.context = context;
        this.items = items;
        this.prices = prices;
        this.sellers = sellers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.items_list, parent, false);
        final TextView itemName = (TextView) rowView.findViewById(R.id.item_name);
        TextView itemPrice = (TextView) rowView.findViewById(R.id.item_unit);
        TextView itemSeller = (TextView) rowView.findViewById(R.id.seller_value);
        itemName.setText(items[position]);
        itemPrice.setText(prices[position].toString());
        itemSeller.setText(sellers[position]);
        Button order = (Button) rowView.findViewById(R.id.orderBtn);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You selected: " + itemName.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return rowView;
    }
}
