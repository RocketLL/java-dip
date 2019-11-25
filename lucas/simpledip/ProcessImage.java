package land.lucas.simpledip;

import land.lucas.simplelinalg.DoubleMatrix2D;

import java.awt.image.BufferedImage;

public class ProcessImage {
    public static void process(String inputFileName, String outputFileName, DoubleMatrix2D kernel) {
        BufferedImage img = ImageUtils.readImage(inputFileName);

        System.out.printf("Read image %s\n", inputFileName);

        DoubleMatrix2D[] imgMatrix2D = ImageUtils.imageToDoubleMatrix2D(img);

        for (int i = 0; i < imgMatrix2D.length; i++) {
            imgMatrix2D[i] = imgMatrix2D[i].convolve(kernel);
        }

        ImageUtils.writeImage(outputFileName, imgMatrix2D);

        System.out.printf("File saved to %s\n", outputFileName);
    }
}
