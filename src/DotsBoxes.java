import java.awt.*;
import java.util.ArrayList;

public class DotsBoxes {
	//定义格子的二维数组
	private Box[][] boxesArray2;

	//定义游戏参与者的一维数组
	private IPlayer[] playersArray;
	//游戏参与者的数量
	private int playerCount;

	//控制游戏控制权在两个游戏参与者之间切换
	private int turn;

	private int[] scoreArray;

	//终止参
	private boolean terminated = false;

	//以下是显示部分的属性
	private Color[] colorArray;
	private int chessRows,chessCols;//棋盘行数 棋盘列数
	private int pixelMargin = 60;//画布预留上下左右边缘的像素数
	private int pixelRadius = 15;//绘制棋盘点的圆形的半径
	private int pixelEdgeHeiight = 10;//棋盘点之间连线部分的高度
	private int pixelEdgeWidth = 10;//棋盘点之间连线部分的宽度
	private int canvasPixelWidth;//画布像素宽度
	private int canvasPixelHeight;//画布像素高度
	private int horizPixelEdgeLen,vertPixelEdgeLens;//单条横边、直边的像素长度
	private int originLeftPixelX,originTopPixelY;//画图上棋盘左下角点的像素坐标值，从左到右Y值增加，自上到下X值增加

	private ArrayList<Dot> dotsList = new ArrayList<>();//棋盘所有点的Dot对象的列表，合计（chessRows+1）*（chessCols+ 1）个元素
	private ArrayList<Edge> horizonEdgesList = new ArrayList<>();//棋盘所有横向边的Edge对象的列表，合计合计（chessRows+1)*（chessCols）个元素
	private ArrayList<Edge> vertEdgesList = new ArrayList<>();//棋盘所有纵向边的Edge对象的列表，合计合计（chessRows）*(chessCols+1)个元素
	private ArrayList<FillRect> fillRectsList = new ArrayList<>();//棋盘格子的填充四边形的FillRect对象的列表，合计chessRows * chessCols个



	public DotsBoxes(int rows, int columns,int canvasPixelWidth,int canvasPixelHeight) {
		chessRows = rows;
		chessCols = columns;
		this.canvasPixelWidth = canvasPixelWidth;
		this.canvasPixelHeight = canvasPixelHeight;
		this.createGameBoxMatrix(chessRows,chessCols);

		playersArray = new IPlayer[2];
		scoreArray = new int[2];
		playerCount = 0;
		initialize();
		createDotsAndEdgesListMatrix(chessRows,chessCols);
	}

