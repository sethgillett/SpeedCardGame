package graphics;

import processing.core.PImage;

public class SpriteSheet {
    private PImage img;
    private final int cellW, cellH;
    private final int nCellsX, nCellsY;

    public SpriteSheet(PImage img, int nCellsX, int nCellsY) {
	// The entire sprite sheet
	this.img = img;
	// The width and height of cells found by
	this.cellW = img.width / nCellsX;
	this.cellH = img.height / nCellsY;
	this.nCellsX = nCellsX;
	this.nCellsY = nCellsY;
    }

    public PImage getSprite(int xCell, int yCell) {
	// Return an individual sprite from
	return img.get(xCell * cellW, yCell * cellH, cellW, cellH);
    }

    public int nCellsX() {
	return nCellsX;
    }

    public int nCellsY() {
	return nCellsY;
    }

    public int getCellW() {
	return cellW;
    }

    public int getCellH() {
	return cellH;
    }
}
