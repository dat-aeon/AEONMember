package mm.com.aeon.vcsaeon.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import mm.com.aeon.vcsaeon.R;

@SuppressLint("AppCompatCustomView")
public class PyidaungsuTextView extends TextView {

    public PyidaungsuTextView(Context context) {
        super(context);
        Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/pyidaungsu_regular.ttf");
        setTypeface(myTypeface);
    }

    public PyidaungsuTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PyidaungsuTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
