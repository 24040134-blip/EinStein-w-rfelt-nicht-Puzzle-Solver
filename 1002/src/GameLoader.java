import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GameLoader {
    // 存储从文件读取的游戏数据
    private int targetPiece;       // 目标棋子（要到达0位置的棋子）
    private int[] initialPositions; // 6个棋子的初始位置（index 0=棋子1，index1=棋子2...）
    private int[] diceSequence;     // 骰子序列（每一步的骰子数）

    // 构造方法：传入level文件名（如"level1.txt"），读取数据
    public GameLoader(String filename) {
        try {
            // 读取文件的工具
            BufferedReader br = new BufferedReader(new FileReader(filename));

            // 第1行：目标棋子（转成整数）
            targetPiece = Integer.parseInt(br.readLine().trim());

            // 第2行：6个棋子的初始位置（按空格分割）
            String[] posStr = br.readLine().trim().split(" ");
            initialPositions = new int[6];
            for (int i = 0; i < 6; i++) {
                initialPositions[i] = Integer.parseInt(posStr[i]);
            }

            // 第3行：骰子序列（按空格分割）
            String[] diceStr = br.readLine().trim().split(" ");
            diceSequence = new int[diceStr.length];
            for (int i = 0; i < diceStr.length; i++) {
                diceSequence[i] = Integer.parseInt(diceStr[i]);
            }

            br.close(); // 关闭文件（必须做，避免资源泄露）
        } catch (IOException e) {
            // 初学者友好：文件读取失败时提示
            System.err.println("读取文件失败！请检查文件名是否正确：" + filename);
            e.printStackTrace();
        }
    }

    // 打印游戏详情到moves.txt（按题目要求的格式）
    public void printGameDetails(String playerName) {
        try {
            // 写入文件的工具（覆盖原有内容）
            PrintWriter pw = new PrintWriter("moves.txt");

            // 第1行：玩家名称
            pw.println(playerName);

            // 第2行：骰子序列（用空格连接）
            for (int i = 0; i < diceSequence.length; i++) {
                pw.print(diceSequence[i]);
                if (i != diceSequence.length - 1) {
                    pw.print(" ");
                }
            }
            pw.println();

            // 第3行：目标棋子
            pw.println(targetPiece);

            // 第4行：初始位置（用空格连接）
            for (int i = 0; i < 6; i++) {
                pw.print(initialPositions[i]);
                if (i != 5) {
                    pw.print(" ");
                }
            }
            pw.println();

            pw.flush(); // 确保数据写入文件
            pw.close(); // 关闭文件
        } catch (IOException e) {
            System.err.println("写入moves.txt失败！");
            e.printStackTrace();
        }
    }

    // Getter方法：让其他类获取读取到的数据
    public int getTargetPiece() {
        return targetPiece;
    }

    public int[] getInitialPositions() {
        return initialPositions;
    }

    public int[] getDiceSequence() {
        return diceSequence;
    }
}