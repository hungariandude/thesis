package hu.thesis.shorthand.textanalyzer.gui.ga;

import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.textanalyzer.logic.Parameters;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Gene;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Segment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * A gént kirajzoló vászon.
 * 
 * @author Istvánfi Zsolt
 */
public class GeneCanvas extends JPanel {

    static class Arrow {
	Path2D linePart;
	Path2D headPart;

	/**
	 * Deep copy constructor.
	 */
	Arrow(Arrow sample) {
	    this.linePart = new Path2D.Double(sample.linePart);
	    this.headPart = new Path2D.Double(sample.headPart);
	}

	Arrow(Path2D linePart, Path2D headPart) {
	    this.linePart = linePart;
	    this.headPart = headPart;
	}

	void draw(Graphics2D g2d) {
	    g2d.draw(linePart);
	    g2d.fill(headPart);
	}

	void transform(AffineTransform at) {
	    this.linePart.transform(at);
	    this.headPart.transform(at);
	}
    }

    private static final long serialVersionUID = 1683037721853854499L;

    private static final Dimension DEFAULT_SIZE = new Dimension(128, 128);
    private static final Border DEFAULT_BORDER = new LineBorder(Color.BLACK, 1);
    private static final Arrow DEFAULT_ARROW_CENTER;
    private static final Arrow DEFAULT_ARROW_ABOVE;
    private static final Arrow DEFAULT_ARROW_BELOW;
    static {
	Path2D line = new Path2D.Double();
	line.moveTo(0.4, 0.0);
	line.lineTo(0.55, 0.0);
	Path2D head = new Path2D.Double();
	head.moveTo(0.55, 0.05);
	head.lineTo(0.6, 0.0);
	head.lineTo(0.55, -0.05);
	head.closePath();

	DEFAULT_ARROW_CENTER = new Arrow(line, head);
	DEFAULT_ARROW_ABOVE = new Arrow(DEFAULT_ARROW_CENTER);
	AffineTransform at = new AffineTransform();
	at.translate(0.0, 0.2);
	DEFAULT_ARROW_ABOVE.transform(at);
	DEFAULT_ARROW_BELOW = new Arrow(DEFAULT_ARROW_CENTER);
	at = new AffineTransform();
	at.translate(0.0, -0.2);
	DEFAULT_ARROW_BELOW.transform(at);
    }

    private Path2D path;
    private Point2D pathStartingPoint;
    private List<Arrow> arrows;

    public GeneCanvas() {
	setPreferredSize(DEFAULT_SIZE);
	setBorder(DEFAULT_BORDER);
	setBackground(Color.WHITE);
    }

    public GeneCanvas(Gene gene) {
	this();
	setGene(gene);
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;

	if (path != null) {
	    g2d.setColor(Color.BLACK);
	    g2d.fillOval((int) pathStartingPoint.getX() - 2,
		    (int) pathStartingPoint.getY() - 2, 4, 4);
	    g2d.draw(path);
	}
	if (arrows != null) {
	    for (Arrow arrow : arrows) {
		arrow.draw(g2d);
	    }
	}
    }

    public void setGene(final Gene gene) {
	if (Parameters.debugMode) {
	    setToolTipText(gene.toString());
	}

	List<Arrow> newArrows = new ArrayList<>();
	Path2D newPath = new Path2D.Double();

	for (Segment segment : gene.getSegments()) {
	    // másoljuk
	    Path2D pathPart = new Path2D.Double(segment);
	    Point2D lastPoint = newPath.getCurrentPoint();
	    Arrow arrow = segment.getForm() == Form.LINE ? new Arrow(
		    DEFAULT_ARROW_CENTER)
		    : (segment.getForm() == Form.SAG_CURVE ? new Arrow(
			    DEFAULT_ARROW_BELOW) : new Arrow(
			    DEFAULT_ARROW_ABOVE));
	    // elforgatjuk a nyilacskát is
	    AffineTransform at = new AffineTransform();
	    at.rotate(Math.toRadians(segment.getRotation().getDegrees()));
	    arrow.transform(at);
	    newArrows.add(arrow);
	    if (lastPoint != null) {
		at = new AffineTransform();
		at.translate(lastPoint.getX(), lastPoint.getY());
		pathPart.transform(at);
		arrow.transform(at);
	    }
	    newPath.append(pathPart, false);
	}

	AffineTransform at = new AffineTransform();
	at.scale(64, -64); // tükrözünk az y tengely mentén
	newPath.transform(at);
	for (Arrow arrow : newArrows) {
	    arrow.transform(at);
	}

	Rectangle bounds = newPath.getBounds();
	double dx = (DEFAULT_SIZE.width - bounds.width) / 2.0;
	double dy = (DEFAULT_SIZE.height - bounds.height) / 2.0;
	if (bounds.x < 0) {
	    dx += -bounds.x;
	}
	if (bounds.y < 0) {
	    dy += -bounds.y;
	}
	at = new AffineTransform();
	at.translate(dx, dy); // középre igazítjuk az alakzatot
	newPath.transform(at);
	for (Arrow arrow : newArrows) {
	    arrow.transform(at);
	}

	pathStartingPoint = new Point2D.Double(dx, dy);
	path = newPath;
	arrows = newArrows;
	repaint();
    }
}
