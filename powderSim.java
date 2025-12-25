/*
Controls:
-arrow keys to move cursor
-space to spawn entity
-keys 1-3 to select the entity type, 1 for stone, 2 for sand, and 3 for water
-you can spawn up to 30 entities

here's a powder sim program I've made. I watched a YouTube video by John Jackson about recreating Noita's sand simulation and learned some of the
basics. After more recently watching MattBatWing's video about building a Minecraft redstone sand/powder sim I learned more basics, and got very
inspired to to make.

This has 3 types: stone, sand, and water. Sand and water, esspecially water, have some issues, but I found them difficult to fix, and was lazy. You can also spawn entities ontop of each other, which I
struggled with fixing and didn't implement. I also included an array based entity system here unlike previous programs where I used a list. I also
used more multi-line comments for better documentation and for the text at the top of the code.
*/

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

class powderSim extends JFrame implements KeyListener{
    /*
    initilizing
    */
    
    //colors
    public static String black = "\u001B[47m  ";
    public static String white = "\u001B[100m  ";
    public static String grey = "\u001B[107m  ";
    public static String yellow = "\u001B[103m  ";
    public static String blue = "\u001B[104m  ";

    public static int width = 10;
    public static int height = 10;
    public static String[] pixels = new String[width * height];

    public static int x = 0;
    public static int y = 0;
    public static boolean leftKey;
    public static boolean rightKey;
    public static boolean upKey;
    public static boolean downKey;
    public static boolean spaceKey;
    public static boolean key1;
    public static boolean key2;
    public static boolean key3;
    public static String type = "stone";

    public static int[] entities = new int[300]; //components: x, y, id, xv, applyVel
    public static int entityCount = 0;

