import processing.core.*;

public class App extends PApplet {
    public static void main(String[] args) {
        PApplet.main("App");
    }

    float ballX;
    float ballY;
    float ballSpeed = 6;
    float ballSpeedX;
    float ballSpeedY;
    int ballDiameter = 40;
    boolean gameOver = false;
    int buttonX;
    int buttonY;
    int buttonWidth = 100;
    int buttonHeight = 50;
    int paddleWidth = 150;
    int paddleHeight = 20;
    float bottomPaddleX;
    float bottomPaddleY;
    float topPaddleX;
    float topPaddleY;
    float paddleSpeed = 8;
    boolean moveLeft, moveRight, moveADLeft, moveADRight;

    public void settings() {
        size(1200, 800);
    }

    public void setup() {
        resetGame();
        buttonX = width / 2 - buttonWidth / 2;
        buttonY = height / 2 + 40;
        bottomPaddleX = width / 2 - paddleWidth / 2;
        bottomPaddleY = height - paddleHeight - 10;
        topPaddleX = width / 2 - paddleWidth / 2;
        topPaddleY = 10;
    }

    public void draw() {
        if (gameOver) {
            drawGameOverScreen();
        } else {
            updateGame();
        }
    }

    public void drawGameOverScreen() {
        background(255, 0, 0);
        fill(255);
        textSize(50);
        textAlign(CENTER, CENTER);
        text("Game Over", width / 2, height / 2 - 30);
        fill(255, 100, 100);
        rect(buttonX, buttonY, buttonWidth, buttonHeight);
        fill(255);
        textSize(20);
        text("Reset", buttonX + buttonWidth / 2, buttonY + buttonHeight / 2);
    }

    public void updateGame() {
        background(135, 206, 235);
        stroke(0);
        strokeWeight(6);
        line(0, height / 2, width, height / 2);
        drawPaddles();
        drawBall();

        if (moveLeft && bottomPaddleX > 0) {
            bottomPaddleX -= paddleSpeed;
        }
        if (moveRight && bottomPaddleX < width - paddleWidth) {
            bottomPaddleX += paddleSpeed;
        }
        if (moveADLeft && topPaddleX > 0) {
            topPaddleX -= paddleSpeed;
        }
        if (moveADRight && topPaddleX < width - paddleWidth) {
            topPaddleX += paddleSpeed;
        }

        checkBallCollision();
    }

    public void drawPaddles() {
        fill(0);
        rect(bottomPaddleX, bottomPaddleY, paddleWidth, paddleHeight);
        rect(topPaddleX, topPaddleY, paddleWidth, paddleHeight);
    }

    public void drawBall() {
        fill(0, 200, 0);
        ellipse(ballX, ballY, ballDiameter, ballDiameter);
        ballX += ballSpeedX;
        ballY += ballSpeedY;
    }

    public void checkBallCollision() {
        if (ballX > width - ballDiameter / 2 || ballX < ballDiameter / 2) {
            ballSpeedX *= -1;
        }

        if (ballY + ballDiameter / 2 >= bottomPaddleY && ballX > bottomPaddleX && ballX < bottomPaddleX + paddleWidth) {
            ballSpeedY *= -1;
            ballY = bottomPaddleY - ballDiameter / 2;
        }

        if (ballY - ballDiameter / 2 <= topPaddleY + paddleHeight && ballX > topPaddleX && ballX < topPaddleX + paddleWidth) {
            ballSpeedY *= -1;
            ballY = topPaddleY + paddleHeight + ballDiameter / 2;
        }

        if (ballY > height - ballDiameter / 2 || ballY < ballDiameter / 2) {
            gameOver = true;
        }
    }

    public void mousePressed() {
        if (gameOver && mouseX > buttonX && mouseX < buttonX + buttonWidth && mouseY > buttonY && mouseY < buttonY + buttonHeight) {
            resetGame();
        }
    }

    public void resetGame() {
        gameOver = false;
        ballX = width / 2;
        ballY = height / 2;
        setRandomDirection();
    }

    public void setRandomDirection() {
        float slope = 0.5f; 
        float randomY = random(-ballSpeed, ballSpeed); 
        float randomX = random(-slope * abs(randomY), slope * abs(randomY)); 
        ballSpeedX = randomX; 
        ballSpeedY = randomY; 
    }

    public void keyPressed() {
        if (keyCode == LEFT) {
            moveLeft = true;
        } else if (keyCode == RIGHT) {
            moveRight = true;
        } else if (key == 'a' || key == 'A') {
            moveADLeft = true;
        } else if (key == 'd' || key == 'D') {
            moveADRight = true;
        }
    }

    public void keyReleased() {
        if (keyCode == LEFT) {
            moveLeft = false;
        } else if (keyCode == RIGHT) {
            moveRight = false;
        } else if (key == 'a' || key == 'A') {
            moveADLeft = false;
        } else if (key == 'd' || key == 'D') {
            moveADRight = false;
        }
    }
}
