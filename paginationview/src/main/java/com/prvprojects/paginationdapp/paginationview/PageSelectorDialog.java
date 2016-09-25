package com.prvprojects.paginationdapp.paginationview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by reena on 9/25/2016.
 */
public class PageSelectorDialog extends DialogFragment {

    private int minPage;
    private int maxPage;
    private int selectedPage;

    private static final String ARG_PAGENUMBER_MIN = "pmin";
    private static final String ARG_PAGENUMBER_MAX = "pmax";
    private static final String ARG_PAGENUMBER_SELECTED = "psel";

    public interface PageNumberSelectedListener{
        void onPageNumberSelected(int pageNumberSelected);
    }

    PageNumberSelectedListener pageNumberSelectedListener = null;

    public void setPageNumberSelectedListener(PageNumberSelectedListener pageNumberSelectedListener1){
        pageNumberSelectedListener = pageNumberSelectedListener1;
    }

    public PageSelectorDialog(){}

    public static PageSelectorDialog newInstance(int minPage1, int maxPage1, int selectedPage1) {
        PageSelectorDialog fragment = new PageSelectorDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGENUMBER_MIN, minPage1);
        args.putInt(ARG_PAGENUMBER_MAX, maxPage1);
        args.putInt(ARG_PAGENUMBER_SELECTED, selectedPage1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments() != null) {
            maxPage = getArguments().getInt(ARG_PAGENUMBER_MAX);
            minPage = getArguments().getInt(ARG_PAGENUMBER_MIN);
            selectedPage = getArguments().getInt(ARG_PAGENUMBER_SELECTED);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View rootView = inflater.inflate(R.layout.dialog_gotopage, null);

        final NumberPicker npPageSelector = (NumberPicker) rootView.findViewById(R.id.dialogue_gotopage_np_pagenumber);
        npPageSelector.setMinValue(minPage);
        npPageSelector.setMaxValue(maxPage);
        npPageSelector.setValue(selectedPage);

        builder.setView(rootView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(pageNumberSelectedListener!=null) {

                            final int pageSelected = npPageSelector.getValue();
                            pageNumberSelectedListener.onPageNumberSelected(pageSelected);
                            dismiss();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setTitle("Go to page");

        return builder.create();
    }

}
