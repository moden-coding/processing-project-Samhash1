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
    int buttonX = 50;
    int buttonY = height / 2 + 40;  
    int buttonWidth = 100;
    int buttonHeight = 50;

    public void settings() {
        size(1200, 800);  
    }

    public void setup() {
        resetGame();
    }

    public void draw() {
        if (gameOver) {
            background(255, 0, 0);
            fill(255);
            textSize(50);
            textAlign(CENTER, CENTER);
            text("Game Over", width / 2, height / 2 - 30);
            int buttonY = height / 2 + 20;
            fill(255, 100, 100);
            rect(buttonX, buttonY, buttonWidth, buttonHeight);
            fill(255);
            textAlign(CENTER, CENTER);
            textSize(20);
            text("Reset", buttonX + buttonWidth / 2, buttonY + buttonHeight / 2);
        } else {
            background(135, 206, 235);
            stroke(0);
            strokeWeight(6);
            line(0, height / 2, width, height / 2);
            strokeWeight(7);
            int diameter = 110;
            ellipse(width / 2, height / 2, diameter, diameter);
            noStroke();
            fill(135, 206, 235);
            int innerDiameter = 104;
            ellipse(width / 2, height / 2, innerDiameter, innerDiameter);

            fill(0, 200, 0);
            ellipse(ballX, ballY, ballDiameter, ballDiameter);

            ballX += ballSpeedX;
            ballY += ballSpeedY;

            if (ballX > width - ballDiameter / 2 || ballX < ballDiameter / 2) {
                ballSpeedX *= -1;  
            }

            if (ballY > height - ballDiameter / 2 || ballY < ballDiameter / 2) {
                gameOver = true;
            }
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
        float angle = random(TWO_PI);
        ballSpeedX = ballSpeed * cos(angle);
        ballSpeedY = ballSpeed * sin(angle);
    }
}
