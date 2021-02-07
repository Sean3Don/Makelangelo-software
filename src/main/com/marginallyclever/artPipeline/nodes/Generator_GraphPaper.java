package com.marginallyclever.artPipeline.nodes;

import com.marginallyclever.artPipeline.Node;
import com.marginallyclever.artPipeline.NodePanel;
import com.marginallyclever.artPipeline.nodeConnector.NodeConnectorDouble;
import com.marginallyclever.artPipeline.nodeConnector.NodeConnectorInt;
import com.marginallyclever.artPipeline.nodeConnector.NodeConnectorTurtle;
import com.marginallyclever.artPipeline.nodes.panels.Generator_GraphPaper_Panel;
import com.marginallyclever.convenience.Clipper2D;
import com.marginallyclever.convenience.Point2D;
import com.marginallyclever.convenience.turtle.Turtle;
import com.marginallyclever.makelangelo.Translator;
import com.marginallyclever.makelangeloRobot.MakelangeloRobotPanel;

/**
 * 1cm and 10cm grid lines
 * @author Dan Royer
 */
public class Generator_GraphPaper extends Node {
	// controls complexity of curve
	private NodeConnectorDouble spacing_mm = new NodeConnectorDouble(10.0);
	// result
	private NodeConnectorTurtle outputTurtle = new NodeConnectorTurtle();
	
	
	public Generator_GraphPaper() {
		super();
		inputs.add(spacing_mm);
		outputs.add(outputTurtle);
	}

	@Override
	public String getName() {
		return Translator.get("GraphPaperName");
	}

	@Override
	public NodePanel getPanel() {
		return new Generator_GraphPaper_Panel(this);
	}
	
	@Override
	public boolean iterate() {
		Turtle turtle = new Turtle();
		
		lines(turtle,spacing_mm.getValue(),0);
		lines(turtle,spacing_mm.getValue(),90);

		outputTurtle.setValue(turtle);
	    return false;
	}

	protected void lines(Turtle turtle,double stepSize_mm,int angle_deg) {
		double majorX = Math.cos(Math.toRadians(angle_deg));
		double majorY = Math.sin(Math.toRadians(angle_deg));

		// from top to bottom of the margin area...
		double yBottom = -100;
		double yTop    = 100;
		double xLeft   = -100;
		double xRight  = 100;
		double dy = (yTop - yBottom)/2;
		double dx = (xRight - xLeft)/2;
		double radius = Math.sqrt(dx*dx+dy*dy);

		Point2D P0=new Point2D();
		Point2D P1=new Point2D();

		Point2D rMax = new Point2D(xRight,yTop);
		Point2D rMin = new Point2D(xLeft,yBottom);
		
		int i=0;
		for(double a = -radius;a<radius;a+=stepSize_mm) {
			double majorPX = majorX * a;
			double majorPY = majorY * a;
			P0.set( majorPX - majorY * radius,
					majorPY + majorX * radius);
			P1.set( majorPX + majorY * radius,
					majorPY - majorX * radius);
			if(Clipper2D.clipLineToRectangle(P0, P1, rMax, rMin)) {
				if ((i % 2) == 0) 	{
					turtle.moveTo(P0.x,P0.y);
					turtle.penDown();
					turtle.moveTo(P1.x,P1.y);
					turtle.penUp();
				} else {
					turtle.moveTo(P1.x,P1.y);
					turtle.penDown();
					turtle.moveTo(P0.x,P0.y);
					turtle.penUp();
				}
			}
			++i;
		}
	}
}
