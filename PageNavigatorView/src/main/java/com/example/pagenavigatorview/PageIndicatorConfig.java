package com.example.pagenavigatorview;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

/**
 * ============================================================
 * PageIndicatorConfig（最终修正版）
 * ============================================================

 * 分页控件统一配置中心（Builder 模式）

 * 功能覆盖：
 * 1. 分页控制栏（上一页 / 下一页 / 页码指示器）
 * 2. 跳转弹窗（输入页码 / 确认跳转）
 * 3. Toast 提示（位置 / 文字颜色 / 文字大小 / 背景色）

 * 字段按布局文件分三大区域：

 *   ├── 区域一：page_control_bar.xml
 *   │     mainLayout / btnPrev / btnNext / btnPageIndicator
 *   │
 *   ├── 区域二：popup_jump_page.xml
 *   │     mainLayout / etJumpPage / btnConfirm


 * 支持：
 * - defaultConfig(Context)
 * - copyFrom(PageIndicatorConfig)
 * - 与 XML attrs 完全对齐

 * ============================================================
 */
public final class PageIndicatorConfig {

    /* =========================================================
     * 区域一：page_control_bar.xml
     * ========================================================= */

    @ColorInt
    public final int barBackgroundColor;
    public final int barHeightDp;
    /** MaterialCardView 描边颜色，0 表示不画 */
    public final int barStrokeColor;
    /** MaterialCardView 描边宽度(dp)，0 表示不画 */
    public final int barStrokeWidthDp;

    public final int barTextSizeSp;
    public final int barIconSizeDp;
    @ColorInt
    public final int barEnableColor;
    @ColorInt
    public final int barDisableColor;

    public final String prevText;
    @DrawableRes
    public final int prevIconRes;

    public final String nextText;
    @DrawableRes
    public final int nextIconRes;

    @ColorInt
    public final int indicatorTextColor;
    public final int indicatorTextSizeSp;

    /* =========================================================
     * 区域二：popup_jump_page.xml
     * ========================================================= */

    @ColorInt
    public final int popupBackgroundColor;

    @ColorInt
    public final int inputTextColor;
    public final int inputTextSizeSp;
    @ColorInt
    public final int inputHintColor;

    public final String confirmText;
    @ColorInt
    public final int confirmTextColor;
    public final int confirmTextSizeSp;
    @ColorInt
    public final int confirmBackgroundColor;
    /** 错误提示文字颜色，默认 #D50000 */
    public final int errorTipTextColor;
    /** 错误提示文字大小(sp)，默认 12 */
    public final int errorTipTextSizeSp;

    public final int popupPaddingDp;

    /* =========================================================
     * 私有构造
     * ========================================================= */
    private PageIndicatorConfig(Builder b) {

        this.barBackgroundColor = b.barBackgroundColor;
        this.barHeightDp = b.barHeightDp;
        this.barStrokeColor = b.barStrokeColor;
        this.barStrokeWidthDp = b.barStrokeWidthDp;

        this.barTextSizeSp = b.barTextSizeSp;
        this.barIconSizeDp = b.barIconSizeDp;
        this.barEnableColor = b.barEnableColor;
        this.barDisableColor = b.barDisableColor;

        this.prevText = b.prevText;
        this.prevIconRes = b.prevIconRes;

        this.nextText = b.nextText;
        this.nextIconRes = b.nextIconRes;

        this.indicatorTextColor = b.indicatorTextColor;
        this.indicatorTextSizeSp = b.indicatorTextSizeSp;

        this.popupBackgroundColor = b.popupBackgroundColor;
        this.inputTextColor = b.inputTextColor;
        this.inputTextSizeSp = b.inputTextSizeSp;
        this.inputHintColor = b.inputHintColor;
        this.confirmText = b.confirmText;
        this.confirmTextColor = b.confirmTextColor;
        this.confirmTextSizeSp = b.confirmTextSizeSp;
        this.confirmBackgroundColor = b.confirmBackgroundColor;
        this.errorTipTextColor = b.errorTipTextColor;
        this.errorTipTextSizeSp = b.errorTipTextSizeSp;


        this.popupPaddingDp = b.popupPaddingDp;
    }

