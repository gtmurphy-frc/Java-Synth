package synth;
// Enum for Dropdown
enum OscillatorType {
    SINE("Sine Wave"),
    SQUARE("Square Wave"),
    SAW("Saw Wave"),
    TRIANGLE("Triangle Wave"),
    NOISE("Noise");

    private String label;

    OscillatorType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