	public DotsBoxes(int rows,int columns,int canvasPixelWidth,int canvasPixelHeight,IPlayer player1,IPlayer player2){
		chessRows = rows;
		chessCols = columns;
		this.canvasPixelWidth = canvasPixelWidth;
		this.canvasPixelHeight = canvasPixelHeight;
		this.createGameBoxMatrix(chessRows,chessCols);

		playersArray = new IPlayer[2];
		scoreArray = new int[2];
		playerCount = 2;
		playersArray[0] = player1;
		playersArray[1] = player2;
		initialize();
		createDotsAndEdgesListMatrix(chessRows,chessCols);
	}
	//初始化
	private void initialize(){
		scoreArray[0] = 0;
		scoreArray[1] = 0;
		turn = 0;
		colorArray = new Color[2];
		colorArray[0] = Color.RED;
		colorArray[1] = Color.BLUE;

		StdDraw.setCanvasSize(canvasPixelWidth, canvasPixelHeight);
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, canvasPixelWidth);
		StdDraw.setYscale(0, canvasPixelHeight);
		horizPixelEdgeLen = (canvasPixelWidth - pixelMargin*2)/chessCols;
		vertPixelEdgeLens = (canvasPixelHeight - pixelMargin*2)/chessRows;
		originLeftPixelX = pixelMargin;
		originTopPixelY = canvasPixelHeight - pixelMargin;
	}

	private void createDotsAndEdgesListMatrix(int rows, int columns) {
		int r,c;
		int x,y;

		//生成点的Dot对象的列表
		y = originTopPixelY;
		for(r = 0;r < chessRows+ 1;++r,y -= vertPixelEdgeLens) {
			x = originLeftPixelX;
			for (c = 0; c < chessCols+ 1; ++c, x += horizPixelEdgeLen) {
				dotsList.add(new Dot(x, y, pixelRadius, Color.BLACK));//颜色可以定制
			}
		}

		int spacePixels = 1;
		//生成格子的FillRectangle对象的列表,绘制时上下左右留一个像素的间隔
		y = originTopPixelY - vertPixelEdgeLens + pixelEdgeHeiight / 2 + spacePixels;//纵向Y坐标需要向下挪动
		for(r = 0;r < chessRows;++r,y -= vertPixelEdgeLens) {//纵向Y坐标的步长，从上到下
			x = originLeftPixelX + pixelEdgeWidth / 2 + spacePixels;
			for (c = 0; c < chessCols; ++c, x += horizPixelEdgeLen) {
				fillRectsList.add(new FillRect(x,y,horizPixelEdgeLen - pixelEdgeWidth - spacePixels * 2,
						vertPixelEdgeLens - pixelEdgeHeiight - spacePixels * 2));
			}
		}

		//生成横向边的Edge对象的列表，存储横边从左到右，然后往下，然后从左到右，依此类推
		//设置的矩形原点坐标是在左上角，类似于二位数组
		y = originTopPixelY - pixelEdgeHeiight / 2;//需要向下调整边的四边形的高度的1/2
		for(r = 0;r < chessRows+1;++r,y -= vertPixelEdgeLens) {
			x = originLeftPixelX;//横向X坐标不需要调整
			for (c = 0; c < chessCols; ++c, x += horizPixelEdgeLen) {
				horizonEdgesList.add(new Edge(x,y,horizPixelEdgeLen,pixelEdgeHeiight));
			}
		}

		//生成纵向边的Edge对象的列表，存储纵边从上到下，然后往右，然后从上到下，依此类推
		//横向X坐标需要调整
		x = originLeftPixelX - pixelEdgeWidth / 2;
		for (c = 0; c < chessCols+ 1; ++c, x += horizPixelEdgeLen) {
			y = originTopPixelY - vertPixelEdgeLens;//纵向Y坐标需要调整
			for(r = 0;r < chessRows;++r,y -= vertPixelEdgeLens){
				vertEdgesList.add(new Edge(x,y,pixelEdgeWidth,vertPixelEdgeLens));
			}
		}
	}

	//根据格子棋盘对应的各个格子选择的边设置对应的边的画笔颜色
	//方法就是根据对应的格子的行列值去找对应的边对象的列表所在的索引值
	private void setBoxEdgeColorAndMarked(int indexX,int indexY,int selectedEdge){
		int edgeIndex;

		switch(selectedEdge) {
			case PlayerAction.TOP_EDGE: //水平线
				edgeIndex = indexX * (chessCols) + indexY;
				horizonEdgesList.get(edgeIndex).setColor(colorArray[turn]);
				horizonEdgesList.get(edgeIndex).setMarked(true);
				horizonEdgesList.get(edgeIndex).setVisible(true);
				break;
			case PlayerAction.BOTTOM_EDGE: //水平线
				edgeIndex = (indexX + 1) * (chessCols) + indexY;
				horizonEdgesList.get(edgeIndex).setColor(colorArray[turn]);
				horizonEdgesList.get(edgeIndex).setMarked(true);
				horizonEdgesList.get(edgeIndex).setVisible(true);
				break;
			case PlayerAction.LEFT_EDGE: //纵向线
				edgeIndex = indexX + indexY * (chessRows);
				vertEdgesList.get(edgeIndex).setColor(colorArray[turn]);
				vertEdgesList.get(edgeIndex).setMarked(true);
				vertEdgesList.get(edgeIndex).setVisible(true);
				break;
			case PlayerAction.RIGHT_EDGE: //纵向线
				edgeIndex =indexX + (indexY+1) * chessRows;
				vertEdgesList.get(edgeIndex).setColor(colorArray[turn]);
				vertEdgesList.get(edgeIndex).setMarked(true);
				vertEdgesList.get(edgeIndex).setVisible(true);
				break;
		}
	}

	private void setFillRectColorAndVisible(int indexX,int indexY){
		int edgeIndex = indexX * chessCols + indexY;

		fillRectsList.get(edgeIndex).setColor(colorArray[turn]);
		fillRectsList.get(edgeIndex).setVisible(true);
	}

	private void createGameBoxMatrix(int rows, int columns) {
		//创建格子二维棋盘		
		boxesArray2 = new Box[rows][columns];

		for(int r=0; r<rows; r++) {
			for(int c=0; c<columns; c++) {
				boxesArray2[r][c] = new Box();

				// 如格子存在相邻的上边格子或者左边格子,true表示相邻格子的信息也需要更新
				if(r>0)
					boxesArray2[r][c].setTopBox(boxesArray2[r-1][c], true);

				if(c>0)
					boxesArray2[r][c].setLeftBox(boxesArray2[r][c-1], true);
			}
		}
	}

	public void addPlayer(IPlayer player) {
		if(playerCount == 2)//已经有两个游戏者，替换数组2位置的游戏者
			--playerCount;
		playersArray[playerCount++]= player;
	}

	public IPlayer getPlayer(int index) {
		if(index < 0 || index >= 2)
			return null;
		return playersArray[index];
	}

	public int getPlayersCount() {
		return playerCount;
	}
	public int getScore(int index) {
		return scoreArray[index];
	}

	public String getWinnerName(){
		if(scoreArray[0] > scoreArray[1])
			return playersArray[0].getName();
		if(scoreArray[0] == scoreArray[1])
			return playersArray[0].getName() + " And " + playersArray[1].getName();
		return playersArray[1].getName();
	}

	public int getWinnerScore(){
		if(scoreArray[0] >= scoreArray[1])
			return scoreArray[0] ;
		return scoreArray[1];
	}

	//游戏逻辑
	public void play() {
		if(playerCount<2)//少于两个玩家退出
			return;

		//取得控制权的游戏参与者标注四边形的边，取得游戏参与者的游戏操作数据action
		PlayerAction action = playersArray[turn].play(this.getBoxesMatrix());
		//判断是不是人参加的
		if(action.getSelectedEdge() == -1 && action.getIndexX() == 0 && action.getIndexY() == 0){//以下是人参与游戏的逻辑
			action = proocessHumanAction(action.getPlayer());
			if(action == null)
				return;
		}

		//根据选边加格子
		switch(action.getSelectedEdge()) {
			case PlayerAction.TOP_EDGE:
				if(boxesArray2[action.getIndexX()][action.getIndexY()].isTopEdgeMarked()){
					return;
				}
				break;
			case PlayerAction.BOTTOM_EDGE:
				if(boxesArray2[action.getIndexX()][action.getIndexY()].isBottomEdgeMarked()){
					return;
				}
				break;
			case PlayerAction.LEFT_EDGE:
				if(boxesArray2[action.getIndexX()][action.getIndexY()].isLeftEdgeMarked()){
					return;
				}

				break;
			case PlayerAction.RIGHT_EDGE:
				if(boxesArray2[action.getIndexX()][action.getIndexY()].isRightEdgeMarked()){
					return;
				}
				break;
		}

 		boolean isCalculatedScore = false;
		//以下switch语句处理四边形各边的标注以及该四边形相邻的四边形的边的标注
		switch(action.getSelectedEdge()) {
			case PlayerAction.TOP_EDGE:
				//标注四边形的上边
				boxesArray2[action.getIndexX()][action.getIndexY()].setTopEdgeMarked(true, true);
				//设置对应边对象的画笔颜色和可见性
				setBoxEdgeColorAndMarked(action.getIndexX(),action.getIndexY(),action.getSelectedEdge());
				//判断当前格子是否有上部格子相邻,相应需要更新上部相邻格子的属性信息
				if(action.getIndexX()>0) {
					boxesArray2[action.getIndexX()-1][action.getIndexY()].setBottomEdgeMarked(true, true);
					//判断上部相邻格子是否四条边已经标注完成，并且是赢方信息为空的情况下，增加该赢方的得分
					if(boxesArray2[action.getIndexX()-1][action.getIndexY()].isAllSelected() &&
							boxesArray2[action.getIndexX()-1][action.getIndexY()].getWinner() == null) {
						boxesArray2[action.getIndexX()-1][action.getIndexY()].setWinner(action.getPlayer());
						scoreArray[turn]++;
						//填充四边形
						setFillRectColorAndVisible(action.getIndexX()-1,action.getIndexY());
						isCalculatedScore = true;
					}
				}
				break;
			case PlayerAction.BOTTOM_EDGE:
				//标注四边形的下边
				boxesArray2[action.getIndexX()][action.getIndexY()].setBottomEdgeMarked(true, true);
				//设置对应边对象的画笔颜色和可见性
				setBoxEdgeColorAndMarked(action.getIndexX(),action.getIndexY(),action.getSelectedEdge());
				//判断当前格子是否有下部格子相邻,相应需要更新部相邻格子的属性信息
				if(action.getIndexX()<boxesArray2.length-1) {
					boxesArray2[action.getIndexX()+1][action.getIndexY()].setTopEdgeMarked(true, true);
					//判断下部相邻格子是否四条边已经标注完成，并且是赢方信息为空的情况下，增加该赢方的得分
					if(boxesArray2[action.getIndexX()+1][action.getIndexY()].isAllSelected() &&
							boxesArray2[action.getIndexX()+1][action.getIndexY()].getWinner() == null) {
						boxesArray2[action.getIndexX()+1][action.getIndexY()].setWinner(action.getPlayer());
						scoreArray[turn]++;
						//填充四边形
						setFillRectColorAndVisible(action.getIndexX()+1,action.getIndexY());
						isCalculatedScore = true;
					}
				}
				break;
			case PlayerAction.LEFT_EDGE:
				//标注四边形的左边
				boxesArray2[action.getIndexX()][action.getIndexY()].setLeftEdgeMarked(true, true);
				//设置对应边对象的画笔颜色和可见性
				setBoxEdgeColorAndMarked(action.getIndexX(),action.getIndexY(),action.getSelectedEdge());
				//判断当前格子是否有左边格子相邻,相应需要更新左边相邻格子的属性信息
				if(action.getIndexY()>0) {
					boxesArray2[action.getIndexX()][action.getIndexY()-1].setRightEdgeMarked(true, true);
					//判断左边相邻格子是否四条边已经标注完成，并且是赢方信息为空的情况下，增加该赢方的得分
					if(boxesArray2[action.getIndexX()][action.getIndexY()-1].isAllSelected() &&
							boxesArray2[action.getIndexX()][action.getIndexY()-1].getWinner() == null) {
						boxesArray2[action.getIndexX()][action.getIndexY()-1].setWinner(action.getPlayer());
						scoreArray[turn]++;
						//填充四边形
						setFillRectColorAndVisible(action.getIndexX(),action.getIndexY()-1);
						isCalculatedScore = true;
					}
				}
				break;
			case PlayerAction.RIGHT_EDGE:
				//标注四边形的右边
				boxesArray2[action.getIndexX()][action.getIndexY()].setRightEdgeMarked(true, true);
				//设置对应边对象的画笔颜色和可见性
				setBoxEdgeColorAndMarked(action.getIndexX(),action.getIndexY(),action.getSelectedEdge());
				//判断当前格子是否有右边格子相邻,相应需要更新右边相邻格子的属性信息
				if(action.getIndexY()<boxesArray2[0].length-1) {
					boxesArray2[action.getIndexX()][action.getIndexY()+1].setLeftEdgeMarked(true, true);
					//判断右边相邻格子是否四条边已经标注完成，并且是赢方信息为空的情况下，增加该赢方的得分
					if(boxesArray2[action.getIndexX()][action.getIndexY()+1].isAllSelected() &&
							boxesArray2[action.getIndexX()][action.getIndexY()+1].getWinner() == null) {
						boxesArray2[action.getIndexX()][action.getIndexY()+1].setWinner(action.getPlayer());
						scoreArray[turn]++;
						//填充四边形
						setFillRectColorAndVisible(action.getIndexX(),action.getIndexY()+1);
						isCalculatedScore = true;
					}
				}
				break;
		}
		//判断当前格子是否已经标注完四条边，如果是，并且没有设置赢方信息的情况下，增加赢方得分并设置赢方信息
		if(boxesArray2[action.getIndexX()][action.getIndexY()].isAllSelected()){
			boxesArray2[action.getIndexX()][action.getIndexY()].setWinner(playersArray[turn]);
			scoreArray[turn]++;
			//填充四边形
			setFillRectColorAndVisible(action.getIndexX(),action.getIndexY());
			isCalculatedScore = true;
		}
		//如果当前游戏参与者标注了四边形的最后一条未标注的边，也就是这个四边形的赢方，该参与者不交换游戏控制权，否则轮换游戏参与者		
		if(!isCalculatedScore) {
			turn++;
			if(turn==playerCount)
				turn = 0;
		}


		//输出游戏信息
	}

	//判断游戏是否结束，只需要循环每个格子看看是不是都完成了
	public boolean isCompleted() {
		for (int i=0; i<boxesArray2.length; i++) {
			for(int j=0; j<boxesArray2[i].length; j++) {
				if(!boxesArray2[i][j].isAllSelected())
					return false;
			}
		}
		return true;
	}

	//复制格子棋盘数组
	public Box[][] getBoxesMatrix() {
		Box[][] tempBoxesArray2 = new Box[boxesArray2.length][boxesArray2[0].length];

		for(int i=0; i<boxesArray2.length; i++) {
			for(int j=0; j<boxesArray2[0].length; j++) {
				tempBoxesArray2[i][j] = new Box(boxesArray2[i][j]);

				//如格子存在相邻的上边格子或者左边格子,true表示相邻格子的信息也需要更新 links the box to its top box, and vice-versa
				if(i>0)
					tempBoxesArray2[i][j].setTopBox(tempBoxesArray2[i-1][j], true);
				if(j>0)
					tempBoxesArray2[i][j].setLeftBox(tempBoxesArray2[i][j-1], true);
			}
		}

		return tempBoxesArray2;
	}

	//实现绘图部分
	public void paint(){
		StdDraw.clear();
		StdDraw.picture(canvasPixelWidth/2.0,canvasPixelHeight/2.0,"Background.jpg",canvasPixelWidth,canvasPixelHeight);
		//绘制横向边
		horizonEdgesList.forEach(Edge::paint);
		//绘制纵向边
		vertEdgesList.forEach(Edge::paint);
		//填充已经选择好的四边形
		fillRectsList.forEach(FillRect::paint);
		//绘制点
		dotsList.forEach(Dot::paint);
		//输出相关信息
		StdDraw.setPenColor(Color.BLACK);
		Font font = new Font("Quicksand",Font.BOLD,15);
		StdDraw.setFont(font);
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.text(400,24,"Now it's " + playersArray[turn].getName() + "\'s turn.");
		StdDraw.text(400,9,"First-hand Player："+ playersArray[0].getName() +"，Score："
				+ scoreArray[0] + "    Second-hand Player："+ playersArray[1].getName() +"，Score："+ scoreArray[1] );
		StdDraw.show();
	}
