package com.androidcodestudio.promhsadmin.eBook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.EbookViewHolder> {
    private Context context;
    private List<EbookPojo> list;

    public EbookAdapter(Context context, List<EbookPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ebooks_item,parent,false);
        return new EbookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EbookViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String title = list.get(position).getPdfTitle();
        String url = list.get(position).getPdfUrl();
        holder.setData(title,url);

        //holder.ebookTitle.setText(list.get(position).getPdfTitle());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context,PdfViewerActivity.class);
//                intent.putExtra("pdfUrl",list.get(position).getPdfUrl());
//                context.startActivity(intent);
//            }
//        });
//        holder.ebookDownloaded.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(list.get(position).getPdfUrl()));
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EbookViewHolder extends RecyclerView.ViewHolder {

       private TextView ebookTitle;
       private ImageView ebookDownloaded;
        private PDFView pdfView;

        public EbookViewHolder(@NonNull View itemView) {
            super(itemView);

            ebookTitle = itemView.findViewById(R.id.ebook_title);
            ebookDownloaded = itemView.findViewById(R.id.ebook_downloaded);
            pdfView = itemView.findViewById(R.id.pdfView);


        }

        private void setData(String title,String url) {
            ebookTitle.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,PdfViewerActivity.class);
                    intent.putExtra("pdfUrl",url);
                    context.startActivity(intent);
                }
            });
            ebookDownloaded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
            new PdfDownload().execute(url);

        }

        private class PdfDownload extends AsyncTask<String,Void, InputStream> {

            @Override
            protected InputStream doInBackground(String... strings) {
                InputStream inputStream = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    if (httpURLConnection.getResponseCode() == 200){
                        inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return inputStream;
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                pdfView.fromStream(inputStream).load();
            }
        }
    }
}
