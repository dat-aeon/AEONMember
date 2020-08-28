package mm.com.aeon.vcsaeon.views.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class SendMessageImageView extends AppCompatImageView {

    public static float radius = 14.0f;

    public SendMessageImageView(Context context) {
        super(context);
    }

    public SendMessageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendMessageImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //float radius = 36.0f;
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}