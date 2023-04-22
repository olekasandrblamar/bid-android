package com.bid.app.adaper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bid.app.R;
import com.bid.app.interfaces.IRecyclerViewClickListener;
import com.bid.app.model.response.CardListInfo;
import com.bid.app.model.view.GetReviewData;
import com.google.android.material.card.MaterialCardView;

import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class AttachedFilesHorizontalAdapter extends RecyclerView.Adapter<AttachedFilesHorizontalAdapter.ViewHolder> {

    private static final String TAG = AttachedFilesHorizontalAdapter.class.getSimpleName();

    private Context mContext;
    private List<Uri> cardListInfos;
    private IRecyclerViewClickListener iRecyclerViewClickListener;

    public AttachedFilesHorizontalAdapter(Context context, List<Uri> list, IRecyclerViewClickListener listener) {
        this.mContext = context;
        this.cardListInfos = list;
        this.iRecyclerViewClickListener = listener;
    }

    public void setList(ArrayList<Uri> list) {
        this.cardListInfos = list;
    }

    @NonNull
    @Override
    public AttachedFilesHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attached_file_list, parent, false);
        return new AttachedFilesHorizontalAdapter.ViewHolder(view);
    }
    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }
    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    @Override
    public void onBindViewHolder(@NonNull final AttachedFilesHorizontalAdapter.ViewHolder holder, final int position) {

        final Uri uri = cardListInfos.get(position);

        final int THUMBSIZE = 64;

        String filePath = getPath(uri);
        if(isImageFile(filePath)) {
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath), THUMBSIZE, THUMBSIZE);
            holder.imagePreview.setImageBitmap(ThumbImage);
            holder.fileName.setText("Image");
        }
        else if(isVideoFile(filePath)) {
            Bitmap ThumbImage = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);
            holder.imagePreview.setImageBitmap(ThumbImage);
            holder.fileName.setText("Video");
        }
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecyclerViewClickListener.clickOnRemove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardListInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagePreview;
        private ImageView removeBtn;
        private TextView fileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePreview = itemView.findViewById(R.id.image_preview);
            removeBtn = itemView.findViewById(R.id.remove_btn);
            fileName = itemView.findViewById(R.id.file_name);
        }
    }
}
