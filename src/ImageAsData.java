import processing.core.*;

/**
 * ImageAsData is a subclass of Processing applet. 
 *     -It will initialize the applet. 
 *     -Shapes and text are drawn on the screen. 
 *     -Mouse and keyboard input are read in. 
 *  Note: Lab adapted from the book Generative Design (example P.4.3.1).
 */
public class ImageAsData extends PApplet {
    private static final int HEIGHT = 400;
    private static final int WIDTH = 400;
    private PImage img;
    private int drawMode = 1;
    private int[][] pixelArray; // 2D array to hold pixel values

    /**
     * Launch the Processing Application. Calls settings() once, then setup()
     * once, then draw() 30 times per second.
     * 
     * @param args - Program arguments are ignored.
     */
    public static void main(String args[]) {
        String packageFilename = "ImageAsData";
        PApplet.main(new String[] { packageFilename });
    }

    /**
     * Sets the Application Properties.
     */
    public void settings() {
        size(WIDTH, HEIGHT); // Set size of screen
    }

    /**
     * Called once at the start
     */
    public void setup() {
        smooth();
        // TODO: Try other images.
        img = loadImage("frog.jpg");

        // Create and initialize the 2D array from individual pixels
        // in the image. Note that img.pixels is a 2D array - it contains
        // all the rows of the images strung in a long line, one row after an
        // other.
        pixelArray = new int[img.width][img.height];
        for (int i = 0; i < img.width; i++) {
            for (int j = 0; j < img.height; j++) {
                pixelArray[i][j] = img.pixels[j * img.width + i];
            }
        }
    }

    /**
     * Called once per frame until program exits.
     */
    public void draw() {
        background(255);

        float mouseXFactor = map(mouseX, 0, width, 0.05f, 1);
        float mouseYFactor = map(mouseY, 0, height, 0.05f, 1);

        for (int gridX = 0; gridX < img.width; gridX++) {
            for (int gridY = 0; gridY < img.height; gridY++) {
                // grid position + tile size
                float tileWidth = width / (float) img.width;
                float tileHeight = height / (float) img.height;
                float posX = tileWidth * gridX;
                float posY = tileHeight * gridY;

                // get current color
                int c = pixelArray[gridX][gridY];

                // greyscale conversion
                int greyscale = round(red(c) * 0.222f + green(c) * 0.707f + blue(c) * 0.071f);

                if (drawMode == 1) {
                    // greyscale to ellipse area
                    fill(0);
                    noStroke();
                    float r2 = 1.1284f * sqrt(tileWidth * tileWidth * (1 - greyscale / 255.0f));
                    r2 = r2 * mouseXFactor * 3;
                    ellipse(posX, posY, r2, r2);
                } else if (drawMode == 2) {
                    // greyscale to rotation, line length and stroke weight
                    stroke(0);
                    float w4 = map(greyscale, 0, 255, 10, 0);
                    strokeWeight(w4 * mouseXFactor + 0.1f);
                    float l4 = map(greyscale, 0, 255, 35, 0);
                    l4 = l4 * mouseYFactor;
                    pushMatrix();
                    translate(posX, posY);
                    rotate(greyscale / 255.0f * PI);
                    line(0, 0, 0 + l4, 0 + l4);
                    popMatrix();
                } else if (drawMode == 3) {
                    // greyscale to line relief
                    float w5 = map(greyscale, 0, 255, 5, 0.2f);
                    strokeWeight(w5 * mouseYFactor + 0.1f);
                    // get neighbour pixel, limit it to image width
                    int /* color */ c2 = img.get(min(gridX + 1, img.width - 1), gridY);
                    stroke(c2);
                    int greyscale2 = (int) (red(c2) * 0.222f + green(c2) * 0.707f + blue(c2) * 0.071f);
                    float h5 = 50 * mouseXFactor;
                    float d1 = map(greyscale, 0, 255, h5, 0);
                    float d2 = map(greyscale2, 0, 255, h5, 0);
                    line(posX - d1, posY + d1, posX + tileWidth - d2, posY + d2);
                } else if (drawMode == 4) {
                    // pixel color to fill, greyscale to ellipse size
                    float w6 = map(greyscale, 0, 255, 25, 0);
                    noStroke();
                    fill(c);
                    ellipse(posX, posY, w6 * mouseXFactor, w6 * mouseXFactor);
                    // TODO 2: Add the following: if(Math.random()<0.1) {pixelArray[gridX][gridY] = lerpColor(c,0xff0000, 0.1f);}
                } else if (drawMode == 5) {
                    stroke(c);
                    float w7 = map(greyscale, 0, 255, 5, 0.1f);
                    strokeWeight(w7);
                    fill(255, 255 * mouseXFactor);
                    pushMatrix();
                    translate(posX, posY);
                    rotate(greyscale / 255.0f * PI * mouseYFactor);
                    rect(0, 0, 15, 15);
                    popMatrix();
                }
                /*
                 * TODO 3: Add a drawMode == 6 case. 
                 *     Try modifying the pixelArray 
                 *     Try drawing shapes or colors depending on c and grayscale variables 
                 *     Try adding other forms of animation
                 */
            }
        }
    }

    /**
     * Handle Key release (Up)
     */
    public void keyReleased() {
        if (key == '1')
            drawMode = 1;
        if (key == '2')
            drawMode = 2;
        if (key == '3')
            drawMode = 3;
        if (key == '4')
            drawMode = 4;
        if (key == '5')
            drawMode = 5;
    }
}