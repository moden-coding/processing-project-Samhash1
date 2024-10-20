import processing.core.*;

public class App extends PApplet {
    public static void main(String[] args) {
        PApplet.main("App");
    }

    // Declaring all the variable ex. paddle speed, dimensions, time delay, game state, score variable, screen color etc.
    int screen = 0;
    int ballColorR = 0, ballColorG = 200, ballColorB = 0;
    float ballX, ballY;
    float speed = 3;
    int lastWindowIncrease = 0;
    int ballDiameter = 40;
    boolean gameOver = false;
    int buttonX, buttonY, buttonWidth = 100, buttonHeight = 50;
    int paddleWidth = 150, paddleHeight = 20;
    float bottomPaddleX, bottomPaddleY, topPaddleX, topPaddleY;
    float paddleSpeed = 4;
    boolean moveLeft, moveRight, moveADLeft, moveADRight;
    float slope = random(0.5f, 5);
    float moveX, moveY;
    int topScore = 0, bottomScore = 0;
    int winningScore = 10;
    boolean ballMoving = false;
    int resetTime;
    int delay = 1000;

    // Starting window size
    public void settings() {
        size(500, 800);
    }

    // Random ball direction, paddle placement/dimensions, frame rate, reset game function, etc.
    public void setup() {
        moveX = (float) Math.sqrt(speed * speed / (1 + slope * slope));
        moveY = slope * moveX;
        if (random(1) < 0.5) moveX = -moveX;
        if (random(1) < 0.5) moveY = -moveY;

        // Positioning if the game is reset
        resetGame();
        buttonX = width / 2 - buttonWidth / 2;
        buttonY = height / 2 + 40;
        bottomPaddleX = width / 2 - paddleWidth / 2;
        bottomPaddleY = height - paddleHeight - 10;
        topPaddleX = width / 2 - paddleWidth / 2;
        topPaddleY = 10;
        frameRate(120);
    }

    // Drawing background, start screen instructions, text size, placement, color, size, etc.
    public void draw() {
        if (screen == 0) {
            // Start screen background
            background(0, 0, 255);
            textSize(50);
            fill(0, 255, 0);
            text("THE PONG GAME", 60, 150);
            // Instructions
            textSize(22);
            fill(0, 255, 0);
            text("   Welcome to the two-person Pong game!", 20, 250);
            text("   To move the bottom paddle use the arrow keys.", 20, 300);
            text("   To move the top paddle use the 'A' and 'D' keys.", 20, 350);
            text("   Over time, the ball will get faster,", 20, 400);
            text("   and the goal size will increase.", 20, 450);
            text("   If a goal is scored, the side that scored -", 20, 500);
            text("   will get a point.", 30, 550);
            text("The first player to reach 10 points wins!", 20, 600);
            textSize(35);
            text("PRESS 'L' TO START", 100, 690);
        } else if (screen == 1) {
            // Game screen backdrop
            background(173, 216, 230); 
            stroke(0);
            strokeWeight(6);
            line(0, height / 2, width, height / 2);
            drawPaddles();
           
            noFill();
            stroke(0);
            strokeWeight(6); 
            ellipse(width / 2, height / 2, 150, 150); 
           
            drawBall();

           
            

            // Paddle movement
            if (moveLeft && bottomPaddleX > 0) bottomPaddleX -= paddleSpeed;
            if (moveRight && bottomPaddleX < width - paddleWidth) bottomPaddleX += paddleSpeed;
            if (moveADLeft && topPaddleX > 0) topPaddleX -= paddleSpeed;
            if (moveADRight && topPaddleX < width - paddleWidth) topPaddleX += paddleSpeed;

            // Resize the window every 60 seconds
            if (millis() - lastWindowIncrease >= 60000) {
                int newWidth = width + 50;
                int newHeight = height;
                surface.setSize(newWidth, newHeight); // Resizing the window every 60 seconds
                lastWindowIncrease = millis(); // Reset timer after resizing
            }

            // Display scores
            textSize(65);
            fill(0);
            text(topScore, 15, 370);
            text(bottomScore, 15, 470);

            // Check ball collision
            checkBallCollision();

            // Display game over screen if the game is over
            if (gameOver) drawGameOverScreen();

            // Delay before the ball starts moving again
            if (millis() - resetTime >= delay) ballMoving = true;

            // Increment the ball speed every 1 min
            if (frameCount % 7200 == 0) {
                moveY += 0.2;
                moveX += (moveX < 0) ? -0.1 : 0.1; // Smaller increment for X movement
            }
        }
    }

