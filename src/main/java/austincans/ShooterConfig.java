package austincans;
import java.util.*;
import java.lang.Math;

public class ShooterConfig implements Comparator<ShooterConfig>,Comparable<ShooterConfig>{
    double distance; // distance in feet
    double velocity; // Speed of object in ft/s
    double theta; // angle in degrees
    static double boost=1; // adjustment to velocity based on field conditions.

    public ShooterConfig( double d, double r, double t){
        this.distance = d;
	this.velocity = r;
	this.theta = t;
    };

    public ShooterConfig( double d, ShooterConfig s1, ShooterConfig s2){
	    // Set the Config to be in between two different Configs using Linear Interpolation.
	    // If we wanted to instead return the minimum or maximum settings, we could do that here.
	    this.distance = d;
	    this.interpolate(s1,s2);
    };

    public double getVelocity(){
	    return(this.velocity);
    };

    public double getAngleDegrees(){
	    return(this.theta);
    };

    public double getAngleRadians(){
	    // If we decided to store radians we could have the conversion in the getDegrees method
	    return(Math.toRadians(this.theta));
    };

    public double getDistance(){
	    return(this.distance);
    };

    public double getRPM(){
	    // This is obviously wrong, but we can make adjustments here to calculate the RPM easily.
	    // Maybe this is where we can set a boost? (Meaning a change to increase or decrease the RPM during competition for overshoot/undershoot)
	    // Maybe we clamp this to never go above max RPM?  Maybe we log when we think we should go above max RPM?
	    // Maybe we record the RPM in our Config List, and display Velocity for logging.
	    return(this.velocity*100*this.boost);
    };

    public static void setBoost(double b){
	    // Set a overshot boost for competition live adjustment?  Just a thought
	    // If so, would we want it to be verified here, or in shuffleboard or both?
	    // Do we want it to be between -1.5 and 1.5 for example?
	    // Careful!  We set boost as static, which means one copy for all objects created for this class.
	    boost=b;
    }

    public void interpolate(ShooterConfig s0,ShooterConfig s1){
	    // Use two ShooterConfig's and this object's distance to linear interpolate angle and velocity and update this object with those values.
	    // https://en.wikipedia.org/wiki/Linear_interpolation
	    this.velocity =  (s0.getVelocity()*(s1.getDistance()-this.distance) + s1.getVelocity()*(this.distance-s0.getDistance()) )/(s1.getDistance()-s0.getDistance());
	    this.theta = ( s0.getAngleDegrees()*(s1.getDistance()-this.distance) + s1.getAngleDegrees()*(this.distance-s0.getDistance()) )/(s1.getDistance()-s0.getDistance());
    };

    // If you have a compare method then you are "implementing" Comparator.  This is used for Arrays.BinarySearch.
    // Double also implements Comparator, so we are using Double's definition for the distance
    // distance is a lower case d double.  But the compare method casts it correctly.
    // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Comparator.html
    public int compare(ShooterConfig s1,ShooterConfig s2){
	    return Double.compare(s1.getDistance(),s2.getDistance());
    };

    // If you have a compareTo method then you are "implementing" Comparable.  This is used for Arrays.sort()
    // Double also implements Comparable, so we are using Double's method for the distance
    // Fun fact, you can have a reverse sort by changing to: `return -1*d.compareTo(s.getDistance());`
    // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Comparable.html
    public int compareTo(ShooterConfig s){
	    Double d = this.getDistance();
	    return d.compareTo(s.getDistance());
    };


};


