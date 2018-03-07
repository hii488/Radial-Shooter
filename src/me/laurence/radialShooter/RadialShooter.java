package me.laurence.radialShooter;

import java.util.ArrayList;
import java.util.Random;

import me.laurence.radialShooter.entities.RockEntity;
import me.laurence.radialShooter.graphics.Window;

public class RadialShooter implements Runnable{

	public static Window w;
	public static ArrayList<RadialShooter> instances;
	public static boolean isRunning;
	public static int targetTPS = 30;
	public static Random rand = new Random();
	
	public static boolean DEBUG = false;
	
	public static void main(String[] args) {
		startGame();
	}
	
	public static void startGame(){
		isRunning = true;
		instances = new ArrayList<RadialShooter>();
		
		setupWindow();
		setupInstance();
		
		startInstances();
		w.start();
	}
	
	public static void stopGame(){
		isRunning = false;
		w.stop();
	}
	
	public static void setupWindow(){
		w = new Window("Radial Shooter", 800, 800);
		w.createDisplay();
	}
	
	public static void setupInstance(){
		instances.add(new RadialShooter());
		new Thread(instances.get(instances.size()-1)).start();
	}
	
	public static void startInstances(){
		for(RadialShooter r : instances){
			r.startRun();
		}
	}
	
	// RedialShooter Object - setup this way to allow for parallel testing of genetic variants.
	
	public Stage stage;
	public boolean finished;
	
	public RadialShooter(){
		stage = new Stage(this);
		finished = true;
	}
	
	public void startRun(){
		finished = false;
	}

	public void updateOnTick(){
		stage.updateOnTick();
		
		// Spawning rocks:
		if(rand.nextFloat() < 0.01){ // 0.01 every tick : avg of 0.3 every second
			boolean side = rand.nextBoolean();
			int x, y;
			if(side) {
				x = rand.nextBoolean() ? 1 : w.width-1;
				y = rand.nextInt(w.height-2)+1;
			}
			else {
				y = rand.nextBoolean() ? 1 : w.height-1;
				x = rand.nextInt(w.width-2)+1;
			}
			
			RockEntity e = new RockEntity(stage, new Vector(x,y));
			stage.addEntity(e);
		}
	}
	
	@Override
    public void run() {
        int tick = 0;
        
        double fpsTimer = System.currentTimeMillis();
        double secondsPerTick = 1D / targetTPS;
        double nsPerTick = secondsPerTick * 1000000000D;
        double then = System.nanoTime();
        double now;
        double unprocessed = 0;

        while(isRunning){
            now = System.nanoTime();
            unprocessed += (now - then) / nsPerTick;
            then = now;
            while(unprocessed >= 1){
                if(!finished) try{updateOnTick();} catch(Exception e){e.printStackTrace();}
                tick++;
                unprocessed--;
            }

            // This is NOT to sleep, but to limit the game loop
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If the current time is 1 second greater than the last time we printed
            if(System.currentTimeMillis() - fpsTimer >= 1000){
                System.out.printf("FPS: %d, TPS: %d%n", w.FPS, tick);
                tick = 0;
                fpsTimer += 1000;
            }
        }
	}

}
