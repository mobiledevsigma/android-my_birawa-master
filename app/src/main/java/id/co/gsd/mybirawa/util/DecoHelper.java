package id.co.gsd.mybirawa.util;

import android.graphics.Color;
import android.view.animation.AnticipateInterpolator;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

/**
 * Created by Biting on 12/21/2017.
 */

public class DecoHelper {

    int valueBack;
    int valueFront;

    public void DecoBack(DecoView mDecoView, Float mSeriesMax) {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .setLineWidth(20f)
                .build();
        valueBack = mDecoView.addSeries(seriesItem);
    }

    public void DecoDataRed(DecoView mDecoView, Float mSeriesMax, final TextView textPercentage) {
        final SeriesItem seriesItem = new SeriesItem
                    .Builder(Color.parseColor("#BC1550"))
                    .setRange(0, mSeriesMax, 0)
                    .setInitialVisibility(false)
                    .setLineWidth(20f)
                    .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
        valueFront = mDecoView.addSeries(seriesItem);
    }

    public void DecoDataYellow(DecoView mDecoView, Float mSeriesMax, final TextView textPercentage) {
        final SeriesItem seriesItem = new SeriesItem
                .Builder(Color.parseColor("#FFB900"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(20f)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
        valueFront = mDecoView.addSeries(seriesItem);
    }

    public void DecoDataGreen(DecoView mDecoView, Float mSeriesMax, final TextView textPercentage) {
        final SeriesItem seriesItem = new SeriesItem
                .Builder(Color.parseColor("#03964B"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(20f)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
        valueFront = mDecoView.addSeries(seriesItem);
    }

    public void DecoEvent(final DecoView mDecoView, final Float angkaMax, final Float angkaActual) {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(angkaMax)
                .setIndex(valueBack)
                .setDuration(3000)
                .setDelay(100)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(valueFront)
                .setDuration(2000)
                .setDelay(1250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(Float.valueOf(angkaActual))
                .setIndex(valueFront)
                .setDelay(3250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(valueFront)
                .setDelay(20000)
                .setDuration(1000)
                .setInterpolator(new AnticipateInterpolator())
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {
                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        DecoEvent(mDecoView, angkaMax, angkaActual);
                    }
                })
                .build());
    }
}
