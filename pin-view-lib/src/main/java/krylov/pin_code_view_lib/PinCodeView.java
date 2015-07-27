package krylov.pin_code_view_lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Krylov on 27.07.2015.
 */
public class PinCodeView extends View {

    private static final int DEFAULT_PIN_LENGTH = 4;
    private static final int DEFAULT_DOT_RADIUS = 8;
    private int mCurrentPinLength;
    private int mPinLength;
    private float mDotRadius;
    private PinFormat mPinFormat;

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
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
}
