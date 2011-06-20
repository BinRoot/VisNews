package com.binroot.visnews;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class NewsOverlay extends ItemizedOverlay<OverlayItem> {

        private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
        private Context mContext;

        public NewsOverlay(Drawable defaultMarker) {
        	super(boundCenterBottom(defaultMarker));
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

        public NewsOverlay(Drawable defaultMarker, Context context) {
                  super(defaultMarker);
                  mContext = context;
                }
        @Override
        protected boolean onTap(int index) {
            Log.v("MAP", "index tapped: "+index);
			return true;
        }                                                                

}

        
