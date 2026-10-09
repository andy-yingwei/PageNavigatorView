package com.example.pagenavigatorview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Objects;

/**
 * ============================================================
 * PageNavigatorView（v2.3）
 * ============================================================
 * 继承 LinearLayout，彻底消除 MaterialCardView outline 问题
 * 描边通过 GradientDrawable 实现，attrs / Builder 灵活控制
 * ============================================================
 */
public class PageNavigatorView extends LinearLayout {

    /* =========================================================
     * 子 View
     * ========================================================= */
    private AppCompatButton btnPrev;
    private AppCompatButton btnNext;
    private AppCompatButton btnPageIndicator;

    /* =========================================================
     * 分页状态
     * ========================================================= */
    private int currentPage = 1;
    private int minPage = 1;
    private int maxPage = 1;

    /* =========================================================
     * 配置
     * ========================================================= */
    private PageIndicatorConfig config;

    /* =========================================================
     * 回调
     * ========================================================= */
    @Nullable
    private PageEventDispatcher dispatcher;

    /* =========================================================
     * 跳转弹窗
     * ========================================================= */
    private PopupWindow jumpPopup;

    /* =========================================================
     * 日志
     * ========================================================= */
    private String logTag = "PageNavigatorView";
    private boolean logEnabled = false;

    /* =========================================================
     * 构造函数
     * ========================================================= */

    public PageNavigatorView(@NonNull Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        init(context, null);
    }

