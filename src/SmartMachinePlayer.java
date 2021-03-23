public class SmartMachinePlayer implements IPlayer {

	private String m_name;

	public SmartMachinePlayer(String name) {
		m_name = name;
	}

	public String getName() {
		return m_name;
	}

	public PlayerAction play(Box[][] boxesArray2) {
		//对于格子棋盘的所有格子来说，边标注的情况存在0、1、2、3、4五种情况，0表示该格子没有任何边被标注，4表示4条边被标注
		boolean[] haveNumsOfSelectedEdge = new boolean[5];

		for (int i = 0; i < boxesArray2.length; i++) {
			for (int j = 0; j < boxesArray2[0].length; j++) {
				haveNumsOfSelectedEdge[boxesArray2[i][j].getSelectedEdgeNums()] = true;
			}
		}

		// 优先从已经标注3条边的四边形开始
		if(haveNumsOfSelectedEdge[3]) {
			if(haveNumsOfSelectedEdge[0] || haveNumsOfSelectedEdge[1]) {
				int maxIndexX = -1;
				int maxIndexY = -1;
				int maxMarkedEdgeCount = -1;
				for (int i = 0; i < boxesArray2.length; i++) {
					for (int j = 0; j < boxesArray2[0].length; j++) {
						// 选择三条边已经标注的格子，然后选择该格子的某条边，会是否能得到最多最快的封闭格子
						if(boxesArray2[i][j].getSelectedEdgeNums() == 3) {
							// 计算
							int markedEdgeCount = this.calculateMaxNumsOfMarkedEdge(boxesArray2[i][j]);
							if(markedEdgeCount>maxMarkedEdgeCount) {
								maxIndexX = i;
								maxIndexY = j;
								maxMarkedEdgeCount = markedEdgeCount;
							}
						}
					}
				}

				if(maxIndexX != -1) {//存在这样的格子
					if(!boxesArray2[maxIndexX][maxIndexY].isTopEdgeMarked())
						return new PlayerAction(this,maxIndexX,maxIndexY,PlayerAction.TOP_EDGE);
					if(!boxesArray2[maxIndexX][maxIndexY].isBottomEdgeMarked())
						return new PlayerAction(this,maxIndexX,maxIndexY,PlayerAction.BOTTOM_EDGE);
					if(!boxesArray2[maxIndexX][maxIndexY].isLeftEdgeMarked())
						return new PlayerAction(this,maxIndexX,maxIndexY,PlayerAction.LEFT_EDGE);
					if(!boxesArray2[maxIndexX][maxIndexY].isRightEdgeMarked())
						return new PlayerAction(this,maxIndexX,maxIndexY,PlayerAction.RIGHT_EDGE);
				}
			}
		}
		else {//如果存在选择0-2条边的情况
			if(haveNumsOfSelectedEdge[0] || haveNumsOfSelectedEdge[1]) {
				for (int i = 0; i < boxesArray2.length; i++) {
					for (int j = 0; j < boxesArray2[0].length; j++) {
						if(boxesArray2[i][j].getSelectedEdgeNums() == 0 || boxesArray2[i][j].getSelectedEdgeNums() == 1) {
							if(!boxesArray2[i][j].isTopEdgeMarked()) {
								Box topBox = boxesArray2[i][j].getTopBox();
								if(topBox == null || topBox.getSelectedEdgeNums() < 2) {
									return new PlayerAction(this,i,j,PlayerAction.TOP_EDGE);
								}
							}

							if(!boxesArray2[i][j].isBottomEdgeMarked()) {
								Box bottomBox = boxesArray2[i][j].getBottomBox();
								if(bottomBox == null || bottomBox.getSelectedEdgeNums() < 2) {
									return new PlayerAction(this,i,j,PlayerAction.BOTTOM_EDGE);
								}
							}

							if(!boxesArray2[i][j].isLeftEdgeMarked()) {
								Box leftBox = boxesArray2[i][j].getLeftBox();
								if(leftBox == null || leftBox.getSelectedEdgeNums() < 2) {
									return new PlayerAction(this,i,j,PlayerAction.LEFT_EDGE);
								}
							}

							if(!boxesArray2[i][j].isRightEdgeMarked()) {
								Box rightBox = boxesArray2[i][j].getRightBox();
								if(rightBox == null || rightBox.getSelectedEdgeNums() < 2) {
									return new PlayerAction(this,i,j,PlayerAction.RIGHT_EDGE);
								}
							}
						}
					}
				}
			}
		}

		//循环选取
		for (int i = 0; i < boxesArray2.length; i++) {
			for (int j = 0; j < boxesArray2[0].length; j++) {
				if(!boxesArray2[i][j].isTopEdgeMarked())
					return new PlayerAction(this,i,j,PlayerAction.TOP_EDGE);
				if(!boxesArray2[i][j].isBottomEdgeMarked())
					return new PlayerAction(this,i,j,PlayerAction.BOTTOM_EDGE);
				if(!boxesArray2[i][j].isLeftEdgeMarked())
					return new PlayerAction(this,i,j,PlayerAction.LEFT_EDGE);
				if(!boxesArray2[i][j].isRightEdgeMarked())
					return new PlayerAction(this,i,j,PlayerAction.RIGHT_EDGE);
			}
		}

		return new PlayerAction(this,0,0,PlayerAction.TOP_EDGE);
	}

	//该函数实现求取当前选择格子依次上下左右判断相邻四个格子能够得到的最多的标注边
	private int calculateMaxNumsOfMarkedEdge(Box box) {
		int count = 1;
		int lastMarkedEdge = -1;

		while(true) {
			if(!box.isBottomEdgeMarked() && lastMarkedEdge !=  PlayerAction.TOP_EDGE) {
				lastMarkedEdge = PlayerAction.BOTTOM_EDGE;
				box = box.getBottomBox();
				if(box == null)
					break;
				if(box.getSelectedEdgeNums() == 2 || box.getSelectedEdgeNums() == 3)
					count++;
				else
					break;
			}

			if(!box.isTopEdgeMarked() && lastMarkedEdge !=  PlayerAction.BOTTOM_EDGE) {
				lastMarkedEdge = PlayerAction.TOP_EDGE;
				box = box.getTopBox();
				if(box == null)
					break;
				if(box.getSelectedEdgeNums() == 2 || box.getSelectedEdgeNums() == 3)
					count++;
				else
					break;
			}

			if(!box.isLeftEdgeMarked() && lastMarkedEdge !=  PlayerAction.RIGHT_EDGE) {
				lastMarkedEdge = PlayerAction.LEFT_EDGE;
				box = box.getLeftBox();
				if(box == null)
					break;
				if(box.getSelectedEdgeNums() == 2 || box.getSelectedEdgeNums() == 3)
					count++;
				else
					break;
			}

			if(!box.isRightEdgeMarked() && lastMarkedEdge !=  PlayerAction.LEFT_EDGE) {
				lastMarkedEdge = PlayerAction.RIGHT_EDGE;
				box = box.getRightBox();
				if(box == null)
					break;
				if(box.getSelectedEdgeNums() == 2 || box.getSelectedEdgeNums() == 3)
					count++;
				else
					break;
			}
		}

		return count;
	}

}