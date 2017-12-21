package com.sma.yamba.wallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.sma.yamba.YambaApplication;

import java.util.List;

/**
 * Created by octavian.salcianu on 12/21/2017.
 */

public class YambaWallpaper extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new YambaWallpaperEngine(((YambaApplication) getApplication()).getYambaClient());
    }

    private class YambaWallpaperEngine extends Engine implements Runnable {
        private Handler handler = new Handler();
        private ContentThread contentThread = new ContentThread();
        private YambaClient yambaClient;

        private Paint paint;

        private String[] content = new String[20];
        private TextPoint[] textPoints = new TextPoint[20];
        private int current = -1;
        private boolean running = true;
        private float offset = 0;

        public YambaWallpaperEngine(YambaClient yambaClient) {
            this.yambaClient = yambaClient;

            paint = new Paint();
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(1);
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            running = true;
            contentThread.start();

            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(this);

            running = false;

            synchronized (contentThread) {
                contentThread.interrupt();
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if(visible){
                drawFrame();
            } else {
                handler.removeCallbacks(this);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            handler.removeCallbacks(this);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            offset = xPixelOffset;

            drawFrame();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                current++;

                if(current >= textPoints.length) {
                    current = 0;
                }

                String text = content[current];

                if(text != null) {
                    textPoints[current] = new TextPoint(text, event.getX() - offset, event.getY());
                }
            }
            super.onTouchEvent(event);
        }

        public void run() {
            drawFrame();
        }

        private void drawFrame() {
            final SurfaceHolder surfaceHolder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas();
                if(c != null) {
                    drawText(c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }

            handler.removeCallbacks(this);

            if(isVisible()) {
                handler.postDelayed(this, 40);
            }
        }

        private boolean getContent() {
            List<YambaClient.Status> timeline = null;

            try {
                timeline = yambaClient.getTimeline(20);

                int i = -1;
                content = new String[20];
                if(timeline != null) {
                    for(YambaClient.Status status : timeline) {
                        i++;
                        content[i] = status.getMessage();
                    }
                }
            } catch (Exception e) {}
            return timeline != null && !timeline.isEmpty();
        }

        private void drawText(Canvas c) {
            c.drawColor(Color.BLACK);

            for(TextPoint textPoint : textPoints) {
                if(textPoint != null) {
                    c.drawText(textPoint.text, textPoint.x + offset, textPoint.y, paint);
                }
            }
        }

        private class TextPoint {
            public String text;
            private float x;
            private float y;

            public TextPoint(String text, float x, float y) {
                this.text = text;
                this.x = x;
                this.y = y;
            }
        }

        private class ContentThread extends Thread {
            public void run() {
                while(running) {
                    try {
                        boolean hasContent = getContent();
                        if(hasContent) {
                            Thread.sleep(60000);
                        } else {
                            Thread.sleep(2000);
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }
    }
}
