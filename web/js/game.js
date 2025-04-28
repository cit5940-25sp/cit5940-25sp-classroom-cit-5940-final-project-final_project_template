/**
 * 电影连接游戏 - 前端逻辑
 */

// 游戏状态
const gameState = {
    players: [],
    currentPlayerIndex: 0,
    turnCount: 1,
    gameStarted: false,
    gameOver: false,
    winner: null,
    usedMovies: new Set(),
    movieData: [] // 将从服务器加载
};

// DOM 元素
const elements = {
    gameSetup: document.getElementById('game-setup'),
    gameBoard: document.getElementById('game-board'),
    gameOver: document.getElementById('game-over'),
    startGame: document.getElementById('start-game'),
    newGame: document.getElementById('new-game'),
    currentPlayer: document.getElementById('current-player'),
    turnCounter: document.getElementById('turn-counter'),
    player1Status: document.getElementById('player1-status'),
    player2Status: document.getElementById('player2-status'),
    movieSearch: document.getElementById('movie-search'),
    movieResults: document.getElementById('movie-results'),
    skipTurn: document.getElementById('skip-turn'),
    blockAction: document.getElementById('block-action'),
    logEntries: document.getElementById('log-entries'),
    winnerDisplay: document.getElementById('winner-display')
};

// 初始化
document.addEventListener('DOMContentLoaded', () => {
    // 绑定事件监听器
    elements.startGame.addEventListener('click', startGame);
    elements.newGame.addEventListener('click', resetGame);
    elements.movieSearch.addEventListener('input', debounce(searchMovies, 300));
    elements.skipTurn.addEventListener('click', useSkipPowerUp);
    elements.blockAction.addEventListener('click', useBlockPowerUp);
    
    // 加载电影数据（模拟）
    loadMovieData();
});

// 加载电影数据
function loadMovieData() {
    // 在实际应用中，这里会从服务器加载电影数据
    // 现在我们使用一些示例数据
    gameState.movieData = [
        {
            id: 1,
            title: "Inception",
            releaseYear: 2010,
            genre: ["Sci-Fi"],
            cast: [
                { name: "Leonardo DiCaprio", id: 1 },
                { name: "Joseph Gordon-Levitt", id: 2 },
                { name: "Elliot Page", id: 3 }
            ],
            crew: [
                { name: "Christopher Nolan", id: 101 },
                { name: "Hans Zimmer", id: 104 }
            ]
        },
        {
            id: 2,
            title: "Interstellar",
            releaseYear: 2014,
            genre: ["Sci-Fi"],
            cast: [
                { name: "Matthew McConaughey", id: 4 },
                { name: "Anne Hathaway", id: 5 }
            ],
            crew: [
                { name: "Christopher Nolan", id: 101 },
                { name: "Hans Zimmer", id: 104 }
            ]
        },
        {
            id: 3,
            title: "The Matrix",
            releaseYear: 1999,
            genre: ["Sci-Fi", "Action"],
            cast: [
                { name: "Keanu Reeves", id: 6 },
                { name: "Laurence Fishburne", id: 7 }
            ],
            crew: [
                { name: "Lana Wachowski", id: 102 },
                { name: "Lilly Wachowski", id: 103 }
            ]
        },
        {
            id: 4,
            title: "The Dark Knight",
            releaseYear: 2008,
            genre: ["Action"],
            cast: [
                { name: "Christian Bale", id: 8 },
                { name: "Heath Ledger", id: 9 }
            ],
            crew: [
                { name: "Christopher Nolan", id: 101 },
                { name: "Hans Zimmer", id: 104 }
            ]
        },
        {
            id: 5,
            title: "Forrest Gump",
            releaseYear: 1994,
            genre: ["Drama"],
            cast: [
                { name: "Tom Hanks", id: 10 },
                { name: "Robin Wright", id: 11 }
            ],
            crew: [
                { name: "Robert Zemeckis", id: 105 },
                { name: "Alan Silvestri", id: 106 }
            ]
        }
    ];
}

// 开始游戏
function startGame() {
    // 获取玩家信息
    const player1Name = document.getElementById('player1-name').value || '玩家1';
    const player1Genre = document.getElementById('player1-genre').value;
    const player2Name = document.getElementById('player2-name').value || '玩家2';
    const player2Genre = document.getElementById('player2-genre').value;
    const winThreshold = parseInt(document.getElementById('win-threshold').value) || 3;
    
    // 创建玩家对象
    gameState.players = [
        {
            name: player1Name,
            winGenre: player1Genre,
            winThreshold: winThreshold,
            genreCount: {},
            usedMovies: new Set(),
            hasBlocked: false,
            isSkipped: false
        },
        {
            name: player2Name,
            winGenre: player2Genre,
            winThreshold: winThreshold,
            genreCount: {},
            usedMovies: new Set(),
            hasBlocked: false,
            isSkipped: false
        }
    ];
    
    // 初始化游戏状态
    gameState.currentPlayerIndex = 0;
    gameState.turnCount = 1;
    gameState.gameStarted = true;
    gameState.gameOver = false;
    gameState.winner = null;
    gameState.usedMovies.clear();
    
    // 更新UI
    elements.gameSetup.classList.add('hidden');
    elements.gameBoard.classList.remove('hidden');
    elements.gameOver.classList.add('hidden');
    
    updateGameStatus();
    addLogEntry('游戏开始！');
}

