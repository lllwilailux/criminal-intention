package com.augmentis.ayp.crimin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import java.io.File;

/**
 * Created by Wilailux on 8/4/2016.
 */
public class DialogPicFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private File photoFile;
    private ImageView imageView;

    public static DialogPicFragment newInstance (File file) {
        DialogPicFragment dp = new DialogPicFragment();
        Bundle args = new Bundle();
        args.putSerializable("PIC" , file);
        dp.setArguments(args);
        return dp;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        photoFile = (File) getArguments().getSerializable("PIC");
        //3.
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_pic, null);

        imageView = (ImageView) v.findViewById(R.id.imageView);

        Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
        imageView.setImageBitmap(bitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(R.string.image_title);
        builder.setPositiveButton(android.R.string.ok, this);



        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
    }
}
