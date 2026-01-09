import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player {
    private Random random; // 随机数生成器

    // 构造方法：初始化玩家名称和随机工具
    public RandomPlayer() {
        super("Random Player"); // 默认名称
        random = new Random();
    }

    // 选择移动：随机选一个合法移动
    @Override
    public Move chooseMove(GameState gameState) {
        List<Move> possibleMoves = gameState.generatePossibleMoves();

        // 如果没有合法移动，返回null（游戏会判定失败）
        if (possibleMoves.isEmpty()) {
            return null;
        }

        // 随机选一个移动
        int randomIndex = random.nextInt(possibleMoves.size());
        return possibleMoves.get(randomIndex);
    }
}
