package com.example.demochat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import io.paperdb.Paper;

public class ManagerDialogEdit extends AppCompatDialogFragment {
    private EditText editName;
    private EditText editNumber;
    private ManagerDialogEditListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_card_dialog,null);

        editName = view.findViewById(R.id.modal_name);
        editNumber = view.findViewById(R.id.modal_number);

        editNumber.setText(getArguments().getString("number"));
        editName.setText(getArguments().getString("name"));

        String title = "Изменение карты";
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF00629F"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);
        ssBuilder.setSpan(foregroundColorSpan,0,title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        builder.setView(view).setTitle(ssBuilder).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                String number = editNumber.getText().toString();
                listener.editTexts(name,number, getArguments().getInt("id"),"edit");
            }
        }).setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.editTexts("","", getArguments().getInt("id"),"delete");
            }
        });



        return builder.create();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ManagerDialogEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ManagerDialogEdit");
        }
    }

    public interface ManagerDialogEditListener{
        void editTexts(String name, String number, int id, String command);
    }

}
