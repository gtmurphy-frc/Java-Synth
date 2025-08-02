package synth;

import java.util.Random;

public class Oscillator {
    private int sampleRate = 44100;
    private Random random = new Random();

    public Oscillator(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public byte[] generateSineWave(double freq, int duration, double attack, double decay, double sustain, double release) {
        byte[] buffer = new byte[sampleRate * duration];

        for (int i = 0; i < buffer.length; i++) {
            double angle = 2.0 * Math.PI * freq * i / sampleRate;
            buffer[i] = (byte) (Math.sin(angle) * 127);
        }

        applyADSR(buffer, attack, decay, sustain, release);
        return buffer;
    }

    public byte[] generateSquareWave(double freq, int duration, double attack, double decay, double sustain, double release) {
        byte[] buffer = new byte[sampleRate * duration];

        for (int i = 0; i < buffer.length; i++) {
            double cycle = (double) i / (sampleRate / freq);
            buffer[i] = (byte) ((cycle % 1.0 < 0.5) ? 127 : -127);
        }

        applyADSR(buffer, attack, decay, sustain, release);
        return buffer;
    }

    public byte[] generateSawWave(double freq, int duration, double attack, double decay, double sustain, double release) {
        byte[] buffer = new byte[sampleRate * duration];

        for (int i = 0; i < buffer.length; i++) {
            double cycle = (double) i / (sampleRate / freq);
            buffer[i] = (byte) ((cycle % 1.0) * 255 - 127);
        }

        applyADSR(buffer, attack, decay, sustain, release);
        return buffer;
    }

    public byte[] generateTriangleWave(double freq, int duration, double attack, double decay, double sustain, double release) {
        byte[] buffer = new byte[sampleRate * duration];

        for (int i = 0; i < buffer.length; i++) {
            double cycle = (double) i / (sampleRate / freq);
            double value = 4 * Math.abs((cycle % 1.0) - 0.5) - 1;
            buffer[i] = (byte) (value * 127);
        }

        applyADSR(buffer, attack, decay, sustain, release);
        return buffer;
    }

    public byte[] generateNoise(int duration, double attack, double decay, double sustain, double release) {
        byte[] buffer = new byte[sampleRate * duration];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) (random.nextInt(255) - 128);
        }

        applyADSR(buffer, attack, decay, sustain, release);
        return buffer;
    }
    
    private void applyADSR(byte[] buffer, double attack, double decay, double sustain, double release) {
        int totalSamples = buffer.length;
        int attackSamples = Math.max(1, (int) (sampleRate * attack));
        int decaySamples = Math.max(1, (int) (sampleRate * decay));
        int releaseSamples = Math.max(1, (int) (sampleRate * release));
        // Set the sustain to the remaining amount of sample time
        int sustainSamples = totalSamples - (attackSamples + decaySamples + releaseSamples);
        // Do not allow negative sustain values
        if (sustainSamples < 0) {
            sustainSamples = 0;
        }
    
        double[] envelope = new double[totalSamples];
        // Applies Attack
        for (int i = 0; i < attackSamples && i < totalSamples; i++) {
            envelope[i] = i / (double) attackSamples;
        }
        // Applies Decay
        for (int i = attackSamples; i < attackSamples + decaySamples && i < totalSamples; i++) {
            envelope[i] = 1.0 - ((i - attackSamples) / (double) decaySamples) * (1.0 - sustain);
        }
        // Applies Sustain
        for (int i = attackSamples + decaySamples; i < attackSamples + decaySamples + sustainSamples && i < totalSamples; i++) {
            envelope[i] = sustain;
        }
        // Applies Release
        double startReleaseLevel = sustain;
        for (int i = attackSamples + decaySamples + sustainSamples; i < totalSamples; i++) {
            envelope[i] = startReleaseLevel * (1.0 - ((i - (attackSamples + decaySamples + sustainSamples)) / (double) releaseSamples));
        }
        // Apply all of the envelopes
        for (int i = 0; i < totalSamples; i++) {
            buffer[i] *= envelope[i];
        }
    }
    
    
}
