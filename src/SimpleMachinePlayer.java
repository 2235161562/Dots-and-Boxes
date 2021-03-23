import java.util.Random;
import java.util.ArrayList;

public class SimpleMachinePlayer implements IPlayer {

	private String m_name;
	private static int m_index = 0; //机器对战，需要区分

	public SimpleMachinePlayer(String name) {
		m_name = name;
	}

	public SimpleMachinePlayer() {
		m_index++;
	}

	public String getName() {
		return m_name;
	}

	public ArrayList<Integer> createDiffRandList(int maxN,boolean isMachine2Machine){
		// 生成不重复随机数序列，最大不超过maxN，不包括maxN
		ArrayList<Integer> diffRandList = new ArrayList<>();

		//如果不是两个简单机器参与游戏
		if(!isMachine2Machine){
			for (int i = 0; i < maxN; i++){
				diffRandList.add(i);
			}
			return diffRandList;
		}

		Random rand = new Random();
		boolean[] existSameArray = new boolean[maxN];
		int num = 0;
		for(int i = 0;i < maxN;++i) {
			do {
				num = rand.nextInt(maxN);
			} while (existSameArray[num]);
			existSameArray[num] = true;
			diffRandList.add(num);
		}
		return diffRandList;
	}

	public PlayerAction play(Box[][] boxesArray2) {
		int [] markingEdgeArray;
		ArrayList<Integer> randomIntList = new ArrayList<>();
		ArrayList<Integer> finalRandomIntList = new ArrayList<>();
		ArrayList<Integer> diffRandRowList;
		ArrayList<Integer>  diffRandColList;
		Random ran1;
		int k;

		markingEdgeArray =new int[4];
		markingEdgeArray[0] = PlayerAction.TOP_EDGE;
		markingEdgeArray[1] = PlayerAction.BOTTOM_EDGE;
		markingEdgeArray[2] = PlayerAction.LEFT_EDGE;
		markingEdgeArray[3] = PlayerAction.RIGHT_EDGE;
		for(k = 0;k < 4;++k)
			randomIntList.add(k);

		//产生不重复的行值随机数
		diffRandRowList = createDiffRandList(boxesArray2.length,m_index == 2);
		//产生不重复的列值随机数
		diffRandColList = createDiffRandList(boxesArray2[0].length,m_index == 2);

		//电脑只是简单的循环棋盘上的所有格子，找到第一个没有标注完所有四边的四边形，
		// 然后依次查找第一个没有标记的边，然后尝试去标记这个边,现在是随机选择某个四边形的某条边，可以考虑随机选择格子
		//序列化选择的行列数值，保存没有完成封闭的四边形的索引值
		for(int r=0; r < diffRandRowList.size(); r++) {
			int i = diffRandRowList.get(r);
			for (int c = 0; c < diffRandColList.size(); c++) {
				int j = diffRandColList.get(c);
				if (!boxesArray2[i][j].isAllSelected()) {
					//随机数选择语句执行顺序
					for (k = 4; k >= 2; --k) {
						ran1 = new Random();
						int index = ran1.nextInt(k);
						finalRandomIntList.add(randomIntList.get(index));
						randomIntList.remove(index);
					}
					finalRandomIntList.add(randomIntList.get(0));
					for (k = 0; k < finalRandomIntList.size(); ++k) {
						if (!boxesArray2[i][j].isEdgeMarked(markingEdgeArray[finalRandomIntList.get(k)]))
							return new PlayerAction(this, i, j, markingEdgeArray[finalRandomIntList.get(k)]);
					}

				}
			}//end for j
		}//
		//如果没有找到对应的边，选择第一个格子
		return new PlayerAction(this,0,0,PlayerAction.TOP_EDGE);
	}

}
