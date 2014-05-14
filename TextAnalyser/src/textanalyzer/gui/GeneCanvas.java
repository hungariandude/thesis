package textanalyzer.gui;

import textanalyzer.logic.algorithm.Gene;
import textanalyzer.logic.algorithm.Shape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GeneCanvas extends JPanel {

    private static final long serialVersionUID = 1683037721853854499L;

    private static final Dimension DEFAULT_SIZE = new Dimension(128, 128);
    private static final Border DEFAULT_BORDER = new LineBorder(Color.BLACK, 1);

    private Gene gene;

    public GeneCanvas(Gene gene) {
	super();
	this.gene = gene;

	setPreferredSize(DEFAULT_SIZE);
	setBorder(DEFAULT_BORDER);
	setBackground(Color.WHITE);

	setToolTipText(gene.toString());
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;
	g2d.setColor(Color.BLACK);

	Path2D fullPath = new Path2D.Double();

	for (Shape shape : gene.getBuildingElements()) {
	    Path2D path = shape.toPath2D();
	    Point2D lastPoint = fullPath.getCurrentPoint();
	    if (lastPoint != null) {
		AffineTransform at = new AffineTransform();
		// at.scale(10, 10);
		// path.transform(at);
		// at = new AffineTransform();
		at.translate(lastPoint.getX(), lastPoint.getY());
		path.transform(at);
	    }
	    fullPath.append(path, false);
	}

	Rectangle bounds = fullPath.getBounds();
	if (bounds.x < 0 || bounds.y < 0) {
	    double transX = bounds.x < 0 ? -bounds.x : 0.0;
	    double transY = bounds.y < 0 ? -bounds.y : 0.0;
	    AffineTransform at = new AffineTransform();
	    at.translate(transX, transY);
	    fullPath.transform(at);
	}

	double scaleX = (double) DEFAULT_SIZE.width / bounds.width;
	double scaleY = (double) DEFAULT_SIZE.height / bounds.height;
	double scale = Math.min(scaleX, scaleY);
	AffineTransform at = new AffineTransform();
	at.scale(scale, scale);
	fullPath.transform(at);

	g2d.draw(fullPath);
    }
}
