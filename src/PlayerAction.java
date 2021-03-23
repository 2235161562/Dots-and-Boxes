//游戏进程数据记录类，在这个类中确定用户标注的线
//是二维棋盘的那个格子以及标注的是哪边的线以及是
//哪个游戏参与者标注的
//游戏进程数据记录类，在这个类中确定用户标注的线
//是二维棋盘的那个格子以及标注的是哪边的线以及是
//哪个游戏参与者标注的

import java.io.Serializable;

public class PlayerAction implements Serializable {

	//边常数定义，上边 下边
	final static public int TOP_EDGE = 0,BOTTOM_EDGE = 1;
	//边常数定义,左边 右边
	final static public int LEFT_EDGE = 2,RIGHT_EDGE = 3;

	//选择的边处于格子棋盘的二维数组的行列索引值
	private int m_indexX,m_indexY;

	//当前选择的线
	private int m_selectedEdge;

	//游戏参与者，以接口形式提供
	private IPlayer m_player;

	//构造函数
	public PlayerAction(IPlayer player, int indexX, int indexY, int selectedEdge) {
		m_player = player;
		m_indexX = indexX;
		m_indexY = indexY;
		m_selectedEdge = selectedEdge;
	}

	public PlayerAction(){
		m_player = null;
		m_indexX = -1;
		m_indexY = -1;
		m_selectedEdge = -1;
	}

	public PlayerAction(PlayerAction playerAction){
		m_player = playerAction.m_player;
		m_indexX = playerAction.m_indexX;
		m_indexY = playerAction.m_indexY;
		m_selectedEdge = playerAction.m_selectedEdge;
	}

	public IPlayer getPlayer() {
		return m_player;
	}

	public int getSelectedEdge() {
		return m_selectedEdge;
	}

	public int getIndexX() {
		return m_indexX;
	}

	public int getIndexY() {
		return m_indexY;
	}

	public void setPlayer(IPlayer player) {
		m_player = player;
	}

	public void setSelectedEdge(int selectedEdge) {
		m_selectedEdge = selectedEdge;
	}

	public void setIndexX(int indexX) {
		m_indexX = indexX;
	}

	public void setIndexY(int indexY) {
		m_indexY = indexY;
	}
}
