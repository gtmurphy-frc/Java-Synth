package synth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Gui extends JFrame {

    private JPanel itemPanel;
    private ArrayList<OscillatorUI> items;
    private JButton addButton;
    private WaveformPanel waveformPanel;
    private AudioManager audioManager;
    private int sampleRate = 44100;

    public Gui() {
        // Initialize the GUI
        items = new ArrayList<>();
        setTitle("Synth");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));

        addItem();

        addButton = new JButton("Add Oscillator");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
                updateGraph();
            }
        });
        // Initalize the waveform graph
        waveformPanel = new WaveformPanel();
        updateGraph();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(itemPanel);
        scrollPane.setPreferredSize(new Dimension(350, getHeight()));
        mainPanel.add(scrollPane, BorderLayout.WEST);

        mainPanel.add(waveformPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane waveformScrollPane = new JScrollPane(waveformPanel);
        waveformScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        waveformScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        mainPanel.add(waveformScrollPane, BorderLayout.CENTER);


        setContentPane(mainPanel);
    }
    // Adds a new oscillator
    private void addItem() {
        OscillatorUI newItem = new OscillatorUI(this::updateGraph);
        items.add(newItem);
        itemPanel.add(newItem.getPanel());

        itemPanel.revalidate();
        itemPanel.repaint();
    }

    private void updateGraph() {
        byte[] sampleData = generateSound();
        waveformPanel.setWaveformData(sampleData);
        
        waveformPanel.revalidate();
        waveformPanel.repaint();
    }
    

    private byte[] generateSound() {
        ArrayList<byte[]> samples = new ArrayList<byte[]>();
        AudioManager audioManager = new AudioManager(sampleRate);
        Oscillator oscillator = new Oscillator(sampleRate);
        // Get the parameters of the oscillators and add the samples
        for (OscillatorUI item : items) {
            JComboBox<OscillatorType> comboBox = item.getComboBox();
            OscillatorType selectedOscillator = (OscillatorType) comboBox.getSelectedItem();

            if (selectedOscillator != null) {
                System.out.println(selectedOscillator);
                switch (selectedOscillator) {
                    case SINE:
                        samples.add(oscillator.generateSineWave(item.getSliderF(), 2, item.getSliderA(), item.getSliderD(), item.getSliderS(), item.getSliderR()));
                        break;
                    case SQUARE:
                        samples.add(oscillator.generateSquareWave(item.getSliderF(), 2, item.getSliderA(), item.getSliderD(), item.getSliderS(), item.getSliderR()));
                        break;
                    case SAW:
                        samples.add(oscillator.generateSawWave(item.getSliderF(), 2, item.getSliderA(), item.getSliderD(), item.getSliderS(), item.getSliderR()));
                        break;
                    case TRIANGLE:
                        samples.add(oscillator.generateTriangleWave(item.getSliderF(), 2, item.getSliderA(), item.getSliderD(), item.getSliderS(), item.getSliderR()));
                        break;
                    case NOISE:
                        samples.add(oscillator.generateNoise(2, item.getSliderA(), item.getSliderD(), item.getSliderS(), item.getSliderR()));
                        break;
                    default:
                        break;
                }
            }
        }
        // Starts a new thread for audio
        byte[] sound = audioManager.mixSound(samples);
        new Thread(() -> {
            audioManager.playSound(sound);
        }).start();
        return sound;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gui().setVisible(true));
    }
}
