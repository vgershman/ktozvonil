package com.vgershman.whocall.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vkosarev
 * Date: 27.02.13
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public class DialogsUtil {
    public static void showDropdownDialog(Context context, String title, List<String> options, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        String[] items = options.toArray(new String[0]);
        builder.setItems(items, listener);
        builder.show();
    }

    public static void showInputDialog(Context context, String title, int inputType, final OnInputChangedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        final EditText input = new EditText(context);
        input.setInputType(inputType);
        builder.setView(input);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onInputChanged(input.getText().toString());
            }
        });
        builder.show();
    }
}