    public PageNavigatorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        init(context, attrs);
    }

    public PageNavigatorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        init(context, attrs);
    }

    /* =========================================================
     * 初始化入口
     * ========================================================= */

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {

        LayoutInflater.from(context).inflate(
                R.layout.page_control_bar, this, true
        );

        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnPageIndicator = findViewById(R.id.btnPageIndicator);

        PageIndicatorConfig.Builder builder =
                new PageIndicatorConfig.Builder()
                        .copyFrom(PageIndicatorConfig.defaultConfig(context));

        if (this.config != null) {
            builder.copyFrom(this.config);
        }

        if (attrs != null) {
            @SuppressLint("CustomViewStyleable")
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageIndicatorView);
            applyAttrsToBuilder(context, a, builder);
            a.recycle();
        }

        this.config = builder.build();

        initView();
        bindEvent();
        refreshUI();
    }

    /* =========================================================
     * 属性解析
     * ========================================================= */

    private void applyAttrsToBuilder(Context context, TypedArray a, PageIndicatorConfig.Builder builder) {

        if (a.hasValue(R.styleable.PageIndicatorView_pic_barBackgroundColor)) {
            builder.barBackgroundColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_barBackgroundColor, 0x00000000)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_barHeightDp)) {
            builder.barHeightDp(
                    a.getInt(R.styleable.PageIndicatorView_pic_barHeightDp, 40)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_barStrokeColor)) {
            builder.barStrokeColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_barStrokeColor, 0x00000000)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_barStrokeWidthDp)) {
            builder.barStrokeWidthDp(
                    a.getInt(R.styleable.PageIndicatorView_pic_barStrokeWidthDp, 0)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_barTextSizeSp)) {
            builder.barTextSizeSp(
                    a.getInt(R.styleable.PageIndicatorView_pic_barTextSizeSp, 14)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_barIconSizeDp)) {
            builder.barIconSizeDp(
                    a.getInt(R.styleable.PageIndicatorView_pic_barIconSizeDp, 18)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_barEnableColor)) {
            builder.barEnableColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_barEnableColor, 0xFFE53935)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_barDisableColor)) {
            builder.barDisableColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_barDisableColor, 0xFF888888)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_prevText)) {
            builder.prevText(a.getString(R.styleable.PageIndicatorView_pic_prevText));
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_prevIconRes)) {
            builder.prevIconRes(
                    a.getResourceId(R.styleable.PageIndicatorView_pic_prevIconRes, R.drawable.ic_left)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_nextText)) {
            builder.nextText(a.getString(R.styleable.PageIndicatorView_pic_nextText));
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_nextIconRes)) {
            builder.nextIconRes(
                    a.getResourceId(R.styleable.PageIndicatorView_pic_nextIconRes, R.drawable.ic_right)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_indicatorTextColor)) {
            builder.indicatorTextColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_indicatorTextColor, 0xFF000000)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_indicatorTextSizeSp)) {
            builder.indicatorTextSizeSp(
                    a.getInt(R.styleable.PageIndicatorView_pic_indicatorTextSizeSp, 14)
            );
        }

        // popup
        if (a.hasValue(R.styleable.PageIndicatorView_pic_popupBackgroundColor)) {
            builder.popupBackgroundColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_popupBackgroundColor, 0xFFFFFFFF)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_inputTextColor)) {
            builder.inputTextColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_inputTextColor, 0xFF000000)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_inputTextSizeSp)) {
            builder.inputTextSizeSp(
                    a.getInt(R.styleable.PageIndicatorView_pic_inputTextSizeSp, 14)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_inputHintColor)) {
            builder.inputHintColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_inputHintColor, 0xFF888888)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_confirmText)) {
            builder.confirmText(a.getString(R.styleable.PageIndicatorView_pic_confirmText));
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_confirmTextColor)) {
            builder.confirmTextColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_confirmTextColor, 0xFF000000)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_confirmTextSizeSp)) {
            builder.confirmTextSizeSp(
                    a.getInt(R.styleable.PageIndicatorView_pic_confirmTextSizeSp, 16)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_confirmBackgroundColor)) {
            builder.confirmBackgroundColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_confirmBackgroundColor, 0xFFFFFFFF)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_errorTipTextColor)) {
            builder.errorTipTextColor(
                    a.getColor(R.styleable.PageIndicatorView_pic_errorTipTextColor, 0xFFD50000)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_errorTipTextSizeSp)) {
            builder.errorTipTextSizeSp(
                    a.getInt(R.styleable.PageIndicatorView_pic_errorTipTextSizeSp, 12)
            );
        }
        if (a.hasValue(R.styleable.PageIndicatorView_pic_popupPaddingDp)) {
            builder.popupPaddingDp(a.getInt(R.styleable.PageIndicatorView_pic_popupPaddingDp, 10));
        }
    }

    /* =========================================================
     * 初始化 UI
     * ========================================================= */

    private void initView() {
        View rootLayout = findViewById(R.id.mainLayout);
        if (rootLayout != null) {
            ViewGroup.LayoutParams rootLp = rootLayout.getLayoutParams();
            if (rootLp != null) {
                rootLp.height = dp2px(getContext(), config.barHeightDp);
                rootLayout.setLayoutParams(rootLp);
            }
        }

        assert rootLayout != null;

        // ===== 背景色 =====
        rootLayout.setBackgroundColor(config.barBackgroundColor);

        // ===== 描边：通过 GradientDrawable 实现 =====
        if (rootLayout instanceof LinearLayout) {
            GradientDrawable strokeBg = new GradientDrawable();
            strokeBg.setColor(config.barBackgroundColor);

            if (config.barStrokeWidthDp > 0) {
                int strokePx = dp2px(getContext(), config.barStrokeWidthDp);
                strokeBg.setStroke(strokePx, config.barStrokeColor);
            }

            rootLayout.setBackground(strokeBg);
        }

        // ===== 上一页 =====
        btnPrev.setText(config.prevText);
        btnPrev.setTextSize(config.barTextSizeSp);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable prevIcon = getContext().getDrawable(config.prevIconRes);
        if (prevIcon != null) {
            int iconSize = dp2px(getContext(), config.barIconSizeDp);
            prevIcon.setBounds(0, 0, iconSize, iconSize);
        }
        btnPrev.setCompoundDrawables(prevIcon, null, null, null);

        // ===== 下一页 =====
        btnNext.setText(config.nextText);
        btnNext.setTextSize(config.barTextSizeSp);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable nextIcon = getContext().getDrawable(config.nextIconRes);
        if (nextIcon != null) {
            int iconSize = dp2px(getContext(), config.barIconSizeDp);
            nextIcon.setBounds(0, 0, iconSize, iconSize);
        }
        btnNext.setCompoundDrawables(null, null, nextIcon, null);

        refreshIndicatorText();
        btnPageIndicator.setTextSize(config.indicatorTextSizeSp);
        btnPageIndicator.setTextColor(config.indicatorTextColor);

        refreshButtonState();
    }

    /* =========================================================
     * 事件绑定
     * ========================================================= */

    private void bindEvent() {

        btnPrev.setOnClickListener(v -> {
            if (currentPage > minPage) {
                currentPage--;
                refreshUI();
                if (dispatcher != null) {
                    if (logEnabled) Log.d(logTag, "点击上一页，当前页：" + currentPage);
                    dispatcher.onPageSelected(currentPage);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < maxPage) {
                currentPage++;
                refreshUI();
                if (dispatcher != null) {
                    if (logEnabled) Log.d(logTag, "点击下一页，当前页：" + currentPage);
                    dispatcher.onPageSelected(currentPage);
                }
            }
        });

        btnPageIndicator.setOnClickListener(v -> {
            if (maxPage <= 1) {
                return;
            }
            if (logEnabled) Log.d(logTag, "点击页码指示器，弹出跳转弹窗");

            if (jumpPopup != null && jumpPopup.isShowing()) {
                jumpPopup.dismiss();
                return;
            }

            if (maxPage > 1) {
                btnPageIndicator.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_jump_up, 0);
                Drawable indicatorIcon = btnPageIndicator.getCompoundDrawables()[2];
                if (indicatorIcon != null) {
                    indicatorIcon.mutate();
                    indicatorIcon.setTint(config.barEnableColor);
                }
            }
            showJumpPopup();
        });
    }

    /* =========================================================
     * 页码设置
     * ========================================================= */

    public void setCurrentPage(int page) {
        setCurrentPage(page, false);
    }

    public void setCurrentPage(int page, boolean notify) {
        if (page < minPage || page > maxPage) return;
        this.currentPage = page;
        refreshUI();
        if (notify && dispatcher != null) {
            dispatcher.onPageSelected(currentPage);
        }
    }

    public void setMinPage(int minPage) {
        if (minPage < 0) minPage = 0;
        this.minPage = minPage;
        if (this.minPage > this.maxPage) {
            this.maxPage = this.minPage;
        }
        if (currentPage < this.minPage) {
            currentPage = this.minPage;
            if (dispatcher != null) {
                dispatcher.onPageSelected(currentPage);
            }
        }
        refreshUI();
    }

    public void setMaxPage(int maxPage) {
        if (maxPage < minPage) maxPage = minPage;
        this.maxPage = maxPage;

        boolean changed = false;
        if (currentPage > maxPage) {
            currentPage = maxPage;
            changed = true;
        }
        refreshUI();
        if (changed && dispatcher != null) {
            dispatcher.onPageSelected(currentPage);
        }
    }

    public void jumpToWithMax(int newMax, int target, boolean notify) {
        setMaxPage(newMax);
        setCurrentPage(target, notify);
    }

    public void jumpTo(int target, boolean notify) {
        setCurrentPage(target, notify);
    }

    /* =========================================================
     * 状态查询
     * ========================================================= */

    public int getCurrentPage() { return currentPage; }
    public int getMinPage() { return minPage; }
    public int getMaxPage() { return maxPage; }
    public boolean isFirstPage() { return currentPage == minPage; }
    public boolean isLastPage() { return currentPage == maxPage; }
    public boolean canGoPrev() { return currentPage > minPage; }
    public boolean canGoNext() { return currentPage < maxPage; }

    /* =========================================================
     * UI 刷新
     * ========================================================= */

    private void refreshUI() {
        refreshIndicatorText();
        refreshButtonState();
    }

    private void refreshIndicatorText() {
        String text = getContext().getString(R.string.page_indicator_format, currentPage, maxPage);
        btnPageIndicator.setText(text);

        if (maxPage <= 1) {
            btnPageIndicator.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            btnPageIndicator.setEnabled(false);
            btnPageIndicator.setClickable(false);
        } else {
            btnPageIndicator.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_jump_down, 0);
            Drawable indicatorIcon = btnPageIndicator.getCompoundDrawables()[2];
            if (indicatorIcon != null) {
                indicatorIcon.mutate();
                indicatorIcon.setTint(config.barEnableColor);
            }
            btnPageIndicator.setEnabled(true);
            btnPageIndicator.setClickable(true);
        }
    }

    private void refreshButtonState() {
        boolean canPrev = currentPage > minPage;
        boolean canNext = currentPage < maxPage;

        int enableColor = config.barEnableColor;
        int disableColor = config.barDisableColor;

        btnPrev.setEnabled(canPrev);
        btnPrev.setTextColor(canPrev ? enableColor : disableColor);

        Drawable prevIcon = btnPrev.getCompoundDrawables()[0];
        if (prevIcon != null) {
            prevIcon.mutate();
            prevIcon.setTint(canPrev ? enableColor : disableColor);
        }

        btnNext.setEnabled(canNext);
        btnNext.setTextColor(canNext ? enableColor : disableColor);

        Drawable nextIcon = btnNext.getCompoundDrawables()[2];
        if (nextIcon != null) {
            nextIcon.mutate();
            nextIcon.setTint(canNext ? enableColor : disableColor);
        }

        btnPrev.setImportantForAccessibility(
                canPrev ? View.IMPORTANT_FOR_ACCESSIBILITY_YES
                        : View.IMPORTANT_FOR_ACCESSIBILITY_NO
        );
        btnNext.setImportantForAccessibility(
                canNext ? View.IMPORTANT_FOR_ACCESSIBILITY_YES
                        : View.IMPORTANT_FOR_ACCESSIBILITY_NO
        );
    }

    /* =========================================================
     * 跳转弹窗
     * ========================================================= */

    private void showJumpPopup() {
        if (jumpPopup != null && jumpPopup.isShowing()) return;

        @SuppressLint("InflateParams")
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_jump_page, null);

        EditText etJumpPage = popupView.findViewById(R.id.etJumpPage);
        AppCompatButton btnConfirm = popupView.findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(true);
        TextView tvErrorTip = popupView.findViewById(R.id.tvErrorTip);

        popupView.setBackgroundColor(config.popupBackgroundColor);

        etJumpPage.setTextColor(config.inputTextColor);
        etJumpPage.setTextSize(config.inputTextSizeSp);
        etJumpPage.setHintTextColor(config.inputHintColor);

        btnConfirm.setText(config.confirmText);
        btnConfirm.setTextSize(config.confirmTextSizeSp);
        btnConfirm.setTextColor(config.confirmTextColor);
        GradientDrawable bg = (GradientDrawable) btnConfirm.getBackground();
        if (bg != null) {
            bg.setColor(config.confirmBackgroundColor);
        }

        tvErrorTip.setTextColor(config.errorTipTextColor);
        tvErrorTip.setTextSize(config.errorTipTextSizeSp);

        int padding = dp2px(getContext(), config.popupPaddingDp);
        popupView.setPadding(padding, padding, padding, padding);

        etJumpPage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnConfirm.performClick();
                return true;
            }
            return false;
        });

        etJumpPage.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvErrorTip.setVisibility(View.GONE);
            }
        });

        int width = dp2px(getContext(), 300);

        jumpPopup = new PopupWindow(
                popupView,
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        jumpPopup.setOutsideTouchable(true);
        jumpPopup.setFocusable(true);
        jumpPopup.setBackgroundDrawable(new ColorDrawable(config.popupBackgroundColor));
        jumpPopup.setOnDismissListener(() -> {
            hideKeyboard(etJumpPage);

            if (maxPage <= 1) {
                btnPageIndicator.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else {
                btnPageIndicator.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_jump_down, 0
                );
                Drawable indicatorIcon = btnPageIndicator.getCompoundDrawables()[2];
                if (indicatorIcon != null) {
                    indicatorIcon.mutate();
                    indicatorIcon.setTint(config.barEnableColor);
                }
            }
        });

        btnConfirm.setOnClickListener(v -> {
            String input = Objects.requireNonNull(etJumpPage.getText()).toString().trim();

            if (TextUtils.isEmpty(input)) {
                tvErrorTip.setText("请输入页码");
                tvErrorTip.setVisibility(View.VISIBLE);
                if (dispatcher != null) {
                    dispatcher.onJumpError("请输入页码");
                }
                return;
            }

            int target;
            try {
                target = Integer.parseInt(input);
            } catch (Exception e) {
                tvErrorTip.setText("请输入有效页码");
                tvErrorTip.setVisibility(View.VISIBLE);
                if (dispatcher != null) {
                    dispatcher.onJumpError("请输入有效页码");
                }
                return;
            }

            if (target < minPage || target > maxPage) {
                tvErrorTip.setText("页码超出范围");
                tvErrorTip.setVisibility(View.VISIBLE);
                if (dispatcher != null) {
                    dispatcher.onJumpError("页码超出范围");
                }
                return;
            }

            tvErrorTip.setVisibility(View.GONE);

            if (logEnabled) Log.d(logTag, "跳转页码：" + target);

            jumpPopup.dismiss();
            hideKeyboard(etJumpPage);

            currentPage = target;
            refreshUI();
            if (dispatcher != null) {
                dispatcher.onJumpRequested(currentPage);
            }
        });

        int offsetX = (btnPageIndicator.getWidth() - width) / 2;
        jumpPopup.showAsDropDown(btnPageIndicator, offsetX, 0);

        etJumpPage.post(() -> {
            etJumpPage.requestFocus();
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etJumpPage, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    /* =========================================================
     * 键盘
     * ========================================================= */

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /* =========================================================
     * 工具方法
     * ========================================================= */

    public PageIndicatorConfig getConfig() {
        return this.config;
    }

    private int dp2px(Context c, int dp) {
        return (int) (dp * c.getResources().getDisplayMetrics().density);
    }

    /* =========================================================
     * 外部接口
     * ========================================================= */

    public void setDispatcher(@Nullable PageEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void setConfig(@NonNull PageIndicatorConfig newConfig) {
        this.config = newConfig;
        initView();
        refreshUI();
        jumpPopup = null;
    }

    public void enableLog(@Nullable String tag) {
        logEnabled = true;
        if (tag != null && !tag.trim().isEmpty()) {
            logTag = tag;
        }
    }

    public void disableLog() {
        logEnabled = false;
    }
}