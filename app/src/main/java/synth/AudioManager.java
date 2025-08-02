package synth;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;

public class AudioManager {
    private int sampleRate = 44100;
    public AudioManager(int sampleRate) {
        this.sampleRate = sampleRate;
    }
    public void playSound(byte[] buffer) {
        try {
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public byte[] mixSound(ArrayList<byte[]> sounds) {
        int length = sounds.get(0).length;

        byte[] mixed = new byte[length];
        for (int i = 0; i < length; i++) {
            int sample = 0;
            for (byte[] sound : sounds) {
                sample += sound[i];
            }
            mixed[i] = (byte) Math.max(Math.min(sample / sounds.size(), 127), -128);
        }
        return mixed;
    }
}
