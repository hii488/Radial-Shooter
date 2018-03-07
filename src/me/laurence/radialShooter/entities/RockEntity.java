package me.laurence.radialShooter.entities;

import java.awt.Graphics;
import java.util.ArrayList;

import me.laurence.radialShooter.RadialShooter;
import me.laurence.radialShooter.Vector;

public class RockEntity extends MovingEntity{
	
	public RockEntity(Vector p){
		super();
		this.collisionBox.setLocation(15, 15);
		position.setLocation(p.getX(), p.getY());
		setVelocity(2);
		System.out.println("Spawned rock at: " + position.getX() + ", " + position.getY() + "\t moving at: " + velocity.getX() + ", " + velocity.getY());
	}
	
	public RockEntity(Vector p, int velocity){
		super();
		this.collisionBox.setLocation(10, 10);
		position.setLocation(p.getX(), p.getY());
		setVelocity(3);
	}
	
	public void setVelocity(double velocity) {
		this.velocity.setLocation(-(this.position.getX() - RadialShooter.w.width/2)/this.velocity.distance(RadialShooter.w.width/2, RadialShooter.w.height/2) * velocity, -(this.position.getY() - RadialShooter.w.width/2)/this.velocity.distance(RadialShooter.w.width/2, RadialShooter.w.height/2) * velocity);
	}
	
	@Override
	public void updateOnTick(){
		super.updateOnTick();
		ArrayList<BaseEntity> entities = RadialShooter.instance.stage.getCollidingEntities(this);
		for(BaseEntity e : entities){
			System.out.println("hit by " + e);
			if(e instanceof BulletEntity){
				this.destroy();
				return;
			}
			
			if(e instanceof PlayerEntity){
				// TODO: End of the game.
			}
			
		}
	}
	
	@Override
	public void render(Graphics g) {
		//g.drawArc(position.getX(), position.getY(), collisionBox.getX(), collisionBox.getY(), 0, 360);
		g.drawRect((int) position.getX(), (int) position.getY(), (int) collisionBox.getX(), (int) collisionBox.getY());
	}
}
