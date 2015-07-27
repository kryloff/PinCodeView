package krylov.pin_code_view_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Krylov on 27.07.2015.
 */
public class PinCodeView extends View {

    public PinCodeView(Context context) {
        this(context,null);
    }

    public PinCodeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PinCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
}
