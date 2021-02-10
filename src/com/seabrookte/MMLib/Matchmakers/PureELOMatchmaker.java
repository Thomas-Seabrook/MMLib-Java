package com.seabrookte.MMLib.Matchmakers;

/*
 * MMLib-Java: PureELOMatchmaker.java
 * By Thomas Seabrook
 * This is the core class for MMLib, and contains all the methods needed to interact with the matchmaker
 * Implement either this or one of the derived classes to implement the matchmaker in your system
 */

/*
 * Implementing this matchmaker will give you a Pure, One on One ELO System
 * This would be suitable for use in something like chess or a DCG
 */

import com.seabrookte.MMLib.Player.SimplePlayer;
import java.lang.Math;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

public class PureELOMatchmaker
{
    //While true, the system will output all operations to the standard Java console
    //False by default, pass it true in the constructor to turn this on
    protected boolean debugOutputMode = false;

    protected ArrayList<SimplePlayer> playerVector = new ArrayList<>();
    protected ArrayList<Integer> playersInQueue = new ArrayList<>();
    protected int numPlayers = 0;
    protected int kFactor = 24;

    public PureELOMatchmaker(boolean a_debugOutputMode)
    {
        debugOutputMode = a_debugOutputMode;

        if(debugOutputMode)
        {
            System.out.println("[MMLib] Initialising System - Pure ELO");
        }
    }

    public PureELOMatchmaker()
    {
        //Overloaded constructor, doesn't really do anything but leaves debugOutputMode as false

    }

    public int getKFactor()
    {
        return kFactor;
    }

    public void setKFactor(int a_k)
    {
        kFactor = a_k;
    }

    public int addPlayer(String name)
    {
        //Add a new player. In this case, the starting elo is just the default (1500)
        //Returns the internal ID of the player
        SimplePlayer player = new SimplePlayer(name);
        playerVector.add(player);

        if(debugOutputMode)
        {
            System.out.println("[MMLib] Added Player " + name + ", ELO " + player.getELO());
        }

        numPlayers++;
        return numPlayers - 1;
    }

    public int addPlayer(String name, int startingELO)
    {
        //Add a new player. In this case, the starting elo is set by the user
        //Returns the internal ID of the player
        SimplePlayer player = new SimplePlayer(name, startingELO);
        playerVector.add(player);

        if(debugOutputMode)
        {
            System.out.println("[MMLib] Added Player " + name + ", ELO " + player.getELO());
        }

        numPlayers++;
        return numPlayers - 1;
    }

    public void registerGame(int winnerID, int loserID)
    {

        //Register a game between 2 players and recalculate their scores

        //Error checking
        if(winnerID >= playerVector.size() || winnerID < 0 || loserID >= playerVector.size() || loserID < 0)
        {

            System.out.println("[MMLib] Matchmaker Error Code 3: Player does not exist");
            return;
        }

        //Save the elo values to be easier
        int p1ELO = playerVector.get(winnerID).getELO();
        int p2ELO = playerVector.get(loserID).getELO();

        //Player 1's expected score
        double eA = 1 / (1 + Math.pow(10, ((p2ELO - p1ELO) / 400)));

        //Player 2's expected score
        double eB = 1 / (1 + Math.pow(10, ((p1ELO - p2ELO) / 400)));

        //Calculate the new scores
        int newP1Score = p1ELO + (int)(kFactor * (1 - eA));
        int newP2Score = p2ELO + (int)(kFactor * (0 - eB));

        if(debugOutputMode)
        {
            System.out.println("[MMLib] Match registered between " + playerVector.get(winnerID).getPlayerName() + " and " + playerVector.get(loserID).getPlayerName());
            System.out.println("[MMLib] " + playerVector.get(winnerID).getPlayerName()  + " New Score: " + newP1Score);
            System.out.println("[MMLib] " + playerVector.get(loserID).getPlayerName()  + " New Score: " + newP2Score);
        }

        //Set the new ELO values
        playerVector.get(winnerID).setELO(newP1Score);
        playerVector.get(loserID).setELO(newP2Score);


    }

