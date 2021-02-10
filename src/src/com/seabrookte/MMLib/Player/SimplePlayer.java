package com.seabrookte.MMLib.Player;

/*
 * MMLib-Java: SimplePlayer.java
 * By Thomas Seabrook
 * A simple player class, containing only an ELO Value
 * This is used in some simple matchmakers to be more efficient
 */

public class SimplePlayer
{
    public enum ProcessModes
    {
        REJECT,
        CUTOFF,
        FORCE
    }

    //Set the minimum or maximum to be a negative number in order to disable minimum/maximum
    //Defaults: Starting at 1500, no maximum, minimum 0
    protected int ELO = 1500;
    protected int maxELO = -1;
    protected int minELO = 0;

    //Player name
    protected String playerName;

    //When REJECT, the system will reject all operations that would take the ELO value above the maximum or beneath the minimum
    //When CUTOFF, the system will take illegal operations and increase/decrease to the minimum/maximum
    //When FORCE, it will not check the minimum or maximum
    protected ProcessModes processMode = ProcessModes.CUTOFF;

    public SimplePlayer( String a_playerName, int startingELO)
    {
        playerName = a_playerName;
        ELO = startingELO;
    }

    public SimplePlayer(String a_playerName)
    {
        playerName = a_playerName;
    }

    /*
     * Getters and setters for the variables
     */

    public void setMaxELO(int a_maxELO)
    {
        maxELO = a_maxELO;
    }

    public void setMinELO(int a_minELO)
    {
        minELO = a_minELO;
    }

    public void setProcessMode(ProcessModes a_mode)
    {
        processMode = a_mode;
    }

    public void setPlayerName(String name)
    {
        playerName = name;
    }


    public int getELO()
    {
        return ELO;
    }

    public int getMaxELO()
    {
        return maxELO;
    }

    public int getMinELO()
    {
        return minELO;
    }

    public ProcessModes getProcessMode()
    {
        return processMode;
    }

    public String getPlayerName()
    {
        return playerName;
    }


    public boolean setELO(int a_ELO)
    {
        //Sets the elo to the given value
        //Returns true if it has modified the ELO, regardless of if fully

        switch (processMode)
        {
            case FORCE:
                ELO = a_ELO;
                return true;

            case REJECT:
                if(maxELO >= 0)
                {
                    //There is a maximum set
                    if(minELO >= 0)
                    {
                        //There is a minimum set
                        if(a_ELO > minELO && a_ELO < maxELO)
                        {
                            ELO = a_ELO;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        //There is a maximum set but no minimum
                        if(a_ELO < maxELO)
                        {
                            ELO = a_ELO;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                }
                else if(minELO >= 0)
                {
                    //There is a minimum set but no maximum
                    if(a_ELO > minELO)
                    {
                        ELO = a_ELO;
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    //No min/Max set to just process
                    ELO = a_ELO;
                    return true;
                }

            case CUTOFF:
                if(maxELO >= 0)
                {
                    //There is a maximum set
                    if(minELO >= 0)
                    {
                        //There is a minimum set
                        if(a_ELO > minELO && a_ELO < maxELO)
                        {
                            ELO = a_ELO;
                            return true;
                        }
                        else if(a_ELO < minELO)
                        {
                            ELO = minELO;
                            return true;
                        }
                        else if(a_ELO > maxELO)
                        {
                            ELO = maxELO;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        //There is a maximum set but no minimum
                        if(a_ELO < maxELO)
                        {
                            ELO = a_ELO;
                            return true;
                        }
                        else
                        {
                            ELO = maxELO;
                            return true;
                        }
                    }
                }
                else if(minELO >= 0)
                {
                    //There is a minimum set but no maximum
                    if(a_ELO > minELO)
                    {
                        ELO = a_ELO;
                        return true;
                    }
                    else
                    {
                        ELO = minELO;
                        return true;
                    }
                }
                else
                {
                    //No min/Max set to just process
                    ELO = a_ELO;
                    return true;
                }

            default:
                //You should never get here
                return false;
        }

    }

    public boolean changeELO(int amount)
    {
        //Increases/decreases the elo by the given amount
        //Returns true if it has modified the ELO, regardless of if fully

        switch (processMode)
        {
            case FORCE:
                ELO += amount;
                return true;

            case REJECT:
                if(maxELO >= 0)
                {
                    //There is a maximum set
                    if(minELO >= 0)
                    {
                        //There is a minimum set
                        if(ELO + amount > minELO && ELO + amount < maxELO)
                        {
                            ELO += amount;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        //There is a maximum set but no minimum
                        if(ELO + amount < maxELO)
                        {
                            ELO += amount;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                }
                else if(minELO >= 0)
                {
                    //There is a minimum set but no maximum
                    if(ELO + amount > minELO)
                    {
                        ELO += amount;
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    //No min/Max set to just process
                    ELO += amount;
                    return true;
                }

            case CUTOFF:
                if(maxELO >= 0)
                {
                    //There is a maximum set
                    if(minELO >= 0)
                    {
                        //There is a minimum set
                        if(ELO + amount > minELO && ELO + amount < maxELO)
                        {
                            ELO += amount;
                            return true;
                        }
                        else if(ELO + amount < minELO)
                        {
                            ELO = minELO;
                            return true;
                        }
                        else if(ELO + amount > maxELO)
                        {
                            ELO = maxELO;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        //There is a maximum set but no minimum
                        if(ELO + amount < maxELO)
                        {
                            ELO += amount;
                            return true;
                        }
                        else
                        {
                            ELO = maxELO;
                            return true;
                        }
                    }
                }
                else if(minELO >= 0)
                {
                    //There is a minimum set but no maximum
                    if(ELO + amount > minELO)
                    {
                        ELO += amount;
                        return true;
                    }
                    else
                    {
                        ELO = minELO;
                        return true;
                    }
                }
                else
                {
                    //No min/Max set to just process
                    ELO += amount;
                    return true;
                }

            default:
                //You should never get here
                return false;
        }

    }






}
