package com.elolozone.trafficwave.util;

public class Geo {
	
	/**
	 * This enumeration represents a cardinal point.
	 * Each of them will be defined by an {@link Integer} value.
	 * 
	 * @author brasseld@gmail.com
	 * @since 26-MARS-2010
	 */
	public enum Direction {
		NORTH(0), EAST(1), SOUTH(2), WEST(3), UNKNOWN(-1);
		
		private int value;
		
		private Direction(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
	
	/**
	 * This method will return the {@link Direction} from an
	 * {@link Integer} value.
	 * 
	 * If the direction passed in parameters is not available, this method will
	 * return an Direction.UNKNOWN.
	 * 
	 * @param direction The {@link Integer} value which defined the {@link Direction}.
	 * @return The {@link Direction}.
	 */
	public static Direction getDirection(int direction) {
		for(Direction dir : Direction.values()) {
			if (dir.getValue() == direction)
				return dir;
		}
		
		return Direction.UNKNOWN;
	}
	
	/**
	 * This method will return the cardinal point from the angle passed
	 * in parameters.
	 * 
	 * @param angle The angle.
	 * @return The {@link Direction} wich is the cardinal point.
	 */
	public static Direction getDirection(double angle) {
		if (angle >= 45 && angle < 135)
			return Direction.EAST;
		if (angle >= 135 && angle < 225)
			return Direction.SOUTH;
		if (angle >= 225 && angle < 315)
			return Direction.WEST;
		if (angle >= 315 || angle < 45)
			return Direction.NORTH;
		
		return Direction.UNKNOWN;
	}
}
