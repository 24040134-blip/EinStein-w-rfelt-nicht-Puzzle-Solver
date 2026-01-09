import java.util.ArrayList;
import java.util.List;

public class GameState {
    private int[] currentPositions; // 当前6个棋子的位置（-1表示被捕获）
    private int targetPiece;        // 目标棋子
    private int currentDice;        // 当前回合的骰子数

    // 构造方法：初始化游戏状态
    public GameState(int[] initialPositions, int targetPiece) {
        // 复制初始位置（避免直接修改原数组）
        this.currentPositions = new int[6];
        System.arraycopy(initialPositions, 0, this.currentPositions, 0, 6);
        this.targetPiece = targetPiece;
    }

    // 设置当前回合的骰子数
    public void setCurrentDice(int currentDice) {
        this.currentDice = currentDice;
    }

    // 新增：获取当前骰子数的方法（解决HumanPlayer调用报错）
    public int getCurrentDice() {
        return currentDice;
    }

    // 生成所有合法移动（核心方法）
    public List<Move> generatePossibleMoves() {
        List<Move> possibleMoves = new ArrayList<>();

        // 步骤1：找到当前可移动的棋子（按题目规则）
        List<Integer> movablePieces = findMovablePieces();

        // 步骤2：为每个可移动的棋子，生成所有合法目标位置
        for (int pieceNum : movablePieces) {
            int pieceIndex = pieceNum - 1; // 棋子1对应index0，棋子2对应index1...
            int currentPos = currentPositions[pieceIndex];

            // 跳过已被捕获的棋子（位置为-1）
            if (currentPos == -1) {
                continue;
            }

            // 步骤3：生成8个相邻位置（上下左右+斜向，类似国际象棋王）
            int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1}; // 行变化（十位数字）
            int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1}; // 列变化（个位数字）

            // 计算当前位置的十位（行）和个位（列）
            int currentRow = currentPos / 10;
            int currentCol = currentPos % 10;

            // 遍历8个方向
            for (int i = 0; i < 8; i++) {
                int newRow = currentRow + dx[i];
                int newCol = currentCol + dy[i];

                // 检查新位置是否合法：行和列都在0-9之间，且不是22
                if (newRow >= 0 && newRow <= 9 && newCol >= 0 && newCol <= 9) {
                    int newPos = newRow * 10 + newCol;
                    if (newPos != 22) { // 22位置被移除，不能进入
                        possibleMoves.add(new Move(pieceNum, currentPos, newPos));
                    }
                }
            }
        }
        return possibleMoves;
    }

    // 查找当前可移动的棋子（按题目规则）
    private List<Integer> findMovablePieces() {
        List<Integer> movablePieces = new ArrayList<>();
        List<Integer> existingPieces = new ArrayList<>();

        // 步骤1：收集当前存在的棋子（位置不是-1的）
        for (int i = 0; i < 6; i++) {
            if (currentPositions[i] != -1) {
                existingPieces.add(i + 1); // 棋子编号是i+1（1-6）
            }
        }

        // 步骤2：按规则判断可移动棋子
        if (existingPieces.contains(currentDice)) {
            // 规则2：骰子数匹配存在的棋子，只能移动该棋子
            movablePieces.add(currentDice);
        } else {
            // 规则3：找比骰子大的最小棋子 + 比骰子小的最大棋子
            Integer minBigger = null;
            Integer maxSmaller = null;

            for (int piece : existingPieces) {
                if (piece > currentDice) {
                    if (minBigger == null || piece < minBigger) {
                        minBigger = piece;
                    }
                } else if (piece < currentDice) {
                    if (maxSmaller == null || piece > maxSmaller) {
                        maxSmaller = piece;
                    }
                }
            }

            // 有则添加到可移动列表
            if (minBigger != null) {
                movablePieces.add(minBigger);
            }
            if (maxSmaller != null) {
                movablePieces.add(maxSmaller);
            }
        }
        return movablePieces;
    }

    // 执行移动（更新棋子位置，处理捕获）
    public void executeMove(Move move) {
        int pieceIndex = move.getPieceNum() - 1;
        int fromPos = move.getFromPos();
        int toPos = move.getToPos();

        // 步骤1：检查目标位置是否有其他棋子（需要捕获）
        for (int i = 0; i < 6; i++) {
            if (currentPositions[i] == toPos) {
                currentPositions[i] = -1; // 捕获：设置为-1
                break;
            }
        }

        // 步骤2：更新当前棋子的位置
        currentPositions[pieceIndex] = toPos;
    }

    // 判断是否获胜（目标棋子到达0位置）
    public boolean isWinning() {
        int targetIndex = targetPiece - 1;
        return currentPositions[targetIndex] == 0;
    }

    // 获取当前棋子位置（供外部打印）
    public int[] getCurrentPositions() {
        return currentPositions.clone(); // 返回副本，避免外部修改
    }

    // 检查是否还有合法移动（避免死局）
    public boolean hasPossibleMoves() {
        return !generatePossibleMoves().isEmpty();
    }
}