    // Code for drawing the paddles
    public void drawPaddles() {
        fill(0);
        rect(bottomPaddleX, bottomPaddleY, paddleWidth, paddleHeight);
        rect(topPaddleX, topPaddleY, paddleWidth, paddleHeight);
    }

    // Drawing the ball
    public void drawBall() {
        fill(ballColorR, ballColorG, ballColorB); 
        ellipse(ballX, ballY, ballDiameter, ballDiameter);
        if (ballMoving) {
            ballX += moveX;
            ballY += moveY;
        }
    }

    // Ball collision checker
    public void checkBallCollision() {
        // Ball bounces off the left and right walls
        if (ballX > width - ballDiameter / 2 || ballX < ballDiameter / 2) moveX *= -1;

        // Collision with the bottom paddle
        if (ballY + ballDiameter / 2 >= bottomPaddleY && ballX > bottomPaddleX && ballX < bottomPaddleX + paddleWidth) {
            moveY *= -1;
            ballY = bottomPaddleY - ballDiameter / 2;

            // Change ball color on paddle hit
            ballColorR = (int) random(0, 256);
            ballColorG = (int) random(0, 256);
            ballColorB = (int) random(0, 256);
        }

        // Collision with the top paddle
        if (ballY - ballDiameter / 2 <= topPaddleY + paddleHeight && ballX > topPaddleX && ballX < topPaddleX + paddleWidth) {
            moveY *= -1;
            ballY = topPaddleY + paddleHeight + ballDiameter / 2;

            // Change ball color on paddle hit
            ballColorR = (int) random(0, 256);
            ballColorG = (int) random(0, 256);
            ballColorB = (int) random(0, 256);
        }

        // Scoring conditions
        if (ballY > height - ballDiameter / 2) {
            topScore++;
            resetBall();
        }
        if (ballY < ballDiameter / 2) {
            bottomScore++;
            resetBall();
        }

        // Check for game over
        if (topScore >= winningScore || bottomScore >= winningScore) gameOver = true;
    }

    // Draw the game over screen
    public void drawGameOverScreen() {
        background(255, 0, 0);
        fill(255);
        textSize(50);
        textAlign(CENTER, CENTER);
        text("Game Over", width / 2, height / 2 - 90);

        textSize(30);
        if (topScore >= winningScore) text("Top Side Won!", width / 2, height / 2 - 10);
        else if (bottomScore >= winningScore) text("Bottom Side Won!", width / 2, height / 2 - 10);
      // Reset Game Button
      buttonX = width / 2 - buttonWidth / 2;
      fill(255, 100, 100);
        rect(buttonX, buttonY, buttonWidth, buttonHeight);
        fill(255);
        textSize(18);
        textAlign(CENTER, CENTER);
        text("Reset Game", buttonX + buttonWidth / 2, buttonY + buttonHeight / 2);
    }

    // Reset the ball position
    public void resetBall() {
        ballX = width / 2;
        ballY = height / 2;
        ballMoving = false;
        resetTime = millis();
    }

    // Handle mouse press events
    public void mousePressed() {
        if (gameOver && mouseX > buttonX && mouseX < buttonX + buttonWidth && mouseY > buttonY && mouseY < buttonY + buttonHeight) {
            resetGame();
            gameOver = false;
        }
    }

    // Reset game variables and other functions
    public void resetGame() {
        surface.setSize(500, 800);
        ballX = width / 2;
        ballY = height / 2;
        topScore = 0;
        bottomScore = 0;
        moveX = (float) Math.sqrt(speed * speed / (1 + slope * slope));
        moveY = slope * moveX;
        if (random(1) < 0.5) moveX = -moveX;
        if (random(1) < 0.5) moveY = -moveY;
        ballMoving = false;
        resetTime = millis();
        lastWindowIncrease = millis();
        screen = 0; // Set to instruction screen at the start
    }

    // Handle key pressed events
    public void keyPressed() {
        if (key == 'l' || key == 'L') screen = 1; // Start the game
        if (keyCode == LEFT) moveLeft = true;
        if (keyCode == RIGHT) moveRight = true;
        if (key == 'a' || key == 'A') moveADLeft = true;
        if (key == 'd' || key == 'D') moveADRight = true;
    }

    // Handle key released events
    public void keyReleased() {
        if (keyCode == LEFT) moveLeft = false;
        if (keyCode == RIGHT) moveRight = false;
        if (key == 'a' || key == 'A') moveADLeft = false;
        if (key == 'd' || key == 'D') moveADRight = false;
    }
}

