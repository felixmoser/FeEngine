package de.fieben.fengine.surface.impl;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class FeSoundElement {

	private SoundPool mSoundPool;
	private AudioManager mAudioManager;
	private int mSoundId;
	private int mStreamId;
	private float mMasterVolume;
	private float mLeftVolume;
	private float mRightVolume;

	public FeSoundElement(final Context context, final int resourceId,
			final LoadCompleteCallback callback) {
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		mSoundId = mSoundPool.load(context, resourceId, 1);
		if (callback != null) {
			mSoundPool
					.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
						@Override
						public void onLoadComplete(final SoundPool soundPool,
								final int sampleId, final int status) {
							callback.soundLoaded();
						}
					});
		}
		mMasterVolume = 1f;
		mLeftVolume = 1f;
		mRightVolume = 1f;
	}

	public void playSound(final float leftVolume, final float rightVolume,
			final boolean loop) {
		final float currentStreamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		mStreamId = mSoundPool.play(mSoundId, currentStreamVolume * leftVolume,
				currentStreamVolume * rightVolume, 0, loop ? -1 : 0, 1f);
	}

	public void stopSound() {
		mSoundPool.stop(mStreamId);
	}

	public void setMasterVolume(final float masterVolume) {
		mMasterVolume = Math.max(0f, Math.min(masterVolume, 1f));
		updateVolume();
	}

	public void setChannelVolumes(final float leftVolume,
			final float rightVolume) {
		mLeftVolume = Math.max(0f, Math.min(leftVolume, 1f));
		mRightVolume = Math.max(0f, Math.min(rightVolume, 1f));
		updateVolume();
	}

	private void updateVolume() {
		mSoundPool.setVolume(mStreamId, mLeftVolume * mMasterVolume,
				mRightVolume * mMasterVolume);
	}

	public interface LoadCompleteCallback {
		public void soundLoaded();
	}
}
