import java.awt.*;

public class Dot {
    //画布canvas上的像素位置，以这个位置(x,y)为中心，radius为半径，绘出实心圆
    private int pixelX, pixelY;
    private int radius;
    private Color color = Color.BLACK;

    public Dot(int pixelX, int pixelY, int radius,Color color) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.radius = radius;
        this.color = color;
    }

    public void paint() {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(pixelX, pixelY, radius);
    }
}
