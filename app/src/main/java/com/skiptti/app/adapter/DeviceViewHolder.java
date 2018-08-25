package com.skiptti.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skiptti.app.R;

/**
 * Created by emmanuel on 24/02/2017.
 *
 */

public class DeviceViewHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView name, description;
    private View upperTimeLinker, lowerTimeLinker;

    public DeviceViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
        name = (TextView) itemView.findViewById(R.id.name);
        description = (TextView) itemView.findViewById(R.id.description);

    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public View getUpperTimeLinker() {
        return upperTimeLinker;
    }

    public View getLowerTimeLinker() {
        return lowerTimeLinker;
    }
}
