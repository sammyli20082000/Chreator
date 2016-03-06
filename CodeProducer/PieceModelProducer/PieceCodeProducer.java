package Chreator.CodeProducer.PieceModelProducer;

/**
 * @author SLi
 * This is used to produce abstract class Piece.java in the chess playing engine (Executable)
 */
public class PieceCodeProducer {
	
	public PieceCodeProducer() { 
		
	}
	
	public void printPieceJavaFrameCode() {
		PieceModelProducer.pieceCodes.add("package Executable.PieceModel;");
		
		PieceModelProducer.pieceCodes.add("import java.awt.image.BufferedImage;");
		PieceModelProducer.pieceCodes.add("import java.io.File;");
		PieceModelProducer.pieceCodes.add("import java.io.Serializable;");
		PieceModelProducer.pieceCodes.add("import java.util.ArrayList;");
		
		PieceModelProducer.pieceCodes.add("import javax.imageio.ImageIO;");
		
		PieceModelProducer.pieceCodes.add("import Executable.BoardModel.Point;");
		
		PieceModelProducer.pieceCodes.add("public abstract class Piece implements Serializable {");
		
		PieceModelProducer.pieceCodes.add("private static int idCounter=0;");
		PieceModelProducer.pieceCodes.add("private int id;");
		PieceModelProducer.pieceCodes.add("private String name;");
		PieceModelProducer.pieceCodes.add("private String side;");
		PieceModelProducer.pieceCodes.add("BufferedImage pieceImage;");
		PieceModelProducer.pieceCodes.add("private String imageLink;");
		PieceModelProducer.pieceCodes.add("private double height, width; // 0 to 1");
		
		PieceModelProducer.pieceCodes.add("public Piece(String side, String imageLink, double w, double h, String name) {");
		PieceModelProducer.pieceCodes.add("id = idCounter;");
		PieceModelProducer.pieceCodes.add("idCounter++;");
		PieceModelProducer.pieceCodes.add("this.imageLink = imageLink;");
		PieceModelProducer.pieceCodes.add("this.side = side;");
		PieceModelProducer.pieceCodes.add("height = h;");
		PieceModelProducer.pieceCodes.add("width = w;");
		PieceModelProducer.pieceCodes.add("this.name = name;");
		PieceModelProducer.pieceCodes.add("try {");
		PieceModelProducer.pieceCodes.add("pieceImage = ImageIO.read(new File(imageLink));");
		PieceModelProducer.pieceCodes.add("} catch (Exception e) {");
		PieceModelProducer.pieceCodes.add("}");
		PieceModelProducer.pieceCodes.add("}");
		
		PieceModelProducer.pieceCodes.add("public BufferedImage getPieceImage() {");
		PieceModelProducer.pieceCodes.add("return pieceImage;");
		PieceModelProducer.pieceCodes.add("}");
		
		PieceModelProducer.pieceCodes.add("public String getSide() {");
		PieceModelProducer.pieceCodes.add("return side;");
		PieceModelProducer.pieceCodes.add("}");
		
		PieceModelProducer.pieceCodes.add("public double getHeight() {");
		PieceModelProducer.pieceCodes.add("return height;");
		PieceModelProducer.pieceCodes.add("}");
		
		PieceModelProducer.pieceCodes.add("public double getWidth() {");
		PieceModelProducer.pieceCodes.add("return width;");
		PieceModelProducer.pieceCodes.add("}");
		
		PieceModelProducer.pieceCodes.add("public String getId() {");
		PieceModelProducer.pieceCodes.add("return id;");
		PieceModelProducer.pieceCodes.add("}");
		
		PieceModelProducer.pieceCodes.add("public String getImageLink() {");
		PieceModelProducer.pieceCodes.add("return imageLink;");
		PieceModelProducer.pieceCodes.add("}");
		
		PieceModelProducer.pieceCodes.add("public String getName(){");
		PieceModelProducer.pieceCodes.add("return name;");
		PieceModelProducer.pieceCodes.add("}");
		
		PieceModelProducer.pieceCodes.add("public abstract ArrayList<Point> moveInvolvingOtherPiece(Point p);");
		
		PieceModelProducer.pieceCodes.add("}");
	}
}
