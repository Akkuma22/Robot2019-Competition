package org.usfirst.frc.team467.robot.vision;

import java.util.ArrayList;

import javax.swing.Box;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

//import javafx.scene.shape.Box;


public class VisionProcessing {
	public double cubeCenterPointY;
	public double cubeCenterPointX;
	private DetectPowerCubePipeline pipeline;
	private static final Logger LOGGER = Logger.getLogger(VisionProcessing.class);
	private static VisionProcessing instance;
	private VideoCapture camera;
	private double windowHeight;
	private double windowWidth;
	public static final double CUBE_WIDTH = 13;
	public static final double CUBE_HEIGHT = 11;
	private static final double FOCAL_LENGTH = 800.0; //623.59;
	
	// the id of the camera to be used
	public static final int CAMERA_ID = 0;
	
	public static 
void main (String args[]) {
		VisionProcessing vision = VisionProcessing.getInstance();
		System.err.println("DONE");
	}
	
	private VisionProcessing() {
		 pipeline = new DetectPowerCubePipeline();
		 camera = new VideoCapture();
		 camera.open(VisionProcessing.CAMERA_ID);		 
	}
	
	public Mat grabFrame() {
		Mat frame = new Mat();
		if (camera.isOpened()) {
			try {
				camera.read(frame);
				if (!frame.empty()) {
					windowHeight = frame.size().height;
					windowWidth = frame.size().width;
					pipeline.process(frame);
				}
			} catch (Exception e) {
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return frame;
	}
	
	public static VisionProcessing getInstance() {
		if (instance == null) {
			instance = new VisionProcessing();
		}
		return instance;
	}
	
	public double findCube(Mat mat) { 
		double cameraDistanceX;
		double cameraDistanceY;
		double lengthOfShortLeg;
		double cubeDistance = 19.5; //Manually measured for now.
		double angle = 0;
		
		grabFrame();
		ArrayList<Rect> boundingBoxes = new ArrayList<Rect>();
		for (MatOfPoint points : pipeline.convexHullsOutput()) {
			Rect box = Imgproc.boundingRect(points);
			boundingBoxes.add(box);
			cubeCenterPointY = (box.height / 2) + box.y;
			cubeCenterPointX = (box.width / 2) + box.x;
			Imgproc.rectangle(mat, new Point(box.x, box.y), new Point(box.x + box.width, box.y + box.height), new Scalar(0, 255, 0, 0), 5);
			
			
//			if (box.height < box.width) {
//				//TODO : This configuration has it so that the grabber barely fits, be exact.
//				System.out.println("orientation 1"); 	
//				System.out.println("Cube center point Y: " + cubeCenterPointY + " Cube center point X: " + cubeCenterPointX);
//				
//			
//			}
//			if(box.height > box.width) {
//				System.out.println("orientation 2");
//				//TODO : Configure this orientation, it has more slop than orientation 1.
//				System.out.println("Cube center point Y: " + cubeCenterPointY + " Cube center point X: " + cubeCenterPointX);
//				
//			}
			cameraDistanceX = cubeCenterPointX - (windowWidth / 2);
			cameraDistanceY = cubeCenterPointY - (windowHeight / 2);
			
			angle = Math.atan2(cubeCenterPointX, FOCAL_LENGTH);
			
			
//			System.out.println("Window width: " + windowWidth + " Window Height: " + windowHeight); //The width is 640 pixels and the height is 480 pixels.
//			System.out.println("Camera distance X: " + cameraDistanceX + " Camera distance Y: " + cameraDistanceY);

//			System.out.println("x: " + box.x + " y: " + box.y + " width: " + box.width + " height: " + box.height);
			System.out.println("Angle Measure in Degrees = " + Math.toDegrees(angle));
			System.out.println("Angle Measure in Radians = " + angle);
			break; 
		}
		return angle;
	}
		
	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public void findContoursOutput() {
		System.out.println("COUNT: " + pipeline.filterContoursOutput().size());
		for (MatOfPoint points: pipeline.convexHullsOutput()) {
		System.out.println("Test " + points);
		}
	}


	@Override
	public String toString() {
		return "HI";
	}
}