// 重置游戏
function resetGame() {
    elements.gameOver.classList.add('hidden');
    elements.gameSetup.classList.remove('hidden');
    elements.logEntries.innerHTML = '';
}

// 更新游戏状态显示
function updateGameStatus() {
    const currentPlayer = gameState.players[gameState.currentPlayerIndex];
    
    // 更新当前玩家和回合信息
    elements.currentPlayer.textContent = `当前玩家: ${currentPlayer.name}`;
    elements.turnCounter.textContent = `回合: ${gameState.turnCount}`;
    
    // 更新玩家状态
    updatePlayerStatus(0, elements.player1Status);
    updatePlayerStatus(1, elements.player2Status);
    
    // 更新能力按钮状态
    const otherPlayerIndex = gameState.currentPlayerIndex === 0 ? 1 : 0;
    elements.skipTurn.disabled = gameState.players[gameState.currentPlayerIndex].hasBlocked;
    elements.blockAction.disabled = gameState.players[gameState.currentPlayerIndex].hasBlocked;
}

// 更新单个玩家状态显示
function updatePlayerStatus(playerIndex, element) {
    const player = gameState.players[playerIndex];
    const genreCount = player.genreCount[player.winGenre] || 0;
    const isCurrentPlayer = playerIndex === gameState.currentPlayerIndex;
    
    let statusHTML = `
        <h3>${player.name}</h3>
        <p>目标类型: ${getGenreDisplayName(player.winGenre)} (${genreCount}/${player.winThreshold})</p>
        <p>已使用电影: ${player.usedMovies.size}</p>
    `;
    
    if (player.isSkipped) {
        statusHTML += '<p class="status-effect">被跳过</p>';
    }
    
    if (player.hasBlocked) {
        statusHTML += '<p class="status-effect">已使用阻止</p>';
    }
    
    if (isCurrentPlayer) {
        element.classList.add('current-player');
    } else {
        element.classList.remove('current-player');
    }
    
    element.innerHTML = statusHTML;
}

// 搜索电影
function searchMovies() {
    const query = elements.movieSearch.value.toLowerCase();
    if (query.length < 2) {
        elements.movieResults.innerHTML = '';
        return;
    }
    
    // 过滤电影
    const results = gameState.movieData.filter(movie => {
        // 排除已使用的电影
        if (gameState.usedMovies.has(movie.id)) return false;
        
        // 按标题搜索
        return movie.title.toLowerCase().includes(query);
    });
    
    // 显示结果
    displayMovieResults(results);
}

// 显示电影搜索结果
function displayMovieResults(movies) {
    elements.movieResults.innerHTML = '';
    
    if (movies.length === 0) {
        elements.movieResults.innerHTML = '<p>没有找到匹配的电影</p>';
        return;
    }
    
    movies.forEach(movie => {
        const movieElement = document.createElement('div');
        movieElement.className = 'movie-item';
        movieElement.innerHTML = `
            <h4>${movie.title} (${movie.releaseYear})</h4>
            <p>类型: ${movie.genre.join(', ')}</p>
        `;
        
        // 添加点击事件
        movieElement.addEventListener('click', () => selectMovie(movie));
        
        elements.movieResults.appendChild(movieElement);
    });
}

// 选择电影
function selectMovie(movie) {
    const currentPlayer = gameState.players[gameState.currentPlayerIndex];
    
    // 检查是否可以选择这部电影
    if (gameState.usedMovies.has(movie.id)) {
        addLogEntry(`${movie.title} 已经被使用过了`);
        return;
    }
    
    // 检查是否与上一部电影相连（第一部电影除外）
    if (gameState.usedMovies.size > 0 && !isMovieConnected(movie)) {
        addLogEntry(`${movie.title} 与上一部电影没有连接，请选择另一部电影`);
        return;
    }
    
    // 添加电影到玩家的已使用列表
    currentPlayer.usedMovies.add(movie.id);
    gameState.usedMovies.add(movie.id);
    
    // 更新类型计数
    movie.genre.forEach(genre => {
        const genreLower = genre.toLowerCase();
        currentPlayer.genreCount[genreLower] = (currentPlayer.genreCount[genreLower] || 0) + 1;
    });
    
    // 添加日志
    addLogEntry(`${currentPlayer.name} 选择了 ${movie.title} (${movie.releaseYear})`);
    
    // 检查胜利条件
    if (currentPlayer.genreCount[currentPlayer.winGenre] >= currentPlayer.winThreshold) {
        endGame(gameState.currentPlayerIndex);
        return;
    }
    
    // 切换到下一个玩家
    nextTurn();
}

