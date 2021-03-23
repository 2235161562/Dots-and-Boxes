import java.io.Serializable;
//提供一个游戏参与者的接口，凡是参加游戏的，无论是电脑还是人都需要实现这个接口
public interface IPlayer extends Serializable {
	//游戏参与者的名字
	public String getName();
	//在给定的点格棋盘中参与游戏，也就是标记其中某个四边形的某条未标记的边，返回这次标记的在点格棋盘二维矩阵的横纵索引位置和所选择的边
	public PlayerAction play(Box[][] Boxes);
}
