package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.surface.FeSurface;
import de.fieben.fengine.surface.FeSurfaceElement;

public class SoundExampleScene extends FeSurface {

	private SoundPool mSoundPool;
	private AudioManager mAudioManager;

	private int mSoundId;

	private SoundElement mSoundElement;

	public SoundExampleScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);

		mSoundId = mSoundPool.load(context, R.raw.sound_view_clicked, 1);

		mSoundElement = new SoundElement();
		// mSoundElement.set3DSound(true);
		addElement(mSoundElement);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		// WIP if touch is on SoundElement
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			final float currentStreamVolume = mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);

			final float maxStreamVolume = mAudioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

			android.util.Log.i("testing", "::onTouchEvent:: "
					+ currentStreamVolume + "|" + maxStreamVolume);

			mSoundPool.play(mSoundId, currentStreamVolume, currentStreamVolume,
					0, 0, 1f);

			// WIP move element
			return true;
		}
		return false;
	}

	private class SoundElement extends FeSurfaceElement {
		// private boolean m3DSound = false;

		public SoundElement() {
		}

		// public void set3DSound(final boolean enabled) {
		// m3DSound = enabled;
		// }

		@Override
		public void onDraw(final Canvas canvas, final Paint paint) {
			paint.setColor(Color.YELLOW);
			canvas.drawCircle(150, 250, 50, paint);
		}

		@Override
		public void onUpdate(final long elapsedMillis) {
		}

		@Override
		public void doUpdate() {
		}
	}
}