package pharmacy.utils;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Duration;

public class NodeUtil {
	private NodeUtil() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	public static List<Node> findNodesWithClass(Node parent, String className) {
		List<Node> result = new ArrayList<>();

		if (parent.getStyleClass().contains(className)) {
			result.add(parent);
		}

		if (parent instanceof Parent) {
			for (Node child : ((Parent) parent).getChildrenUnmodifiable()) {
				result.addAll(findNodesWithClass(child, className));
			}
		}

		return result;
	}

	public static void addClass(Node node, String className) {
		if (!node.getStyleClass().contains(className)) {
			node.getStyleClass().add(className);
		}
	}

	public static void removeClass(Node node, String className) {
		node.getStyleClass().remove(className);
	}

	public static boolean hasClass(Node node, String className) {
		return node.getStyleClass().contains(className);
	}

	public static void applyTranslateXTransition(Node node, double fromX, double toX, double durationMillis,
			Runnable onFinished) {
		TranslateTransition translate = new TranslateTransition();
		translate.setNode(node);
		translate.setDuration(Duration.millis(durationMillis));
		translate.setFromX(fromX);
		translate.setToX(toX);
		translate.setOnFinished(event -> onFinished.run());
		translate.play();
	}

	public static void applyTranslateYTransition(Node node, double fromY, double toY, double durationMillis,
			Runnable onFinished) {
		TranslateTransition translate = new TranslateTransition();
		translate.setNode(node);
		translate.setDuration(Duration.millis(durationMillis));
		translate.setFromY(fromY);
		translate.setToY(toY);
		translate.setOnFinished(event -> onFinished.run());
		translate.play();
	}

	public static void applyScaleTransition(Node node, double fromX, double toX, double fromY, double toY,
			double durationMillis, Runnable onFinished) {
		ScaleTransition scaleTransition = new ScaleTransition();
		scaleTransition.setNode(node);
		scaleTransition.setDuration(Duration.millis(durationMillis));
		scaleTransition.setFromX(fromX);
		scaleTransition.setToX(toX);
		scaleTransition.setFromY(fromY);
		scaleTransition.setToY(toY);
		scaleTransition.setOnFinished(event -> onFinished.run());
		scaleTransition.play();
	}

	public static void applyFadeTransition(Node node, double fromOpacity, double toOpacity, double durationMillis,
			Runnable onFinished) {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setNode(node);
		fadeTransition.setDuration(Duration.millis(durationMillis));
		fadeTransition.setFromValue(fromOpacity);
		fadeTransition.setToValue(toOpacity);
		fadeTransition.setOnFinished(event -> onFinished.run());
		fadeTransition.play();
	}

	public static void applyRotateTransition(Node node, double fromAngle, double toAngle, double durationMillis,
			Runnable onFinished) {
		RotateTransition rotateTransition = new RotateTransition();
		rotateTransition.setNode(node);
		rotateTransition.setFromAngle(fromAngle);
		rotateTransition.setToAngle(toAngle);
		rotateTransition.setDuration(Duration.millis(durationMillis));
		rotateTransition.setOnFinished(event -> onFinished.run());
		rotateTransition.play();
	}

}