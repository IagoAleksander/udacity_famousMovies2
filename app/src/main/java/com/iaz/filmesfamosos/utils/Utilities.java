package com.iaz.filmesfamosos.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.iaz.filmesfamosos.Constants;

public class Utilities {

    public static class BottomOffsetDecoration extends RecyclerView.ItemDecoration {
        final private int mBottomOffset;

        public BottomOffsetDecoration(int bottomOffset) {
            mBottomOffset = bottomOffset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            if (dataSize > 0 && position == dataSize - 2 || position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }

        }
    }

    public static boolean needsUpdatePopular(Context context) {
        return System.currentTimeMillis() - Prefs.getLastUpdatedTimePopular(context) > Constants.updateTime;
    }

    public static boolean needsUpdateUpcoming(Context context) {
        return System.currentTimeMillis() - Prefs.getLastUpdatedTimeUpcoming(context) > Constants.updateTime;
    }

    public static boolean needsUpdateTopRated(Context context) {
        return System.currentTimeMillis() - Prefs.getLastUpdatedTimeTopRated(context) > Constants.updateTime;
    }

}
