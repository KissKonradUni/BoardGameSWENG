const canvas = document.getElementById('gameboard');
/** @type {CanvasRenderingContext2D} */
const ctx = canvas.getContext('2d');

const borderWidth = 1;
const squareSize = 128;
const boardSize = 11;

let tiles = null;
async function loadTiles() {
    tiles = await fetch("/static/tiles.png").then(res => res.blob()).then(blob => createImageBitmap(blob));
} loadTiles();
/** 3x3 atlas */
const atlas = {
    "tree" :     { x: 0,   y: 0,   color: "#228B2230" },
    "house" :    { x: 128, y: 0,   color: "#fB451330" },
    "field" :    { x: 256, y: 0,   color: "#CDcd3260" },
    "water" :    { x: 0,   y: 128, color: "#0000FF30" },
    "monster" :  { x: 128, y: 128, color: "#FF008830" },
    "mountain" : { x: 256, y: 128, color: "#8B451330" },
    "ruins" :    { x: 0,   y: 256, color: "#80808030" },
}

class Piece {
    constructor(shape, fill, x, y, rotation, special) {
        this.x = x ?? 0;
        this.y = y ?? 0;
        this.shape = shape ?? [[0, 0, 1], [1, 1, 1]];
        this.fill = fill ?? "tree";
        this.rotation = 0; // 0, 90, 180, 270
        this.special = special ?? null; // hill, ruin
        this.rotate(rotation ?? 0);
    }

    move(x, y) {
        this.x += x;
        this.y += y;
        return this;
    }

    rotate(rotation) {
        let oldRotation = this.rotation;
        this.rotation = (this.rotation + rotation) % 360;

        let diff = this.rotation - oldRotation;
        if (diff === 0) return this;
        if (diff === 90 || diff === -270) {
            // Rotate 90 degrees
            let newShape = [];
            newShape = this.shape[0].map((_, i) => this.shape.map(row => row[i]).reverse()); // Copilot pls
            this.shape = newShape;
        } else if (diff === 180 || diff === -180) {
            // Rotate 180 degrees
            this.shape = this.shape.map(row => row.reverse()).reverse();
        } else if (diff === 270 || diff === -90) {
            // Rotate 270 degrees
            let newShape = [];
            newShape = this.shape[0].map((_, i) => this.shape.map(row => row[i])); // Copilot pls
            this.shape = newShape.reverse();
        }

        return this;
    }

    draw(ctx) {
        if (this.special === "hill") {
            this.drawHill(ctx);
            return;
        } else if (this.special === "ruin") {
            this.drawRuin(ctx);
            return;
        }

        if (tiles == null)
            return;

        ctx.fillStyle = atlas[this.fill].color;
        for (let i = 0; i < this.shape.length; i++) {
            for (let j = 0; j < this.shape[i].length; j++) {
                if (this.shape[i][j] === 1) {
                    ctx.fillRect(
                        j * squareSize + this.x * squareSize + borderWidth * (this.x + j),
                        i * squareSize + this.y * squareSize + borderWidth * (this.y + i),
                        squareSize + 1,
                        squareSize + 1
                    );
                    ctx.drawImage(tiles, atlas[this.fill].x, atlas[this.fill].y, 128, 128,
                        j * squareSize + this.x * squareSize + borderWidth * (this.x + j),
                        i * squareSize + this.y * squareSize + borderWidth * (this.y + i),
                        squareSize + 1,
                        squareSize + 1
                    );
                }
            }
        }
    }

    /**
     * Draw a hill
     * @param {CanvasRenderingContext2D} ctx 
     */
    drawHill(ctx) {
        ctx.fillStyle = "#8B4513";
        let x = this.x * squareSize + borderWidth * (this.x);
        let y = this.y * squareSize + borderWidth * (this.y);
        
        if (tiles == null)
            return;
        
        ctx.fillStyle = atlas["mountain"].color;
        ctx.fillRect(x, y, 128, 128);

        ctx.drawImage(tiles, atlas["mountain"].x, atlas["mountain"].y, 128, 128,
            x, y,
            squareSize + 1,
            squareSize + 1
        );
    }

    /**
     * Draw a ruin
     * @param {CanvasRenderingContext2D} ctx 
     */
    drawRuin(ctx) {
        ctx.fillStyle = "gray";
        
        let x = this.x * squareSize + borderWidth * (this.x);
        let y = this.y * squareSize + borderWidth * (this.y);

        if (tiles == null)
            return;

        ctx.fillStyle = atlas["ruins"].color;
        ctx.fillRect(x, y, 128, 128);

        ctx.drawImage(tiles, atlas["ruins"].x, atlas["ruins"].y, 128, 128,
            x, y,
            squareSize + 1,
            squareSize + 1
        );
    }
}

class Board {
    constructor() {
        this.pieces = [];
    }

    placePiece(piece) {
        this.pieces.push(piece);
    }

    /**
     * Draw the board on the canvas
     * @param {CanvasRenderingContext2D} ctx 
     */
    drawBoard(ctx) {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        ctx.fillStyle = "#ffffff88";
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        for (let i = 0; i < this.pieces.length; i++) {
            this.pieces[i].draw(ctx);
        }

        ctx.strokeStyle = "black";
        ctx.lineWidth = borderWidth;
        for (let i = 0; i < boardSize + 1; i++) {
            ctx.beginPath();
            let x = i * squareSize + borderWidth * (i + 1);
            ctx.moveTo(x, 0);
            ctx.lineTo(x, canvas.height);
            ctx.stroke();
        }

        for (let i = 0; i < boardSize + 1; i++) {
            ctx.beginPath();
            let y = i * squareSize + borderWidth * (i + 1);
            ctx.moveTo(0, y);
            ctx.lineTo(canvas.width, y);
            ctx.stroke();
        }
    }
}

class Player {
    constructor() {

    }
}

class Game {
    constructor() {
        this.board  = new Board();
        this.player = new Player(); // TODO: Get player from backend
        this.score  = 0;
    }
}

const game = new Game();
game.board.placePiece(new Piece());
game.board.placePiece(new Piece([[1, 1], [1, 1]], "house", 4, 0));
game.board.placePiece(new Piece([[1, 1, 1], [1, 1]], "water", 1, 3, 0));
game.board.placePiece(new Piece([[1], [1, 1]], "field", 5, 3, 90));
const funnyPiece = new Piece([[1, 1, 1, 1], [1, 0, 0, 0], [1, 0, 0, 0]], "monster", 5, 5, 0);
game.board.placePiece(funnyPiece);
// hill
game.board.placePiece(new Piece([[1, 1], [1, 1]], "mountain", 0, 7, 0, "hill"));
// ruin
game.board.placePiece(new Piece([[1, 1], [1, 1]], "ruin", 2, 7, 0, "ruin"));
game.board.drawBoard(ctx);

setInterval(() => {
    funnyPiece.rotate(90);
    game.board.drawBoard(ctx);
}, 1000);