// 检查电影是否与上一部电影相连
function isMovieConnected(movie) {
    // 如果是第一部电影，总是可以选择
    if (gameState.usedMovies.size === 0) return true;
    
    // 获取上一部电影
    const lastMovieId = Array.from(gameState.usedMovies).pop();
    const lastMovie = gameState.movieData.find(m => m.id === lastMovieId);
    
    // 检查演员连接
    for (const cast1 of lastMovie.cast) {
        for (const cast2 of movie.cast) {
            if (cast1.id === cast2.id) return true;
        }
    }
    
    // 检查剧组连接
    for (const crew1 of lastMovie.crew) {
        for (const crew2 of movie.crew) {
            if (crew1.id === crew2.id) return true;
        }
    }
    
    return false;
}

// 下一个回合
function nextTurn() {
    // 切换玩家
    gameState.currentPlayerIndex = (gameState.currentPlayerIndex + 1) % gameState.players.length;
    
    // 检查是否被跳过
    if (gameState.players[gameState.currentPlayerIndex].isSkipped) {
        gameState.players[gameState.currentPlayerIndex].isSkipped = false;
        addLogEntry(`${gameState.players[gameState.currentPlayerIndex].name} 被跳过了这个回合`);
        nextTurn();
        return;
    }
    
    // 更新回合计数
    if (gameState.currentPlayerIndex === 0) {
        gameState.turnCount++;
    }
    
    // 清空搜索
    elements.movieSearch.value = '';
    elements.movieResults.innerHTML = '';
    
    // 更新游戏状态
    updateGameStatus();
}

// 使用跳过能力
function useSkipPowerUp() {
    const currentPlayer = gameState.players[gameState.currentPlayerIndex];
    const otherPlayerIndex = gameState.currentPlayerIndex === 0 ? 1 : 0;
    
    if (currentPlayer.hasBlocked) {
        addLogEntry(`${currentPlayer.name} 已经使用过能力`);
        return;
    }
    
    // 激活跳过
    gameState.players[otherPlayerIndex].isSkipped = true;
    currentPlayer.hasBlocked = true;
    
    addLogEntry(`${currentPlayer.name} 使用了跳过能力，${gameState.players[otherPlayerIndex].name} 将跳过下一回合`);
    
    // 切换到下一个玩家
    nextTurn();
}

// 使用阻止能力
function useBlockPowerUp() {
    const currentPlayer = gameState.players[gameState.currentPlayerIndex];
    
    if (currentPlayer.hasBlocked) {
        addLogEntry(`${currentPlayer.name} 已经使用过能力`);
        return;
    }
    
    // 激活阻止
    currentPlayer.hasBlocked = true;
    
    addLogEntry(`${currentPlayer.name} 使用了阻止能力，可以阻止对手的下一次能力使用`);
    
    // 切换到下一个玩家
    nextTurn();
}

// 结束游戏
function endGame(winnerIndex) {
    const winner = gameState.players[winnerIndex];
    gameState.gameOver = true;
    gameState.winner = winner;
    
    // 更新UI
    elements.gameBoard.classList.add('hidden');
    elements.gameOver.classList.remove('hidden');
    elements.winnerDisplay.innerHTML = `
        <h3>${winner.name} 获胜!</h3>
        <p>成功收集了 ${winner.genreCount[winner.winGenre]} 部 ${getGenreDisplayName(winner.winGenre)} 电影</p>
    `;
    
    addLogEntry(`游戏结束! ${winner.name} 获胜!`);
}

// 添加日志条目
function addLogEntry(message) {
    const entry = document.createElement('div');
    entry.className = 'log-entry';
    
    const time = new Date().toLocaleTimeString();
    entry.textContent = `[${time}] ${message}`;
    
    elements.logEntries.appendChild(entry);
    elements.logEntries.scrollTop = elements.logEntries.scrollHeight;
}

// 获取类型的显示名称
function getGenreDisplayName(genre) {
    const genreMap = {
        'sci-fi': '科幻',
        'action': '动作',
        'drama': '剧情',
        'comedy': '喜剧',
        'horror': '恐怖'
    };
    
    return genreMap[genre] || genre;
}

// 防抖函数
function debounce(func, delay) {
    let timeout;
    return function() {
        const context = this;
        const args = arguments;
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(context, args), delay);
    };
}