    public SimplePlayer getPlayer(int playerID)
    {
        //Returns the player object associated with the given ID

        //Error checking
        if(playerID >= playerVector.size() || playerID < 0)
        {
            if(debugOutputMode)
            {
                System.out.println("[MMLib] Matchmaker Error Code 3: Player does not exist");
            }
            return new SimplePlayer("ERROR");
        }
        else
        {
            return playerVector.get(playerID);
        }
    }



    public boolean addPlayerToQueue(int playerID)
    {
        //Adds a player to queue

        //Check to see if the player exists
        if(playerID >= playerVector.size() || playerID < 0)
        {
            if(debugOutputMode)
            {
                System.out.println("[MMLib] Matchmaker Error Code 3: Player does not exist");
            }
            return false;
        }

        //Check to see if the player is in queue already
        if(playersInQueue.contains(playerID))
        {
            if(debugOutputMode)
            {
                System.out.println("[MMLib] Matchmaker Error Code 4: Attempted to add player to queue, but they were in queue already");
            }
            return false;
        }

        playersInQueue.add(playerID);

        if(debugOutputMode)
        {
            System.out.println("[MMLib] Player " + playerVector.get(playerID).getPlayerName() + " joined queue");
        }

        return true;

    }

    public boolean removePlayerFromQueue(int playerID)
    {
        //Check to see if the player is in queue
        if(!playersInQueue.contains(playerID))
        {
            if(debugOutputMode)
            {
                System.out.println("[MMLib] Matchmaker Error Code 5: Attempted to remove player from queue, but they were not in queue");
            }

            return false;
        }

        int index = -1;
        //Because java evaluates arrlist.remove(playerID) as a remove by index, have to do it the old fashioned way
        for(int i = 0; i < playersInQueue.size(); i++)
        {
            if(playersInQueue.get(i) == playerID)
            {
                index = i;
            }
        }

        if(index == -1)
        {
            if(debugOutputMode)
            {
                System.out.println("[MMLib] Matchmaker Error Code 3: Player does not exist");
            }
            return false;
        }

        playersInQueue.remove(index);

        if(debugOutputMode)
        {
            System.out.println("[MMLib] Player " + playerVector.get(playerID).getPlayerName() + " left queue");
        }

        return true;

    }

    public int[] formMatch(int maxEloDifference) {
        //Loop through the players in queue and attempt to find a pair that satisfies the match elo difference
        if (debugOutputMode) {
            System.out.println("[MMLib] Attempting to resolve match with differential " + maxEloDifference);
        }

        if (playersInQueue.size() <= 1) {
            System.out.println("[MMLib] Matchmaker Error Code 1: Too few players to create match");
            return new int[]{-1};
        }

        for (int i = 0; i < playersInQueue.size(); i++) {
            for (int j = 1; j < playersInQueue.size(); j++) {
                //Dont check if they're the same
                if (i != j) {
                    //Get the difference in elos
                    int eloDiff = Math.abs(playerVector.get(playersInQueue.get(i)).getELO() - playerVector.get(playersInQueue.get(j)).getELO());
                    if (eloDiff <= maxEloDifference) {
                        if(debugOutputMode) {
                            System.out.println("[MMLib] Match created between player " + playerVector.get(playersInQueue.get(i)).getPlayerName() + " and player " + playerVector.get(playersInQueue.get(j)).getPlayerName());
                        }

                        removePlayerFromQueue(i);
                        removePlayerFromQueue(j);

                        return new int[]{i, j};
                    }
                }
            }
        }

        if(debugOutputMode) {
            System.out.println("[MMLib] Matchmaker Error Code 2: Could not resolve match with differential " + maxEloDifference);
        }
        return new int[]{-1};
    }
}
