package hu.janos.kirtyan.multiswitch;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Jani007 on 2016. 07. 18..
 */

public class MultiSwitch extends FrameLayout {
    // region OnSwitchChangeListener

    public interface OnSwitchChangeListener {
        void onSwitchChanged(int selectedPosition);
    }

    private OnSwitchChangeListener onSwitchChangeListener;

    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
        this.onSwitchChangeListener = onSwitchChangeListener;
    }

    // endregion

    private View selector;
    private int width, itemWidth;
    private BalancedLinearLayout contentLayout;
    private boolean isInitialized = false;

    private int itemColor, itemColorSelected, itemColorSelectedDisabled;
    private int selectedPosition = 0;

    public MultiSwitch(Context context) {
        super(context);
        init(context);
    }

    public MultiSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        initColors(context);

        initSelector(context);
        initContentLayout(context);

        setBackgroundResource(R.drawable.background_switch);

        isInitialized = true;
    }

    private void initColors(Context context) {
        if (isInEditMode()) {
            itemColor = 0x88FFFFFF;
            itemColorSelected = Color.WHITE;
            itemColorSelectedDisabled = 0x88FFFFFF;
        } else {
            itemColor = context.getResources().getColor(R.color.switch_itemTextColor);
            itemColorSelected = context.getResources().getColor(R.color.switch_itemTextColorSelected);
            itemColorSelectedDisabled = context.getResources().getColor(R.color.switch_itemTextColorSelectedDisabled);
        }
    }

    private void initContentLayout(Context context) {
        contentLayout = new BalancedLinearLayout(context);
        contentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(contentLayout);
    }

    private void initSelector(Context context) {
        selector = new View(context);
        selector.setBackgroundResource(R.drawable.background_switch_selected_item);
        selector.setLayoutParams(new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));

        addView(selector);
    }

    private void changeSelectedItem(int selectedPosition) {
        if (isEnabled()) {
            ((TextView) contentLayout.getChildAt(this.selectedPosition)).setTextColor(itemColor);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((TextView) contentLayout.getChildAt(MultiSwitch.this.selectedPosition)).setTextColor(itemColorSelected);
                }
            }, 150);

            ObjectAnimator.ofFloat(selector, "translationX", selectedPosition * itemWidth).setDuration(250).start();

            this.selectedPosition = selectedPosition;
            if (onSwitchChangeListener != null) {
                onSwitchChangeListener.onSwitchChanged(selectedPosition);
            }
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        changeSelectedItem(selectedPosition);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            selector.setBackgroundResource(R.drawable.background_switch_selected_item);
            ((TextView) contentLayout.getChildAt(selectedPosition)).setTextColor(itemColorSelected);
        } else {
            selector.setBackgroundResource(R.drawable.background_switch_selected_item_disabled);
            ((TextView) contentLayout.getChildAt(selectedPosition)).setTextColor(itemColorSelectedDisabled);
        }
    }

    // region AddView

    private final OnClickListener onItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!isEnabled()) {
                return;
            }

            changeSelectedItem((int) view.getTag());
        }
    };

    @Override
    public void addView(View child) {
        if (isInitialized) {
            if (contentLayout != null) {
                if (child instanceof TextView) {
                    child.setTag(contentLayout.getChildCount());

                    child.setOnClickListener(onItemClickListener);

                    ((TextView) child).setTextColor(contentLayout.getChildCount() == selectedPosition ? itemColorSelected : itemColor);
                    ((TextView) child).setGravity(Gravity.CENTER);
                    ((TextView) child).setAllCaps(true);

                    contentLayout.addView(child);

                    updateSelectorSize();
                } else {
                    throw new IllegalArgumentException("MultiSwitch can handle TextView only.");
                }
            }
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (isInitialized) {
            if (contentLayout != null) {
                addView(child);
            }
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isInitialized) {
            if (contentLayout != null) {
                addView(child);
            }
        } else {
            super.addView(child, width, height);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isInitialized) {
            if (contentLayout != null) {
                addView(child);
            }
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isInitialized) {
            if (contentLayout != null) {
                addView(child);
            }
        } else {
            super.addView(child, index, params);
        }
    }

    // endregion

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        updateSelectorSize();
    }

    private void updateSelectorSize() {
        if (selector != null) {
            ViewGroup.LayoutParams layoutParams = selector.getLayoutParams();
            int childCount = contentLayout.getChildCount();
            if (childCount > 0) {
                itemWidth = width / childCount;
                layoutParams.width = itemWidth;
                selector.setTranslationX(selectedPosition * itemWidth);
            }
        }
    }

    private final class BalancedLinearLayout extends LinearLayout {
        public BalancedLinearLayout(Context context) {
            super(context);
        }

        public BalancedLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public BalancedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void addView(View child) {
                child.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));

                super.addView(child);
        }
    }
}
