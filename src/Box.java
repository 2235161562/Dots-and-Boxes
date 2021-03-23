/*
//点格棋盘游戏是以四边形为基础的游戏，四边形的
//四条边可以选中标注，四条边如果都被选中，选中
//最后一条边的游戏参与者就得分，并可以继续游戏，
//不切换游戏参与者，并在棋盘中标注胜者的名字或者
//颜色以示区分，每个格子都有上下左右四条边，也
//可能存在上下左右四个相邻的格子，至少两个格子
//是相邻的，如果当前格子处在格子棋盘的边缘的话
 */
//该类将实现对象的序列化，以便保存对象到文件
//和从文件中读取

public class Box{

	//选中该格子未被标注的最后一条边的游戏参与者，是以接口形式实现，以体现
	//机器参与者和人参与者的差异
	private IPlayer winner;
	//是否标注了该格子的上下左右四条边
	private boolean isTopEdgeMarked,isBottomEdgeMarked,isLeftEdgeMarked,isRightEdgeMarked;
	//该格子相邻的上下左右四个格子对象，如果没有相邻的，该对象赋值NULL
	private Box topBox,bottomBox,leftBox,rightBox;

	//格子构造函数
	public Box() {
	}
	//格子的复制构造函数
	public Box(Box box) {
		//复制被复制格子的每个对应格子相邻的上面格子的链接信息topBox，
		//并设置该相邻格子的对应链接信息bottomBox
		this.setTopBox(box.getTopBox(), true);

		//复制被复制格子的每个对应格子相邻的上面格子的链接信息bottomBox，
		//并设置该相邻格子的对应链接信息topBox
		this.setBottomBox(box.getBottomBox(), true);

		//复制被复制格子的每个对应格子相邻的上面格子的链接信息leftBox，
		//并设置该相邻格子的对应链接信息rightBox
		this.setLeftBox(box.getLeftBox(),true);

		//复制被复制格子的每个对应格子相邻的上面格子的链接信息rightBox，
		//并设置该相邻格子的对应链接信息leftBox
		this.setRightBox(box.getRightBox(), true);

		//复制被复制格子的对应格子的isTopEdgeMarked属性，并设置当前格子相邻上边格子
		//topBox的对应格子的isBottomEdgeMarked属性
		this.setTopEdgeMarked(box.isTopEdgeMarked(), true);

		//复制被复制格子的对应格子的isBottomEdgeMarked属性，并设置当前格子相邻上边格子
		//topBox的对应格子的isTopEdgeMarked属性
		this.setBottomEdgeMarked(box.isBottomEdgeMarked(), true);

		//复制被复制格子的对应格子的isLeftEdgeMarked属性，并设置当前格子相邻上边格子
		//topBox的对应格子的isRightEdgeMarked属性
		this.setLeftEdgeMarked(box.isLeftEdgeMarked(), true);

		//复制被复制格子的对应格子的isRightEdgeMarked属性，并设置当前格子相邻上边格子
		//topBox的对应格子的isLeftEdgeMarked属性
		this.setRightEdgeMarked(box.isRightEdgeMarked(), true);

		//复制参与游戏者的赢方对象信息
		this.setWinner(box.getWinner());
	}

	//返回格子的上边的选中标注与否
	public boolean isTopEdgeMarked() {
		return isTopEdgeMarked;
	}
	//返回格子的下边的选中标注与否
	public boolean isBottomEdgeMarked() {
		return isBottomEdgeMarked;
	}
	//返回格子的左边的选中标注与否
	public boolean isLeftEdgeMarked() {
		return isLeftEdgeMarked;
	}
	//返回格子的右边的选中标注与否
	public boolean isRightEdgeMarked() {
		return isRightEdgeMarked;
	}
	//返回格子的某边的选中标注与否
	public boolean isEdgeMarked(int edge) {
		boolean marked = false;

		switch(edge){
			case PlayerAction.TOP_EDGE:
				marked = isTopEdgeMarked;
				break;
			case PlayerAction.BOTTOM_EDGE:
				marked = isBottomEdgeMarked;
				break;
			case PlayerAction.LEFT_EDGE:
				marked = isLeftEdgeMarked;
				break;
			case PlayerAction.RIGHT_EDGE:
				marked = isRightEdgeMarked;
				break;

		}
		return marked;
	}

