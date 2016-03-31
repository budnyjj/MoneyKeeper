package budny.moneykeeper.cv;

public class DigitRecognizer {
    static {
        System.loadLibrary("opencv_java3");
    }

    public static DigitRecognizer getInstance() {
        return Holder.INSTANCE;
    }

    private DigitRecognizer() {
        // to do not be instantiated by mistake
    }

    private static class Holder {
        private static final DigitRecognizer INSTANCE = new DigitRecognizer();
    }
}
