package synth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OscillatorUI {
    private JPanel panel;
    private JSlider sliderF, sliderA, sliderD, sliderS, sliderR, sliderV;
    private JComboBox<OscillatorType> comboBox;
    private Runnable updateGraphCallback;

    public OscillatorUI(Runnable updateGraphCallback) {
        // Set the update callback
        this.updateGraphCallback = updateGraphCallback;
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 10, 15, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Frequency Slider
        sliderF = new JSlider(40, 4000, 261);
        sliderF.setMajorTickSpacing(792);
        sliderF.setMinorTickSpacing(132);
        sliderF.setPaintTicks(true);
        sliderF.setPaintLabels(true);
        // Attack Slider
        sliderA = new JSlider(0, 200, 1);
        sliderA.setMajorTickSpacing(40);
        sliderA.setMinorTickSpacing(4);
        sliderA.setPaintTicks(true);
        sliderA.setPaintLabels(true);
        // Decay Slider
        sliderD = new JSlider(0, 200, 100);
        sliderD.setMajorTickSpacing(40);
        sliderD.setMinorTickSpacing(4);
        sliderD.setPaintTicks(true);
        sliderD.setPaintLabels(true);
        // Sustain Slider
        sliderS = new JSlider(0, 100, 100);
        sliderS.setMajorTickSpacing(20);
        sliderS.setMinorTickSpacing(2);
        sliderS.setPaintTicks(true);
        sliderS.setPaintLabels(true);
        // Release Slider
        sliderR = new JSlider(0, 500, 100);
        sliderR.setMajorTickSpacing(100);
        sliderR.setMinorTickSpacing(10);
        sliderR.setPaintTicks(true);
        sliderR.setPaintLabels(true);
        // Volume Slider
        sliderV = new JSlider(0, 100, 100);
        sliderV.setMajorTickSpacing(20);
        sliderV.setMinorTickSpacing(2);
        sliderV.setPaintTicks(true);
        sliderV.setPaintLabels(true);

        comboBox = new JComboBox<>(OscillatorType.values());

        addListeners();

        int row = 0;
        addComponent(new JLabel("Oscillator:"), comboBox, gbc, row++);
        addComponent(new JLabel("Frequency:"), sliderF, gbc, row++);
        addComponent(new JLabel("Attack (ms):"), sliderA, gbc, row++);
        addComponent(new JLabel("Decay (ms):"), sliderD, gbc, row++);
        addComponent(new JLabel("Sustain %:"), sliderS, gbc, row++);
        addComponent(new JLabel("Release (ms):"), sliderR, gbc, row++);
        addComponent(new JLabel("Volume %:"), sliderV, gbc, row);
        
    }

    private JSlider createSlider() {
        JSlider slider = new JSlider(0, 100, 50);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private void addComponent(JLabel label, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; 
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.LINE_END; 
        panel.add(label, gbc);

        gbc.gridx = 1; 
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(component, gbc);
    }
    private void addListeners() {
        ChangeListener sliderListener = e -> updateGraphCallback.run();
        ActionListener comboListener = e -> updateGraphCallback.run();

        sliderF.addChangeListener(sliderListener);
        sliderA.addChangeListener(sliderListener);
        sliderD.addChangeListener(sliderListener);
        sliderS.addChangeListener(sliderListener);
        sliderR.addChangeListener(sliderListener);
        comboBox.addActionListener(comboListener);
    }
    public JPanel getPanel() {
        return panel;
    }

    public int getSliderF() {
        return sliderF.getValue();
    }
    public int getSliderA() {
        return sliderA.getValue() / 100;
    }
    public int getSliderD() {
        return sliderD.getValue() / 100;
    }
    public int getSliderS() {
        return sliderS.getValue() / 100;
    }
    public int getSliderR() {
        return sliderR.getValue() / 100;
    }
    public int getSliderV() {
        return sliderV.getValue() / 100;
    }
    public JComboBox<OscillatorType> getComboBox() {
        return comboBox;
    }
}
