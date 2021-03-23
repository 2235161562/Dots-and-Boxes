public class HumanPlayer implements IPlayer{

	private String m_name;
	private DotsBoxes m_DotsBoxes;

	public HumanPlayer(String name){
		m_name = name;
	}

	public String getName() {
		return m_name;
	}

	public PlayerAction play(Box[][] boxesArray2) {
		return new PlayerAction(this,0,0,-1);
	}
}