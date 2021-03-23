import java.awt.*;
//该类用来填充已经封闭的四边形，用某个指定给某个玩家的颜色
class FillRect {
    private Rectangle rect;//这个结构不是填充四边形，其中的想x,y属性是代填充四边形的中心位置，不是左上角位置，width height也是代填充四边形的长宽的一半
    private Color penColor=Color.BLACK;
    private boolean visible;//控制填充四边形显示与否

    public FillRect(int pixelX,int pixelY,int pixelWidth,int pixelHeight){
        rect = new Rectangle(pixelX,pixelY,pixelWidth,pixelHeight);
        visible = false;
    }

    public Rectangle getRectangle() {return rect;};
    public Color getColor(){return penColor;};
    public void setColor(Color color){penColor = color;};
    public void setVisible(boolean visible){
        this.visible =  visible;
    }

    public void paint(){
        if(!visible)
            return;
        //设置画笔颜色
        StdDraw.setPenColor(penColor);
        //填充四边形
        StdDraw.filledRectangle(rect.x + rect.width / 2.0,rect.y + rect.height / 2.0,rect.width / 2.0,rect.height / 2.0);
    }
}