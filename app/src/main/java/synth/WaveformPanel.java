package synth;

import javax.swing.*;
import java.awt.*;
// Graph class for the Waveform
public class WaveformPanel extends JPanel {
    private byte[] waveformData;

    public WaveformPanel() {
        this.waveformData = new byte[0];
    }

    public void setWaveformData(byte[] data) {
        this.waveformData = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (waveformData == null || waveformData.length == 0) return;

        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.GREEN);

        int midY = height / 2;
        int xStep = Math.max(1, width / waveformData.length);

        for (int i = 1; i < waveformData.length; i++) {
            int x1 = (i - 1) * xStep;
            int y1 = midY - (waveformData[i - 1] * midY / 128);
            int x2 = i * xStep;
            int y2 = midY - (waveformData[i] * midY / 128);

            g.drawLine(x1, y1, x2, y2);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Math.max(800, waveformData.length * 2), 300);
    }

}