    //JFrame setup
    public powderSim(){
        this.setTitle("Powder Sim");
        this.setSize(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
        gameLoop();
    }

    /*
    game loop
    */
    static void gameLoop(){
        while (1==1){
            System.out.print("\033[H\033[2J");
            System.out.flush();

            /*
            updating
            */

            //cursor
            if (rightKey && x < (width-1)){
                x++;
            }
            if (leftKey && x > 0){
                x--;
            }
            if (upKey && y < 0){
                y++;
            }
            if (downKey && y > -(height-1)){
                y--;
            }

            if (key1){
                type = "stone";
            }
            if (key2){
                type = "sand";
            }
            if (key3){
                type = "water";
            }

            
            if (spaceKey){
                if (type == "stone"){
                    addEntity(x, y, 1);
                }
                if (type == "sand"){
                    addEntity(x, y, 2);
                }
                if (type == "water"){
                    addEntity(x, y, 3);
                }
            }

            //entities
            for (int i = 0; i < entities.length; i += 5){
                int[] bottomSand = {0, 0};
                int[] bottomRightSand = {0, 0};
                int[] bottomLeftSand = {0, 0};
                int[] rightSand = {0, 0};
                int[] leftSand = {0, 0};
                
                if (entities[i+2] == 2){
                    for (int j = 0; j < entities.length; j += 5){
                        if (entities[j] == entities[i] && entities[j+1] == (entities[i+1]-1)){
                            bottomSand[0] = entities[j];
                            bottomSand[1] = entities[j+1];
                        }
                        if (entities[j] == (entities[i]+1) && entities[j+1] == (entities[i+1]-1)){
                            bottomRightSand[0] = entities[j];
                            bottomRightSand[1] = entities[j+1];
                        }
                        if (entities[j] == (entities[i]-1) && entities[j+1] == (entities[i+1]-1)){
                            bottomLeftSand[0] = entities[j];
                            bottomLeftSand[1] = entities[j+1];
                        }
                    }
                    if (!(entities[i] == bottomSand[0] && (entities[i+1]-1) == bottomSand[1])){
                        if (entities[i+1] > -(height-1)){
                            entities[i+1]--;
                        }
                    }
                    else{
                        if (!((entities[i]+1) == bottomRightSand[0] && (entities[i+1]-1) == bottomRightSand[1]) && entities[i] < (width-1)){
                            entities[i]++;
                            entities[i+1]--;
                        }
                        else{
                            if (!((entities[i]-1) == bottomLeftSand[0] && (entities[i+1]-1) == bottomLeftSand[1]) && entities[i] > 0){
                                entities[i]--;
                                entities[i+1]--;
                            }
                        }
                    }
                }
                if (entities[i+2] == 3){
                    for (int j = 0; j < entities.length; j += 5){
                        if (entities[j] == entities[i] && entities[j+1] == (entities[i+1]-1)){
                            bottomSand[0] = entities[j];
                            bottomSand[1] = entities[j+1];
                        }
                        if (entities[j] == (entities[i]+1) && entities[j+1] == (entities[i+1]-1)){
                            bottomRightSand[0] = entities[j];
                            bottomRightSand[1] = entities[j+1];
                        }
                        if (entities[j] == (entities[i]-1) && entities[j+1] == (entities[i+1]-1)){
                            bottomLeftSand[0] = entities[j];
                            bottomLeftSand[1] = entities[j+1];
                        }
                        if (entities[j] == (entities[i]+1) && entities[j+1] == entities[i+1]){
                            rightSand[0] = entities[j];
                            rightSand[1] = entities[j+1];
                        }
                        if (entities[j] == (entities[i]-1) && entities[j+1] == entities[i+1]){
                            leftSand[0] = entities[j];
                            leftSand[1] = entities[j+1];
                        }
                    }
                    if (!(entities[i] == bottomSand[0] && (entities[i+1]-1) == bottomSand[1])){
                        if (entities[i+1] > -(height-1)){
                            entities[i+1]--;
                        }
                        else{
                            if (!((entities[i]+1) == rightSand[0] && entities[i+1] == rightSand[1] && entities[i+4] == 1)){
                                entities[i+3] = 1;
                                entities[i] += entities[i+3];
                                entities[i+4] = 0;
                                if (entities[i] == 10){
                                    entities[i+3] = -1;
                                    entities[i] += entities[i+3];
                                    entities[i+4] = -1;
                                }
                            }
                            else{
                                if (!((entities[i]-1) == leftSand[0] && entities[i+1] == leftSand[1] && entities[i+4] == -1) || entities[i] == (width-1)){
                                    entities[i+3] = -1;
                                    entities[i] += entities[i+3];
                                    entities[i+4] = -1;
                                    if (entities[i] == 0){
                                        entities[i+3] = 1;
                                        entities[i] += entities[i+3];
                                        entities[i+4] = -1;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if (!((entities[i]+1) == bottomRightSand[0] && (entities[i+1]-1) == bottomRightSand[1]) && entities[i] < (width-1)){
                            entities[i]++;
                            entities[i+1]--;
                        }
                        else{
                            if (!((entities[i]-1) == bottomLeftSand[0] && (entities[i+1]-1) == bottomLeftSand[1]) && entities[i] > 0){
                                entities[i]--;
                                entities[i+1]--;
                            }
                            else{
                                if (entities[i+4] == 0){
                                    entities[i+3] = 1;
                                    entities[i+4] = 1;
                                }
                                if (!((entities[i]+1) == rightSand[0] && entities[i+1] == rightSand[1] && entities[i+4] == 1)){
                                    entities[i+3] = 1;
                                    entities[i] += entities[i+3];
                                    entities[i+4] = 0;
                                }
                                else{
                                    if (!((entities[i]-1) == leftSand[0] && entities[i+1] == leftSand[1] && entities[i+4] == -1)){
                                        entities[i+3] = -1;
                                        entities[i] += entities[i+3];
                                        entities[i+4] = -1;
                                    }
                                }
                            }
                        }
                    }
                }
                if (entities[i+2] == 0){
                    break;
                }
            }
            
            /*
            rendering
            */
            for (int i = 0; i < pixels.length; i++){
                pixels[i] = white;
            }

            //entities
            for (int i = 0; i < entities.length; i += 5){
                if (entities[i+2] == 1){
                    setPixel(entities[i], entities[i+1], grey);    
                }
                if (entities[i+2] == 2){
                    setPixel(entities[i], entities[i+1], yellow);    
                }
                if (entities[i+2] == 3){
                    setPixel(entities[i], entities[i+1], blue);    
                }
                if (entities[i+2] == 0){
                    break;
                }
            }

            //cursor
            setPixel(x, y, black);

            System.out.println("selected type: " + type);
            System.out.println("entity count: " + entityCount);
            for (int i = 0; i < height; i++){
                for (int j = 0; j < width; j++){
                    System.out.print(pixels[i * width + j]);
                }
                System.out.println("\u001B[0m");
            }

            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new powderSim();
    }

    static void addEntity(int x, int y, int id){
        if (entityCount < 60){
            int i = 0;
            while (entities[i+2] != 0){
                i += 5;
            }
            entities[i] = x;
            entities[i+1] = y;
            entities[i+2] = id;
            entities[i+3] = 0;
            entities[i+4] = 0;
            entityCount++;
        }
    }

    static void setPixel(int x, int y, String color){
        pixels[Math.abs(y) * width + x] = color;
    }

    public void keyTyped(KeyEvent e){
        
    }
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
                case 37: leftKey = true;
                break;
                case 38: upKey = true;
                break;
                case 39: rightKey = true;
                break;
                case 40: downKey = true;
                break;
                case 32: spaceKey = true;
                break;
                case 49: key1 = true;
                break;
                case 50: key2 = true;
                break;
                case 51: key3 = true;
                break;
        }
    }
    public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()){
                case 37: leftKey = false;
                break;
                case 38: upKey = false;
                break;
                case 39: rightKey = false;
                break;
                case 40: downKey = false;
                break;
                case 32: spaceKey = false;
                break;
                case 49: key1 = false;
                break;
                case 50: key2 = false;
                break;
                case 51: key3 = false;
                break;
        }
    }
}