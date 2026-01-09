import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class HumanPlayer extends Player {
    private Scanner scanner; // 读取控制台输入

    // 构造方法：初始化玩家名称和输入工具
    public HumanPlayer(String name) {
        super(name);
        scanner = new Scanner(System.in);
    }

    // 选择移动：提示玩家输入
    @Override
    public Move chooseMove(GameState gameState) {
        List<Move> possibleMoves = gameState.generatePossibleMoves();

        // 提示当前可移动的棋子（去重）
        System.out.println("\n当前骰子数：" + gameState.getCurrentDice());
        System.out.print("可移动的棋子：");
        Set<Integer> movablePieceSet = new HashSet<>();
        for (Move move : possibleMoves) {
            movablePieceSet.add(move.getPieceNum());
        }
        for (int piece : movablePieceSet) {
            System.out.print(piece + " ");
        }
        System.out.println();

        // 提示当前棋子位置
        int[] currentPos = gameState.getCurrentPositions();
        System.out.println("当前棋子位置（1-6号）：");
        for (int i = 0; i < 6; i++) {
            System.out.println("棋子" + (i + 1) + "：" + currentPos[i]);
        }

        // 输入要移动的棋子编号
        int pieceNum;
        while (true) {
            System.out.print("请输入要移动的棋子编号（1-6）：");
            pieceNum = scanner.nextInt();
            // 检查棋子是否可移动
            boolean isValid = false;
            for (int piece : movablePieceSet) {
                if (piece == pieceNum) {
                    isValid = true;
                    break;
                }
            }
            if (isValid) {
                break;
            }
            System.out.println("无效的棋子！请选择可移动的棋子。");
        }

        // 打印该棋子能移动到的所有目标位置
        List<Integer> validToPositions = new ArrayList<>();
        for (Move move : possibleMoves) {
            if (move.getPieceNum() == pieceNum) {
                validToPositions.add(move.getToPos());
            }
        }
        System.out.print("该棋子可移动到的位置：");
        for (int pos : validToPositions) {
            System.out.print(pos + " ");
        }
        System.out.println();

        // 输入目标位置
        int toPos;
        while (true) {
            System.out.print("请输入目标位置（从上面的可选位置中选）：");
            toPos = scanner.nextInt();
            // 检查目标位置是否在合法列表中
            if (validToPositions.contains(toPos)) {
                break;
            }
            System.out.println("无效位置！请从上面的可选位置中选择。");
        }

        // 返回玩家选择的移动
        return new Move(pieceNum, currentPos[pieceNum - 1], toPos);
    }
}
