package com.llf.universallibrary.widget.refresh;

import android.view.View;

/**
 * Created by llf on 2016/10/26.
 */

public interface IHeaderView {
    View getView();

    /**
     * 下拉准备刷新动作
     * @param fraction  当前下拉高度与总高度的比
     * @param maxHeadHeight
     * @param headHeight
     */
    void onPullingDown(float fraction,float maxHeadHeight,float headHeight);

    /**
     * 下拉释放过程
     * @param fraction
     * @param maxHeadHeight
     * @param headHeight
     */
    void onPullReleasing(float fraction,float maxHeadHeight,float headHeight);

    void startAnim(float maxHeadHeight,float headHeight);

    void onFinish();
}
