package mm.com.aeon.vcsaeon.views.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class LanguageFlagIV extends AppCompatImageView {

    public static float radius = 6.0f;

    public LanguageFlagIV(Context context) {
        super(context);
    }

    public LanguageFlagIV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LanguageFlagIV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