	//返回当前格子相邻的上面格子对象
	public Box getTopBox() {
		return topBox;
	}
	//返回当前格子相邻的下面格子对象
	public Box getBottomBox() {
		return bottomBox;
	}
	//返回当前格子相邻的左边格子对象
	public Box getLeftBox() {
		return leftBox;
	}
	//返回当前格子相邻的右边格子对象
	public Box getRightBox() {
		return rightBox;
	}

	//获得当前格子对象的赢方对象接口信息
	public IPlayer getWinner() {
		return winner;
	}
	//设置赢方对象
	public void setWinner(IPlayer player) {
		winner = player;
	}
	//判断当前格子是否全部选中
	public boolean isAllSelected() {
		return isTopEdgeMarked && isBottomEdgeMarked && isLeftEdgeMarked && isRightEdgeMarked;
	}
	//返回当前格子的标注的边的数量，例如选中一条边就是1，两条是2，依此类推，数量范围为0-4,4就表示全部标注了
	public int getSelectedEdgeNums() {
		return (isTopEdgeMarked ? 1 : 0) + (isBottomEdgeMarked ? 1 : 0) + (isLeftEdgeMarked ? 1 : 0) + (isRightEdgeMarked ? 1 : 0);
	}
	//设置是否选中该格子的下边，backAssignment表示是否需要反向设置该相邻格子的
	//对应边的选中信息，例如如果当前格子是下边isBottomEdgeMarked，如果backAssignment
	//(往回赋值)为真，就需要设置相邻下面格子的上边isTopEdgeMarked的选中与否信息
	public void setBottomEdgeMarked(boolean isBottomEdgeMarked, boolean backAssignment) {
		this.isBottomEdgeMarked = isBottomEdgeMarked;

		if(backAssignment && bottomBox!=null)
			bottomBox.setTopEdgeMarked(isBottomEdgeMarked, false);
	}

	public void setTopEdgeMarked(boolean isTopEdgeMarked, boolean backAssignment) {
		this.isTopEdgeMarked = isTopEdgeMarked;

		if(backAssignment && topBox!=null)
			topBox.setBottomEdgeMarked(isTopEdgeMarked, false);
	}

	public void setLeftEdgeMarked(boolean isLeftEdgeMarked, boolean backAssignment) {
		this.isLeftEdgeMarked = isLeftEdgeMarked;

		if(backAssignment && leftBox!=null)
			leftBox.setRightEdgeMarked(isLeftEdgeMarked, false);
	}

	public void setRightEdgeMarked(boolean isRightEdgeMarked, boolean backAssignment) {
		this.isRightEdgeMarked = isRightEdgeMarked;

		if(backAssignment && rightBox!=null)
			rightBox.setLeftEdgeMarked(isRightEdgeMarked, false);
	}

	public void setTopBox(Box box, boolean backAssignment) {
		topBox = box;
		if(box != null && backAssignment)
			box.setBottomBox(this, false);
	}

	public void setBottomBox(Box box, boolean backAssignment) {
		bottomBox = box;
		if(box != null && backAssignment)
			box.setTopBox(this, false);
	}

	public void setLeftBox(Box box, boolean backAssignment) {
		leftBox = box;
		if(box != null && backAssignment)
			box.setRightBox(this, false);
	}

	public void setRightBox(Box box, boolean backAssignment) {
		rightBox = box;
		if(box != null && backAssignment)
			box.setLeftBox(this, false);
	}
}
