import java.io.PrintWriter;
import java.io.IOException;

public abstract class Player {
    protected String name; // 玩家名称

    // 构造方法：初始化玩家名称
    public Player(String name) {
        this.name = name;
    }

    // 打印移动到moves.txt（追加到文件末尾）
    public void printMove(int[] currentPositions) {
        try {
            // 追加模式（不会覆盖之前的内容）
            PrintWriter pw = new PrintWriter(new java.io.FileWriter("moves.txt", true));

            // 打印当前所有棋子位置（用空格连接，-1表示被捕获）
            for (int i = 0; i < 6; i++) {
                pw.print(currentPositions[i]);
                if (i != 5) {
                    pw.print(" ");
                }
            }
            pw.println();

            pw.flush();
            pw.close();
        } catch (IOException e) {
            System.err.println("打印移动失败！");
            e.printStackTrace();
        }
    }

    // 抽象方法：选择移动（子类必须实现）
    public abstract Move chooseMove(GameState gameState);

    // 获取玩家名称
    public String getName() {
        return name;
    }
}
