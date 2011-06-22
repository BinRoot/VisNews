package com.binroot.visnews;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class NewsOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private NewsBlock newsBlock = null;

	public NewsOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}
	public void setNews(NewsBlock newsBlock) {
		this.newsBlock = newsBlock;
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
		//Log.v("MAP", "index tapped: "+index);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(newsBlock.getPubDate());
		builder.setMessage(newsBlock.getTitle());
		builder.setCancelable(false);
		builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton("Read More", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.d("LINK",newsBlock.getLink());
				String urlLink = newsBlock.getLink();
				int urlStart = urlLink.indexOf("http://", 10);
				urlLink = urlLink.substring(urlStart);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlLink));
				mContext.startActivity(browserIntent);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}       
	

}


