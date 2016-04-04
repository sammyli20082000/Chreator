package Chreator.CodeProducer.PieceModelProducer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import Chreator.CodeProducer.CodeProducer;
import Chreator.ObjectModel.PieceProfile;

public class IndividualPieceCodeProducer {

	PieceProfile pieceProfile;

	public IndividualPieceCodeProducer(PieceProfile pieceProfile) {
		this.pieceProfile = pieceProfile;
	}

	public void printIndividualPieceJava() {
		makePieceImage(pieceProfile.sourcePicLink, pieceProfile.playerSide, pieceProfile.pieceClassName,
				pieceProfile.pieceColor);
		PieceModelProducer.individualPieceCodes.add(printIndividualPieceJavaFrame(pieceProfile));
	}

	private List<String> printIndividualPieceJavaFrame(PieceProfile pieceProfile) {
		List<String> indiPieceCode = new LinkedList<>();
		
		String[] tempCode = pieceProfile.code.split("\n");
		for (int i = 0; i < tempCode.length; i++) {
			indiPieceCode.add(tempCode[i]);
		}
			
		return indiPieceCode;
	}

	private void makePieceImage(String imageLink, String playerSide, String pieceClassName, Color pieceColor) {
		File picF;
		if (!imageLink.equals("")) {
			File file = new File(imageLink);
			picF = new File(CodeProducer.pathname + "\\pic\\" + file.getName());
			try {
				Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(picF.getAbsolutePath()),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {

			}
		} else {
			picF = new File(CodeProducer.pathname + "\\pic\\" + playerSide + "_" + pieceClassName + ".png");
			BufferedImage bImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = bImage.createGraphics();

			graphics.setComposite(AlphaComposite.Clear);
			graphics.fillRect(0, 0, 200, 200);
			graphics.setComposite(AlphaComposite.Src);
			graphics.setColor(pieceColor);
			graphics.fillOval(0, 0, 200, 200);
			graphics.setColor(Color.BLACK);
			graphics.drawString(pieceClassName, (200 - graphics.getFontMetrics().stringWidth(pieceClassName)) / 2,
					(200 + graphics.getFont().getSize()) / 2);
			graphics.dispose();
			try {
				ImageIO.write(bImage, "png", picF);
			} catch (Exception e) {

			}
		}
	}
}
