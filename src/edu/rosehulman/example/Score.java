package edu.rosehulman.example;

/**
 * Our score data object. Basically just a String and an int (a name and a score)
 */
public class Score implements Comparable<Score>{
    private String mName;
    private int mScore;
    private int mID;
    
    public int getID() { return mID; }
    public void setID(int id) { mID = id; }
    
    public String getName() { return mName; }
    public void setName(String name) { mName = name; }
    
    public int getScore() { return mScore; }
    public void setScore(int score) { mScore = score; }
    
    public int compareTo(Score other) { return getScore() - other.getScore(); }
    
    public String toString() { return getName() + " " + getScore(); }
}
