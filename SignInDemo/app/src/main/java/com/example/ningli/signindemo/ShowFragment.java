package com.example.ningli.signindemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ningli.signindemo.database.DBHelper;

public class ShowFragment extends Fragment {

    public static final String KEY_PAGE = "page";


    private static String USER_ID;

    @NonNull
    public static ShowFragment newInstance(int page, String NAME) {
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
        args.putString("USER_ID", NAME);
        USER_ID = NAME;

        ShowFragment showFragment = new ShowFragment();
        showFragment.setArguments(args);
        return showFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_show, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int page = getArguments().getInt(KEY_PAGE);

        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.LinearLayout);

        final SQLiteOpenHelper db = DBHelper.getInstance(getActivity());
        final SQLiteDatabase database = db.getWritableDatabase();

        Cursor cursor = database.query(USER_ID, new String[]{"Id","item","num", "state", "image"},"state = ?", new String[]{"0"}, null, null,null);

        while(cursor.moveToNext()){
            final String itemid = cursor.getString(cursor.getColumnIndex("Id"));
            String itemname = cursor.getString(cursor.getColumnIndex("item"));
            String itemnum = cursor.getString(cursor.getColumnIndex("num"));
            String itemimage = cursor.getString(cursor.getColumnIndex("image"));


            final View itemview = ((SuccessActivity) getContext()).getLayoutInflater().inflate(R.layout.item_show, null);
            ((TextView) itemview.findViewById(R.id.ShowItemName)).setText(itemname);
            ((TextView) itemview.findViewById(R.id.ShowItemNum)).setText(itemnum);
            ((ImageView) itemview.findViewById(R.id.ShowItemimage)).setImageURI(Uri.parse(itemimage));

            Button button = (Button) itemview.findViewById(R.id.DoneButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues values = new ContentValues();
                    values.put("state","1");
                    database.update(USER_ID,values,"Id=?",new String[]{itemid});
                    ((SuccessActivity) getContext()).pagerAdapter.notifyDataSetChanged();

                }
            });
            linearLayout.addView(itemview);
        }
    }
}
