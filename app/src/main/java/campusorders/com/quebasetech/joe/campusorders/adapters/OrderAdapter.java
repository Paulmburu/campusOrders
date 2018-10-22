package campusorders.com.quebasetech.joe.campusorders.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import campusorders.com.quebasetech.joe.campusorders.R;


public class OrderAdapter extends ArrayAdapter<String> {
    private  final Context context;
    private final String[] orders;
    private final String[] buyers;
    private final int[] qty;
    private final double[] prices;

    public OrderAdapter(@NonNull Context context, String[] orders, String[] buyers, int[] qty, double[] prices) {
        super(context, R.layout.orders_list, orders);
        this.context = context;
        this.orders = orders;
        this.buyers = buyers;
        this.qty = qty;
        this.prices = prices;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.orders_list,  parent, false);
        // Populate view with content
        TextView location = (TextView) view.findViewById(R.id.item_name);
        TextView qtyLabel = (TextView) view.findViewById(R.id.item_unit);
        TextView price = (TextView) view.findViewById(R.id.order_value);
        TextView buyer = (TextView) view.findViewById(R.id.buyer_name);

        location.setText(orders[position]);
        qtyLabel.setText(""+qty[position]);
        price.setText(""+prices[position]);
        buyer.setText(buyers[position]);

        return view;
    }
}
