package com.example.ningli.signindemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ningli.signindemo.database.DBHelper;

public class DoneFragment extends Fragment {

    public static final String KEY_PAGE = "page";

    private static String USER_NAME;

    @NonNull
    public static DoneFragment newInstance(int page, String NAME) {
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
        args.putString("USER_NAME", NAME);
        USER_NAME = NAME;

        DoneFragment doneFragment = new DoneFragment();
        doneFragment.setArguments(args);
        return doneFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_done, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int page = getArguments().getInt(KEY_PAGE);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.LinearLayout);

        final SQLiteOpenHelper db = DBHelper.getInstance(getActivity());
        SQLiteDatabase database = db.getWritableDatabase();

        Cursor cursor = database.query(USER_NAME, new String[]{"Id","item","num", "state"},"state = ?", new String[]{"1"}, null, null,null);

        while(cursor.moveToNext()){
            String itemname = cursor.getString(cursor.getColumnIndex("item"));
            String itemnum = cursor.getString(cursor.getColumnIndex("num"));


            View itemview = ((SuccessActivity) getContext()).getLayoutInflater().inflate(R.layout.item_done, null);
            ((TextView) itemview.findViewById(R.id.ShowItemName)).setText(itemname);
            ((TextView) itemview.findViewById(R.id.ShowItemNum)).setText(itemnum);
            linearLayout.addView(itemview);
        }
    }
}
