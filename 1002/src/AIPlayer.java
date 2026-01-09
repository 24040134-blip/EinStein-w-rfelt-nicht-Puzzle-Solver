import java.util.List;

public class AIPlayer extends Player {
    private int targetPiece; // 目标棋子（要到达0位置）

    // 构造方法：初始化玩家名称（无参数）
    public AIPlayer() {
        super("AI Player"); // 默认名称
    }

    // 新增：设置目标棋子的方法（解决GameMain赋值报错）
    public void setTargetPiece(int targetPiece) {
        this.targetPiece = targetPiece;
    }

    // 选择移动：贪心策略
    @Override
    public Move chooseMove(GameState gameState) {
        List<Move> possibleMoves = gameState.generatePossibleMoves();

        // 没有合法移动，返回null
        if (possibleMoves.isEmpty()) {
            return null;
        }

        Move bestMove = null;
        int minDistance = Integer.MAX_VALUE; // 目标：距离0位置越近越好

        for (Move move : possibleMoves) {
            // 优先处理目标棋子的移动
            if (move.getPieceNum() == targetPiece) {
                // 计算移动后到0位置的距离（曼哈顿距离：行差+列差，简单有效）
                int newPos = move.getToPos();
                int distance = calculateDistance(newPos, 0);

                // 选择距离最近的移动
                if (distance < minDistance) {
                    minDistance = distance;
                    bestMove = move;
                }
            }
        }

        // 如果没有目标棋子的移动，选择其他棋子的任意合法移动（优先不挡路）
        if (bestMove == null) {
            bestMove = possibleMoves.get(0);
            // 简单优化：避免移动到0位置附近（给目标棋子留路）
            for (Move move : possibleMoves) {
                int distance = calculateDistance(move.getToPos(), 0);
                if (distance > 5) { // 远离0位置，不挡路
                    bestMove = move;
                    break;
                }
            }
        }

        return bestMove;
    }

    // 计算两个位置的曼哈顿距离（行差的绝对值 + 列差的绝对值）
    private int calculateDistance(int pos1, int pos2) {
        int row1 = pos1 / 10;
        int col1 = pos1 % 10;
        int row2 = pos2 / 10;
        int col2 = pos2 % 10;
        return Math.abs(row1 - row2) + Math.abs(col1 - col2);
    }
}