    /* =========================================================
     * 默认配置
     * ========================================================= */
    public static PageIndicatorConfig defaultConfig(Context ctx) {
        return new Builder()

                .barBackgroundColor(0x00000000)
                .barHeightDp(40)
                .barStrokeColor(0x00000000)
                .barStrokeWidthDp(0)

                .barTextSizeSp(14)
                .barIconSizeDp(18)
                .barEnableColor(0xFFE53935)
                .barDisableColor(0xFF888888)

                .prevText("上一页")
                .prevIconRes(R.drawable.ic_left)

                .nextText("下一页")
                .nextIconRes(R.drawable.ic_right)

                .indicatorTextColor(0xFF000000)
                .indicatorTextSizeSp(14)

                .popupBackgroundColor(0xFFFFFFFF)

                .inputTextColor(0xFF000000)
                .inputTextSizeSp(14)
                .inputHintColor(0xFF888888)

                .confirmText("点击确认")
                .confirmTextColor(0xFF000000)
                .confirmTextSizeSp(16)
                .confirmBackgroundColor(0xFFFFFFFF)
                .errorTipTextColor(0xFFD50000)
                .errorTipTextSizeSp(12)

                .popupPaddingDp(10)

                .build();
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        private int barBackgroundColor = 0x00000000;
        private int barHeightDp = 40;
        private int barStrokeColor = 0x00000000;
        private int barStrokeWidthDp = 0;

        private int barTextSizeSp = 14;
        private int barIconSizeDp = 18;
        private int barEnableColor = 0xFFE53935;
        private int barDisableColor = 0xFF888888;

        private String prevText = "上一页";
        private int prevIconRes = R.drawable.ic_left;

        private String nextText = "下一页";
        private int nextIconRes = R.drawable.ic_right;

        private int indicatorTextColor = 0xFF000000;
        private int indicatorTextSizeSp = 14;

        private int popupBackgroundColor = 0xFFFFFFFF;
        private int inputTextColor = 0xFF000000;
        private int inputTextSizeSp = 14;
        private int inputHintColor = 0xFF888888;
        private String confirmText = "点击确认";
        private int confirmTextColor = 0xFF000000;
        private int confirmTextSizeSp = 16;
        private int confirmBackgroundColor = 0xFFFFFFFF;
        private int errorTipTextColor = 0xFFD50000;
        private int errorTipTextSizeSp = 12;

        private int popupPaddingDp = 10;

        /* ==================== setter ==================== */

        public Builder barBackgroundColor(@ColorInt int v) { barBackgroundColor = v; return this; }
        public Builder barHeightDp(int v) { barHeightDp = v; return this; }
        public Builder barStrokeColor(@ColorInt int v) { barStrokeColor = v; return this; }
        public Builder barStrokeWidthDp(int v) { barStrokeWidthDp = v; return this; }

        public Builder barTextSizeSp(int v) { barTextSizeSp = v; return this; }
        public Builder barIconSizeDp(int v) { barIconSizeDp = v; return this; }
        public Builder barEnableColor(@ColorInt int v) { barEnableColor = v; return this; }
        public Builder barDisableColor(@ColorInt int v) { barDisableColor = v; return this; }

        public Builder prevText(String v) { prevText = v; return this; }
        public Builder prevIconRes(@DrawableRes int v) { prevIconRes = v; return this; }

        public Builder nextText(String v) { nextText = v; return this; }
        public Builder nextIconRes(@DrawableRes int v) { nextIconRes = v; return this; }

        public Builder indicatorTextColor(@ColorInt int v) { indicatorTextColor = v; return this; }
        public Builder indicatorTextSizeSp(int v) { indicatorTextSizeSp = v; return this; }

        public Builder popupBackgroundColor(@ColorInt int v) { popupBackgroundColor = v; return this; }

        public Builder inputTextColor(@ColorInt int v) { inputTextColor = v; return this; }
        public Builder inputTextSizeSp(int v) { inputTextSizeSp = v; return this; }
        public Builder inputHintColor(@ColorInt int v) { inputHintColor = v; return this; }

        public Builder confirmText(String v) { confirmText = v; return this; }
        public Builder confirmTextColor(@ColorInt int v) { confirmTextColor = v; return this; }
        public Builder confirmTextSizeSp(int v) { confirmTextSizeSp = v; return this; }
        public Builder confirmBackgroundColor(@ColorInt int v) {confirmBackgroundColor = v;return this;}
        public Builder errorTipTextColor(int errorTipTextColor) {this.errorTipTextColor = errorTipTextColor;return this;}
        public Builder errorTipTextSizeSp(int errorTipTextSizeSp) {this.errorTipTextSizeSp = errorTipTextSizeSp;return this;}

        public Builder popupPaddingDp(int v) { popupPaddingDp = v; return this; }

        /* ==================== copyFrom ==================== */
        public Builder copyFrom(PageIndicatorConfig old) {
            if (old == null) return this;

            this.barBackgroundColor = old.barBackgroundColor;
            this.barHeightDp = old.barHeightDp;
            this.barStrokeColor = old.barStrokeColor;
            this.barStrokeWidthDp = old.barStrokeWidthDp;

            this.barTextSizeSp = old.barTextSizeSp;
            this.barIconSizeDp = old.barIconSizeDp;
            this.barEnableColor = old.barEnableColor;
            this.barDisableColor = old.barDisableColor;

            this.prevText = old.prevText;
            this.prevIconRes = old.prevIconRes;

            this.nextText = old.nextText;
            this.nextIconRes = old.nextIconRes;

            this.indicatorTextColor = old.indicatorTextColor;
            this.indicatorTextSizeSp = old.indicatorTextSizeSp;

            this.popupBackgroundColor = old.popupBackgroundColor;
            this.inputTextColor = old.inputTextColor;
            this.inputTextSizeSp = old.inputTextSizeSp;
            this.inputHintColor = old.inputHintColor;
            this.confirmText = old.confirmText;
            this.confirmTextColor = old.confirmTextColor;
            this.confirmTextSizeSp = old.confirmTextSizeSp;
            this.confirmBackgroundColor = old.confirmBackgroundColor;
            this.errorTipTextColor = old.errorTipTextColor;
            this.errorTipTextSizeSp = old.errorTipTextSizeSp;

            this.popupPaddingDp = old.popupPaddingDp;

            return this;
        }

        public PageIndicatorConfig build() {
            return new PageIndicatorConfig(this);
        }
    }
}