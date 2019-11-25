package land.lucas.simpledip;

import land.lucas.simplelinalg.DoubleMatrix2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static BufferedImage readImage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.getStackTrace();
        }
        return image;
    }


    public static DoubleMatrix2D[] imageToDoubleMatrix2D(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        double[][][] imageArray = new double[3][height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = image.getRGB(j, i);
                imageArray[0][i][j] = (color >> 16) & 0xFF;
                imageArray[1][i][j] = (color >> 8) & 0xFF;
                imageArray[2][i][j] = color & 0xFF;
            }
        }
        return new DoubleMatrix2D[]{
                new DoubleMatrix2D(imageArray[0]),
                new DoubleMatrix2D(imageArray[1]),
                new DoubleMatrix2D(imageArray[2])
        };
    }

    public static void writeImage(String fileName, DoubleMatrix2D[] rgbMatrix2D) {
        BufferedImage output = new BufferedImage(rgbMatrix2D[0].shape[1], rgbMatrix2D[0].shape[0], BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < rgbMatrix2D[0].shape[0]; i++) {
            for (int j = 0; j < rgbMatrix2D[0].shape[1]; j++) {
                int red = boundary((int) rgbMatrix2D[0].get(i, j), 256);
                int green = boundary((int) rgbMatrix2D[1].get(i, j), 256);
                int blue = boundary((int) rgbMatrix2D[2].get(i, j), 256);
                output.setRGB(j, i, (red << 16) | (green << 8) | blue | -0x1000000);
            }
        }
        try {
            ImageIO.write(output, "PNG", new File(fileName));
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private static int boundary(int value, int boundaryIndex) {
        if (value < 0) {
            return 0;
        }
        if (value < boundaryIndex) {
            return value;
        }

        return boundaryIndex - 1;
    }
}

