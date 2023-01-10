package commonnetwork.networking.data;

public enum Side
{
    /**
     * CLIENT is the client side.
     */
    CLIENT,
    /**
     * SERVER can be dedicated server, or logical server in singleplayer.
     */
    SERVER;

    /**
     * Gets the opposite side.
     *
     * @return - The opposite side
     */
    public Side opposite()
    {
        if (CLIENT.equals(this))
        {
            return SERVER;
        }
        return CLIENT;
    }
}
