package face.tracker;

import java.io.IOException;
import java.net.URL;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

// Introduction to Java Development with opencv
// http://docs.opencv.org/doc/tutorials/introduction/desktop_java/java_dev_intro.html#java-dev-intro
public class FacePainterExample {
	// Commented out is the code responsible for painting the image and
	// presenting it.
	// It has been commented out to optimize performance speed.
	// public class FacePainterExample extends JFrame implements Runnable,
	// WebcamPanel.Painter {

	// private WebcamPanel.Painter painter = null;
	// private List<DetectedFace> faces = null;
	// private BufferedImage crosshair = null;

	// @Override
	// public void paintPanel(WebcamPanel panel, Graphics2D g2) {
	// if (painter != null) {
	// painter.paintPanel(panel, g2);
	// }
	// }

	// @Override
	// public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D
	// g2) {
	//
	// if (painter != null) {
	// painter.paintImage(panel, image, g2);
	// }
	//
	// if (faces == null) {
	// return;
	// }
	//
	// Iterator<DetectedFace> dfi = faces.iterator();
	// while (dfi.hasNext()) {
	//
	// DetectedFace face = dfi.next();
	// Rectangle bounds = face.getBounds();
	//
	// int dx = (int) (0.1 * bounds.width);
	// int dy = (int) (0.2 * bounds.height);
	// int x = (int) bounds.x - dx;
	// int y = (int) bounds.y - dy;
	// int w = (int) bounds.width + 2 * dx;
	// int h = (int) bounds.height + dy;
	//
	// g2.drawImage(crosshair, x, y, w, h, null);
	// g2.setStroke(STROKE);
	// g2.setColor(Color.GREEN);
	// g2.drawRect(x, y, w, h);
	// }
	// }

	public static void main(String[] args) throws IOException, InterruptedException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		URL resource = FacePainterExample.class.getResource("/haarcascade_frontalface_alt.xml");
		String path = resource.getPath();
		CascadeClassifier faceDetector = new CascadeClassifier(path.substring(1));

		VideoCapture camera = new VideoCapture(0);
		Thread.sleep(1000);
		camera.open(0); // Useless
		if (!camera.isOpened()) {
			System.out.println("Camera Error");
			throw new RuntimeException("Camera Error");
		} else {
			System.out.println("Camera OK?");
		}
		for (int i = 0; i < 50; i++) {
			long start = System.currentTimeMillis();
			runCycle(faceDetector, camera);
			long end = System.currentTimeMillis();
			System.out.println("Cycle took: " + ((end - start) / 1000.0) + " seconds");
		}
		camera.release();

	}

	private static void runCycle(CascadeClassifier faceDetector, VideoCapture camera) {
		Mat frame = new Mat();

		// camera.grab();
		// System.out.println("Frame Grabbed");
		// camera.retrieve(frame);
		// System.out.println("Frame Decoded");

		camera.read(frame);
		System.out.println("Frame Obtained");
		// surround with try/catch

		System.out.println("Captured Frame Width " + frame.width());
		System.out.println("Captured Frame Height " + frame.height());

		// new FacePainterExample();

		// http://stackoverflow.com/questions/26672682/opencv-inputstream-to-mat-type-and-then-templatematching-works-with-jpg-but-n
		// see also:
		// http://stackoverflow.com/questions/23455095/how-to-create-mat-from-file-bytes
		// convert Mat to a bytes:
		// http://sumitkumariit.blogspot.com/2013/08/coverting-opencv-mat-to-bufferedimage.html

		// Mat image = Highgui.imdecode(frame, -1);
		// http://stackoverflow.com/questions/15835424/opencv-2-4-4-java-grab-webcam-picture-stream-osx
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(frame, faceDetections);

		for (Rect rect : faceDetections.toArray()) {
			Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));

			int mid_x = rect.x + (rect.width / 2);
			int mid_y = rect.y + (rect.height) / 2;

			System.out.println("x-mid: " + mid_x + ", y-mid: " + mid_y);
		}
	}
}
