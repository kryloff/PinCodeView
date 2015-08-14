package krylov.pin_code_view_lib;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author Nikolay Krylov
 */
public class PinCodeView extends View implements View.OnKeyListener {

    private static final int DEFAULT_PIN_LENGTH = 4;
    private static final int DEFAULT_DOT_RADIUS = 8;
    private final Paint mPaint;
    private int mCurrentPinLength;
    private int mPinLength;
    private float mDotRadius;
    private PinFormat mPinFormat;
    private String mPin;
    private OnPinChangeListener pinChangeListener;

    public PinCodeView(Context context) {
        this(context, null);
    }

    public PinCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PinCodeView, 0, 0);
        try {
            int value = typedArray.getInt(R.styleable.PinCodeView_pin_type, 1);
            mPinFormat = PinFormat.getById(value);
            mPinLength = typedArray.getInt(R.styleable.PinCodeView_pin_length, DEFAULT_PIN_LENGTH);
            mDotRadius = typedArray.getFloat(R.styleable.PinCodeView_dot_radius, DEFAULT_DOT_RADIUS);
            mPaint = new Paint();
        } finally {
            typedArray.recycle();
        }
    }


    public int getCurrentPinLength() {
        return mCurrentPinLength;
    }

    public void setCurrentPinLength(int currentPinLength) {
        this.mCurrentPinLength = currentPinLength;
    }

    public int getPinLength() {
        return mPinLength;
    }

    public void setPinLength(int pinLength) {
        this.mPinLength = pinLength > 0 ? pinLength : DEFAULT_PIN_LENGTH;
    }

    public OnPinChangeListener getPinChangeListener() {
        return pinChangeListener;
    }

    public void setPinChangeListener(OnPinChangeListener pinChangeListener) {
        this.pinChangeListener = pinChangeListener;
    }

    public void refreshPin(int length, String value) {
        if (length >= -1 && length <= mPinLength) {
            mCurrentPinLength = length;
            if (!TextUtils.isEmpty(value)) {
                mPin += value;
            } else if (!TextUtils.isEmpty(mPin)) {
                mPin = mPin.substring(0, mPin.length() - 1);

            }
            if (pinChangeListener != null) {
                pinChangeListener.onPinChanged(mPin);
            }
            invalidate();
        }
    }

    public void clear() {
        mCurrentPinLength = -1;
        mPin = "";
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredHeight = (int) convertDpToPixel(2 * mDotRadius + 4, getContext());
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }
        setMeasuredDimension(widthSize, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2.5f);
        float radius = convertDpToPixel(mDotRadius, getContext());
        float delta = convertDpToPixel(3 * mDotRadius, getContext());
        float newX = getWidth() / 2 - 1.5f * delta - 1.5f * radius;
        for (int i = 0; i < mPinLength; i++) {
            if (i <= mCurrentPinLength) {
                mPaint.setStyle(Paint.Style.FILL);
            } else {
                mPaint.setStyle(Paint.Style.STROKE);
            }
            canvas.drawCircle(newX, getHeight() / 2, radius, mPaint);
            newX += (delta + radius);
        }
    }


    private float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    private float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return true;
    }

    private enum PinFormat {
        text(0),
        number(1),
        all(3);

        private int id;

        PinFormat(int id) {
            this.id = id;
        }

        static PinFormat getById(int id) {
            PinFormat value = number; // by default
            for (PinFormat pinFormat : values()) {
                if (pinFormat.id == id) {
                    value = pinFormat;
                    break;
                }
            }

            return value;
        }
    }

    public interface OnPinChangeListener {
        void onPinChanged(String newPinValue);
    }
}
