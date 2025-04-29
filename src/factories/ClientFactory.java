package factories;

import models.Client;

/**
 * 工厂类，负责创建客户端/玩家对象
 */
public class ClientFactory {
    
    /**
     * 创建一个新的客户端/玩家对象
     */
    public static Client createClient(String name, String winGenre, int winThreshold) {
        return new Client(name, winGenre, winThreshold);
    }
}
