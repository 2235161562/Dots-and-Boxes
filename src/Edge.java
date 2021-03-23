import java.awt.*;

public class Edge {
    private int pixelX, pixelY;
    private int pixelWidth, pixelHeight;

    private Color color = Color.BLACK;
    private boolean visible = false;
    private boolean marked = false;

    public Edge(int pixelX, int pixelY, int pixelWidth, int m_pixelHeight) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = m_pixelHeight;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

    public Color getColor() {  return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
    public boolean getMarked() {return marked;}

    public Rectangle getBounds() {
        return new Rectangle(pixelX, pixelY, pixelWidth, pixelHeight);
    }

    public void paint() {
        if (!isVisible()) {
            return;
        }
        //以（pixelX，pixelY）为中心，填充四边形
        boolean isHorizontal = getPixelWidth() > getPixelHeight();
        //如果是水平线，绘图原点的X坐标就增加width/2，高度不变
        if(marked) {//如果已经标记
            StdDraw.setPenColor(this.color);
            StdDraw.filledRectangle(pixelX + pixelWidth / 2.0, pixelY + pixelHeight / 2.0,
                    pixelWidth / 2.0, pixelHeight / 2.0);
        } else {//没有标记，只是鼠标移动到了该边的区域内
            int midValue = (isHorizontal ? pixelHeight : pixelWidth) / 2;
            int alphaStep = 255 / midValue;
            for (int i = 0; i < midValue; i+=1) {
                StdDraw.setPenColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - alphaStep * i));
                if(isHorizontal) {
                    StdDraw.filledRectangle(pixelX + pixelWidth / 2.0, pixelY + pixelHeight / 2.0 + i, pixelWidth / 2.0, i);
                    StdDraw.filledRectangle(pixelX + pixelWidth / 2.0, pixelY + pixelHeight / 2.0 - i, pixelWidth / 2.0, i);
                } else {
                    StdDraw.filledRectangle(pixelX + pixelWidth / 2.0 + i, pixelY + pixelHeight / 2.0, i,pixelHeight / 2.0);
                    StdDraw.filledRectangle(pixelX + pixelWidth / 2.0 - i, pixelY + pixelHeight / 2.0, i,pixelHeight / 2.0);
                }
            }
        }
    }
}
