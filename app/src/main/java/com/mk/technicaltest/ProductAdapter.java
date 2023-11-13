package com.mk.technicaltest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    List<ProductData> list;
    Context context;


    public ProductAdapter(List<ProductData> list, Context context, DrawController.ClickListener listener) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View photoView = inflater.inflate(R.layout.list_item, parent, false);

        return new ProductViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder viewHolder, final int position) {
        final int index = viewHolder.getAdapterPosition();
        //DownloadImageFromPath(list.get(position).image,viewHolder.productImage);
        new DownloadImage(viewHolder.productImage).execute(list.get(position).image);
        viewHolder.productName.setText(list.get(position).name);
        viewHolder.productPrice.setText(list.get(position).price+" AED ");
        viewHolder.productDesc.setText(list.get(position).desc);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class DownloadImage extends AsyncTask<String, Void,Void> {

        ImageView iv;

        public DownloadImage(ImageView iv){
            this.iv=iv;
        }
        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bm = null;
            try {
                bm =    BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create Path to save Image
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+ "/TechTest");

            if(!path.exists()) {
                path.mkdirs();
            }

            File imageFile = new File(path, String.valueOf(System.currentTimeMillis())+".png"); // Imagename.png
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try{
                assert bm != null;
                bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                assert out != null;
                out.flush();
                out.close();
                iv.setImageBitmap(bm);
                Log.d("TAG", "DownloadImageFromPath: iv set ok");

            } catch(Exception ignored) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
