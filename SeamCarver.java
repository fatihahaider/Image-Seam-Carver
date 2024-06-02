import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stopwatch;

public class SeamCarver {
    private Picture picture; // current pic obj
    private int width; // width of pic
    private int height; // height of pic

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("null argument");
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }

    // helper method to calculate x value-energy of neighboring pixels.
    private double calculateX(int row, int colL, int colR) {
        int rgbR = picture.getRGB(colR, row);

        int rR = (rgbR >> 16) & 0xFF;
        int gR = (rgbR >> 8) & 0xFF;
        int bR = (rgbR) & 0xFF;

        int rgbL = picture.getRGB(colL, row);

        int rL = (rgbL >> 16) & 0xFF;
        int gL = (rgbL >> 8) & 0xFF;
        int bL = (rgbL) & 0xFF;

        return (Math.pow(rR - rL, 2) + Math.pow(gR - gL, 2) + Math.pow(bR - bL, 2));
    }

    // helper method to calculate y value-energy of neighboring pixels.
    private double calculateY(int col, int rowT, int rowB) {
        int rgbT = picture.getRGB(col, rowT);

        int rT = (rgbT >> 16) & 0xFF;
        int gT = (rgbT >> 8) & 0xFF;
        int bT = (rgbT) & 0xFF;

        int rgbB = picture.getRGB(col, rowB);

        int rB = (rgbB >> 16) & 0xFF;
        int gB = (rgbB >> 8) & 0xFF;
        int bB = (rgbB) & 0xFF;

        return (Math.pow(rT - rB, 2) + Math.pow(gT - gB, 2) + Math.pow(bT - bB, 2));
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("invalid points");


        double xVal = 0;
        double yVal = 0;
        double onePixelEnergy;

        if (height() == 1) {
            yVal = 0;
        }
        else if (y == 0) yVal = calculateY(x, height - 1, y + 1);
        else if (y != height - 1) yVal = calculateY(x, y - 1, y + 1);
        else yVal = calculateY(x, y - 1, 0);

        if (width() == 1) {
            xVal = 0;
        }
        else if (x == 0) xVal = calculateX(y, width - 1, x + 1);
        else if (x != width - 1) xVal = calculateX(y, x - 1, x + 1);
        else xVal = calculateX(y, x - 1, 0);


        onePixelEnergy = Math.sqrt(xVal + yVal);
        return onePixelEnergy;
    }

    // transposes an image
    private void transpose() {
        Picture transposedPic = new Picture(picture.height(), picture.width());


        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                transposedPic.setRGB(row, col, picture.getRGB(col, row));
            }
        }
        picture = transposedPic;
        width = width();
        height = height();

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        transpose();
        int[] horizantalSeam = findVerticalSeam();
        transpose();
        return horizantalSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[height][width]; // array of energies of pixels

        for (int row = 0; row < height(); row++) { // WAS WIDTH
            for (int col = 0; col < width(); col++) { // WAS HEIGHT
                energy[row][col] = energy(col, row);
            }
        }

        double[][] distTo = new double[height][width];
        int[][] seamIndex = new int[height][width];
        int[] verticalSeam = new int[height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                distTo[row][col] = Double.POSITIVE_INFINITY;
            }
        }

        for (int col = 0; col < width; col++) {
            distTo[0][col] = energy[0][col];
        }

        for (int row = 1; row < height; row++) {
            for (int col = 0; col < width; col++) {

                for (int above = -1; above <= 1; above++) { // for three pixels
                    int prevX = col + above; // for before, above and after
                    if (prevX >= 0 && prevX < width) {  // within bounds

                        double totalEnergy = distTo[row - 1][prevX] + energy[row][col];
                        if (totalEnergy < distTo[row][col]) {
                            distTo[row][col] = totalEnergy;
                            seamIndex[row][col] = prevX;
                        }
                    }
                }
            }
        }

        int minIndex = 0;
        for (int col = 1; col < width; col++) {
            if (distTo[height - 1][col] < distTo[height - 1][minIndex]) {
                minIndex = col;
            }
        }

        verticalSeam[height - 1] = minIndex;
        for (int row = height - 2; row >= 0; row--) {
            minIndex = seamIndex[row + 1][minIndex];
            verticalSeam[row] = minIndex;
        }
        return verticalSeam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

        if (seam == null || seam.length != picture.height()) {
            throw new IllegalArgumentException("invalid seam array");
        }
        for (int x : seam) {
            if (x < 0 || x >= width()) {
                throw new IllegalArgumentException("invalid seam array");
            }
        }
        for (int i = 1; i < seam.length; i++) {
            int dif = seam[i - 1] - seam[i];
            if (dif <= -2 || dif >= 2) {
                throw new IllegalArgumentException("invalid seam array");
            }
        }

        Picture newPic = new Picture(width - 1, height);

        for (int row = 0; row < newPic.height(); row++) {
            for (int col = 0; col < newPic.width(); col++) {
                if (col < seam[row]) {
                    newPic.setRGB(col, row, picture.getRGB(col, row));
                }
                else {
                    newPic.setRGB(col, row, picture.getRGB(col + 1, row));
                }
            }
        }
        picture = newPic;
        width = picture.width();
        height = picture.height();
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture picture = new Picture("city8000-by-2000.png");
        SeamCarver seamCarver = new SeamCarver(picture);

        Stopwatch time = new Stopwatch();

        for (int i = 0; i < 1; i++) {
            int[] vSeam = seamCarver.findVerticalSeam();
            seamCarver.removeVerticalSeam(vSeam);
            int[] hSeam = seamCarver.findHorizontalSeam();
            seamCarver.removeHorizontalSeam(hSeam);
        }

        System.out.println(time.elapsedTime());

        int width = seamCarver.width();
        int height = seamCarver.height();
        System.out.println("width: " + width + ", height: " + height);

        double energy = seamCarver.energy(3, 2);
        System.out.println("Energy at pixel (3,2): " + energy);

        seamCarver.picture().show();
    }
}
