package austincans;

import java.lang.Math;
import java.io.*;
import java.util.*;
import java.util.Arrays;
import austincans.ShooterConfig;

public final class TestLookup {

    public static ShooterConfig ProjectilePredictionBinarySearch(double d, boolean highGoal) {
      int index;
      ShooterConfig s = new ShooterConfig(d,0.0,0.0); // We need an object to use for the Binary Search, so create it with dummy values.
      if ( highGoal ){
        // https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#binarySearch(T[],%20T,%20java.util.Comparator)
        // This will return the insertion point of a distance, and we can use that to determine which value in the array to use.
        index=Arrays.binarySearch(shooterConfigList,s,s);
        if ( index >= 0 )
	   // This means that there was an exact match.  We should send that calibrated number.
	   return shooterConfigList[index];
        else {
	   // the value is ( -insertion_point) -1
	   index = ( index+1 ) * -1;
	   if ( index == shooterConfigList.length ) {
		   // We are beyond range, provide the last value and hope for the best
		   return shooterConfigList[shooterConfigList.length-1];
	    }else if ( index == 0 ) {
		   // We are too close range, provide the first value and hope for the best 
		   return shooterConfigList[0];
	    } else {
		   // We should send the interpolated value between two entries
		   s.interpolate(shooterConfigList[index-1],shooterConfigList[index]);
		   return s;
	    }
        }
      }else{
	return (s); // No low goal for now;
      }
	   
    };

    public static ShooterConfig ProjectilePredictionLinearSearch(double d, boolean highGoal) {
	  // Linearly search through and array, and stop when sLow< d < sHigh
	  // shooterConfigList must be sorted for this to work.
	  ShooterConfig sLow = null;
	  ShooterConfig sHigh = null;
	  int index = 0;

	  if ( ! highGoal ){
		  return null; // Not coded right now, should assert and break.
          };

	  do{ // Minimum once through the loop
		  sLow = sHigh; // Save the previous high ( Save null first time through. )
		  sHigh = shooterConfigList[index]; // Next!
		  index++;
          } while ( index < shooterConfigList.length && sHigh.getDistance() < d ) ; 
	  // End the loop if index is the same as the length, meaning outside of the array bounds...or
	  // if sHigh has a larger distance than d.

	  // One can combine the first three if/else statements...left this way for clarity.  
	  if ( d == sHigh.getDistance() )
		 // Exact match, return sHigh
		 return sHigh;
	  else if ( index == shooterConfigList.length )
		 // Went through whole list, must be beyond max value in measurements
		 return sHigh;
	  else if ( sLow == null )
		 // First entry is farther than our distance, must be below min value in measurements
		 return sHigh;
	  else 
		 // We found a value closer and farther than the value we need.
		 // Get ShooterConfig to interpolate what we should return.
		 return new ShooterConfig(d,sLow,sHigh);
          
    };

    public static ShooterConfig ProjectilePredictionIndex(double d, boolean highGoal ) {
	    // Use information about the start, end and step distance of a list to determine
	    // the exact index of the relevant measurements.

	    ShooterConfig sLow = null;
	    ShooterConfig sHigh = null;
	    int index = 0;

	    if ( ! highGoal ){
		    return null; // Didn't code, should assert and break.
	    };

	    if ( d>= ShooterRegularMax ){
		    // Distance is farther than we measured, return the last element of the array and hope for the best.
		    // ( Remember array's start indexing at 0, so length-1 is last element.
		    return shooterConfigStepList[shooterConfigStepList.length-1];
	    }else if ( d <= ShooterRegularMin ){
		    // Shorter than we measured, return the first element of the array and hope for the best.
		    return shooterConfigStepList[0];
	    }else {
		    // Calculate which of the regular intervals this fits into
		    // And return the interpolation between that config and the next higher one.
		    index = (int)Math.floor((d-ShooterRegularMin)/ShooterRegularStep); 
		    return new ShooterConfig(d,shooterConfigStepList[index],shooterConfigStepList[index+1]);
	    }
    };

    private static ShooterConfig[] shooterConfigList = {
	  // These need to either be sorted, or stored in order
	  // You can notice a mistake below.
	  // We can sort the array at runtime to make sure that no mistakes exist.
	  
	    new ShooterConfig(  6.0, 25.0, 80 ),
	    new ShooterConfig(  2.0, 23.5, 80 ),
	    new ShooterConfig(  8.0, 25.0, 70 ),
	    new ShooterConfig( 14.0, 26.5, 60 )
    };

    private static ShooterConfig[] shooterConfigStepList = {
	    // This is an example of a regularly sampled list with a start, end and defined step.
	    // You can use this type of list for an indexed lookup, where there is no searching required at all.
	    // This list starts at 2, ends at 14 and each step is 2 feet.
	    
	    new ShooterConfig(  2.0, 23.5, 80 ),
	    new ShooterConfig(  4.0, 24.5, 80 ),
	    new ShooterConfig(  6.0, 25.0, 80 ),
	    new ShooterConfig(  8.0, 25.0, 70 ),
	    new ShooterConfig( 10.0, 26.0, 70 ),
	    new ShooterConfig( 12.0, 26.0, 60 ),
	    new ShooterConfig( 14.0, 26.5, 60 )
    };
    private static double ShooterRegularMin = 2.0;
    private static double ShooterRegularMax = 14.0;
    private static double ShooterRegularStep = 2.0;



  public static void main(String... args) {
    Arrays.sort(shooterConfigList);
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    ShooterConfig results;
    double distance;
    try{
	  System.out.println("There should be duplicate lines below for using binary search, and linear search\n");
	  System.out.println("The index will match on 2,6,8,14 and might be different otherwise.\n");
	  while ( true ){
             System.out.println("Enter Distance:");
             distance = Double.parseDouble(br.readLine());
	     results = ProjectilePredictionIndex(distance,true);
	     System.out.printf("Using Index lookup: Distance: %f Velocity in Ft/s: %f fake RPM: (100*v): %f Angle for Hood: %f\n",distance,
			     results.getVelocity(),results.getRPM(),results.getAngleDegrees());
	     results = ProjectilePredictionBinarySearch(distance,true);
	     System.out.printf("Using Binary Search: Distance: %f Velocity in Ft/s: %f fake RPM: (100*v): %f Angle for Hood: %f\n",distance,
			     results.getVelocity(),results.getRPM(),results.getAngleDegrees());
	     results = ProjectilePredictionLinearSearch(distance,true);
	     System.out.printf("Using Linear Search: Distance: %f Velocity in Ft/s: %f fake RPM: (100*v): %f Angle for Hood: %f\n",distance,
			     results.getVelocity(),results.getRPM(),results.getAngleDegrees());
	     ShooterConfig.setBoost(1.5); // Set boost to 1.5 for all configs.
	     System.out.printf("Using boost to RPM: Distance: %f Velocity in Ft/s: %f fake RPM: (100*v*1.5): %f Angle for Hood: %f\n",distance,
			     results.getVelocity(),results.getRPM(),results.getAngleDegrees());
	     ShooterConfig.setBoost(1.0); // Set boost back to normal.

  	  }
	}catch(IOException ioe) {
         System.out.println(ioe);
    }	 
  }
}

