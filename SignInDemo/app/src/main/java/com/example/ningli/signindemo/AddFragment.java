package com.example.ningli.signindemo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ningli.signindemo.database.DBHelper;

public class AddFragment extends Fragment {

    public static final String KEY_PAGE = "page";

    private static String USER_ID;

    @NonNull
    public static AddFragment newInstance(int page, String NAME) {
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
        args.putString("USER_ID", NAME);
        USER_ID = NAME;

        AddFragment addFragment = new AddFragment();
        addFragment.setArguments(args);
        return addFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_add, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int page = getArguments().getInt(KEY_PAGE);

        View add = view.findViewById(R.id.AddButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText itemName = (EditText) getView().findViewById(R.id.AddItemName);
                String name = itemName.getText().toString();

                EditText itemNumber = (EditText) getView().findViewById(R.id.AddItemNumber);
                String num = itemNumber.getText().toString();


                final SQLiteOpenHelper db = DBHelper.getInstance(getActivity());
                SQLiteDatabase database = db.getWritableDatabase();

                if (name.isEmpty() || num.isEmpty())
                    Toast.makeText(getActivity(), "Invalid!", Toast.LENGTH_LONG).show();
                else {
                    ContentValues values = new ContentValues();
                    values.put("item", name);
                    values.put("num", Integer.valueOf(num));
                    values.put("state", 0);

                    long id = database.insert(USER_ID, null, values);
                    itemName.setText("");
                    itemNumber.setText("");
                    Toast.makeText(getActivity(), "Adding item to List ... " + " ( " + name + ", " + num + " ) " + "on " + id, Toast.LENGTH_LONG).show();
                    ((SuccessActivity) getContext()).pagerAdapter.notifyDataSetChanged();

                }
            }
        });

    }
}
