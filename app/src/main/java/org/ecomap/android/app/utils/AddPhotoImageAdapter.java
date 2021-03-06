package org.ecomap.android.app.utils;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.ecomap.android.app.R;
import org.ecomap.android.app.data.model.ProblemPhotoEntry;

import java.util.List;

public class AddPhotoImageAdapter extends BaseAdapter {

    private List<ProblemPhotoEntry> mImagesURLArray;
    private final DisplayImageOptions options;
    private int totalHeight;
    public AddPhotoImageAdapter(List<ProblemPhotoEntry> titledPhotos) {

        this.mImagesURLArray = titledPhotos;

        this.options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    /**
     * Update adapter data set
     */
    public void updateDataSet(List<ProblemPhotoEntry> data) {
        mImagesURLArray = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImagesURLArray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_problem_photo_item, parent, false);
            holder = new ViewHolder();

            assert view != null;

            holder.imageView = (ImageView) view.findViewById(R.id.iv_photo);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
            holder.closeButton = (ImageView) view.findViewById(R.id.iv_error);
            holder.comment = (EditText) view.findViewById(R.id.add_photo_edit_text);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Clearing array of photos and updating adapter
        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagesURLArray.remove(position);   // changed selectedPhotos to mImagesURLArray
                updateDataSet(mImagesURLArray);     // changed selectedPhotos to mImagesURLArray
            }
        });

        // Remove previous TextWatcher if any
        if (holder.txtWatcher != null) {
            holder.comment.removeTextChangedListener(holder.txtWatcher);
        }

        holder.txtWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (mImagesURLArray != null && mImagesURLArray.size() > 0) {
                    ProblemPhotoEntry photoEntry = mImagesURLArray.get(position);
                    photoEntry.setTitle(s.toString());
                }

            }
        };

        holder.comment.addTextChangedListener(holder.txtWatcher);

        if (mImagesURLArray != null && mImagesURLArray.size() > 0) {

            final ProblemPhotoEntry photoEntry = mImagesURLArray.get(position);
            final String imgURL = "file://"+ photoEntry.getImgURL();


            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(
                    imgURL,
                    holder.imageView,
                    options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.progressBar.setProgress(0);
                            holder.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    },
                    new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            holder.progressBar.setProgress(Math.round(100.0f * current / total));
                        }
                    });
        }
        //We're need to clear each edittext after changing photos
        //holder.comment.setText("");

        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        ImageView closeButton;
        ProgressBar progressBar;
        EditText comment;
        TextWatcher txtWatcher;
    }




}
