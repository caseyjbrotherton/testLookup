# Readme

## Purpose of this repository

This repository illustrates two concepts in Java that may be helpful for creating a lookup table with interpolation.

It is an example, that can be built upon or ignored entirely. :)

## First example: Using a class for return values

I have created a new class in order to pass back Shooter Configurations.  In our current code, we use a two element Array to return values.

With just two return values, an Array is manageable.  It is likely that we will be able to understand the return quickly, and not encounter any errors.

However, if we create a class, we have better *encapsulation* of the data.  The information that makes up the Shooter Configuration is in the same class as methods to return the information.
The class can determine to store angle in Radians, or Degrees.  And choose to implement a return method that translates between the two without the rest of the code knowing or caring.

Similarly for speed.  It can be stored in flywheel RPM, or linear velocity, and returned in either of those.  
Or it can be returned in Velocity in the X direction without much additional work.

For the example in this repository, there is also an interpolate method.  It sets the current angle and speed based on two other Objects of Shooter Configuration.  
If we decide that we need a different interpolation method.  
No matter if that is a non-linear approximation, or if it is simply returning the smallest value speed, this can be changed without changing any other code.

Or, what if you need to add an adjustment to the speed?  You find out that your test values that worked well in the test lab are undershooting in a competition?

One code change can add a "boost" to the ShooterConfigs.  You can also use a static element in ShooterConfig to make sure that all ShooterConfigs have the same boost added to all of the RPM values.

## Second example: Using an Array of objects to provide a lookup table.

An if-else ladder provides the minimum code that needs to search a list of predefined speeds and angles.
It is absolutely the best choice for 4 values.  However, what about for 40?  It becomes less clear the more predefined measurements we have.

This may be too complex.  But the idea is to use an array of objects, and search through them linearly in order to find the right two configurations to examine.

There is another method of using a [Binary Search](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Collections.html#binarySearch(java.util.List,T,java.util.Comparator) 

This probably makes more sense as your list grows to a size that your computer can't hold the Array in it's processor cache.  Maybe 400 measurements, for example.

## Third example: Simplifying the Projectile Prediction knowing that we have a class for Shooter Config

With the configuration encapsulated in it's own class, we can simplify the method for Projectile Prediction to the minimum information needed to perform the prediction.

We could even choose to hold local constants in the class for gravity, instead of in Constants.java

## To Run:

- change directory to ./src/java/main/
- Compile ShooterConfig: `javac -d . ShooterConfig.java`
- This creates the `austincans` subdirectory
- Compile TestLookup: `javac -d . TestLookup.java`
- The directory is because of the package specification, and needs to be used to run the test
- Run: `java austincans.TestLookup`
