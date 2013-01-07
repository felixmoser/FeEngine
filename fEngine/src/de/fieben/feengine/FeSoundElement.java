package de.fieben.feengine;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public final class FeSoundElement {

	private SoundPool mSoundPool;
	private int mSoundId;
	private int mStreamId;
	private float mMasterVolume;
	private float mLeftVolume;
	private float mRightVolume;

	protected FeSoundElement(final Context context, final int resourceId,
			final LoadCompleteCallback callback) {
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
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
		mStreamId = mSoundPool.play(mSoundId, leftVolume * mMasterVolume,
				rightVolume * mMasterVolume, 0, loop ? -1 : 0, 1f);
		mLeftVolume = leftVolume;
		mRightVolume = rightVolume;
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
