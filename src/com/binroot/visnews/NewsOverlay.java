package com.binroot.visnews;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class NewsOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private ArrayList<NewsBlock> newsBlocks = new ArrayList<NewsBlock>();

	public NewsOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}
	public void setNewsBlocks(ArrayList<NewsBlock> newsBlocks) {
		this.newsBlocks = newsBlocks;
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	public void removeOverLay(OverlayItem overlay) {
		mOverlays.remove(mOverlays.indexOf(overlay));
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		Log.v("MAP", "index tapped: "+index);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(newsBlocks.get(index).getTitle())
		.setCancelable(false)
		.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}                                                                

}


