package com.example.w28l30.foursquareexample.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.w28l30.foursquareexample.Activity.MainActivity;
import com.example.w28l30.foursquareexample.AppController;
import com.example.w28l30.foursquareexample.Model.Dinner;
import com.example.w28l30.foursquareexample.Model.JSONUtl;
import com.example.w28l30.foursquareexample.R;
import com.example.w28l30.foursquareexample.Service.SocketService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class HomeFragment extends Fragment {


    private static final String TAG = "HomeFragment";
    private SwipeMenuListView listView;
    private Button historybtn;
    private boolean ishistory;
    private JSONArray historyarray = new JSONArray();
    private SwipeMenuCreator creator;

    private TimeAxisAdapter mTimeAxisAdapter;

    // added by jxw, broadcastreceiver
    private BroadcastReceiver mBroadcastReceiver;

    private MainActivity activity;
    private SocketService mservice;
    private SharedPreferences mSharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "home fragment onCreate");
        activity = (MainActivity)getActivity();
        //button at bottom


        mSharedPreferences = activity.getSharedpreferences();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.hasExtra("bundle")) {
                    Bundle bundle = intent.getBundleExtra("bundle");
                    if(bundle.containsKey("Result")) {
                        try {
                            String toasttxt = "";
                            if(isJSONValid(bundle.getString("Result")))
                            {
                                JSONObject jsonObject = new JSONObject(bundle.getString("Result"));
                                int result = jsonObject.getInt("result");
                                switch (result)
                                {
                                    case 0:
                                    {
                                        toasttxt = getString(R.string.cancel_fail);
                                        break;
                                    }
                                    case 1:
                                    {
                                        toasttxt = getString(R.string.cancel_success);
                                        break;
                                    }
                                }
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), toasttxt, Toast.LENGTH_LONG);
                                toast.show();
                                jsonObject = JSONUtl.RequestUpcomingJson(activity.getUsername());
                                mservice.sendMSG(jsonObject.toString());
                            }
                            else
                            {
                                JSONArray array = new JSONArray(bundle.getString("Result"));
                                historyarray = array;
                                mTimeAxisAdapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        };
        activity.registerReceiver(mBroadcastReceiver, intentFilter);

    }

    public void pushlist() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Time",".....");
        jsonObject.put("Name",",,,,");
        jsonObject.put("Property",0);
        jsonObject.put("Member", new JSONArray());
        historyarray.put(jsonObject);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onviewcreated");
        activity.getToolbar().setTitle(getString(R.string.home_actionbar_upcomning));
        initView();
        historybtn = (Button) activity.findViewById(R.id.historyorupcoming);
        historybtn.setText(getString(R.string.history));
        ishistory = false;
        historybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while ((mservice = activity.getService()) == null || !activity.getmBound()) {
                }
                try {
                    if (!ishistory) {
                        JSONObject jsonObject = JSONUtl.RequestHistoryJson(activity.getUsername());
                        mservice.sendMSG(jsonObject.toString());
                        ishistory = true;
                        historybtn.setText(getString(R.string.upcoming));
                        activity.getToolbar().setTitle(getString(R.string.home_actionbar_history));
                    } else {
                        JSONObject jsonObject = JSONUtl.RequestUpcomingJson(activity.getUsername());
                        mservice.sendMSG(jsonObject.toString());
                        ishistory = false;
                        historybtn.setText(getString(R.string.history));
                        activity.getToolbar().setTitle(getString(R.string.home_actionbar_upcomning));
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //the first time, call future events
        Thread startrefresh = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //try untill the mservice is bounded
                    while((mservice=activity.getService())==null||!activity.getmBound())
                    {
                    }
                        mservice.sendMSG(JSONUtl.RequestUpcomingJson(activity.getUsername()).toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        startrefresh.start();
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                // create "Add" item
                SwipeMenuItem addItem = new SwipeMenuItem(
                        getContext());
                // set item background
                addItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                addItem.setWidth(dp2px(90));
                addItem.setTitleColor(Color.WHITE);
                addItem.setTitleSize(18);
                addItem.setTitle("Cancel");
                // add to menu
                swipeMenu.addMenuItem(addItem);
            }
        };

        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                try {
                    final JSONObject item = historyarray.getJSONObject(position);
                    switch (index) {
                        case 0: {
                            if(!ishistory) {
                                new AlertDialog.Builder(activity).setTitle("Confirm Cancel").setMessage("Are you sure" +
                                        " you want to cancel this appointment?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (mservice != null && activity.getmBound()) {
                                            try {
                                                JSONObject jsonObject = JSONUtl.CancelJson(activity.getUsername(),
                                                        item.getInt("Appoint_num"), item.getInt("Property"));
                                                mservice.sendMSG(jsonObject.toString());
                                            } catch (JSONException | IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                }).setNegativeButton("No", null).show();

                            }
                            else
                            {
                                Toast toast = Toast.makeText(activity.getApplicationContext(),"Sorry, you cannot " +
                                        "cancel history event.",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        // add

                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
        if(mSharedPreferences.contains("Newmessage"))
        {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.remove("Newmessage");
            editor.commit();
            activity.setMsgnumber(0);
            if(activity.getDrawerFragment()!=null&&activity.getDrawerFragment().getAdapter()!=null) {
                activity.getDrawerFragment().getAdapter().clearCounter();
            }
        }
    }

    //judge the string is a JSONObject or a JSONArray
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void initView() {

        listView = (SwipeMenuListView) getActivity().findViewById(R.id.Timeline_listView);
        listView.setDividerHeight(0);
        mTimeAxisAdapter = new TimeAxisAdapter(getContext());
        listView.setAdapter(mTimeAxisAdapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class TimeAxisAdapter extends BaseAdapter {


        private Context context;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        private  class ViewHolder {
            private TextView tvProperty;
            private TextView tvRestaurant;
            private TextView tvPeople;
            private TextView tvTime;
            private TextView tvAddress;
            private NetworkImageView tvThumbnail;
        }

        public TimeAxisAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return historyarray.length();
        }

        @Override
        public Object getItem(int position) {
            JSONObject jsonObject = null;
            try {
                jsonObject =  historyarray.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  jsonObject;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {
                JSONObject jsonObject = historyarray.getJSONObject(position);
                ViewHolder viewHolder = null;

                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.timeline_item, null);
                    viewHolder.tvPeople = (TextView) convertView
                            .findViewById(R.id.history_people);
                    viewHolder.tvProperty = (TextView)convertView.findViewById(R.id.history_property);
                    viewHolder.tvRestaurant = (TextView)convertView.findViewById(R.id.history_restaurant);
                    viewHolder.tvAddress = (TextView)convertView.findViewById(R.id.history_address);
                    viewHolder.tvTime = (TextView)convertView.findViewById(R.id.tv_time);
                    viewHolder.tvThumbnail = (NetworkImageView)convertView.findViewById(R.id.history_thumbnail);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                if (imageLoader == null) {
                    imageLoader = AppController.getInstance().getImageLoader();
                }
                //set time text to two lines
                viewHolder.tvTime.setText(jsonObject.getString("Time").replaceAll(" ", "\n"));
                viewHolder.tvProperty.setText(TranProperty(jsonObject.getInt("Property")));
                viewHolder.tvRestaurant.setText(jsonObject.getString("Name"));
                viewHolder.tvAddress.setText(jsonObject.getString("Address"));
                viewHolder.tvPeople.setText("With: " + TranPeople(jsonObject.getJSONArray("Member")));
                if(jsonObject.has("url")) {
                    viewHolder.tvThumbnail.setImageUrl(jsonObject.getString("url"), imageLoader);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }
    //translate property value to string
    private String TranProperty(int property)
    {
        String result = "";
        switch (property)
        {
            case 0:
            {
                result = "Creator";
                break;
            }
            case 1:
            {
                result = "Participant";
                break;
            }
            default:
                break;
        }
        return result;
    }

    //translate people from JSONARRAY to string
    private String TranPeople(JSONArray peoplearray) throws JSONException {
        String result = "";
        for(int i = 0;i<peoplearray.length();i++)
        {
            if(peoplearray.getString(i).equals(activity.getUsername()))
            {continue;}
            result+= peoplearray.getString(i);
            if(i<peoplearray.length()-1)
            {result+=", ";}
            else
            {
                result+=".";
            }
        }
        return result;
    }

}