//人落子步骤
	private PlayerAction proocessHumanAction(IPlayer player){

		PlayerAction action = null;

		int r,c,index;
		int selectedEdge;

		//没有终止的话，始终循环，除非用户选择退出或者标记了线
		while(!terminated){
			Point mousePoint = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
			boolean isMousePressed = StdDraw.isMousePressed();
			//点击水平边
			for (index = 0; index < horizonEdgesList.size(); ++index) {
				if(horizonEdgesList.get(index).getMarked())
					continue;
				//该边没有标记并且该边矩形包饱含鼠标
				if (horizonEdgesList.get(index).getBounds().contains(mousePoint)) {
					horizonEdgesList.get(index).setColor(colorArray[turn]);
					horizonEdgesList.get(index).setVisible(true);

					if(isMousePressed) {
						r = index / chessCols;
						c = index % chessCols;
						//判断是否最下边的边
						if (r == chessRows)
						{
							selectedEdge = PlayerAction.BOTTOM_EDGE;
							--r;
						}
						else
							selectedEdge = PlayerAction.TOP_EDGE;
						return new PlayerAction(player,r,c,selectedEdge);
					}
				}
				else
					horizonEdgesList.get(index).setVisible(false);
			}
			//点击竖直边
			for (index = 0; index < vertEdgesList.size(); ++index) {
				if(vertEdgesList.get(index).getMarked())
					continue;
				//该边没有标记并且该边矩形包饱含鼠标
				if (vertEdgesList.get(index).getBounds().contains(mousePoint)) {
					vertEdgesList.get(index).setColor(colorArray[turn]);
					vertEdgesList.get(index).setVisible(true);

					if(isMousePressed) {
						c = index / chessRows;
						r = index % chessRows;
						//System.out.println("0,size=" + vertEdgesList.size());
						//判断是否最下边的边
						if (c == chessCols)
						{
							selectedEdge = PlayerAction.RIGHT_EDGE;
							--c;
						}
						else
							selectedEdge = PlayerAction.LEFT_EDGE;
						return new PlayerAction(player,r,c,selectedEdge);
					}
				}
				else
					vertEdgesList.get(index).setVisible(false);
			}

			this.paint();
		}

		terminated = false;
		return action;
	}
}

