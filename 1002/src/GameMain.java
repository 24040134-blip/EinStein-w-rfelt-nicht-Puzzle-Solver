import java.util.Scanner;

public class GameMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 步骤1：选择游戏模式
        System.out.println("===== Einstein würfelt nicht! 游戏 =====");
        System.out.println("请选择游戏模式：");
        System.out.println("1. 人类玩家（Human Player）");
        System.out.println("2. 随机玩家（Random Player）");
        System.out.println("3. AI玩家（AI Player）");
        System.out.print("输入模式编号（1/2/3）：");
        int mode = scanner.nextInt();
        scanner.nextLine(); // 吸收换行符（避免后续输入问题）

        // 步骤2：创建玩家对象
        Player player = null;
        int targetPiece = 0; // 目标棋子（后续从level文件读取）
        switch (mode) {
            case 1:
                System.out.print("请输入你的名称：");
                String userName = scanner.nextLine();
                player = new HumanPlayer(userName);
                break;
            case 2:
                player = new RandomPlayer();
                System.out.println("已选择随机玩家（默认名称：Random Player）");
                break;
            case 3:
                // AI玩家先创建对象，后续用set方法设置目标棋子
                player = new AIPlayer();
                System.out.println("已选择AI玩家（默认名称：AI Player）");
                break;
            default:
                System.err.println("无效模式！程序退出。");
                return;
        }

        // 步骤3：选择游戏关卡
        System.out.println("\n请选择关卡：");
        System.out.println("1. level1");
        System.out.println("2. level2");
        System.out.println("3. level3");
        System.out.println("4. level4");
        System.out.print("输入关卡编号（1/4）：");
        int level = scanner.nextInt();
        String levelFileName = "level" + level + ".txt";

        // 步骤4：加载游戏数据
        GameLoader gameLoader = new GameLoader(levelFileName);
        targetPiece = gameLoader.getTargetPiece();
        int[] initialPositions = gameLoader.getInitialPositions();
        int[] diceSequence = gameLoader.getDiceSequence();

        // 更新AI玩家的目标棋子（用set方法，解决私有变量访问报错）
        if (player instanceof AIPlayer) {
            ((AIPlayer) player).setTargetPiece(targetPiece);
        }

        // 打印游戏详情到moves.txt
        gameLoader.printGameDetails(player.getName());

        // 步骤5：初始化游戏状态
        GameState gameState = new GameState(initialPositions, targetPiece);
        boolean isWin = false;
        int moveCount = 0;
        final int MAX_MOVES = 30; // 最大移动次数（题目要求）

        // 步骤6：游戏主循环（最多30步）
        System.out.println("\n===== 游戏开始 =====");
        while (moveCount < MAX_MOVES) {
            // 检查是否已经获胜
            if (gameState.isWinning()) {
                isWin = true;
                break;
            }

            // 检查当前是否有骰子可用（避免骰子序列用尽）
            if (moveCount >= diceSequence.length) {
                System.out.println("骰子序列已用尽！");
                break;
            }

            // 设置当前回合的骰子数
            int currentDice = diceSequence[moveCount];
            gameState.setCurrentDice(currentDice);

            // 玩家选择移动
            Move move = player.chooseMove(gameState);
            if (move == null) {
                System.out.println("没有合法移动！游戏结束。");
                break;
            }

            // 执行移动并打印
            gameState.executeMove(move);
            player.printMove(gameState.getCurrentPositions());

            // 移动次数+1
            moveCount++;
            System.out.println("已完成" + moveCount + "步（最多30步）");
        }

        // 步骤7：显示游戏结果
        System.out.println("\n===== 游戏结束 =====");
        if (isWin) {
            System.out.println("恭喜！你获胜了！目标棋子到达了位置0！");
        } else {
            System.out.println("很遗憾！未能在30步内让目标棋子到达位置0，游戏失败。");
        }

        scanner.close(); // 关闭输入工具
    }
}
