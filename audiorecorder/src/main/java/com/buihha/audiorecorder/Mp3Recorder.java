package com.buihha.audiorecorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * MP3录制类
 */
public class Mp3Recorder {
	
	private static final String TAG = Mp3Recorder.class.getSimpleName();
	
	static {
		System.loadLibrary("mp3lame");
	}

	private static final int DEFAULT_SAMPLING_RATE = 22050;
	
	private static final int FRAME_COUNT = 160;
	
	/* Encoded bit rate. MP3 file will be encoded with bit rate 32kbps */ 
	private static final int BIT_RATE = 32;

	private AudioRecord audioRecord = null;

	private int bufferSize;

	private File mp3File;
	
	private RingBuffer ringBuffer;
	
	private byte[] buffer;

	private FileOutputStream os = null;

	private DataEncodeThread encodeThread;

	private int samplingRate;

	private int channelConfig;

	private PCMFormat audioFormat;
	
	private boolean isRecording = false;
	
	private boolean isPause = false;

	private String mp3Directory;
	private String mp3FileName;

    /**
     * 获取mp3文件
     * @return
     */
	public String getMp3FilePath() {
		return mp3File.getAbsolutePath();
	}

	/**
	 * 设置mp3存放文件夹，如果没有设置，默认目录为Environment.getDownloadCacheDirectory().getAbsolutePath() + "/voice"
	 * @param mp3Directory
     */
	public void setMp3Directory(String mp3Directory) {
		this.mp3Directory = mp3Directory;
	}

	/**
	 * 设置mp3文件名，如果没有设置，默认为recording_时间戳.mp3
	 * @param mp3FileName
     */
	public void setMp3FileName(String mp3FileName) {
		this.mp3FileName = mp3FileName;
	}

	public Mp3Recorder(int samplingRate, int channelConfig,
			PCMFormat audioFormat) {
		this.samplingRate = samplingRate;
		this.channelConfig = channelConfig;
		this.audioFormat = audioFormat;
	}

	/**
	 * Default constructor. Setup recorder with default sampling rate 1 channel,
	 * 16 bits pcm
	 */
	public Mp3Recorder() {
		this(DEFAULT_SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,
				PCMFormat.PCM_16BIT);
	}

	/**
     * 开启录制，创建编码线程，在当前线程录制
	 * 
	 * @throws IOException
	 */
	public void startRecording() throws IOException {
		if (isRecording) return;
		Log.d(TAG, "Start recording");
		Log.d(TAG, "BufferSize = " + bufferSize);
		// Initialize audioRecord if it's null.
		if (audioRecord == null) {
			initAudioRecorder();
		}
		audioRecord.startRecording();
		
		new Thread() {

			@Override
			public void run() {
				isRecording = true;
				while (isRecording) {
					int bytes = audioRecord.read(buffer, 0, bufferSize);
					if (bytes > 0) {
						ringBuffer.write(buffer, bytes);
					}
					while(isPause){
						try {
							sleep(200);
						} catch (InterruptedException e) {}
					}
				}
				
				// release and finalize audioRecord
				try {
					audioRecord.stop();
					audioRecord.release();
					audioRecord = null;

					// stop the encoding thread and try to wait
					// until the thread finishes its job
					Message msg = Message.obtain(encodeThread.getHandler(),
							DataEncodeThread.PROCESS_STOP);
					msg.sendToTarget();
					
					Log.d(TAG, "waiting for encoding thread");
					encodeThread.join();
					Log.d(TAG, "done encoding thread");
				} catch (InterruptedException e) {
					Log.d(TAG, "Faile to join encode thread");
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}			
		}.start();
	}

	/**
	 * 停止
	 * @throws IOException
	 */
	public void stopRecording() throws IOException {
		Log.d(TAG, "stop recording");
		isRecording = false;
	}
	
	/**
	 * 暂停
	 * @throws IOException
	 */
	public void pauseRecording() throws IOException{
		Log.d(TAG, "stop pause");
		isPause = true;
	}
	
	/**
	 * 重新开始
	 * @throws IOException
	 */
	public void resumeRecording() throws IOException{
		Log.d(TAG, "stop resume");
		isPause = false;
	}

	/**
	 * 初始化录音
	 */
	private void initAudioRecorder() throws IOException {
		int bytesPerFrame = audioFormat.getBytesPerFrame();
		/* Get number of samples. Calculate the buffer size (round up to the
		   factor of given frame size) */
		int frameSize = AudioRecord.getMinBufferSize(samplingRate,
				channelConfig, audioFormat.getAudioFormat()) / bytesPerFrame;
		if (frameSize % FRAME_COUNT != 0) {
			frameSize = frameSize + (FRAME_COUNT - frameSize % FRAME_COUNT);
			Log.d(TAG, "Frame size: " + frameSize);
		}
		
		bufferSize = frameSize * bytesPerFrame;

		/* Setup audio recorder */
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
				samplingRate, channelConfig, audioFormat.getAudioFormat(),
				bufferSize);
		
		// Setup RingBuffer. Currently is 10 times size of hardware buffer
		// Initialize buffer to hold data
		ringBuffer = new RingBuffer(10 * bufferSize);
		buffer = new byte[bufferSize];
		
		// Initialize lame buffer
		// mp3 sampling rate is the same as the recorded pcm sampling rate 
		// The bit rate is 32kbps
		SimpleLame.init(samplingRate, 1, samplingRate, BIT_RATE);
		
		// Initialize the place to put mp3 file
		if(mp3Directory == null){
			mp3Directory = Environment.getDownloadCacheDirectory().getAbsolutePath() + "/record";
		}
		if(mp3FileName == null){
			mp3FileName = "record_" + new Date().getTime() + ".mp3";
		}

		File directory = new File(mp3Directory);
		if (!directory.exists()) {
			directory.mkdirs();
			Log.d(TAG, "Created directory");
		}
		mp3File = new File(directory, mp3FileName);
		os = new FileOutputStream(mp3File);

		// Create and run thread used to encode data
		// The thread will 
		encodeThread = new DataEncodeThread(ringBuffer, os, bufferSize);
		encodeThread.start();
		audioRecord.setRecordPositionUpdateListener(encodeThread, encodeThread.getHandler());
		audioRecord.setPositionNotificationPeriod(FRAME_COUNT);
	}

    /**
     * 是否正在录制
     * @return
     */
	public boolean isRecording() {
		return isRecording;
	}

    /**
     * 是否暂停
     * @return
     */
	public boolean isPause() {
		return isPause;
	}
	
}