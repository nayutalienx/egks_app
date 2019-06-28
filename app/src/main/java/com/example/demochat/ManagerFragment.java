package com.example.demochat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import io.paperdb.Paper;

public class ManagerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_manager,container,false);
        ListView listView = view.findViewById(R.id.listView);



                Paper.init(view.getContext());
                if(Paper.exist("cardList")) {
            HashMap<Integer,EgksCard> listCard = Paper.book().read("cardList");

            ArrayList<EgksCard> tempList = new ArrayList<EgksCard>();
            Iterator iterator = listCard.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry pair = (Map.Entry) iterator.next();
                EgksCard tempCard = (EgksCard) pair.getValue();
                tempList.add(tempCard);
            }


            CardListAdapter adapter = new CardListAdapter(view.getContext(), R.layout.list_item_layout, tempList);
            listView.setAdapter(adapter);

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView) view.findViewById(R.id.cardNumberItem)).getText().toString();

                ManagerDialogEdit managerDialogEdit = new ManagerDialogEdit();
                Bundle args = new Bundle();
                args.putString("number",selected);
                args.putString("name",((TextView) view.findViewById(R.id.cardNameHolder)).getText().toString());
                args.putInt("id",Integer.parseInt(((TextView) view.findViewById(R.id.cardId)).getText().toString()));
                managerDialogEdit.setArguments(args);
                managerDialogEdit.show(getFragmentManager(),"Editing dialog");




            }
        });


        return view;
    }
}
