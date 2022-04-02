package commons;


public class UserReaction {
    private int gameID;
    private String username;
    private String reaction;
    public UserReaction() {

    }
    public UserReaction(int gameID, String username, String reaction) {
        this.gameID = gameID;
        this.username = username;
        this.reaction = reaction;
        validate();
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
        validate();
    }

    public void validate() {
        if (this.reaction == null || !(this.reaction.equals("angry") || this.reaction.equals("happy") || this.reaction.equals("angel"))) {
            throw new IllegalArgumentException();
        }
    }
    @Override
    public String toString() {
        return this.username+" reacted with an "+this.reaction+" emoji in game "+this.gameID;
    }
}
