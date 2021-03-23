import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int PlayAgainst = 0;
        while(PlayAgainst == 0){
        //取得点数 横向纵向各几个点，不低于3个点
        int pointNumsH = 0,pointNumsV = 0;
        String s;
        while (pointNumsH < 1){
            s = JOptionPane.showInputDialog(null,
                    "Please enter the Height of the chessboard：\n","INPUT MESSAGE",JOptionPane.PLAIN_MESSAGE);
//            isDigit() 方法用于判断指定字符是否为数字
            boolean valid = true;
            for(int i=0;i<s.length();i++){
                if(!Character.isDigit(s.charAt(i)))
                    valid = false;
            }if(valid == true)
            pointNumsH = Integer.parseInt(s);
            else pointNumsH = -1;
        }
        while (pointNumsV < 1){
            s = JOptionPane.showInputDialog(null,
                    "Please enter the Weight of the chessboard：\n", "INPUT MESSAGE",JOptionPane.PLAIN_MESSAGE);
            boolean valid = true;
            for(int i=0;i<s.length();i++){
                if(!Character.isDigit(s.charAt(i)))
                    valid = false;
            }if(valid == true)
                pointNumsV = Integer.parseInt(s);
            else pointNumsV = -1;
        }

        DotsBoxes game = new DotsBoxes(pointNumsH,pointNumsV,800,800);

        IPlayer player1 = new HumanPlayer("");
        IPlayer player2 = new HumanPlayer("");

        IPlayer player3 = new SmartMachinePlayer("AlphaZero");
        IPlayer player4 = new SmartMachinePlayer("AlphaGo");
        IPlayer player5 = new SimpleMachinePlayer("Tom");
        IPlayer player6 = new SimpleMachinePlayer("Jerry");

        Object[] options ={ "Simple machine","Hard machine","players" };
        String FirstPlayer = (String) JOptionPane.showInputDialog(null,
                "Please choose the First-hand Player:\n", "INPUT MESSAGE", JOptionPane.PLAIN_MESSAGE,
                new ImageIcon("icon.png"),options,"Simple machine");
        if(FirstPlayer.equals("players")){
            String name = JOptionPane.showInputDialog(null,
                    "Please enter the yours name：\n","INPUT MESSAGE",JOptionPane.PLAIN_MESSAGE);
            if(name.equals(""))
                player1  = new HumanPlayer("Faceless Men");
            else{player1 = new HumanPlayer(name);}
        }
        String SecondPlayer = (String) JOptionPane.showInputDialog(null,
                "Please choose the Second-hand Player:\n", "INPUT MESSAGE", JOptionPane.PLAIN_MESSAGE,
                new ImageIcon("icon.png"),options,"Simple machine");
        if(SecondPlayer.equals("players")){
            String name = JOptionPane.showInputDialog(null,
                    "Please enter the yours name：\n","INPUT MESSAGE",JOptionPane.PLAIN_MESSAGE);
            if(name.equals(""))
                player2  = new HumanPlayer("Faceless Men");
            else{player2 = new HumanPlayer(name);}
        }

        switch (FirstPlayer){
            case "Simple machine":
                game.addPlayer(player5);
                break;
            case "Hard machine":
                game.addPlayer(player3);
                break;
            case "players":
                game.addPlayer(player1);
        }
        switch (SecondPlayer){
            case "Simple machine":
                game.addPlayer(player6);
                break;
            case "Hard machine":
                game.addPlayer(player4);
                break;
            case "players":
                game.addPlayer(player2);
        }

        while(!game.isCompleted()) {
            game.play();
            game.paint();
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(game.isCompleted()){
            //弹出一个对话框
            PlayAgainst = JOptionPane.showConfirmDialog(null,
                    "The winner is " + game.getWinnerName() + ", with " + game.getWinnerScore() + " points.\n " +
                            "Would you like to play it again?", "GAME OVER！",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
        }
        }
    }
}