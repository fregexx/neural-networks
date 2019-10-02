package ru.vsu.perceptron;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    private static final int BLOCKS_COUNT = 3;
    private static final int BRUSH_SIZE = 10;

    private static final Map<List<Integer>, Integer> trainingSamples = new HashMap<>();

    static {
        trainingSamples.put(Arrays.asList(1, 1, 1, 1, 0, 0, 0, 0, 0, 0), 0);
        trainingSamples.put(Arrays.asList(1, 0, 0, 0, 1, 1, 1, 0, 0, 0), 0);
        trainingSamples.put(Arrays.asList(1, 0, 0, 0, 0, 0, 0, 1, 1, 1), 0);
        trainingSamples.put(Arrays.asList(1, 1, 0, 0, 1, 0, 0, 1, 0, 0), 1);
        trainingSamples.put(Arrays.asList(1, 0, 1, 0, 0, 1, 0, 0, 1, 0), 1);
        trainingSamples.put(Arrays.asList(1, 0, 0, 1, 0, 0, 1, 0, 0, 1), 1);
    }

    @FXML
    private Canvas canvas;

    @FXML
    void onMouseDragged(MouseEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillRect(event.getX(), event.getY(), BRUSH_SIZE, BRUSH_SIZE);
    }

    @FXML
    private Button button;

    @FXML
    void onActon(ActionEvent event) {
        List<Integer> inputs = getInputs();
        Perceptron perceptron = new Perceptron(inputs.size());
        System.out.println("Initial weights: " + perceptron.getWeights());
        trainingSamples.forEach((k, v) -> {
            Integer calc = perceptron.calc(k);
            if (calc.equals(v)) {
                System.out.println("EQUAL");
            } else {
                System.out.println("NOT");
            }
        });
        System.out.println("====================");
        for (int i = 0; i < 25; i++) {
            perceptron.train(trainingSamples);
            System.out.println("Trained weights: " + perceptron.getWeights());

            trainingSamples.forEach((k, v) -> {
                Integer calc = perceptron.calc(k);
                if (calc.equals(v)) {
                    System.out.println("EQUAL");
                } else {
                    System.out.println("NOT");
                }
            });
            System.out.println("====================");

        }
        System.out.println();


    }


    public List<Integer> getInputs() {
        int height = (int) canvas.getHeight();
        int width = (int) canvas.getWidth();
        WritableImage snapshot = canvas.snapshot(null, null);
        int blockSize = height / BLOCKS_COUNT;
        List<Integer> inputs = new ArrayList<>();
        inputs.add(1);

        for (int i = 0; i < BLOCKS_COUNT; i++) {
            for (int j = 0; j < BLOCKS_COUNT; j++) {
                int input = getInput(snapshot, i * blockSize, j * blockSize, blockSize);
                inputs.add(input);
            }
        }
        return inputs;
    }

    private int getInput(WritableImage snapshot, int w, int h, int blockSize) {
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        PixelWriter pixelWriter = gc.getPixelWriter();
//        pixelWriter.setColor(100, 200, Color.RED);
        PixelReader pixelReader = snapshot.getPixelReader();
        int iMax = w + blockSize;
        int jMax = h + blockSize;
        for (int i = h; i < iMax; i++) {
            for (int j = w; j < jMax; j++) {
                if (pixelReader.getColor(i, j).equals(Color.BLACK)) {
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
