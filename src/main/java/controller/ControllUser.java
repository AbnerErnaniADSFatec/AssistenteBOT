package model;
import model.User;
import java.util.List;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
public class ControllUser {
    private ObjectContainer users;
    private Query query;
    private boolean teste;

    public ControllUser(){
        try {
            this.users = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "bd//users.db4o");
            this.query = users.query();
            this.teste = true;
        } catch (Exception e) {
            this.teste = false;
            e.printStackTrace();
        }
    }

    public User searchUser( long chatID ) {
        List<User> allUsers = this.listUser();
        User user = null;
		for ( User usr : allUsers ) {
			if ( usr.getChatID() == chatID ){
                user = usr;
                break;
            }
		}
        return user;
    }

    public List<User> listUser() {
        this.query.constrain(User.class);
        List<User> allUsers = this.query.execute();
        return allUsers;
    }

    public boolean removeUser (long chatID) {
        List<User> allUsers = this.listUser();
        User user = null;
        for (User usr : allUsers) {
            if (usr.getChatID() == chatID) {
                user = usr;
                break;
            }
        }
        this.users.delete(user);
        this.users.commit();
        return true;
    }

    public boolean saveUser(User user){
        this.users.store(user);
		this.users.commit();
        return true;
    }

    public boolean testeCon() {
        return this.teste;
    }
}