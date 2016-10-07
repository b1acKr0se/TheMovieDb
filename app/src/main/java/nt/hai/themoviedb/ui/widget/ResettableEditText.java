package nt.hai.themoviedb.ui.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import nt.hai.themoviedb.R;


public class ResettableEditText extends EditText implements View.OnTouchListener, View.OnFocusChangeListener {
    private Drawable drawable;
    private ClearListener listener;

    public ResettableEditText(Context context) {
        super(context);
        init();
    }

    public ResettableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ResettableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(ClearListener clearListener) {
        listener = clearListener;
    }

    public interface ClearListener {
        void onTextCleared();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            setIconVisible(!TextUtils.isEmpty(getText()));
        } else {
            setIconVisible(false);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (getDisplayedDrawable() != null) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int left = getWidth() - getPaddingRight() - drawable.getIntrinsicWidth();
            int right = getWidth();
            boolean tappedX = x >= left && x <= right && y >= 0 && y <= (getBottom() - getTop());
            if (tappedX) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) listener.onTextCleared();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        initIcon();
    }

    private void init() {
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        RxTextView.textChanges(this)
                .subscribe(charSequence -> {
                    setIconVisible(isFocused() && charSequence.length() > 0);
                });
        initIcon();
        setIconVisible(false);
    }

    private void initIcon() {
        drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_black_24dp);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        applyTint(drawable);
        setPadding(getPaddingLeft(), getPaddingTop(), getResources().getDimensionPixelSize(R.dimen.clear_button_padding_right), getPaddingBottom());
    }

    void applyTint(Drawable icon) {
        icon.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(), R.color.gray600), PorterDuff.Mode.SRC_IN));
    }

    private Drawable getDisplayedDrawable() {
        return getCompoundDrawables()[2];
    }

    protected void setIconVisible(boolean visible) {
        Drawable[] cd = getCompoundDrawables();
        Drawable displayed = getDisplayedDrawable();
        boolean wasVisible = (displayed != null);
        if (visible != wasVisible) {
            Drawable x = visible ? drawable : null;
            super.setCompoundDrawables(cd[0], cd[1], x, cd[3]);
        }
    }
}
